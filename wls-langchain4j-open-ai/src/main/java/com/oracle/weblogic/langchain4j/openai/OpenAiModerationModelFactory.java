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

import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.model.openai.OpenAiModerationModel.OpenAiModerationModelBuilder;

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

    private Configuration configuration;

    // Required by CDI
    OpenAiModerationModelFactory() {
    }

    /**
     * Creates OpenAiModerationModelFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public OpenAiModerationModelFactory(Configuration configuration) {
        this.configuration = configuration;
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
        OpenAiModerationModelBuilder builder = OpenAiModerationModel.builder();
        configuration.getString("langchain4j.open-ai.base-url").ifPresent(builder::baseUrl);
        configuration.getString("langchain4j.open-ai.api-key").ifPresent(builder::apiKey);
        configuration.getString("langchain4j.open-ai.organization-id").ifPresent(builder::organizationId);
        configuration.getString("langchain4j.open-ai.model-name").ifPresent(builder::modelName);
        configuration.getLong("langchain4j.open-ai.timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getInteger("langchain4j.open-ai.max-retries").ifPresent(builder::maxRetries);
        configuration.getBoolean("langchain4j.open-ai.log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("log-responses").ifPresent(builder::logResponses);
        configuration.getString("langchain4j.open-ai.proxy").ifPresent(p -> builder.proxy(BeanResolver.resolve(Proxy.class, BeanName.create(p))));
        Map<String, String> customHeaders = configuration.getMapString("custom-headers");
        if (!customHeaders.isEmpty()) {
            builder.customHeaders(customHeaders);
        }
        return builder.build();
    }
}
