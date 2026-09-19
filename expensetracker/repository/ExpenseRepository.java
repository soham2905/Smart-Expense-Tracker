package com.hackathon.expensetracker.repository;

import com.hackathon.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository = the layer that talks to the database.
 *
 * By extending JpaRepository<Expense, Long>, Spring Data JPA automatically
 * generates the implementation for common methods like save(), findAll(),
 * findById(), deleteById(), etc. — we don't have to write any SQL for those.
 *
 * For anything more specific, we just declare a method signature following
 * Spring Data's naming convention, or write a custom @Query.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Spring Data JPA reads the method name and builds the query automatically:
    // "findByCategory" -> SELECT * FROM expense WHERE category = ?
    List<Expense> findByCategory(String category);

    // "findByDate" -> SELECT * FROM expense WHERE date = ?
    List<Expense> findByDate(LocalDate date);

    // "findByDateBetween" -> SELECT * FROM expense WHERE date BETWEEN ? AND ?
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);

    // All expenses, newest date first (used on the "All Expenses" page)
    List<Expense> findAllByOrderByDateDesc();

    // Custom JPQL query: total amount spent, grouped by category.
    // Returns rows of [category, totalAmount, transactionCount].
    @Query("SELECT e.category, SUM(e.amount), COUNT(e) FROM Expense e GROUP BY e.category")
    List<Object[]> sumAmountGroupedByCategory();

    // Custom JPQL query: total amount spent between two dates (inclusive).
    // COALESCE makes sure we get 0.0 instead of null when there are no matching rows.
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.date BETWEEN :start AND :end")
    Double sumAmountBetweenDates(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
