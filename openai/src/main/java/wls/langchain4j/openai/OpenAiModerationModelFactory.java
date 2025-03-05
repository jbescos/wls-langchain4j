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

import dev.langchain4j.model.openai.OpenAiModerationModel;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Factory class for creating a configured {@link OpenAiModerationModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the configuration property
 * <i>langchain4j.open-ai.moderation-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiModerationModel
 * @see OpenAiModerationModelConfig
 */
@ApplicationScoped
public class OpenAiModerationModelFactory {

    private BeanResolver beanResolver;

    // Required by CDI
    protected OpenAiModerationModelFactory() {
    }

    /**
     * Creates {@link io.helidon.integrations.langchain4j.providers.ollama.OpenAiModerationModelFactory}.
     *
     * @param beanResolver helper class containing methods for CDI beans resolving
     */
    @Inject
    public OpenAiModerationModelFactory(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    /**
     * Registers and produces a configured {@link OpenAiModerationModel} bean in the CDI registry with the name
     * <i>openAiModerationModel</i> if the configuration property <i>langchain4j.open-ai.moderation-model.enabled</i> is set to
     * <i>true</i>.
     *
     * @return a configured instance of {@link OpenAiModerationModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.moderation-model.enabled", value = "true")
    @Named("openAiModerationModel")
    public OpenAiModerationModel create() {
        var config = MpConfig.toHelidonConfig(ConfigurationProvider.getConfig()).get(OpenAiModerationModelConfig.CONFIG_ROOT);
        return create(OpenAiModerationModelConfig.create(config));
    }

    /**
     * Creates and returns an {@link OpenAiModerationModel} configured using the provided configuration.
     *
     * @param config the properties used to configure the {@link OpenAiModerationModel}
     * @return a configured instance of {@link OpenAiModerationModel}
     */
    public OpenAiModerationModel create(OpenAiModerationModelConfig config) {
        var builder = OpenAiModerationModel.builder();
        config.baseUrl().ifPresent(builder::baseUrl);
        config.apiKey().ifPresent(builder::apiKey);
        config.organizationId().ifPresent(builder::organizationId);
        config.modelName().ifPresent(builder::modelName);
        config.timeout().ifPresent(builder::timeout);
        config.maxRetries().ifPresent(builder::maxRetries);
        config.logRequests().ifPresent(builder::logRequests);
        config.logResponses().ifPresent(builder::logResponses);
        config.proxy().ifPresent(p -> builder.proxy(beanResolver.resolve(Proxy.class, BeanName.create(p))));
        if (!config.customHeaders().isEmpty()) {
            builder.customHeaders(config.customHeaders());
        }
        return builder.build();
    }
}
