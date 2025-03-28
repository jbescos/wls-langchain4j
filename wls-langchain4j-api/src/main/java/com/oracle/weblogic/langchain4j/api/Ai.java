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

package com.oracle.weblogic.langchain4j.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This interface contains a set of annotations for defining langChain4J declarative services.
 */
public final class Ai {
    private Ai() {
    }

    /**
     * Annotation to define a langChain4J service. A langChain4J service aggregates various components
     * that support functionalities like chat, memory management, moderation, content retrieval, and
     * tool-based augmentations.
     *
     * <p>The primary components include:</p>
     * <ul>
     *   <li>{@code dev.langchain4j.model.chat.ChatLanguageModel} or
     *   {@code dev.langchain4j.model.chat.StreamingChatLanguageModel} -
     *       Models that handle chat-based language interactions.</li>
     *   <li>{@code dev.langchain4j.memory.ChatMemory} or {@code dev.langchain4j.memory.chat.ChatMemoryProvider} -
     *       Components for storing and managing chat memory.</li>
     *   <li>{@code dev.langchain4j.model.moderation.ModerationModel} - Model for moderating chat content.</li>
     *   <li>{@code dev.langchain4j.rag.content.retriever.ContentRetriever} - Retrieves relevant content to support responses
     *   .</li>
     *   <li>{@code dev.langchain4j.rag.RetrievalAugmentor} - Enhances retrieval processes with additional context.</li>
     *   <li>CDI bean methods annotated with {@code dev.langchain4j.agent.tool.Tool} -
     *       Tool methods that further extend service capabilities.</li>
     * </ul>
     *
     * <p>If the {@code autoDiscovery} parameter is set to {@code true} (the default value), components are
     * automatically added from the CDI registry. Components explicitly specified using corresponding annotations
     * are prioritized over automatically discovered ones.</p>
     *
     * <p>If {@code autoDiscovery} is set to {@code false}, only components explicitly specified using annotations
     * are included in the service, allowing manual control over the service composition.</p>
     *
     * <p>At a minimum, either a {@code dev.langchain4j.model.chat.ChatLanguageModel} or
     * {@code dev.langchain4j.model.chat.StreamingChatLanguageModel} is required for the service to function
     * effectively.</p>
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface Service {

        /**
         * Specifies whether to use auto-discovery mode to locate service components or to rely on manual
         * component specification.
         *
         * @return {@code true} if auto-discovery is enabled, {@code false} if manual discovery mode is used.
         */
        boolean autoDiscovery() default true;
    }

    /**
     * Annotation to specify a ChatModel for the service.
     * If an empty string is specified, no chat model will be used or discovered.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface ChatModel {
        /**
         * Name of the chat model to be used. If an empty string is specified, no chat model will be used or
         * discovered.
         *
         * @return name of the chat model
         */
        String value();
    }

    /**
     * Annotation to specify a StreamingChatModel for the service.
     * If an empty string is specified, no streaming chat model will be used or discovered.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface StreamingChatModel {
        /**
         * Name of the streaming chat model to be used. If an empty string is specified, no streaming chat model will
         * be used or discovered.
         *
         * @return name of the streaming chat model
         */
        String value();
    }

    /**
     * Annotation to specify a ChatMemory for the service.
     * If an empty string is specified, no chat memory will be used or discovered.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface ChatMemory {
        /**
         * Name of the chat memory to be used. If an empty string is specified, no chat memory will be used or
         * discovered.
         *
         * @return name of the chat memory
         */
        String value();
    }

    /**
     * Annotation to specify a ChatMemoryProvider for the service.
     * If an empty string is specified, no chat memory provider will be used or discovered.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface ChatMemoryProvider {
        /**
         * Name of the chat memory provider to be used. If an empty string is specified, no chat memory provider will
         * be used or discovered.
         *
         * @return name of the chat memory provider
         */
        String value();
    }

    /**
     * Annotation to specify a ModerationModel for the service.
     * If an empty string is specified, no moderation model will be used or discovered.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface ModerationModel {
        /**
         * Name of the moderation model to be used. If an empty string is specified, no moderation model will be used
         * or discovered.
         *
         * @return name of the moderation model
         */
        String value();
    }

    /**
     * Annotation to specify a ContentRetriever for the service.
     * If an empty string is specified, no content retriever will be used or discovered.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface ContentRetriever {
        /**
         * Name of the content retriever to be used. If an empty string is specified, no content retriever will be
         * used or discovered.
         *
         * @return name of the content retriever
         */
        String value();
    }

    /**
     * Annotation to specify a RetrievalAugmentor for the service.
     * If an empty string is specified, no retrieval augmentor will be used or discovered.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface RetrievalAugmentor {
        /**
         * Name of the retrieval augmentor to be used. If an empty string is specified, no retrieval augmentor will be
         * used or discovered.
         *
         * @return name of the retrieval augmentor
         */
        String value();
    }

    /**
     * Annotation to manually specify classes containing tools for the service.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface Tools {
        /**
         * Classes with tool methods (methods annotated with lc4j annotation {@code Tool}).
         *
         * @return an array of classes containing tool methods
         */
        Class<?>[] value();
    }
}
