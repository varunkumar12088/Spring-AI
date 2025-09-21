package com.learning.ai.service;

import com.learning.ai.dto.Response;

public interface ChatService {

    String chat(String query);

    String chatJavaCode(String query);

    String chatPython(String query);

    String chatWithCompleteResponse(String query);

    Response chatWithResponseMapping(String query);

}
