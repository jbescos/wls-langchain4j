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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * REST resource for interacting with the AI-powered resources.
 *
 */
@ApplicationScoped
@Path("/chat")
public class ChatResource {

    private OpenAiChatService openAiChatService;
    private OllamaChatService ollamaChatService;

    // Required by CDI
    protected ChatResource() {
    }

    /**
     * Constructs a ChatResource instance.
     *
     */
    @Inject
    public ChatResource(OpenAiChatService chatAiService, OllamaChatService ollamaChatService) {
        this.openAiChatService = chatAiService;
        this.ollamaChatService = ollamaChatService;
    }

    /**
     * Handles chat requests by forwarding the test name to the AI assistant.
     *
     *
     * @param testName the test name
     * @return the AI assistant's response in plain text
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/open-ai")
    public String openAi(@QueryParam("testName") String testName) {
        return openAiChatService.chatModel(testName);
    }

    /**
     * Handles chat requests by forwarding the test name to the AI assistant.
     *
     *
     * @param testName the test name
     * @return the AI assistant's response in plain text
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/ollama")
    public String ollama(@QueryParam("testName") String testName) {
        return ollamaChatService.chatModel(testName);
    }
}
