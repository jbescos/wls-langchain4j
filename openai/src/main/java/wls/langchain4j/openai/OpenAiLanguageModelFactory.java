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
import dev.langchain4j.model.openai.OpenAiLanguageModel;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Factory class for creating a configured {@link OpenAiLanguageModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the configuration property
 * <i>langchain4j.open-ai.language-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiLanguageModel
 * @see OpenAiLanguageModelConfig
 */
@ApplicationScoped
public class OpenAiLanguageModelFactory {

    private BeanResolver beanResolver;

    // Required by CDI
    protected OpenAiLanguageModelFactory() {
    }
    /**
     * Creates {@link io.helidon.integrations.langchain4j.providers.ollama.OpenAiLanguageModelFactory}.
     *
     * @param beanResolver helper class containing methods for CDI beans resolving
     */
    @Inject
    public OpenAiLanguageModelFactory(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    /**
     * Registers and produces a configured {@link OpenAiLanguageModel} bean in the CDI registry with the name
     * <i>openAiLanguageModel</i> if the configuration property <i>langchain4j.open-ai.language-model.enabled</i> is set to
     * <i>true</i>.
     *
     * @return a configured instance of {@link OpenAiLanguageModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.language-model.enabled", value = "true")
    @Named("openAiLanguageModel")
    public OpenAiLanguageModel create() {
        var config = MpConfig.toHelidonConfig(ConfigProvider.getConfig()).get(OpenAiLanguageModelConfig.CONFIG_ROOT);
        return create(OpenAiLanguageModelConfig.create(config));
    }

    /**
     * Creates and returns an {@link OpenAiLanguageModel} configured using the provided configuration.
     *
     * @param config the properties used to configure the {@link OpenAiLanguageModel}
     * @return a configured instance of {@link OpenAiLanguageModel}
     */
    public OpenAiLanguageModel create(OpenAiLanguageModelConfig config) {
        var builder = OpenAiLanguageModel.builder();
        config.baseUrl().ifPresent(builder::baseUrl);
        config.apiKey().ifPresent(builder::apiKey);
        config.organizationId().ifPresent(builder::organizationId);
        config.modelName().ifPresent(builder::modelName);
        config.temperature().ifPresent(builder::temperature);
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
