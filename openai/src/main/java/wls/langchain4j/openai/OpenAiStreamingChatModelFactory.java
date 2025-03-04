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
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Factory class for creating a configured {@link OpenAiStreamingChatModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the configuration property
 * <i>langchain4j.open-ai.streaming-chat-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiStreamingChatModel
 * @see OpenAiStreamingChatModelConfig
 */
@ApplicationScoped
public class OpenAiStreamingChatModelFactory {

    private final BeanResolver beanResolver;

    // Required by CDI
    protected OpenAiStreamingChatModelFactory() {
    }

    /**
     * Creates {@link io.helidon.integrations.langchain4j.providers.ollama.OpenAiChatModelFactory}.
     *
     * @param beanResolver helper class containing methods for CDI beans resolving
     */
    @Inject
    public OpenAiStreamingChatModelFactory(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    /**
     * Registers and produces a configured {@link OpenAiStreamingChatModel} bean in the CDI registry with the name
     * <i>openAiStreamingChatModel</i> if the configuration property <i>langchain4j.open-ai.streaming-chat-model.enabled</i> is
     * set to <i>true</i>.
     *
     * @return a configured instance of {@link OpenAiStreamingChatModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.streaming-chat-model.enabled", value = "true")
    @Named("openAiStreamingChatModel")
    public OpenAiStreamingChatModel create() {
        var config = MpConfig.toHelidonConfig(ConfigProvider.getConfig()).get(OpenAiStreamingChatModelConfig.CONFIG_ROOT);
        return create(OpenAiStreamingChatModelConfig.create(config));
    }

    /**
     * Creates and returns an {@link OpenAiStreamingChatModel} configured using the provided configuration.
     *
     * @param config the configuration bean
     * @return a configured instance of {@link OpenAiStreamingChatModel}
     */
    public OpenAiStreamingChatModel create(OpenAiStreamingChatModelConfig config) {
        var builder = OpenAiStreamingChatModel.builder();
        config.baseUrl().ifPresent(builder::baseUrl);
        config.apiKey().ifPresent(builder::apiKey);
        config.organizationId().ifPresent(builder::organizationId);
        config.modelName().ifPresent(builder::modelName);
        config.temperature().ifPresent(builder::temperature);
        config.topP().ifPresent(builder::topP);
        if (!config.stop().isEmpty()) {
            builder.stop(config.stop());
        }
        config.maxTokens().ifPresent(builder::maxTokens);
        config.maxCompletionTokens().ifPresent(builder::maxCompletionTokens);
        config.presencePenalty().ifPresent(builder::presencePenalty);
        config.frequencyPenalty().ifPresent(builder::frequencyPenalty);
        if (!config.logitBias().isEmpty()) {
            builder.logitBias(config.logitBias());
        }
        config.responseFormat().ifPresent(builder::responseFormat);
        config.seed().ifPresent(builder::seed);
        config.user().ifPresent(builder::user);
        config.strictTools().ifPresent(builder::strictTools);
        config.parallelToolCalls().ifPresent(builder::parallelToolCalls);
        config.timeout().ifPresent(builder::timeout);
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
