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

package com.oracle.weblogic.langchain4j.ollama;

import java.time.Duration;
import java.util.Map;

import com.oracle.weblogic.langchain4j.cdi.ConditionalProduce;
import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel.OllamaEmbeddingModelBuilder;

/**
 * Factory class for creating a configured {@link dev.langchain4j.model.ollama.OllamaEmbeddingModel}.
 *
 * @see dev.langchain4j.model.ollama.OllamaEmbeddingModel
 */
@ApplicationScoped
public class OllamaEmbeddingModelFactory {

    private Configuration configuration;

    // Required by CDI
    protected OllamaEmbeddingModelFactory() {
    }

    /**
     * Creates OllamaEmbeddingModelFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public OllamaEmbeddingModelFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link dev.langchain4j.model.ollama.OllamaEmbeddingModel} bean in the CDI registry
     * with the name <i>ollamaEmbeddingModel</i> if the configuration property
     * <i>langchain4j.ollama.embedding-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link dev.langchain4j.model.ollama.OllamaEmbeddingModel}
     */
    @ConditionalProduce(key = "langchain4j.ollama.embedding-model.enabled", value = "true")
    @Named("ollamaEmbeddingModel")
    public OllamaEmbeddingModel create() {
        OllamaEmbeddingModelBuilder builder = OllamaEmbeddingModel.builder();
        configuration.getString("langchain4j.ollama.base-url").ifPresent(builder::baseUrl);
        configuration.getString("langchain4j.ollama.model-name").ifPresent(builder::modelName);
        configuration.getLong("langchain4j.ollama.timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getInteger("langchain4j.ollama.max-retries").ifPresent(builder::maxRetries);
        configuration.getBoolean("langchain4j.ollama.log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("langchain4j.ollama.log-responses").ifPresent(builder::logResponses);
        Map<String, String> customHeaders = configuration.getMapString("langchain4j.ollama.custom-headers");
        if (!customHeaders.isEmpty()) {
            builder.customHeaders(customHeaders);
        }
        return builder.build();
    }
}
