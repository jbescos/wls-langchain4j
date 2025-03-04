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

import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Factory class for creating a configured {@link OpenAiEmbeddingModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the
 * configuration property <i>langchain4j.open-ai.embedding-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiEmbeddingModel
 * @see OpenAiEmbeddingModelConfig
 */
@ApplicationScoped
public class OpenAiEmbeddingModelFactory {

    private BeanResolver beanResolver;

    // Required by CDI
    protected OpenAiEmbeddingModelFactory() {
    }

    /**
     * Creates {@link io.helidon.integrations.langchain4j.providers.ollama.OpenAiEmbeddingModelFactory}.
     *
     * @param beanResolver helper class containing methods for CDI beans resolving
     */
    @Inject
    public OpenAiEmbeddingModelFactory(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
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
        var config = MpConfig.toHelidonConfig(ConfigProvider.getConfig())
                .get(OpenAiEmbeddingModelConfig.CONFIG_ROOT);

        return create(OpenAiEmbeddingModelConfig.create(config));
    }

    /**
     * Creates and returns an {@link dev.langchain4j.model.openai.OpenAiEmbeddingModel} configured using the provided
     * configuration bean.
     *
     * @param config configuration bean
     * @return a configured instance of {@link dev.langchain4j.model.openai.OpenAiEmbeddingModel}
     */
    public OpenAiEmbeddingModel create(OpenAiEmbeddingModelConfig config) {
        var builder = OpenAiEmbeddingModel.builder();
        config.baseUrl().ifPresent(builder::baseUrl);
        config.apiKey().ifPresent(builder::apiKey);
        config.organizationId().ifPresent(builder::organizationId);
        config.modelName().ifPresent(builder::modelName);
        config.dimensions().ifPresent(builder::dimensions);
        config.user().ifPresent(builder::user);
        config.timeout().ifPresent(builder::timeout);
        config.maxRetries().ifPresent(builder::maxRetries);
        config.logRequests().ifPresent(builder::logRequests);
        config.logResponses().ifPresent(builder::logResponses);
        config.tokenizer().ifPresent(t -> builder.tokenizer(beanResolver.resolve(Tokenizer.class, BeanName.create(t))));
        config.proxy().ifPresent(p -> builder.proxy(beanResolver.resolve(Proxy.class, BeanName.create(p))));
        if (!config.customHeaders().isEmpty()) {
            builder.customHeaders(config.customHeaders());
        }
        return builder.build();
    }
}
