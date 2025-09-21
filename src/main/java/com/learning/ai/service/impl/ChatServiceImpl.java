package com.learning.ai.service.impl;

import com.learning.ai.dto.Response;
import com.learning.ai.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    @Qualifier("deepseek-general")
    private ChatClient chatClient;

    @Override
    public String chat(String query) {
        String response = chatClient.prompt().user(query).call().content();
        return response;
    }

    @Override
    public String chatJavaCode(String query) {
        String response = chatClient.prompt()
                .user(query)
                .system("Act as java expert")
                .call()
                .content();
        return response;
    }

    @Override
    public String chatPython(String query) {
        Prompt prompt = new Prompt(query);

        return "";
    }

    @Override
    public String chatWithCompleteResponse(String query) {
        ChatResponse response = chatClient.prompt()
                .user(query)
                .system("Act as java expert")
                .call()
                .chatResponse();
        System.out.println(response.getMetadata());
        System.out.println(response.getResult());
        System.out.println(response.getResult().getOutput());

        return response.getResult().getOutput().getText();
    }

    @Override
    public Response chatWithResponseMapping(String query) {
        Response response = chatClient.prompt(query)
                .call()
                .entity(Response.class);
        return response;
    }
}
