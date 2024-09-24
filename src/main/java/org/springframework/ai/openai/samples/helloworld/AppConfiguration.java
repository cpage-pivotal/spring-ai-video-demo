package org.springframework.ai.openai.samples.helloworld;

import org.springframework.ai.openai.samples.helloworld.functions.MockWeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class AppConfiguration {

    @Bean
    @Description("Get the weather in location")
    public Function<MockWeatherService.Request, MockWeatherService.Response> weatherFunction() {
        return new MockWeatherService();
    }
}
