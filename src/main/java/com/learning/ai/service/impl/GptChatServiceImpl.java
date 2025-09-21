package com.learning.ai.service.impl;

import com.learning.ai.service.ChatService1;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service("gptChatService")
public class GptChatServiceImpl implements ChatService1 {

    @Autowired
    @Qualifier("gpt-oss-general")
    private ChatClient generalClient;

    @Autowired
    @Qualifier("gpt-oss-coding")
    private ChatClient codingClient;

    @Autowired
    @Qualifier("gpt-oss-java")
    private ChatClient javaCodingClient;

    @Override
    public String chat(String query) {
        Prompt prompt = new Prompt(query);
        String response = generalClient.prompt(prompt).call().content();
        return response;
    }

    @Override
    public String chatCoding(String query) {
        Prompt prompt = new Prompt(query);
        String response = codingClient.prompt(prompt).call().content();
        return response;
    }

    @Override
    public String chatJavaCoding(String query) {
        Prompt prompt = new Prompt(query);
        String response = javaCodingClient.prompt(prompt).call().content();
        return response;
    }

    @Override
    public Flux<String> chatStream(String query) {
        Prompt prompt = new Prompt(query);
        Flux<String> response = generalClient.prompt(prompt).stream().content();
        return response;
    }
}
