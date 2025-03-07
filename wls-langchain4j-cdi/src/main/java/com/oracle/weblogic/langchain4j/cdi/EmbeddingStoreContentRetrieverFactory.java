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

package com.oracle.weblogic.langchain4j.cdi;

import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever.EmbeddingStoreContentRetrieverBuilder;
import dev.langchain4j.store.embedding.EmbeddingStore;

/**
 * Factory class for creating a configured {@link EmbeddingStoreContentRetriever}.
 **
 * @see EmbeddingStoreContentRetriever
 * @see EmbeddingStoreContentRetrieverConfig
 */
@ApplicationScoped
public class EmbeddingStoreContentRetrieverFactory {
    private Configuration configuration;

    // Required for CDI
    protected EmbeddingStoreContentRetrieverFactory() {
    }

    /**
     * Creates EmbeddingStoreContentRetrieverFactory.
     *
     * @param beanResolver helper class containing methods for CDI beans resolving
     */
    @Inject
    EmbeddingStoreContentRetrieverFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link EmbeddingStoreContentRetriever} bean in the CDI registry with the name
     * <i>embeddingStoreContentRetriever</i> if the configuration property
     * <i>langchain4j.rag.embedding-store-content-retriever.enabled</i> is set to <i>true</i>.
     **
     * @return a configured instance of {@link EmbeddingStoreContentRetriever}
     */
    @ConditionalProduce(key = "langchain4j.rag.embedding-store-content-retriever.enabled", value = "true")
    @Named("embeddingStoreContentRetriever")
    public EmbeddingStoreContentRetriever create() {
        EmbeddingStoreContentRetrieverBuilder builder = EmbeddingStoreContentRetriever.builder();
        configuration.getString("langchain4j.rag.embedding-model").ifPresent(em -> builder.embeddingModel(
                BeanResolver.resolve(EmbeddingModel.class, BeanName.create(em))));
        var typeLiteral = new TypeLiteral<EmbeddingStore<TextSegment>>() {};
        configuration.getString("langchain4j.rag.embedding-store").ifPresent(es -> builder.embeddingStore(
                BeanResolver.resolve(typeLiteral, BeanName.create(es))));

        configuration.getString("langchain4j.rag.display-name").ifPresent(builder::displayName);
        configuration.getInteger("langchain4j.rag.max-results").ifPresent(builder::maxResults);
        configuration.getDouble("langchain4j.rag.min-score").ifPresent(builder::minScore);

        return builder.build();
    }
}
