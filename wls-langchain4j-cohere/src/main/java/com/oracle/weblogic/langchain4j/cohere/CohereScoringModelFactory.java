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

package com.oracle.weblogic.langchain4j.cohere;

import java.net.Proxy;
import java.time.Duration;

import com.oracle.weblogic.langchain4j.cdi.BeanName;
import com.oracle.weblogic.langchain4j.cdi.BeanResolver;
import com.oracle.weblogic.langchain4j.cdi.ConditionalProduce;
import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dev.langchain4j.model.cohere.CohereScoringModel;
import dev.langchain4j.model.cohere.CohereScoringModel.CohereScoringModelBuilder;

/**
 * Factory class for creating a configured {@link dev.langchain4j.model.cohere.CohereScoringModel}.
 *
 * <p>This factory automatically registers a bean in the CDI registry if the
 * configuration property <i>langchain4j.cohere.scoring-model.enabled</i> is set to <i>true</i>.</p>
 *
 * @see dev.langchain4j.model.cohere.CohereScoringModel
 */
@ApplicationScoped
public class CohereScoringModelFactory {

    private Configuration configuration;

    // CDI required
    CohereScoringModelFactory() {
    }

    /**
     * Creates CohereScoringModelFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public CohereScoringModelFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link dev.langchain4j.model.cohere.CohereScoringModel} bean in the CDI registry
     * with the name <i>cohereScoringModel</i> if the configuration property
     * <i>langchain4j.cohere.scoring-model.enabled</i> is set to <i>true</i>.
     *
     * @return a configured instance of {@link dev.langchain4j.model.cohere.CohereScoringModel}
     */
    @ConditionalProduce(key = "langchain4j.cohere.scoring-model.enabled", value = "true")
    @Named("cohereScoringModel")
    public CohereScoringModel create() {
        CohereScoringModelBuilder builder = CohereScoringModel.builder();
        configuration.getString("langchain4j.cohere.base-url").ifPresent(builder::baseUrl);
        configuration.getString("langchain4j.cohere.api-key").ifPresent(builder::apiKey);
        configuration.getString("langchain4j.cohere.model-name").ifPresent(builder::modelName);
        configuration.getLong("langchain4j.cohere.timeout").ifPresent(timeout -> builder.timeout(Duration.ofMillis(timeout)));
        configuration.getBoolean("langchain4j.cohere.log-requests").ifPresent(builder::logRequests);
        configuration.getBoolean("langchain4j.cohere.log-responses").ifPresent(builder::logResponses);
        configuration.getInteger("langchain4j.cohere.max-retries").ifPresent(builder::maxRetries);
        configuration.getString("langchain4j.cohere.proxy").ifPresent(p -> builder.proxy(BeanResolver.resolve(Proxy.class, BeanName.create(p))));
        return builder.build();
    }
}
