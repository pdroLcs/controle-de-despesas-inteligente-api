package dev.pdrolcs.budgeting.domains;

import java.util.UUID;

public record TransactionId(UUID uuid) {

    public TransactionId() {
        this(UUID.randomUUID());
    }

}
