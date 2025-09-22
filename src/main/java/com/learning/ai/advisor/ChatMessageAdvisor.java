package com.learning.ai.advisor;

import com.learning.ai.constant.MessageType;
import com.learning.ai.model.ChatMessage;
import com.learning.ai.repository.ChatMessageRepository;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class ChatMessageAdvisor implements BaseChatMemoryAdvisor {

    private final ChatMessageRepository chatMessageRepository;

    private final String conversationId;
    private final String userId;

    private final int order;
    private final int maxCount;
    private final Scheduler scheduler;

    private ChatMessageAdvisor(ChatMessageRepository chatMessageRepository,
                               String conversationId,
                               String userId,
                               int order, int maxCount,
                               Scheduler scheduler) {
        Assert.notNull(chatMessageRepository, "chatMessageRepository cannot be null");
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Assert.hasText(userId, "userId cannot be null or empty");
        Assert.isTrue(maxCount > 0, "History count can't be zero or negative number");
        Assert.notNull(scheduler, "scheduler cannot be null");

        this.chatMessageRepository = chatMessageRepository;
        this.conversationId = conversationId;
        this.userId = userId;
        this.order = order;
        this.maxCount = maxCount;
        this.scheduler = scheduler;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest request, AdvisorChain advisorChain) {

        List<ChatMessage> lastMessages = chatMessageRepository.findNBySessionId(conversationId, this.maxCount);

        List<Message> processedMessages = Stream.iterate(lastMessages.size() - 1, index -> index >= 0, index -> index - 1)
                .map(lastMessages::get)
                .map(chatMessage -> (Message) switch (chatMessage.getMessageType()) {
                    case USER      -> new UserMessage(chatMessage.getMessage());
                    case ASSISTANT -> new AssistantMessage(chatMessage.getMessage());
                }).toList();

        List<Message> userMessages = request.prompt().getInstructions();
        processedMessages.addAll(userMessages);

        save(userMessages, MessageType.USER);

        ChatClientRequest processedChatClientRequest = request.mutate()
                .prompt(request.prompt().mutate().messages(processedMessages).build())
                .build();

        return processedChatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        List<Message> assistantMessages = new ArrayList<>();
        if (chatClientResponse.chatResponse() != null) {
            assistantMessages = chatClientResponse.chatResponse()
                    .getResults()
                    .stream()
                    .map(g -> (Message) g.getOutput())
                    .toList();
        }

        save(assistantMessages, MessageType.ASSISTANT);

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
                                                 StreamAdvisorChain streamAdvisorChain) {
        Scheduler scheduler = this.getScheduler();

        // Process the request with the before method
        return Mono.just(chatClientRequest)
                .publishOn(scheduler)
                .map(request -> this.before(request, streamAdvisorChain))
                .flatMapMany(streamAdvisorChain::nextStream)
                .transform(flux -> new ChatClientMessageAggregator().aggregateChatClientResponse(flux,
                        response -> this.after(response, streamAdvisorChain)));
    }

    private void save(List<Message> messages, MessageType messageType){
        Instant now = Instant.now();
        List<ChatMessage> chatMessages = messages.stream()
                .map(msg -> ChatMessage.builder()
                        .id(UUID.randomUUID().toString())
                        .chatSessionId(conversationId)
                        .messageType(messageType)
                        .message(msg.getText())
                        .timestamp(now)
                        .userId(userId)
                        .build())
                .toList();

        chatMessageRepository.saveAll(chatMessages);
    }

    public static Builder builder(ChatMessageRepository chatMessageRepository){
        return new Builder(chatMessageRepository);
    }

    public static final class Builder {

        private String conversationId = ChatMemory.DEFAULT_CONVERSATION_ID;
        private String userId = "default";

        private int order = Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER;
        private Scheduler scheduler = BaseAdvisor.DEFAULT_SCHEDULER;

        private ChatMessageRepository chatMessageRepository;

        private Integer maxCount = 10;

        private Builder(ChatMessageRepository chatMessageRepository) {
            this.chatMessageRepository = chatMessageRepository;
        }

        public ChatMessageAdvisor.Builder conversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public ChatMessageAdvisor.Builder userId(String userId){
            this.userId = userId;
            return this;
        }

        public ChatMessageAdvisor.Builder order(int order) {
            this.order = order;
            return this;
        }

        public ChatMessageAdvisor.Builder scheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public ChatMessageAdvisor.Builder maxCount(Integer maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public ChatMessageAdvisor build() {
            return new ChatMessageAdvisor(this.chatMessageRepository,
                    this.conversationId, this.userId,
                    this.order, this.maxCount, this.scheduler);
        }
    }
}
