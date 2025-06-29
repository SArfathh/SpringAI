package org.arfath.springai.controller;

import org.arfath.springai.Rank;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.util.List;

@RestController
public class chatcontroller {
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private ChatClient chatClient;
    private OpenAiAudioApi openAiAudioApi;

    public chatcontroller(ChatClient chatClient, OpenAiAudioApi openAiAudioApi, OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel) {
        this.chatClient = chatClient;
        this.openAiAudioApi = openAiAudioApi;
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
    }

    @GetMapping("/chat")
    public String chat(){
        return chatClient.prompt()
                .user("what are best java frameworks ")
                .call()
                .content();
    }
    @GetMapping("/rank")
    public List<Rank> chat1(){
        return chatClient.prompt()
                .user("what are best java frameworks ")
                .call()
                .entity(new ParameterizedTypeReference<List<Rank>>() {
                });

    }

    @GetMapping("/images/describe")
    public String describeImage(){
        ClassPathResource image1 = new ClassPathResource("dog.jpeg");
        ClassPathResource image2 = new ClassPathResource("girl.jpeg");
        UserMessage message = UserMessage.builder()
                .text("describe these images")
                .media(List.of(
                        new Media(MimeTypeUtils.IMAGE_JPEG,image1),
                        new Media(MimeTypeUtils.IMAGE_JPEG,image2)

                )).build();
        return chatClient.prompt()
                .messages(message)
                .call()
                .content();

    }

    @GetMapping("/openai/transcribe")
    public String transcribe(){
        try {
            ClassPathResource audio = new ClassPathResource("test-audio.m4a");
            if (!audio.exists() || audio.contentLength() == 0) {
                return "Audio file not found or empty!";
            }


            OpenAiAudioTranscriptionOptions options =
                    OpenAiAudioTranscriptionOptions.builder()
                            .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT)
                            .temperature(0f)
                            .language("en")
                            .build();

//            AudioTranscriptionPrompt prompt =
//                    new AudioTranscriptionPrompt(audio, options);
            AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audio,options);
            return openAiAudioTranscriptionModel.call(prompt).getResult().getOutput();
        }catch (Exception e){
            e.printStackTrace();
            return "Error transcribing audio" + e.getMessage();
        }

    }



}
