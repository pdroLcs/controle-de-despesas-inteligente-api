package dev.pdrolcs.budgeting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatModelTest {

    @Autowired
    OpenAiChatModel chatModel;

    @Test
    @DisplayName("Should receive response when chat model is called")
    void shouldReceiveResponseWhenChatModelIsCalled() {

        var response = chatModel.call("Gere um registro de budgeting com descrição de gasto, valor em reais e local");

        assertThat(response).isNotEmpty();
        System.out.println(response);
    }

}
