package org.springframework.ai.openai.samples.helloworld.simple;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

@RestController
public class ChatController {

	private final ChatClient chatClient;
	private final OpenAiImageModel imageModel;

	public ChatController(ChatClient.Builder chatClientBuilder, OpenAiImageModel imageModel) {
		this.chatClient = chatClientBuilder.build();
		this.imageModel = imageModel;
	}

	@GetMapping("/ai/simple")
	public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		return Map.of("generation", chatClient.prompt().user(message).call().content());
	}

	@GetMapping("/ai/image")
	public String image(@RequestParam String message) {

		OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
				.withQuality("hd")
//				.withN(4)
				.withHeight(1024)
				.withWidth(1024).build();

		ImageResponse response = imageModel.call(
				new ImagePrompt(message, imageOptions)
		);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new PrintStream(os).println(response);
		return os.toString();
	}

	@GetMapping("/ai/generateStream")
	public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		return chatClient.prompt().
				user(message).
				stream().
				content();
	}
}
