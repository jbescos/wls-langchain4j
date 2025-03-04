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

package wls.langchain4j.cdi;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * Factory class for creating a configured {@link EmbeddingStoreContentRetriever}.
 **
 * @see EmbeddingStoreContentRetriever
 * @see EmbeddingStoreContentRetrieverConfig
 */
@ApplicationScoped
public class EmbeddingStoreContentRetrieverFactory {

    private BeanResolver beanResolver;

    // Required for CDI
    protected EmbeddingStoreContentRetrieverFactory() {
    }

    /**
     * Creates {@link EmbeddingStoreContentRetrieverFactory}.
     *
     * @param beanResolver helper class containing methods for CDI beans resolving
     */
    @Inject
    EmbeddingStoreContentRetrieverFactory(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
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
        var config = MpConfig.toHelidonConfig(ConfigProvider.getConfig()).get(EmbeddingStoreContentRetrieverConfig.CONFIG_ROOT);
        return create(EmbeddingStoreContentRetrieverConfig.create(config));
    }

    /**
     * Creates and returns an {@link EmbeddingStoreContentRetriever} configured using the provided configuration.
     *
     * @param config the configuration bean
     * @return a configured instance of {@link EmbeddingStoreContentRetriever}
     */
    public EmbeddingStoreContentRetriever create(EmbeddingStoreContentRetrieverConfig config) {
        var builder = EmbeddingStoreContentRetriever.builder();

        config.embeddingModel().ifPresent(em -> builder.embeddingModel(
                beanResolver.resolve(EmbeddingModel.class, BeanName.create(em))));

        var typeLiteral = new TypeLiteral<EmbeddingStore<TextSegment>>() {};
        config.embeddingStore().ifPresent(es -> builder.embeddingStore(
                beanResolver.resolve(typeLiteral, BeanName.create(es))));

        config.displayName().ifPresent(builder::displayName);
        config.maxResults().ifPresent(builder::maxResults);
        config.minScore().ifPresent(builder::minScore);

        return builder.build();
    }
}
