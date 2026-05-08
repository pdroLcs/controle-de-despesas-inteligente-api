package dev.pdrolcs.budgeting.infrastucture.persistence.repository;

import dev.pdrolcs.budgeting.domains.Category;
import dev.pdrolcs.budgeting.infrastucture.persistence.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionEntityRepository extends CrudRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByCategory(Category category);
}
