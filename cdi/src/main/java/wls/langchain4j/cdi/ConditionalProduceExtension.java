/*
 * Copyright (c) 2025 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wls.langchain4j.cdi;

import java.lang.System.Logger.Level;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Named;

/**
 * This class leverages CDI (Contexts and Dependency Injection) to dynamically produce and register beans based on
 * configuration properties at runtime. By using the {@link ConditionalProduce} annotation,
 * developers can conditionally enable or disable beans depending on the presence and value of specific configuration
 * properties in the MicroProfile Config.
 *
 * <p>Example usage:</p>
 * <pre>
 * &#64;ConditionalProduce(propertyName = "feature.enabled", expectedValue = "true")
 * public MyBean produceMyBean() {
 *     return new MyBean();
 * }
 * </pre>
 *
 * <p>In this example, the {@code MyBean} will only be registered as a CDI bean if the configuration property
 * {@code feature.enabled} exists and its value is {@code true}.</p>
 *
 * @see ConditionalProduce
 */
public class ConditionalProduceExtension implements Extension {
    private static final System.Logger LOGGER = System.getLogger(ConditionalProduceExtension.class.getName());
    private final List<Method> conditionalProducerMethods = new ArrayList<>();

    /**
     * Public no-arg constructor is required by {@link java.util.ServiceLoader}.
     */
    public ConditionalProduceExtension() {
    }

    /**
     * Observer method for the {@link ProcessAnnotatedType} event. This method identifies all methods
     * annotated with the {@link ConditionalProduce} annotation and
     * stores them internally for future processing.
     *
     * @param pat the {@link ProcessAnnotatedType} event that contains metadata about the annotated type being processed
     * @param <T> the type of the annotated class being processed
     */
    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        for (var method : pat.getAnnotatedType().getJavaClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ConditionalProduce.class)) {
                conditionalProducerMethods.add(method);
            }
        }
    }

    /**
     * This method is invoked during the CDI (Contexts and Dependency Injection) lifecycle after the bean discovery
     * process is complete. It evaluates each method stored by the {@link #processAnnotatedType} method to determine
     * whether a bean should be produced based on the application's configuration properties.
     *
     * @param abd         the {@link AfterBeanDiscovery} event that signals the end of the bean discovery phase
     * @param beanManager the {@link BeanManager} used to manage the lifecycle and dependencies of CDI beans
     */
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {
        for (var producerMethod : conditionalProducerMethods) {
            var conditionalProduce = producerMethod.getAnnotation(ConditionalProduce.class);
            var key = conditionalProduce.key();
            var value = conditionalProduce.value();

            var propertyValue = ConfigProvider.getConfig().getOptionalValue(key, String.class);

            // Only register the bean if the configuration property exists
            if (propertyValue.isPresent() && propertyValue.get().equals(value)) {
                var named = producerMethod.getAnnotation(Named.class);
                var name = named != null ? named.value() : null;

                abd.addBean()
                        .beanClass(producerMethod.getReturnType())
                        .scope(ApplicationScoped.class)
                        .name(name)
                        .createWith(ctx -> {
                            try {
                                // Create an instance of the declaring class
                                var declaringBeanInstance = beanManager.getReference(
                                        beanManager.resolve(beanManager.getBeans(producerMethod.getDeclaringClass())),
                                        producerMethod.getDeclaringClass(),
                                        ctx
                                );

                                // Invoke the producer method to get the instance
                                return producerMethod.invoke(declaringBeanInstance);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to invoke producer method: " + producerMethod.getName(), e);
                            }
                        })
                        .addTransitiveTypeClosure(producerMethod.getReturnType());

                if (LOGGER.isLoggable(Level.TRACE)) {
                    LOGGER.log(Level.TRACE, "Registered synthetic bean for: " + producerMethod.getName());
                }
            } else {
                if (LOGGER.isLoggable(Level.TRACE)) {
                    LOGGER.log(Level.TRACE,
                               "Skipping producer method: " + producerMethod.getName() + " due to missing property: " + key);
                }
            }
        }
    }
}
