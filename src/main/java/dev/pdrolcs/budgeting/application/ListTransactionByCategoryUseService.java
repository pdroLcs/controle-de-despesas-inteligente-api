package dev.pdrolcs.budgeting.application;

import dev.pdrolcs.budgeting.application.output.TransactionOutput;
import dev.pdrolcs.budgeting.domains.Category;
import dev.pdrolcs.budgeting.repository.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTransactionByCategoryUseService {

    private final TransactionRepository transactionRepository;

    public ListTransactionByCategoryUseService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "list-transactions-by-category", description = "Lista transações financeiras por categoria")
    public List<TransactionOutput> execute(@ToolParam(description = "Categoria de uma transação") Category category) {
        return transactionRepository.findAllByCategory(category)
                .stream()
                .map(TransactionOutput::from)
                .toList();
    }
}
