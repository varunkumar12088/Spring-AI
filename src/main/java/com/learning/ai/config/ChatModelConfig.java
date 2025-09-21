package com.learning.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ChatModelConfig {


    @Bean(name = "deepseek-general")
    public ChatClient deepseekClientGeneral(OllamaChatModel chatModel, ChatMemory chatMemory){
        System.out.println("ChatMemory ==> " + chatMemory.getClass().getName());
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(), chatMemoryAdvisor)
                .defaultOptions(OllamaOptions.builder()
                        .model("deepseek-r1:latest")
                        .temperature(0.4)
                        .build())
                .build();
    }

    @Bean(name = "deepseek-coding")
    public ChatClient deepseekClientCoding(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultSystem("You are a helpful coding assistant, you are an expert in coding.")
                .defaultOptions(OllamaOptions.builder()
                        .model("deepseek-r1:latest")
                        .temperature(0.4)
                        .build())
                .build();
    }

    @Bean(name = "deepseek-coding-java")
    public ChatClient deepseekClient(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful coding assistant, you are an expert in coding, you are an expert in coding and you are a java expert, generate code and explain always in java")
                .defaultOptions(OllamaOptions.builder()
                        .model("deepseek-r1:latest")
                        .temperature(0.4)
                        .build())
                .build();
    }

    @Bean(name = "gpt-oss-general")
    public ChatClient gptClient(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel)
                .defaultOptions(OllamaOptions.builder()
                        .model("gpt-oss:latest")
                        .temperature(0.4)
                        .build())
                .build();
    }

    @Bean(name = "gpt-oss-coding")
    public ChatClient gptClientCoding(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful coding assistant.")
                .defaultOptions(OllamaOptions.builder()
                        .temperature(0.4)
                        .model("gpt-oss:latest")
                        .build())
                .build();
    }

    @Bean(name = "gpt-oss-java")
    public ChatClient gptClientJava(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful coding assistant, you are an expert in coding and you are a java expert, generate code and explain always in java")
                .defaultOptions(OllamaOptions.builder()
                        .temperature(0.4)
                        .model("gpt-oss:latest")
                        .build())
                .build();
    }
}
