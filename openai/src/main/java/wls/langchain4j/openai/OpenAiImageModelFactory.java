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
import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dev.langchain4j.model.openai.OpenAiImageModel;
import wls.langchain4j.cdi.BeanName;
import wls.langchain4j.cdi.BeanResolver;
import wls.langchain4j.cdi.ConditionalProduce;
import wls.langchain4j.cdi.ConfigurationProvider;

/**
 * Factory class for creating a configured {@link OpenAiImageModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the configuration property
 * <i>langchain4j.open-ai.image-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see OpenAiImageModel
 * @see OpenAiImageModelConfig
 */
@ApplicationScoped
public class OpenAiImageModelFactory {

    private BeanResolver beanResolver;

    // Required by CDI
    OpenAiImageModelFactory() {
    }

    /**
     * Creates {@link io.helidon.integrations.langchain4j.providers.ollama.OpenAiImageModelFactory}.
     *
     * @param beanResolver helper class containing methods for CDI beans resolving
     */
    @Inject
    public OpenAiImageModelFactory(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    /**
     * Registers and produces a configured {@link OpenAiImageModel} bean in the CDI registry with the name
     * <i>openAiImageModel</i> if the configuration property <i>langchain4j.open-ai.image-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link OpenAiImageModel}
     */
    @ConditionalProduce(key = "langchain4j.open-ai.image-model.enabled", value = "true")
    @Named("openAiImageModel")
    public OpenAiImageModel create() {
        var config = MpConfig.toHelidonConfig(ConfigurationProvider.getConfig()).get(OpenAiImageModelConfig.CONFIG_ROOT);
        return create(OpenAiImageModelConfig.create(config));
    }

    /**
     * Creates and returns an {@link OpenAiImageModel} configured using the provided configuration.
     *
     * @param config the properties used to configure the {@link OpenAiImageModel}
     * @return a configured instance of {@link OpenAiImageModel}
     */
    public OpenAiImageModel create(OpenAiImageModelConfig config) {
        var builder = OpenAiImageModel.builder();
        config.baseUrl().ifPresent(builder::baseUrl);
        config.apiKey().ifPresent(builder::apiKey);
        config.organizationId().ifPresent(builder::organizationId);
        config.modelName().ifPresent(builder::modelName);
        config.size().ifPresent(builder::size);
        config.quality().ifPresent(builder::quality);
        config.style().ifPresent(builder::style);
        config.user().ifPresent(builder::user);
        config.responseFormat().ifPresent(builder::responseFormat);
        config.timeout().ifPresent(builder::timeout);
        config.maxRetries().ifPresent(builder::maxRetries);
        config.logRequests().ifPresent(builder::logRequests);
        config.logResponses().ifPresent(builder::logResponses);
        config.withPersisting().ifPresent(builder::withPersisting);
        config.persistTo().ifPresent(value -> builder.persistTo(Path.of(value)));
        config.proxy().ifPresent(p -> builder.proxy(beanResolver.resolve(Proxy.class, BeanName.create(p))));
        if (!config.customHeaders().isEmpty()) {
            builder.customHeaders(config.customHeaders());
        }
        return builder.build();
    }
}
