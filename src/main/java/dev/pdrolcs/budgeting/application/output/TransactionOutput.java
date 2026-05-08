package dev.pdrolcs.budgeting.application.output;

import dev.pdrolcs.budgeting.domains.Transaction;

import java.math.BigDecimal;

public record TransactionOutput(String id, String description, BigDecimal amount, String category) {

    public static TransactionOutput from(Transaction transaction) {
        return new TransactionOutput(
                transaction.getId().uuid().toString(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getCategory().name());
    }
}
