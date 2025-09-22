package com.learning.ai.repository;

import com.learning.ai.model.ChatMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    default List<ChatMessage> findNBySessionId(String sessionId, int n){
        return findAllBySessionId(sessionId, PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "timestamp")));
    }

    @Query("{'userId': ?0, 'chatSessionId': ?1}")
    List<ChatMessage> findByUserIdAndSessionId(String userId, String sessionId, Pageable pageable);

    @Query("{'chatSessionId': ?0}")
    List<ChatMessage> findAllBySessionId(String sessionId, Pageable pageable);

}
