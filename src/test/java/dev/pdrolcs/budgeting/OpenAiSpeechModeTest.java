package dev.pdrolcs.budgeting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiSpeechModeTest {

    @Autowired
    OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @Test
    @DisplayName("Should produce audio when text is provided")
    void shouldProduceAudioWhenTextIsProvided() throws IOException {
        var response = openAiAudioSpeechModel.call("O serviço ficou em R$ 80,00. Posso confirmar o pagamento?");

        assertThat(response).hasSizeGreaterThan(1024);

        var tempFile = Files.createTempFile("AUDIO_", ".mp3");
        Files.write(tempFile, response);
        System.out.println(tempFile.toAbsolutePath());
    }

}
