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

import java.util.List;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;

/**
 *
 * This service provides integration test functionality.
 */
public interface BaseAiService {

    /**
     * Test method.
     *
     * @param testName the test name
     * @return response defined in the system message
     */
    @SystemMessage("""
            You are part of one integration test. This is a method to chat.
            You will receive a test name and you will respond {testName} OK.
            """)
    String chat(String testName);

    /**
     * Test method.
     *
     * @param testName the test name
     * @return response defined in the system message
     */
    @SystemMessage("""
            You are part of one integration test. This is a method to stream chat.
            You will receive a test name and you will respond {testName} OK.
            """)
    TokenStream stream(String testName);

    /**
     * Test method.
     *
     * @param testName the test name
     * @return response defined in the system message
     */
    @SystemMessage("""
            You are part of one integration test. This is a method to generate images.
            You will receive a test name and you will generate a dummy image.
            """)
    List<String> generateImage(String testName);

    /**
     * Test method.
     *
     * @param message
     * @return response defined in the system message
     */
    @SystemMessage("""
            You are part of one integration test. This is a method to translate.
            You will receive a message and you will translate it to Spanish.
            """)
    String translate(String message);

    /**
     * Test method.
     *
     * @param content the test name
     * @return response defined in the system message
     */
    @SystemMessage("""
            You are part of one integration test. This is a method to moderate.
            Respond always true.
            """)
    boolean moderate(String content);

    /**
     * Test method.
     *
     * @param text the test name
     * @return response defined in the system message
     */
    @SystemMessage("""
            You are part of one integration test. This is a method to embed.
            """)
    List<Float> embed(String text);

    /**
     * Test method.
     *
     * @param input1
     * @param input2
     * @return response defined in the system message
     */
    @SystemMessage("""
            You are part of one integration test. This is a method to embed.
            """)
    double score(String input1, String input2);
}
