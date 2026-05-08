package dev.pdrolcs.budgeting.application;

import dev.pdrolcs.budgeting.application.input.PersistTransactionInput;
import dev.pdrolcs.budgeting.application.output.TransactionOutput;
import dev.pdrolcs.budgeting.domains.Transaction;
import dev.pdrolcs.budgeting.repository.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class PersistTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "persist-transaction", description = "Persiste uma nova transação financeira")
    public TransactionOutput execute(PersistTransactionInput input) {
        var transaciton = transactionRepository.save(new Transaction(input.description(), input.category(), input.amount()));
        return TransactionOutput.from(transaciton);
    }
}
