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

package com.oracle.weblogic.langchain4j.oracle;

import com.oracle.weblogic.langchain4j.cdi.BeanResolver;
import com.oracle.weblogic.langchain4j.cdi.ConditionalProduce;
import com.oracle.weblogic.langchain4j.cdi.ConfigurationProvider.Configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import javax.sql.DataSource;

import dev.langchain4j.store.embedding.oracle.CreateOption;
import dev.langchain4j.store.embedding.oracle.EmbeddingTable;
import dev.langchain4j.store.embedding.oracle.OracleEmbeddingStore;

/**
 * Factory class for creating a configured {@link OracleEmbeddingStore}.
 *
 * @see OracleEmbeddingStore
 * @see OracleEmbeddingStoreConfig
 */
@ApplicationScoped
public class OracleEmbeddingStoreFactory {

    private Configuration configuration;

    // CDI required
    OracleEmbeddingStoreFactory() {
    }

    /**
     * Creates OracleEmbeddingStoreFactory.
     *
     * @param configuration the AI properties.
     */
    @Inject
    public OracleEmbeddingStoreFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Registers and produces a configured {@link OracleEmbeddingStore} bean in the CDI registry with the name
     * <i>openAiChatModel</i> if the configuration property <i>langchain4j.oracle.embedding-store.embedding-store.enabled</i> is set to
     * <i>true</i>.
     *
     * @return a configured instance of {@link OracleEmbeddingStore}
     */
    @ConditionalProduce(key = "langchain4j.oracle.embedding-store.enabled", value = "true")
    @Named("oracleEmbeddingStore")
    public OracleEmbeddingStore create() {
        OracleEmbeddingStore.Builder builder = OracleEmbeddingStore.builder();
        configuration.getString("langchain4j.oracle.embedding-store.data-source").ifPresent(bn -> builder.dataSource(BeanResolver.resolve(DataSource.class, bn)));
        configuration.getBoolean("langchain4j.oracle.embedding-store.embedding-table").ifPresent(b -> {
                if (b) {
                    builder.embeddingTable(creatembeddingTable());
                }
            }
        );
        configuration.getBoolean("langchain4j.oracle.embedding-store.exact-search").ifPresent(builder::exactSearch);
        configuration.getString("langchain4j.oracle.embedding-store.vector-index").ifPresent(v -> builder.vectorIndex(CreateOption.valueOf(v)));

        return builder.build();
    }

    private EmbeddingTable creatembeddingTable() {
        EmbeddingTable.Builder builder = EmbeddingTable.builder();
        configuration.getString("langchain4j.oracle.embedding-store.create-option").ifPresent(v -> builder.createOption(CreateOption.valueOf(v)));
        configuration.getString("langchain4j.oracle.embedding-store.name").ifPresent(builder::name);
        configuration.getString("langchain4j.oracle.embedding-store.id-column").ifPresent(builder::idColumn);
        configuration.getString("langchain4j.oracle.embedding-store.embedding-column").ifPresent(builder::embeddingColumn);
        configuration.getString("langchain4j.oracle.embedding-store.text-column").ifPresent(builder::textColumn);
        configuration.getString("langchain4j.oracle.embedding-store.meta-column").ifPresent(builder::metadataColumn);

        return builder.build();
    }
}
