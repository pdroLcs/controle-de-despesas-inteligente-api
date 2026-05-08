package dev.pdrolcs.budgeting.infrastucture.http.response;

import dev.pdrolcs.budgeting.application.output.TransactionOutput;

import java.math.BigDecimal;

public record TransactionResponse(String id, String description, String category, BigDecimal amount) {

    public static TransactionResponse from(TransactionOutput transaction) {
        return new TransactionResponse(transaction.id(), transaction.description(), transaction.category(), transaction.amount());
    }
}
