package org.springframework.ai.openai.samples.helloworld.functions;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/function")
public class FunctionController {

    private final OpenAiChatClient openAiChatClient;

    public FunctionController(OpenAiChatClient openAiChatClient) {
        this.openAiChatClient = openAiChatClient;
    }

    @GetMapping
    public Map completion(@RequestParam(value = "question") String question) {
        String answer = this.generate(question);
        Map map = new LinkedHashMap();
        map.put("question", question);
        map.put("answer", answer);
        return map;
    }

    private String generate(String message) {
        UserMessage userMessage = new UserMessage(message);

        ChatResponse response = openAiChatClient.call(new Prompt(List.of(userMessage),
                OpenAiChatOptions.builder().withFunction("weatherFunction").build()));
        return response.getResult().getOutput().getContent();
    }

}
