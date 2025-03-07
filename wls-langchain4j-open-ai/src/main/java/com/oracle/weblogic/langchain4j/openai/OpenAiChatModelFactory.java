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
import java.util.List;
import java.util.Map;

import com.oracle.weblogic.langchain4j.cdi.BeanName;
import com.oracle.weblogic.langchain4j.cdi.BeanResolver;
import com.oracle.weblogic.langchain4j.cdi.ConditionalProduce;
import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel.OpenAiChatModelBuilder;

/**
 * Factory class for creating a configured {@link OpenAiChatModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the configuration property
 * <i>langchain4j.open-ai.chat-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiChatModel
 * @see OpenAiChatModelConfig
 */
@ApplicationScoped
public class OpenAiChatModelFactory {

    private Configuration configuration;

    // Required by CDI
    protected OpenAiChatModelFactory() {
    }

    /**
     * Creates OpenAiChatModelFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public OpenAiChatModelFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link OpenAiChatModel} bean in the CDI registry with the name
     * <i>openAiChatModel</i> if the configuration property <i>langchain4j.open-ai.chat-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link OpenAiChatModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.chat-model.enabled", value = "true")
    @Named("openAiChatModel")
    public OpenAiChatModel create() {
        OpenAiChatModelBuilder builder = OpenAiChatModel.builder();
        configuration.getString("langchain4j.open-ai.base-url").ifPresent(builder::baseUrl);
        configuration.getString("langchain4j.open-ai.api-key").ifPresent(builder::apiKey);
        configuration.getString("langchain4j.open-ai.organization-id").ifPresent(builder::organizationId);
        configuration.getString("langchain4j.open-ai.model-name").ifPresent(builder::modelName);
        configuration.getDouble("langchain4j.open-ai.temperature").ifPresent(builder::temperature);
        configuration.getDouble("langchain4j.open-ai.top-p").ifPresent(builder::topP);
        List<String> stop = configuration.getList("langchain4j.open-ai.stop");
        if (!stop.isEmpty()) {
            builder.stop(stop);
        }
        configuration.getInteger("langchain4j.open-ai.max-tokens").ifPresent(builder::maxTokens);
        configuration.getInteger("langchain4j.open-ai.max-completion-tokens").ifPresent(builder::maxCompletionTokens);
        configuration.getDouble("langchain4j.open-ai.presence-penalty").ifPresent(builder::presencePenalty);
        configuration.getDouble("langchain4j.open-ai.frequency-penalty").ifPresent(builder::frequencyPenalty);
        Map<String, Integer> logitBias = configuration.getMapInteger("langchain4j.open-ai.logit-bias");
        if (!logitBias.isEmpty()) {
            builder.logitBias(logitBias);
        }
        configuration.getString("langchain4j.open-ai.response-format").ifPresent(builder::responseFormat);
        configuration.getBoolean("langchain4j.open-ai.strict-json-schema").ifPresent(builder::strictJsonSchema);
        configuration.getInteger("langchain4j.open-ai.seed").ifPresent(builder::seed);
        configuration.getString("langchain4j.open-ai.user").ifPresent(builder::user);
        configuration.getBoolean("langchain4j.open-ai.strict-tools").ifPresent(builder::strictTools);
        configuration.getBoolean("langchain4j.open-ai.parallel-tool-calls").ifPresent(builder::parallelToolCalls);
        configuration.getLong("langchain4j.open-ai.timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getInteger("langchain4j.open-ai.max-retries").ifPresent(builder::maxRetries);
        configuration.getBoolean("langchain4j.open-ai.log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("langchain4j.open-ai.log-responses").ifPresent(builder::logResponses);
        configuration.getString("langchain4j.open-ai.tokenizer").ifPresent(t -> builder.tokenizer(BeanResolver.resolve(Tokenizer.class, BeanName.create(t))));
        configuration.getString("langchain4j.open-ai.proxy").ifPresent(p -> builder.proxy(BeanResolver.resolve(Proxy.class, BeanName.create(p))));
        Map<String, String> customHeaders = configuration.getMapString("langchain4j.open-ai.custom-headers");
        if (!customHeaders.isEmpty()) {
            builder.customHeaders(customHeaders);
        }
        return builder.build();
    }
}
