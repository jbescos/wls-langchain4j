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
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;

import com.oracle.weblogic.langchain4j.cdi.BeanName;
import com.oracle.weblogic.langchain4j.cdi.BeanResolver;
import com.oracle.weblogic.langchain4j.cdi.ConditionalProduce;
import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel.OpenAiImageModelBuilder;

/**
 * Factory class for creating a configured {@link OpenAiImageModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the configuration property
 * <i>langchain4j.open-ai.image-model.image-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiImageModel
 * @see OpenAiImageModelConfig
 */
@ApplicationScoped
public class OpenAiImageModelFactory {

    private Configuration configuration;

    // Required by CDI
    OpenAiImageModelFactory() {
    }

    /**
     * Creates OpenAiImageModelFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public OpenAiImageModelFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link OpenAiImageModel} bean in the CDI registry with the name
     * <i>openAiImageModel</i> if the configuration property <i>langchain4j.open-ai.image-model.image-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link OpenAiImageModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.image-model.enabled", value = "true")
    @Named("openAiImageModel")
    public OpenAiImageModel create() {
        OpenAiImageModelBuilder builder = OpenAiImageModel.builder();
        configuration.getString("langchain4j.open-ai.image-model.base-url").ifPresent(builder::baseUrl);
        configuration.getString("langchain4j.open-ai.image-model.api-key").ifPresent(builder::apiKey);
        configuration.getString("langchain4j.open-ai.image-model.organization-id").ifPresent(builder::organizationId);
        configuration.getString("langchain4j.open-ai.image-model.model-name").ifPresent(builder::modelName);
        configuration.getString("langchain4j.open-ai.image-model.size").ifPresent(builder::size);
        configuration.getString("langchain4j.open-ai.image-model.quality").ifPresent(builder::quality);
        configuration.getString("langchain4j.open-ai.image-model.style").ifPresent(builder::style);
        configuration.getString("langchain4j.open-ai.image-model.user").ifPresent(builder::user);
        configuration.getString("langchain4j.open-ai.image-model.response-format").ifPresent(builder::responseFormat);
        configuration.getLong("langchain4j.open-ai.image-model.timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getInteger("langchain4j.open-ai.image-model.max-retries").ifPresent(builder::maxRetries);
        configuration.getBoolean("langchain4j.open-ai.image-model.log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("langchain4j.open-ai.image-model.log-responses").ifPresent(builder::logResponses);
        configuration.getBoolean("langchain4j.open-ai.image-model.with-persisting").ifPresent(builder::withPersisting);
        configuration.getString("langchain4j.open-ai.image-model.persist-to").ifPresent(value -> builder.persistTo(Path.of(value)));
        configuration.getString("langchain4j.open-ai.image-model.proxy").ifPresent(p -> builder.proxy(BeanResolver.resolve(Proxy.class, BeanName.create(p))));
        Map<String, String> customHeaders = configuration.getMapString("langchain4j.open-ai.image-model.custom-headers");
        if (!customHeaders.isEmpty()) {
            builder.customHeaders(customHeaders);
        }
        return builder.build();
    }
}
