package dev.pdrolcs.budgeting.infrastucture.http.request;

import dev.pdrolcs.budgeting.application.input.PersistTransactionInput;
import dev.pdrolcs.budgeting.domains.Category;

import java.math.BigDecimal;

public record TransactionRequest(String description, Category category, BigDecimal amount) {
    public PersistTransactionInput toInput() {
        return new PersistTransactionInput(description, category, amount);
    }
}
