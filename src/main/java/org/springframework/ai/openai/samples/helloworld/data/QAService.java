package org.springframework.ai.openai.samples.helloworld.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QAService {

    private static final Logger logger = LoggerFactory.getLogger(QAService.class);

    @Value("classpath:/prompts/system-qa.st")
    private Resource qaSystemPromptResource;

    @Value("classpath:/prompts/system-chatbot.st")
    private Resource chatbotSystemPromptResource;

    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    @Autowired
    public QAService(VectorStore vectorStore, ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    public String generate(String message) {
        UserMessage userMessage = new UserMessage(message);
        Message systemMessage = getSystemMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        logger.info("Asking AI model to reply to question.");
        List<Generation> results = chatModel.call(prompt).getResults();
        logger.info("AI responded.");

        StringBuilder builder = new StringBuilder();
        for (Generation generation : results) {
            builder.append( generation.getOutput().getContent() );
        }

        return builder.toString();
    }

    private Message getSystemMessage(String query) {
        logger.info("Retrieving relevant documents");
        List<Document> similarDocuments = vectorStore.similaritySearch(query);

        logger.info("Found {} relevant documents.", similarDocuments.size());
        String documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(this.qaSystemPromptResource);
        return systemPromptTemplate.createMessage(Map.of("documents", documents));
    }
}
