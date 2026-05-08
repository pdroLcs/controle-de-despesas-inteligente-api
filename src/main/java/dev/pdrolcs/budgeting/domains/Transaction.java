package dev.pdrolcs.budgeting.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {

    private TransactionId id;
    private String description;
    private BigDecimal amount;
    private Category category;

    public Transaction(String description, Category category, BigDecimal amount) {
        this.id = new TransactionId();
        this.description = description;
        this.category = category;
        this.amount = amount;
    }
}
