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

package com.oracle.weblogic.langchain4j.openai;

import java.net.Proxy;
import java.time.Duration;
import java.util.Map;

import com.oracle.weblogic.langchain4j.cdi.BeanName;
import com.oracle.weblogic.langchain4j.cdi.BeanResolver;
import com.oracle.weblogic.langchain4j.cdi.ConditionalProduce;
import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder;

/**
 * Factory class for creating a configured {@link OpenAiEmbeddingModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the
 * configuration property <i>langchain4j.open-ai.embedding-model.embedding-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiEmbeddingModel
 */
@ApplicationScoped
public class OpenAiEmbeddingModelFactory {

    private Configuration configuration;

    // Required by CDI
    protected OpenAiEmbeddingModelFactory() {
    }

    /**
     * Creates OpenAiEmbeddingModelFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public OpenAiEmbeddingModelFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link dev.langchain4j.model.openai.OpenAiEmbeddingModel} bean in the CDI registry
     * with the name <i>openAiEmbeddingModel</i> if the configuration property
     * <i>langchain4j.open-ai.embedding-model.embedding-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link dev.langchain4j.model.openai.OpenAiEmbeddingModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.embedding-model.enabled", value = "true")
    @Named("openAiEmbeddingModel")
    public OpenAiEmbeddingModel create() {
        OpenAiEmbeddingModelBuilder builder = OpenAiEmbeddingModel.builder();
        configuration.getString("langchain4j.open-ai.embedding-model.base-url").ifPresent(builder::baseUrl);
        configuration.getString("langchain4j.open-ai.embedding-model.api-key").ifPresent(builder::apiKey);
        configuration.getString("langchain4j.open-ai.embedding-model.organization-id").ifPresent(builder::organizationId);
        configuration.getString("langchain4j.open-ai.embedding-model.model-name").ifPresent(builder::modelName);
        configuration.getInteger("langchain4j.open-ai.embedding-model.dimensions").ifPresent(builder::dimensions);
        configuration.getString("langchain4j.open-ai.embedding-model.user").ifPresent(builder::user);
        configuration.getLong("langchain4j.open-ai.embedding-model.timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getInteger("langchain4j.open-ai.embedding-model.max-retries").ifPresent(builder::maxRetries);
        configuration.getBoolean("langchain4j.open-ai.embedding-model.log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("langchain4j.open-ai.embedding-model.log-responses").ifPresent(builder::logResponses);
        configuration.getString("langchain4j.open-ai.embedding-model.tokenizer").ifPresent(t -> builder.tokenizer(BeanResolver.resolve(Tokenizer.class, BeanName.create(t))));
        configuration.getString("langchain4j.open-ai.embedding-model.proxy").ifPresent(p -> builder.proxy(BeanResolver.resolve(Proxy.class, BeanName.create(p))));
        Map<String, String> customHeaders = configuration.getMapString("langchain4j.open-ai.embedding-model.custom-headers");
        if (!customHeaders.isEmpty()) {
            builder.customHeaders(customHeaders);
        }
        return builder.build();
    }
}
