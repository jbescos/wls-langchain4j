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

package wls.langchain4j.openai;

import java.net.Proxy;
import java.time.Duration;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder;
import wls.langchain4j.cdi.BeanName;
import wls.langchain4j.cdi.BeanResolver;
import wls.langchain4j.cdi.ConditionalProduce;
import wls.langchain4j.cdi.ConfigurationProvider.Configuration;

/**
 * Factory class for creating a configured {@link OpenAiEmbeddingModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the
 * configuration property <i>langchain4j.open-ai.embedding-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiEmbeddingModel
 */
@ApplicationScoped
public class OpenAiEmbeddingModelFactory {

    private Configuration configuration;

    // Required by CDI
    protected OpenAiEmbeddingModelFactory() {
    }

    @Inject
    public OpenAiEmbeddingModelFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link dev.langchain4j.model.openai.OpenAiEmbeddingModel} bean in the CDI registry
     * with the name <i>openAiEmbeddingModel</i> if the configuration property
     * <i>langchain4j.open-ai.embedding-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link dev.langchain4j.model.openai.OpenAiEmbeddingModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.embedding-model.enabled", value = "true")
    @Named("openAiEmbeddingModel")
    public OpenAiEmbeddingModel create() {
        OpenAiEmbeddingModelBuilder builder = OpenAiEmbeddingModel.builder();
        configuration.getString("base-url").ifPresent(builder::baseUrl);
        configuration.getString("api-key").ifPresent(builder::apiKey);
        configuration.getString("organization-id").ifPresent(builder::organizationId);
        configuration.getString("model-name").ifPresent(builder::modelName);
        configuration.getInteger("dimensions").ifPresent(builder::dimensions);
        configuration.getString("user").ifPresent(builder::user);
        configuration.getLong("timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getInteger("max-retries").ifPresent(builder::maxRetries);
        configuration.getBoolean("log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("log-responses").ifPresent(builder::logResponses);
        configuration.getString("tokenizer").ifPresent(t -> builder.tokenizer(BeanResolver.resolve(Tokenizer.class, BeanName.create(t))));
        configuration.getString("proxy").ifPresent(p -> builder.proxy(BeanResolver.resolve(Proxy.class, BeanName.create(p))));
        Map<String, String> customHeaders = configuration.getMapString("custom-headers");
        if (!customHeaders.isEmpty()) {
            builder.customHeaders(customHeaders);
        }
        return builder.build();
    }
}
