package dev.pdrolcs.budgeting.repository;

import dev.pdrolcs.budgeting.domains.Category;
import dev.pdrolcs.budgeting.domains.Transaction;

import java.util.List;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findAllByCategory(Category category);

}
