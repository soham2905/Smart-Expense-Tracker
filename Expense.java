package com.hackathon.expensetracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a single expense entry.
 *
 * The @Entity annotation tells Spring Data JPA that this class maps to a
 * database table (by default, a table named "expense"). Each field becomes
 * a column in that table.
 */
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment primary key
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Date is required")
    private LocalDate date;

    // Automatically set once, when the record is first created (see @PrePersist below)
    private LocalDateTime createdAt;

    // Required no-args constructor — JPA needs this to build objects from database rows
    public Expense() {
    }

    // Convenience constructor used by the seed data (CommandLineRunner)
    public Expense(String description, Double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Runs automatically right before the entity is first saved to the database
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ----- Getters and setters (Spring/Thymeleaf use these to read & bind form data) -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
