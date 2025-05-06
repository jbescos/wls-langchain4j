# integration
This is an application to quickly verify the integration with different LLMs

Modify `src/main/resources/ai.properties` according with your requirements.

Compile it with: `$ mvn clean package`

Deploy the war generated under: `target/integration.war`

Test OpenAI Chat Model: `http://localhost:7001/integration/open-ai/chat`
Test OpenAI Stream Chat Model: `http://localhost:7001/integration/open-ai/stream`
Test Ollama Chat Model: `http://localhost:7001/integration/ollama/chat`
Test Ollama Stream Chat Model: `http://localhost:7001/integration/ollama/stream`

## Copyright
Copyright (c) 2025, Oracle and/or its affiliates.