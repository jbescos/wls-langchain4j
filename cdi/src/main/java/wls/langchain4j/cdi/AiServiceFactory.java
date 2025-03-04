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

import java.util.ArrayList;
import java.util.List;

import wls.langchain4j.api.*;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 * A helper CDI bean used internally to create implementations for AI services using LangChain4J APIs.
 *
 * <p>This bean facilitates the creation and registration of AI service implementations within the CDI container.
 * It leverages LangChain4J APIs to dynamically generate the necessary service implementations based on the
 * annotations and configurations provided in the application.</p>
 *
 * <p>Typically, this bean is not directly accessed by application developers. Instead, it operates behind the scenes
 * to ensure that AI services are correctly instantiated and integrated into the CDI context, allowing developers to
 * focus on defining and using these services.</p>
 */
@ApplicationScoped
public class AiServiceFactory {

    private BeanResolver beanResolver;
    private BeanManager beanManager;

    // Required by CDI
    protected AiServiceFactory() {
    }

    @Inject
    AiServiceFactory(BeanResolver beanResolver, BeanManager beanManager) {
        this.beanResolver = beanResolver;
        this.beanManager = beanManager;
    }

    /**
     * Factory method to create or retrieve AI service implementations. Used internally.
     *
     * @param serviceInterface AI service interface to implement
     * @param <T>              the type of the interface to implement
     * @return the implementation of passed interface
     */
    public <T> T createAiService(Class<T> serviceInterface) {
        var builder = AiServices.builder(serviceInterface);
        var serviceAnnotation = serviceInterface.getAnnotation(Ai.Service.class);
        var autoDiscoveryMode = serviceAnnotation.autoDiscovery(); // TODO

        var chatModelAnnotation = serviceInterface.getAnnotation(Ai.ChatModel.class);
        if (chatModelAnnotation == null) {
            var instance = beanResolver.instance(ChatLanguageModel.class);
            if (autoDiscoveryMode && !instance.isUnsatisfied()) {
                builder.chatLanguageModel(instance.get());
            }
        } else {
            builder.chatLanguageModel(beanResolver.resolve(ChatLanguageModel.class, chatModelAnnotation.value()));
        }

        var streamingChatModelAnnotation = serviceInterface.getAnnotation(Ai.StreamingChatModel.class);
        if (streamingChatModelAnnotation == null) {
            var instance = beanResolver.instance(StreamingChatLanguageModel.class);
            if (autoDiscoveryMode && !instance.isUnsatisfied()) {
                builder.streamingChatLanguageModel(instance.get());
            }
        } else {
            builder.streamingChatLanguageModel(beanResolver.resolve(StreamingChatLanguageModel.class,
                                                                    streamingChatModelAnnotation.value()));
        }

        var chatMemoryAnnotation = serviceInterface.getAnnotation(Ai.ChatMemory.class);
        if (chatMemoryAnnotation == null) {
            var instance = beanResolver.instance(ChatMemory.class);
            if (autoDiscoveryMode && !instance.isUnsatisfied()) {
                builder.chatMemory(instance.get());
            }
        } else {
            builder.chatMemory(beanResolver.resolve(ChatMemory.class, chatMemoryAnnotation.value()));
        }

        var chatMemoryProviderAnnotation = serviceInterface.getAnnotation(Ai.ChatMemoryProvider.class);
        if (chatMemoryProviderAnnotation == null) {
            var instance = beanResolver.instance(ChatMemoryProvider.class);
            if (autoDiscoveryMode && !instance.isUnsatisfied()) {
                builder.chatMemoryProvider(instance.get());
            }
        } else {
            builder.chatMemoryProvider(beanResolver.resolve(ChatMemoryProvider.class, chatMemoryProviderAnnotation.value()));
        }

        var moderationModelAnnotation = serviceInterface.getAnnotation(Ai.ModerationModel.class);
        if (moderationModelAnnotation == null) {
            var instance = beanResolver.instance(ModerationModel.class);
            if (autoDiscoveryMode && !instance.isUnsatisfied()) {
                builder.moderationModel(instance.get());
            }
        } else {
            builder.moderationModel(beanResolver.resolve(ModerationModel.class, moderationModelAnnotation.value()));
        }

        var retrievalAugmentorAnnotation = serviceInterface.getAnnotation(Ai.RetrievalAugmentor.class);
        if (retrievalAugmentorAnnotation == null) {
            var instance = beanResolver.instance(RetrievalAugmentor.class);
            if (autoDiscoveryMode && !instance.isUnsatisfied()) {
                builder.retrievalAugmentor(instance.get());
            }
        } else {
            builder.retrievalAugmentor(beanResolver.resolve(RetrievalAugmentor.class, retrievalAugmentorAnnotation.value()));
        }

        var contentRetrieverAnnotation = serviceInterface.getAnnotation(Ai.ContentRetriever.class);
        if (contentRetrieverAnnotation == null) {
            var instance = beanResolver.instance(ContentRetriever.class);
            if (autoDiscoveryMode && !instance.isUnsatisfied()) {
                builder.contentRetriever(instance.get());
            }
        } else {
            builder.contentRetriever(beanResolver.resolve(ContentRetriever.class, contentRetrieverAnnotation.value()));
        }

        var toolsAnnotation = serviceInterface.getAnnotation(Ai.Tools.class);
        var tools = resolveTools(toolsAnnotation != null ? toolsAnnotation.value() : null);
        if (!tools.isEmpty()) {
            builder.tools(tools);
        }

        return serviceInterface.cast(builder.build());
    }

    private List<Object> resolveTools(Class<?>[] classes) {
        if (classes == null) {
            return resolveTools();
        }

        var cdi = CDI.current();
        var tools = new ArrayList<Object>();
        for (var cls : classes) {
            tools.add(cdi.select(cls).get());
        }
        return tools;
    }

    private List<Object> resolveTools() {
        var tools = new ArrayList<Object>();
        var beans = beanManager.getBeans(Object.class);
        for (var bean : beans) {
            var beanClass = bean.getBeanClass();
            for (var method : beanClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Tool.class)) {
                    tools.add(beanManager.getReference(bean, beanClass, beanManager.createCreationalContext(bean)));
                }
            }
        }
        return tools;
    }
}
