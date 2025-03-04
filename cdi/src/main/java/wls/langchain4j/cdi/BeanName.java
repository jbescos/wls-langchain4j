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

/**
 * Represents the name of a bean, with an option for automatic discovery.
 * The {@link BeanName} class provides a mechanism to either specify a bean name explicitly
 * or indicate that the bean should be automatically discovered.
 */
public class BeanName {

    /**
     * Constant representing a bean name that indicates the bean should be automatically discovered.
     */
    public static final String AUTO_DISCOVER = "discover:auto";

    private final String name;

    private BeanName(String name) {
        this.name = name;
    }

    /**
     * Creates a new {@link BeanName} instance with automatic discovery enabled.
     *
     * @return a new {@link BeanName} instance set for automatic discovery
     */
    public static BeanName create() {
        return new BeanName(AUTO_DISCOVER);
    }

    /**
     * Creates a new {@link BeanName} instance with the specified name.
     *
     * @param name the name of the bean
     * @return a new {@link BeanName} instance with the specified name
     */
    public static BeanName create(String name) {
        return new BeanName(name);
    }

    /**
     * Checks if this {@link BeanName} instance is set to automatic discovery.
     *
     * @return {@code true} if the bean is set for automatic discovery, {@code false} otherwise
     */
    public boolean isAutoDiscover() {
        return name.equals(AUTO_DISCOVER);
    }

    /**
     * Retrieves the name of the bean.
     *
     * @return the name of the bean
     */
    public String name() {
        return name;
    }
}

