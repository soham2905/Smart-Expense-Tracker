package com.hackathon.expensetracker.service;

import com.hackathon.expensetracker.model.Expense;
import com.hackathon.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service layer = where the business logic lives.
 * The Controller stays thin (handles web requests/responses) and delegates
 * the actual work — calculations, queries, validation logic — to this class.
 */
@Service
public class ExpenseService {

    // Hardcoded monthly budget limit for the "Budget Alert" bonus feature.
    // Could easily be moved to application.properties if you want it configurable.
    public static final double MONTHLY_BUDGET_LIMIT = 5000.0;

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // ----- Basic CRUD operations -----

    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAllByOrderByDateDesc();
    }

    public Expense findById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Expense not found with id: " + id));
    }

    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }

    // ----- Filtering (Feature 6) -----

    public List<Expense> findByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return findAll();
        }
        return expenseRepository.findByCategory(category);
    }

    // ----- Dashboard calculations (Feature 3) -----

    public double getTotalSpentToday() {
        LocalDate today = LocalDate.now();
        Double total = expenseRepository.sumAmountBetweenDates(today, today);
        return total == null ? 0.0 : total;
    }

    public double getTotalSpentThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        Double total = expenseRepository.sumAmountBetweenDates(startOfWeek, today);
        return total == null ? 0.0 : total;
    }

    public double getTotalSpentThisMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        Double total = expenseRepository.sumAmountBetweenDates(startOfMonth, today);
        return total == null ? 0.0 : total;
    }

    public long getTotalTransactionCount() {
        return expenseRepository.count();
    }

    public List<Expense> getRecentExpenses(int limit) {
        List<Expense> all = findAll(); // already sorted newest-first
        return all.size() > limit ? all.subList(0, limit) : all;
    }

    /**
     * Builds the category-wise breakdown table shown on the dashboard:
     * category name, total amount spent, and number of transactions.
     */
    public List<CategorySummary> getCategoryBreakdown() {
        List<Object[]> rows = expenseRepository.sumAmountGroupedByCategory();
        List<CategorySummary> summaries = new ArrayList<>();
        for (Object[] row : rows) {
            String category = (String) row[0];
            Double totalAmount = (Double) row[1];
            Long count = (Long) row[2];
            summaries.add(new CategorySummary(category, totalAmount, count));
        }
        // Show the highest-spending category first
        summaries.sort((a, b) -> Double.compare(b.getTotalAmount(), a.getTotalAmount()));
        return summaries;
    }

    // ----- Budget alert (Feature 7) -----

    public boolean isOverBudget() {
        return getTotalSpentThisMonth() > MONTHLY_BUDGET_LIMIT;
    }

    /**
     * Small, immutable "data holder" class for the category breakdown table.
     * Kept as a nested static class here (instead of a separate file) since
     * it's only ever used by this service/dashboard view.
     */
    public static class CategorySummary {
        private final String category;
        private final Double totalAmount;
        private final Long transactionCount;

        public CategorySummary(String category, Double totalAmount, Long transactionCount) {
            this.category = category;
            this.totalAmount = totalAmount;
            this.transactionCount = transactionCount;
        }

        public String getCategory() {
            return category;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public Long getTransactionCount() {
            return transactionCount;
        }
    }
}
