package com.redhat.developers.memory;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;

@Path("/")
public class DeveloperResource {

    @Inject
    AssistantWithMemory assistant;

    @Inject
    ChatLanguageModel model;


    @GET
    @Path("/rest")
    @Produces(MediaType.TEXT_PLAIN)
    public String createRestEndpoint() {
        Tokenizer tokenizer = new OpenAiTokenizer(GPT_3_5_TURBO);
        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(1000, tokenizer);

        UserMessage userMessage1 = userMessage("How to write a REST endpoint in Java using Quarkus?");
        chatMemory.add(userMessage1);

        System.out.println("User: " + userMessage1.text() + System.lineSeparator());

        Response<AiMessage> response1 = model.generate(chatMemory.messages());
        chatMemory.add(response1.content());

        System.out.println("LLM: " + response1.content().text() + System.lineSeparator());

        UserMessage userMessage2 = userMessage("Create a test of the first point using Quarkus. " +
                "Be short, 15 lines of code maximum.");
        chatMemory.add(userMessage2);

        System.out.println("User: " + userMessage2.text() + System.lineSeparator());

        Response<AiMessage> response2 = model.generate(chatMemory.messages());

        System.out.println("LLM: " + response2.content().text() + System.lineSeparator());

        return "Check Quarkus Log for results";
    }


    //BEST APPROACH, more clear and is not coupled with OpenAI model like the previous one,
    // we don't need to add the messages to the memory manually,
    // and we don't need to work with UserMessage and AiMessage objects, we work only with strings.
    @GET
    @Path("/k8s")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateKubernetes(){

        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model)
                .build();

        String userMessage1 = "Can you give a brief explanation of kubernetes, 3 lines max ?";
        System.out.println("User: " + userMessage1 + System.lineSeparator());

        String answer1 = chain.execute(userMessage1);
        System.out.println("LLM: " + answer1 + System.lineSeparator());

        String userMessage2 = "Can you give me a YAML example to deploy an application for that ?";
        System.out.println("User: " + userMessage2 + System.lineSeparator());

        String answer2 = chain.execute(userMessage2);
        System.out.println("LLM: " + answer2 + System.lineSeparator());

        return "Check Kubernetes Log for results";
    }


    @GET
    @Path("/memory")
    @Produces(MediaType.TEXT_PLAIN)
    public String memory() {
        String msg1 = "How do I write a REST endpoint in Java using Quarkus?";

        String response = "[User]: " + msg1 + "\n\n" +
                "[LLM]: "+ assistant.chat(1, msg1) + "\n\n\n" +
                "------------------------------------------\n\n\n";

        String msg2 = "Create a test of the first step. " +
                "Be short, 15 lines of code maximum.";

        response += "[User]: " + msg2 + "\n\n"+
                "[LLM]: "+ assistant.chat(1, msg2);

        return response;

    }

    @GET
    @Path("/guess")
    @Produces(MediaType.TEXT_PLAIN)
    public String guess() {
        String msg1FromUser1 = "Hello, my name is Klaus and I'm a doctor";

        String response = "[User1]: " + msg1FromUser1 + "\n\n" +
                "[LLM]: " + assistant.chat(1, msg1FromUser1) + "\n\n\n" +
                "------------------------------------------\n\n\n";

        String msg1FromUser2 = "Hi, I'm Francine and I'm a lawyer";

        response += "[User2]: " + msg1FromUser2 + "\n\n" +
                "[LLM]: " + assistant.chat(2, msg1FromUser2) + "\n\n\n" +
                "------------------------------------------\n\n\n";

        String msg2FromUser2 = "What is my name?";

        response += "[User2]: " + msg2FromUser2 + "\n\n" +
                "[LLM]: " + assistant.chat(2, msg2FromUser2) + "\n\n\n" +
                "------------------------------------------\n\n\n";

        String msg2FromUser1 = "What is my profession?";

        response += "[User1]: " + msg2FromUser1 + "\n\n" +
                "[LLM]: " + assistant.chat(1, msg2FromUser1) + "\n\n\n" +
                "------------------------------------------\n\n\n";

        return response;
    }
}