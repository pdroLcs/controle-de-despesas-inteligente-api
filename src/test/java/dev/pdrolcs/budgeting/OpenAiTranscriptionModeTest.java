package dev.pdrolcs.budgeting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiTranscriptionModeTest {

    @Autowired
    OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    @ParameterizedTest
    @CsvSource({
            "Generated-Audio-May-07_-2026-3_12PM.mp3, R$ 80,00",
            "Generated-Audio-May-07_-2026-3_27PM.mp3, R$ 80,00",
            "Generated-Audio-May-07_-2026-3_28PM.mp3, R$ 500,00"
    })
    @DisplayName("Should contain expected key words when audio files are processed")
    void shouldContainExpectedKeywordsWhenAudioFilesAreProcessed(String fileName, String expectedKeyWord) {
        var recording = new ClassPathResource("audios/" + fileName);

        var response = openAiAudioTranscriptionModel.call(recording);

        assertThat(response).contains(expectedKeyWord);
        System.out.println(response);
    }

}
