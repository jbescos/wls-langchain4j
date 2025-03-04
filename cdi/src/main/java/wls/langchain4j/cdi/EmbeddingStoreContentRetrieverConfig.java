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

import java.util.Objects;
import java.util.Optional;

import io.helidon.builder.api.Prototype;
import io.helidon.common.Generated;
import io.helidon.common.config.Config;

/**
 * Configuration class for {@link io.helidon.integrations.langchain4j.cdi.EmbeddingStoreContentRetrieverConfigBlueprint}.
 *
 * @see dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
 * @see #builder()
 * @see #create()
 */
@Generated(value = "io.helidon.builder.codegen.BuilderCodegen", trigger = "io.helidon.integrations.langchain4j.cdi.EmbeddingStoreContentRetrieverConfigBlueprint")
public interface EmbeddingStoreContentRetrieverConfig extends EmbeddingStoreContentRetrieverConfigBlueprint, Prototype.Api {

    /**
     * Create a new fluent API builder to customize configuration.
     *
     * @return a new builder
     */
    static EmbeddingStoreContentRetrieverConfig.Builder builder() {
        return new EmbeddingStoreContentRetrieverConfig.Builder();
    }

    /**
     * Create a new fluent API builder from an existing instance.
     *
     * @param instance an existing instance used as a base for the builder
     * @return a builder based on an instance
     */
    static EmbeddingStoreContentRetrieverConfig.Builder builder(EmbeddingStoreContentRetrieverConfig instance) {
        return EmbeddingStoreContentRetrieverConfig.builder().from(instance);
    }

    /**
     * Create a new instance from configuration.
     *
     * @param config used to configure the new instance
     * @return a new instance configured from configuration
     */
    static EmbeddingStoreContentRetrieverConfig create(Config config) {
        return EmbeddingStoreContentRetrieverConfig.builder().config(config).buildPrototype();
    }

    /**
     * Create a new instance with default values.
     *
     * @return a new instance
     */
    static EmbeddingStoreContentRetrieverConfig create() {
        return EmbeddingStoreContentRetrieverConfig.builder().buildPrototype();
    }

    /**
     * Fluent API builder base for {@link EmbeddingStoreContentRetrieverConfig}.
     *
     * @param <BUILDER> type of the builder extending this abstract builder
     * @param <PROTOTYPE> type of the prototype interface that would be built by {@link #buildPrototype()}
     */
    abstract class BuilderBase<BUILDER extends EmbeddingStoreContentRetrieverConfig.BuilderBase<BUILDER, PROTOTYPE>, PROTOTYPE extends EmbeddingStoreContentRetrieverConfig> implements Prototype.ConfiguredBuilder<BUILDER, PROTOTYPE> {

        private Config config;
        private Double minScore;
        private Integer maxResults;
        private String displayName;
        private String embeddingModel;
        private String embeddingStore;

        /**
         * Protected to support extensibility.
         */
        protected BuilderBase() {
        }

        /**
         * Update this builder from an existing prototype instance. This method disables automatic service discovery.
         *
         * @param prototype existing prototype to update this builder from
         * @return updated builder instance
         */
        public BUILDER from(EmbeddingStoreContentRetrieverConfig prototype) {
            embeddingStore(prototype.embeddingStore());
            embeddingModel(prototype.embeddingModel());
            displayName(prototype.displayName());
            maxResults(prototype.maxResults());
            minScore(prototype.minScore());
            return self();
        }

        /**
         * Update this builder from an existing prototype builder instance.
         *
         * @param builder existing builder prototype to update this builder from
         * @return updated builder instance
         */
        public BUILDER from(EmbeddingStoreContentRetrieverConfig.BuilderBase<?, ?> builder) {
            builder.embeddingStore().ifPresent(this::embeddingStore);
            builder.embeddingModel().ifPresent(this::embeddingModel);
            builder.displayName().ifPresent(this::displayName);
            builder.maxResults().ifPresent(this::maxResults);
            builder.minScore().ifPresent(this::minScore);
            return self();
        }

        /**
         * Update builder from configuration (node of this type).
         * If a value is present in configuration, it would override currently configured values.
         *
         * @param config configuration instance used to obtain values to update this builder
         * @return updated builder instance
         */
        @Override
        public BUILDER config(Config config) {
            Objects.requireNonNull(config);
            this.config = config;
            config.get("embedding-store").as(String.class).ifPresent(this::embeddingStore);
            config.get("embedding-model").as(String.class).ifPresent(this::embeddingModel);
            config.get("display-name").as(String.class).ifPresent(this::displayName);
            config.get("max-results").as(Integer.class).ifPresent(this::maxResults);
            config.get("min-score").as(Double.class).ifPresent(this::minScore);
            return self();
        }

        /**
         * Clear existing value of this property.
         *
         * @return updated builder instance
         * @see #embeddingStore()
         */
        public BUILDER clearEmbeddingStore() {
            this.embeddingStore = null;
            return self();
        }

