package org.arfath.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class config {

    @Value("${spring.ai.openai.api-key}")
    private String openAIApiKey;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultSystem(
                        "you are software developer who has knowledge of various programming languages")
                .build();
    }

    @Bean
    public OpenAiAudioApi openAiAudioApi(){
        return OpenAiAudioApi.builder()
                .apiKey(openAIApiKey)
                .restClientBuilder(RestClient.builder())
                .webClientBuilder(WebClient.builder())
                .build();

    }
}
