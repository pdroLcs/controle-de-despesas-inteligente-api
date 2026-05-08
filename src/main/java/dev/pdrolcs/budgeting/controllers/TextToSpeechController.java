package dev.pdrolcs.budgeting.controllers;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TextToSpeechController {

    private final OpenAiAudioSpeechModel speechModel;

    public TextToSpeechController(OpenAiAudioSpeechModel speechModel) {
        this.speechModel = speechModel;
    }

    @PostMapping(value = "/sinthesize", produces = "audio/mp3")
    public ResponseEntity<Resource> sinthesize(@RequestBody SynthesizeRequest request) {
        byte[] audio = speechModel.call(request.text);
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }

    public record SynthesizeRequest(String text) {}

}