        /**
         * Gets the embedding store CDI bean name or "discovery:auto" if the bean must be discovered automatically.
         *
         * @param embeddingStore an {@link java.util.Optional} containing the embedding store bean name or "discovery:auto" if the bean must be
         *                       discovered automatically
         * @return updated builder instance
         * @see #embeddingStore()
         */
        public BUILDER embeddingStore(String embeddingStore) {
            Objects.requireNonNull(embeddingStore);
            this.embeddingStore = embeddingStore;
            return self();
        }

        /**
         * Clear existing value of this property.
         *
         * @return updated builder instance
         * @see #embeddingModel()
         */
        public BUILDER clearEmbeddingModel() {
            this.embeddingModel = null;
            return self();
        }

        /**
         * Gets the embedding model CDI bean name or "discovery:auto" if the bean must be discovered automatically.
         *
         * @param embeddingModel an {@link java.util.Optional} containing the embedding model bean name or "discovery:auto" if the bean must be
         *                       discovered automatically
         * @return updated builder instance
         * @see #embeddingModel()
         */
        public BUILDER embeddingModel(String embeddingModel) {
            Objects.requireNonNull(embeddingModel);
            this.embeddingModel = embeddingModel;
            return self();
        }

        /**
         * Clear existing value of this property.
         *
         * @return updated builder instance
         * @see #displayName()
         */
        public BUILDER clearDisplayName() {
            this.displayName = null;
            return self();
        }

        /**
         * Gets the display name.
         *
         * @param displayName an {@link java.util.Optional} containing the display name if set, otherwise an empty {@link java.util.Optional}
         * @return updated builder instance
         * @see #displayName()
         */
        public BUILDER displayName(String displayName) {
            Objects.requireNonNull(displayName);
            this.displayName = displayName;
            return self();
        }

        /**
         * Clear existing value of this property.
         *
         * @return updated builder instance
         * @see #maxResults()
         */
        public BUILDER clearMaxResults() {
            this.maxResults = null;
            return self();
        }

        /**
         * Gets the maximum number of results.
         *
         * @param maxResults an {@link java.util.Optional} containing the maximum results if set, otherwise an empty {@link java.util.Optional}
         * @return updated builder instance
         * @see #maxResults()
         */
        public BUILDER maxResults(int maxResults) {
            Objects.requireNonNull(maxResults);
            this.maxResults = maxResults;
            return self();
        }

        /**
         * Clear existing value of this property.
         *
         * @return updated builder instance
         * @see #minScore()
         */
        public BUILDER clearMinScore() {
            this.minScore = null;
            return self();
        }

        /**
         * Gets the minimum score threshold.
         *
         * @param minScore an {@link java.util.Optional} containing the minimum score if set, otherwise an empty {@link java.util.Optional}
         * @return updated builder instance
         * @see #minScore()
         */
        public BUILDER minScore(double minScore) {
            Objects.requireNonNull(minScore);
            this.minScore = minScore;
            return self();
        }

        /**
         * Gets the embedding store CDI bean name or "discovery:auto" if the bean must be discovered automatically.
         *
         * @return the embedding store
         */
        public Optional<String> embeddingStore() {
            return Optional.ofNullable(embeddingStore);
        }

        /**
         * Gets the embedding model CDI bean name or "discovery:auto" if the bean must be discovered automatically.
         *
         * @return the embedding model
         */
        public Optional<String> embeddingModel() {
            return Optional.ofNullable(embeddingModel);
        }

        /**
         * Gets the display name.
         *
         * @return the display name
         */
        public Optional<String> displayName() {
            return Optional.ofNullable(displayName);
        }

        /**
         * Gets the maximum number of results.
         *
         * @return the max results
         */
        public Optional<Integer> maxResults() {
            return Optional.ofNullable(maxResults);
        }

        /**
         * Gets the minimum score threshold.
         *
         * @return the min score
         */
        public Optional<Double> minScore() {
            return Optional.ofNullable(minScore);
        }

        /**
         * If this instance was configured, this would be the config instance used.
         *
         * @return config node used to configure this builder, or empty if not configured
         */
        public Optional<Config> config() {
            return Optional.ofNullable(config);
        }

        @Override
        public String toString() {
            return "EmbeddingStoreContentRetrieverConfigBuilder{"
                    + "embeddingStore=" + embeddingStore + ","
                    + "embeddingModel=" + embeddingModel + ","
                    + "displayName=" + displayName + ","
                    + "maxResults=" + maxResults + ","
                    + "minScore=" + minScore
                    + "}";
        }

        /**
         * Handles providers and decorators.
         */
        protected void preBuildPrototype() {
        }

        /**
         * Validates required properties.
         */
        protected void validatePrototype() {
        }

        /**
         * Gets the embedding store CDI bean name or "discovery:auto" if the bean must be discovered automatically.
         *
         * @param embeddingStore an {@link java.util.Optional} containing the embedding store bean name or "discovery:auto" if the bean must be
         *                       discovered automatically
         * @return updated builder instance
         * @see #embeddingStore()
         */
        BUILDER embeddingStore(Optional<String> embeddingStore) {
            Objects.requireNonNull(embeddingStore);
            this.embeddingStore = embeddingStore.map(java.lang.String.class::cast).orElse(this.embeddingStore);
            return self();
        }

