package org.springframework.ai.openai.samples.helloworld;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.samples.helloworld.functions.MockWeatherService;
import org.springframework.ai.vectorstore.RedisVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Primary;

import java.util.function.Function;

@Configuration
public class AppConfiguration {

    @Bean
    @Description("Get the weather in location")
    public Function<MockWeatherService.Request, MockWeatherService.Response> weatherFunction() {
        return new MockWeatherService();
    }
}
