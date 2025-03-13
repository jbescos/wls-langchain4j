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
package com.oracle.weblogic.langchain4j.integration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import dev.langchain4j.service.TokenStream;

/**
 * REST resource for interacting with the AI-powered resources.
 *
 */
@ApplicationScoped
@Path("/")
public class Resource {

    private static final Logger LOGGER = Logger.getLogger(Resource.class.getName());
    private OpenAiService openAiService;
    private OllamaService ollamaService;

    // Required by CDI
    Resource() {
    }

    /**
     * Constructs a ChatResource instance.
     *
     */
    @Inject
    public Resource(OpenAiService chatAiService, OllamaService ollamaChatService) {
        this.openAiService = chatAiService;
        this.ollamaService = ollamaChatService;
    }

    /**
     * Handles requests by forwarding to the AI assistant.
     *
     * @return the AI assistant's response in plain text
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/open-ai/chat")
    public String openAiChat() {
        return chat(openAiService);
    }

    /**
     * Handles requests by forwarding to the AI assistant.
     *
     * @return the AI assistant's response in plain text
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/open-ai/stream")
    public String openAiStream() {
        return stream(openAiService);
    }

    /**
     * Handles requests by forwarding to the AI assistant.
     *
     * @return the AI assistant's response in plain text
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/ollama/chat")
    public String ollama() {
        return chat(ollamaService);
    }

    /**
     * Handles requests by forwarding to the AI assistant.
     *
     * @return the AI assistant's response in plain text
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/ollama/stream")
    public String ollamaStream() {
        return stream(ollamaService);
    }

    private String stream(BaseAiService service) {
        try {
            TokenStream stream = service.stream("Stream model");
            AtomicReference<String> ref = new AtomicReference<>();
            CountDownLatch latch = new CountDownLatch(1);
            stream.onNext(s -> LOGGER.info(s));
            stream.onError(t -> LOGGER.log(Level.SEVERE, "Stream failed", t));
            stream.onComplete(r -> {
               ref.set(r.content().text());
               LOGGER.info(r.content().text());
               latch.countDown();
            });
            stream.start();
            latch.await(20000, TimeUnit.MILLISECONDS);
            if (ref.get() != null) {
                return ref.get();
            } else {
                return "failed";
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Stream failed", e);
            return "ERROR: " + e.getMessage();
        }
    }

    private String chat(BaseAiService service) {
        try {
            return service.chat("Chat model");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Stream failed", e);
            return "ERROR: " + e.getMessage();
        }
    }
}