        /**
         * Gets the embedding model CDI bean name or "discovery:auto" if the bean must be discovered automatically.
         *
         * @param embeddingModel an {@link java.util.Optional} containing the embedding model bean name or "discovery:auto" if the bean must be
         *                       discovered automatically
         * @return updated builder instance
         * @see #embeddingModel()
         */
        BUILDER embeddingModel(Optional<String> embeddingModel) {
            Objects.requireNonNull(embeddingModel);
            this.embeddingModel = embeddingModel.map(java.lang.String.class::cast).orElse(this.embeddingModel);
            return self();
        }

        /**
         * Gets the display name.
         *
         * @param displayName an {@link java.util.Optional} containing the display name if set, otherwise an empty {@link java.util.Optional}
         * @return updated builder instance
         * @see #displayName()
         */
        BUILDER displayName(Optional<String> displayName) {
            Objects.requireNonNull(displayName);
            this.displayName = displayName.map(java.lang.String.class::cast).orElse(this.displayName);
            return self();
        }

        /**
         * Gets the maximum number of results.
         *
         * @param maxResults an {@link java.util.Optional} containing the maximum results if set, otherwise an empty {@link java.util.Optional}
         * @return updated builder instance
         * @see #maxResults()
         */
        BUILDER maxResults(Optional<Integer> maxResults) {
            Objects.requireNonNull(maxResults);
            this.maxResults = maxResults.map(java.lang.Integer.class::cast).orElse(this.maxResults);
            return self();
        }

        /**
         * Gets the minimum score threshold.
         *
         * @param minScore an {@link java.util.Optional} containing the minimum score if set, otherwise an empty {@link java.util.Optional}
         * @return updated builder instance
         * @see #minScore()
         */
        BUILDER minScore(Optional<Double> minScore) {
            Objects.requireNonNull(minScore);
            this.minScore = minScore.map(java.lang.Double.class::cast).orElse(this.minScore);
            return self();
        }

        /**
         * Generated implementation of the prototype, can be extended by descendant prototype implementations.
         */
        protected static class EmbeddingStoreContentRetrieverConfigImpl implements EmbeddingStoreContentRetrieverConfig {

            private final Optional<Double> minScore;
            private final Optional<Integer> maxResults;
            private final Optional<String> displayName;
            private final Optional<String> embeddingModel;
            private final Optional<String> embeddingStore;

            /**
             * Create an instance providing a builder.
             *
             * @param builder extending builder base of this prototype
             */
            protected EmbeddingStoreContentRetrieverConfigImpl(EmbeddingStoreContentRetrieverConfig.BuilderBase<?, ?> builder) {
                this.embeddingStore = builder.embeddingStore();
                this.embeddingModel = builder.embeddingModel();
                this.displayName = builder.displayName();
                this.maxResults = builder.maxResults();
                this.minScore = builder.minScore();
            }

            @Override
            public Optional<String> embeddingStore() {
                return embeddingStore;
            }

            @Override
            public Optional<String> embeddingModel() {
                return embeddingModel;
            }

            @Override
            public Optional<String> displayName() {
                return displayName;
            }

            @Override
            public Optional<Integer> maxResults() {
                return maxResults;
            }

            @Override
            public Optional<Double> minScore() {
                return minScore;
            }

            @Override
            public String toString() {
                return "EmbeddingStoreContentRetrieverConfig{"
                        + "embeddingStore=" + embeddingStore + ","
                        + "embeddingModel=" + embeddingModel + ","
                        + "displayName=" + displayName + ","
                        + "maxResults=" + maxResults + ","
                        + "minScore=" + minScore
                        + "}";
            }

            @Override
            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                }
                if (!(o instanceof EmbeddingStoreContentRetrieverConfig other)) {
                    return false;
                }
                return Objects.equals(embeddingStore, other.embeddingStore())
                    && Objects.equals(embeddingModel, other.embeddingModel())
                    && Objects.equals(displayName, other.displayName())
                    && Objects.equals(maxResults, other.maxResults())
                    && Objects.equals(minScore, other.minScore());
            }

            @Override
            public int hashCode() {
                return Objects.hash(embeddingStore, embeddingModel, displayName, maxResults, minScore);
            }

        }

    }

    /**
     * Fluent API builder for {@link EmbeddingStoreContentRetrieverConfig}.
     */
    class Builder extends EmbeddingStoreContentRetrieverConfig.BuilderBase<EmbeddingStoreContentRetrieverConfig.Builder, EmbeddingStoreContentRetrieverConfig> implements io.helidon.common.Builder<EmbeddingStoreContentRetrieverConfig.Builder, EmbeddingStoreContentRetrieverConfig> {

        private Builder() {
        }

        @Override
        public EmbeddingStoreContentRetrieverConfig buildPrototype() {
            preBuildPrototype();
            validatePrototype();
            return new EmbeddingStoreContentRetrieverConfigImpl(this);
        }

        @Override
        public EmbeddingStoreContentRetrieverConfig build() {
            return buildPrototype();
        }

    }

}
