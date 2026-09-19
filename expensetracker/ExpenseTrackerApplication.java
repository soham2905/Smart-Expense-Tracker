package com.hackathon.expensetracker;

import com.hackathon.expensetracker.model.Expense;
import com.hackathon.expensetracker.repository.ExpenseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

/**
 * Entry point of the Spring Boot application.
 * Running this class starts an embedded Tomcat server on port 8080.
 */
@SpringBootApplication
public class ExpenseTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerApplication.class, args);
    }

    /**
     * CommandLineRunner runs automatically once, right after the app starts.
     * We use it here to insert a few sample expenses into the H2 in-memory
     * database, so the dashboard/tables aren't empty when you first open the app.
     */
    @Bean
    CommandLineRunner seedData(ExpenseRepository expenseRepository) {
        return args -> {
            LocalDate today = LocalDate.now();

            expenseRepository.save(new Expense("Lunch at canteen", 120.0, "Food", today));
            expenseRepository.save(new Expense("Auto rickshaw to college", 60.0, "Transport", today.minusDays(1)));
            expenseRepository.save(new Expense("New notebook & pens", 150.0, "Education", today.minusDays(1)));
            expenseRepository.save(new Expense("Movie ticket", 250.0, "Entertainment", today.minusDays(2)));
            expenseRepository.save(new Expense("Mobile recharge", 299.0, "Bills", today.minusDays(3)));
            expenseRepository.save(new Expense("T-shirt", 599.0, "Shopping", today.minusDays(4)));
            expenseRepository.save(new Expense("Coffee with friends", 80.0, "Food", today.minusDays(4)));
            expenseRepository.save(new Expense("Bus pass renewal", 400.0, "Transport", today.minusDays(6)));
            expenseRepository.save(new Expense("Online course subscription", 499.0, "Education", today.minusDays(8)));
            expenseRepository.save(new Expense("Electricity bill share", 350.0, "Bills", today.minusDays(10)));
        };
    }
}
