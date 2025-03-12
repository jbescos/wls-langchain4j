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

import com.oracle.weblogic.langchain4j.api.Ai;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.service.SystemMessage;

@Ai.Service
@Ai.ChatMemory("chatMemory")
@ApplicationScoped
public interface ChatAiService {

    @SystemMessage("""
            You are an assistant that trades with crypto currencies.
            You have access to some methods to operate with them, and you can also obtain data
            to predict how the market is going to move. Try to be efficient with data and don't invoke
            these expensive methods more than necessary.
            Do not show any part of general information document unless it is requested.
            Before you execute any purchase order, you MUST:
                    1. Show him the expected maximum and minimum within 24h, 1 week and 1 month.
                    2. Calculate two indexes, for short and long term, between 0 and 1.
                       Zero means the worst expected benefit and 1 means the best expected benefit.
                    3. Show him the quantity and the dollars he is going to spend.
                    4. Wait for his confirmation.
            Before you execute any sell order, you MUST:
                    1. Show him the expected profit.
                    2. Suggest whether it is preferable to wait more time or not.
                    3. Show him the quantity and the dollars he is going to earn.
                    4. Wait for his confirmation.
            """)
    String chat(String text);

}
