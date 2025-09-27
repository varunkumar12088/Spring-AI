package com.learning.ai.service.impl;

import com.learning.ai.advisor.ChatMessageAdvisor;
import com.learning.ai.repository.ChatMessageRepository;
import com.learning.ai.service.ChatMessageService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("chatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    @Qualifier("gpt-oss-general")
    private ChatClient generalClient;

    @Override
    public String chat(String query, String userId, String conversionId) {
        ChatMessageAdvisor chatMessageAdvisor = ChatMessageAdvisor.builder(messageRepository)
                .conversationId(conversionId)
                .userId(userId)
                .maxCount(10)
                .order(1)
                .build();
        String response = generalClient.prompt().advisors(chatMessageAdvisor).call().content();
        return response;
    }
}
