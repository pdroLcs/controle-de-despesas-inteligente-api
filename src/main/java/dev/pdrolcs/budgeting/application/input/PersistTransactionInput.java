package dev.pdrolcs.budgeting.application.input;

import dev.pdrolcs.budgeting.domains.Category;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;

public record PersistTransactionInput(
        @ToolParam(description = "Descrição do gasto") String description,
        @ToolParam(description = "Categoria de uma transação") Category category,
        @ToolParam(description = "Valor do gasto em reais") BigDecimal amount) {
}
