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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be placed on a method to conditionally register the method's return value as a CDI bean.
 * The {@link ConditionalProduceExtension} processes this annotation to register
 * the bean only if the specified configuration key exists in the MicroProfile Config configuration and its value
 * matches the value specified in the annotation's properties.
 *
 * <p>Usage example:</p>
 * <pre>
 * &#64;ConditionalProduce(key = "feature.enabled", value = "true")
 * public MyBean produceConditionalBean() {
 *     return new MyBean();
 * }
 * </pre>
 *
 * <p>In this example, {@code MyBean} will be registered as a CDI bean only if the configuration property
 * {@code feature.enabled} exists and its value is {@code true}.</p>
 *
 * <p>This mechanism allows for fine-grained control over bean registration based on dynamic configuration properties,
 * facilitating feature toggling and conditional bean instantiation.</p>
 *
 * @see ConditionalProduceExtension
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalProduce {
    /**
     * The key of the configuration property to check.
     *
     * @return the key of the configuration property to check.
     */
    String key();

    /**
     * The value that the configuration property must have for the bean to be registered.
     *
     * @return the value that the configuration property must have for the bean to be registered.
     */
    String value();
}
