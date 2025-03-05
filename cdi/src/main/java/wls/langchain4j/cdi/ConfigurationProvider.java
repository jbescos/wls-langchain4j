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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Provides the loaded {@link Configuration} of ai.properties.
 */
@ApplicationScoped
public class ConfigurationProvider {

    private static final Logger LOGGER = Logger.getLogger(ConfigurationProvider.class.getName());

    // Required by CDI
    protected ConfigurationProvider() {
    }

    @Produces
    @Singleton
    public Configuration configuration() {
        Properties config = new Properties();
        try {
            StringBuilder builder = new StringBuilder();
            config.load(ConfigurationProvider.class.getResourceAsStream("/ai.properties"));
            config.forEach((key, value) -> builder.append("\n").append(key + " = " + value));
            LOGGER.finest(builder.toString());
            return new Configuration(config);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load ai.properties", e);
        }
    }

    /**
     * Utility class to obtain the property values.
     */
    public static class Configuration {

        private final Properties config;

        private Configuration(Properties config) {
            this.config = config;
        }

        /**
         * Returns an optional of the property value for the specified key.
         * 
         * @param key the property key
         * @return the String value
         */
        public Optional<String> getString(String key) {
            return get(key, String.class);
        }

        /**
         * Returns an optional of the property value for the specified key.
         * 
         * @param key the property key
         * @return the Double value
         */
        public Optional<Double> getDouble(String key) {
            return get(key, Double.class);
        }

        /**
         * Returns an optional of the property value for the specified key.
         * 
         * @param key the property key
         * @return the Boolean value
         */
        public Optional<Boolean> getBoolean(String key) {
            return get(key, Boolean.class);
        }

        /**
         * Returns an optional of the property value for the specified key.
         * 
         * @param key the property key
         * @return the Integer value
         */
        public Optional<Integer> getInteger(String key) {
            return get(key, Integer.class);
        }

        /**
         * Returns an optional of the property value for the specified key.
         * 
         * @param key the property key
         * @return the Long value
         */
        public Optional<Long> getLong(String key) {
            return get(key, Long.class);
        }

        private <T> Optional<T> get(String key, Class<T> type) {
            String value = config.getProperty(key);
            if (value == null) {
                return Optional.empty();
            }
            Object castValue = castValue(value, type);
            return Optional.of(type.cast(castValue));
        }

        public List<String> getList(String key) {
            String value = config.getProperty(key);
            return value != null ? Arrays.asList(value.split(",")) : Collections.emptyList();
        }

        public Map<String, Integer> getMapInteger(String key) {
            String value = config.getProperty(key);
            if (value == null || value.isEmpty()) {
                return Collections.emptyMap();
            }
            Map<String, Integer> map = new HashMap<>();
            String[] entries = value.split(",");
            for (String entry : entries) {
                String[] kv = entry.split("=");
                if (kv.length == 2) {
                    map.put(kv[0].trim(), Integer.parseInt(kv[1].trim()));
                }
            }
            return map;
        }

        public Map<String, String> getMapString(String key) {
            String value = config.getProperty(key);
            if (value == null || value.isEmpty()) {
                return Collections.emptyMap();
            }
            Map<String, String> map = new HashMap<>();
            String[] entries = value.split(",");
            for (String entry : entries) {
                String[] kv = entry.split("=");
                if (kv.length == 2) {
                    map.put(kv[0].trim(), kv[1].trim());
                }
            }
            return map;
        }

        private Object castValue(String value, Class<?> type) {
            if (type == String.class) {
                return value;
            } else if (type == Integer.class) {
                return Integer.parseInt(value);
            } else if (type == Long.class) {
                return Long.parseLong(value);
            } else if (type == Double.class) {
                return Double.parseDouble(value);
            } else if (type == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }
}
