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
import java.util.List;
import java.util.Map;

import com.oracle.weblogic.langchain4j.cdi.ConditionalProduce;
import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel.OllamaStreamingChatModelBuilder;

/**
 * Factory class for creating a configured {@link dev.langchain4j.model.ollama.OllamaStreamingChatModel}.
 *
 * @see dev.langchain4j.model.ollama.OllamaStreamingChatModel
 */
@ApplicationScoped
public class OllamaStreamingChatModelFactory {

    private Configuration configuration;

    // Required by CDI
    protected OllamaStreamingChatModelFactory() {
    }

    /**
     * Creates OllamaStreamingChatModelFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public OllamaStreamingChatModelFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link dev.langchain4j.model.ollama.OllamaStreamingChatModel} bean in the CDI
     * registry  with the name <i>ollamaStreamingChatModel</i> if the configuration property
     * <i>langchain4j.ollama.chat-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link dev.langchain4j.model.ollama.OllamaStreamingChatModel}
     */
    @ConditionalProduce(key = "langchain4j.ollama.streaming-chat-model.enabled", value = "true")
    @Named("ollamaStreamingChatModel")
    public OllamaStreamingChatModel create() {
        OllamaStreamingChatModelBuilder builder = OllamaStreamingChatModel.builder();
        configuration.getString("langchain4j.ollama.base-url").ifPresent(builder::baseUrl);
        configuration.getString("langchain4j.ollama.model-name").ifPresent(builder::modelName);
        configuration.getDouble("langchain4j.ollama.temperature").ifPresent(builder::temperature);
        configuration.getDouble("langchain4j.ollama.top-p").ifPresent(builder::topP);
        configuration.getInteger("langchain4j.ollama.top-k").ifPresent(builder::topK);
        configuration.getInteger("langchain4j.ollama.seed").ifPresent(builder::seed);
        configuration.getDouble("langchain4j.ollama.repeat-penalty").ifPresent(builder::repeatPenalty);
        configuration.getInteger("langchain4j.ollama.num-predict").ifPresent(builder::numPredict);
        List<String> stop = configuration.getList("langchain4j.ollama.stop");
        if (!stop.isEmpty()) {
            builder.stop(stop);
        }
        configuration.getString("langchain4j.ollama.format").ifPresent(builder::format);
        configuration.getLong("langchain4j.ollama.timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getBoolean("langchain4j.ollama.log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("langchain4j.ollama.log-responses").ifPresent(builder::logResponses);
        Map<String, String> customHeaders = configuration.getMapString("langchain4j.ollama.custom-headers");
        if (!customHeaders.isEmpty()) {
            builder.customHeaders(customHeaders);
        }
        return builder.build();
    }

}
