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

package com.oracle.weblogic.langchain4j.samples.crypto.trader;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/trader")
public class TraderResource {

    private static final Logger LOGGER = Logger.getLogger(TraderResource.class.getName());
    private ChatAiService chatAiService;

    // CDI required
    TraderResource() {
    }

    @Inject
    public TraderResource(ChatAiService chatAiService) {
        this.chatAiService = chatAiService;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/chat")
    public String chat(@QueryParam("text") String text) {
        String answer;
        try {
            answer = chatAiService.chat(text);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing: " + text, e);
            answer = "My failure reason is:\n\n" + e.getMessage();
        }

        return answer;
    }
}
