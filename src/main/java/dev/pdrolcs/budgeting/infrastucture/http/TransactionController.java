package dev.pdrolcs.budgeting.infrastucture.http;

import dev.pdrolcs.budgeting.application.ListTransactionByCategoryUseService;
import dev.pdrolcs.budgeting.application.PersistTransactionUseCase;
import dev.pdrolcs.budgeting.domains.Category;
import dev.pdrolcs.budgeting.infrastucture.http.request.TransactionRequest;
import dev.pdrolcs.budgeting.infrastucture.http.response.TransactionResponse;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionByCategoryUseService listTransactionByCategoryUseService;
    private final OpenAiAudioTranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final OpenAiAudioSpeechModel speechModel;

    public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
                                 ListTransactionByCategoryUseService listTransactionByCategoryUseService,
                                 OpenAiAudioTranscriptionModel transcriptionModel,
                                 ChatClient.Builder chatClientBuilder,
                                 @Value("classpath:/prompts/system-message.st") Resource systemPrompt,
                                 OpenAiAudioSpeechModel speechModel) throws IOException {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionByCategoryUseService = listTransactionByCategoryUseService;
        this.transcriptionModel = transcriptionModel;
        this.speechModel = speechModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
                .defaultTools(persistTransactionUseCase, listTransactionByCategoryUseService)
                .build();
    }

    @GetMapping("/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> readTransactions(@PathVariable Category category) {
        return listTransactionByCategoryUseService.execute(category)
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionRequest request) {
        var transaction = persistTransactionUseCase.execute(request.toInput());
        return TransactionResponse.from(transaction);
    }

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    public ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
        var userMessage = transcriptionModel.call(new AudioTranscriptionPrompt(file.getResource())).getResult().getOutput();
        var result = chatClient.prompt().user(userMessage).call().content();

        byte[] audio = speechModel.call(result);
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                        .filename("audio.mp3")
                        .build()
                        .toString())
                .body(resource);
    }
}
