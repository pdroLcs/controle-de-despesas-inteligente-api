package dev.pdrolcs.budgeting.infrastucture.persistence.repository;

import dev.pdrolcs.budgeting.domains.Category;
import dev.pdrolcs.budgeting.domains.Transaction;
import dev.pdrolcs.budgeting.infrastucture.persistence.entity.TransactionEntity;
import dev.pdrolcs.budgeting.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaTransactionRepository implements TransactionRepository {

    private final TransactionEntityRepository transactionEntityRepository;

    public JpaTransactionRepository(TransactionEntityRepository transactionEntityRepository) {
        this.transactionEntityRepository = transactionEntityRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        var entity = TransactionEntity.from(transaction);
        return transactionEntityRepository.save(entity).toDomain();
    }

    @Override
    public List<Transaction> findAllByCategory(Category category) {
        return transactionEntityRepository.findAllByCategory(category)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}
