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

import java.util.HashSet;
import java.util.Set;

import wls.langchain4j.api.Ai;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * This class leverages CDI (Contexts and Dependency Injection) to dynamically produce declarative AI services.
 */
public class AiServiceExtension implements Extension {

    private final Set<Class<?>> aiServiceInterfaces = new HashSet<>();

    /**
     * Public no-arg constructor is required by {@link java.util.ServiceLoader}.
     */
    public AiServiceExtension() {
    }

    /**
     * Observer method for the {@link ProcessAnnotatedType} event. This method identifies all interfaces
     * annotated with the {@link wls.langchain4j.api.Ai.Service} annotation and
     * stores them internally for future processing.
     *
     * @param pat the {@link ProcessAnnotatedType} event that contains metadata about the annotated type being processed
     * @param <T> the type of the annotated class being processed
     */
    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        AnnotatedType<T> annotatedType = pat.getAnnotatedType();
        if (annotatedType.getJavaClass().isInterface() && annotatedType.isAnnotationPresent(Ai.Service.class)) {
            aiServiceInterfaces.add(annotatedType.getJavaClass());
        }
    }

    /**
     * Invoked during the CDI lifecycle after the bean discovery process is complete. This method creates
     * implementations for all interfaces stored by the {@link #processAnnotatedType} method and registers them in the CDI
     * registry.
     *
     * @param abd         the {@link AfterBeanDiscovery} event that signals the end of the bean discovery phase
     * @param beanManager the {@link BeanManager} used to manage the lifecycle and dependencies of CDI beans
     */
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {
        for (Class<?> aiServiceInterface : aiServiceInterfaces) {
            abd.addBean()
                    .addType(aiServiceInterface)
                    .createWith(ctx -> {
                        var aiServiceFactory = resolveAiServiceFactory(beanManager);
                        return aiServiceFactory.createAiService(aiServiceInterface);
                    });
        }
    }

    private AiServiceFactory resolveAiServiceFactory(BeanManager bm) {
        var aiServiceFactories = bm.getBeans(AiServiceFactory.class);
        var bean = aiServiceFactories.iterator().next();
        return (AiServiceFactory) bm.getReference(bean, AiServiceFactory.class, bm.createCreationalContext(bean));
    }
}
