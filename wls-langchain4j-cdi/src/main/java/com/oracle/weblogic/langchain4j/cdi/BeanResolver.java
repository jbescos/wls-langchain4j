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

package com.oracle.weblogic.langchain4j.cdi;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.TypeLiteral;

/**
 * Provides helper methods for resolving beans.
 */
public final class BeanResolver {

    private BeanResolver() {
    }

    /**
     * Resolves a bean by its type and {@link BeanName}. If {@code beanName} is set
     * to auto-discovery, the method will automatically discover the bean. Otherwise, it resolves the bean by the
     * name specified in {@code beanName}.
     *
     * @param <T>      the type of the bean to resolve
     * @param type     the class of the bean to resolve
     * @param beanName an instance of {@link BeanName} specifying either automatic discovery or an explicit bean name
     * @return the resolved bean instance
     */
    public static <T> T resolve(Class<T> type, BeanName beanName) {
        if (beanName == null || beanName.isAutoDiscover()) {
            return resolve(type);
        } else {
            return resolve(type, beanName.name());
        }
    }

    /**
     * Resolves a bean by its {@link TypeLiteral} and {@link BeanName}.
     * If {@code beanName} is set to auto-discovery, the method will automatically discover the bean.
     * Otherwise, it resolves the bean by the name specified in {@code beanName}.
     *
     * @param <T>         the type of the bean to resolve
     * @param typeLiteral a {@link TypeLiteral} representing the type of the bean to resolve
     * @param beanName    an instance of {@link BeanName} specifying either automatic discovery or an explicit bean name
     * @return the resolved bean instance
     */
    public static <T> T resolve(TypeLiteral<T> typeLiteral, BeanName beanName) {
        if (beanName == null || beanName.isAutoDiscover()) {
            return instance(typeLiteral).get();
        } else {
            return resolve(typeLiteral, beanName.name());
        }
    }

    /**
     * Retrieves an {@link Instance} of the specified bean type, allowing further manipulation or selection.
     *
     * @param <T>  the type of the bean
     * @param type the class of the bean to retrieve
     * @return an {@link Instance} of the specified bean type
     */
    public static <T> Instance<T> instance(Class<T> type) {
        return CDI.current().select(type);
    }

    /**
     * Retrieves an {@link Instance} of the specified type literal, allowing further manipulation or selection.
     *
     * @param <T>  the type of the bean
     * @param type the {@link TypeLiteral} of the bean to retrieve
     * @return an {@link Instance} of the specified type literal
     */
    public static <T> Instance<T> instance(TypeLiteral<T> type) {
        return CDI.current().select(type);
    }

    /**
     * Resolves a bean by its class type using CDI auto-discovery.
     *
     * @param <T>  the type of the bean to resolve
     * @param type the class of the bean to resolve
     * @return the resolved bean instance
     */
    public static <T> T resolve(Class<T> type) {
        return instance(type).get();
    }

    /**
     * Resolves a bean by its class type and a specific name.
     *
     * @param <T>  the type of the bean to resolve
     * @param type the class of the bean to resolve
     * @param name the name of the bean
     * @return the resolved bean instance
     */
    public static <T> T resolve(Class<T> type, String name) {
        var instance = CDI.current().select(type, NamedLiteral.of(name));
        return instance.get();
    }

    /**
     * Resolves a bean by its type literal and a specific name.
     *
     * @param <T>         the type of the bean to resolve
     * @param typeLiteral the {@link TypeLiteral} of the bean to resolve
     * @param name        the name of the bean
     * @return the resolved bean instance
     */
    public static <T> T resolve(TypeLiteral<T> typeLiteral, String name) {
        var instance = CDI.current().select(typeLiteral, NamedLiteral.of(name));
        return instance.get();
    }
}

