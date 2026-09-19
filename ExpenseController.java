package com.hackathon.expensetracker.controller;

import com.hackathon.expensetracker.model.Expense;
import com.hackathon.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Handles all HTTP requests (routes) for the app.
 *
 * Since we're using Thymeleaf with NO JavaScript, every interaction — adding,
 * editing, deleting, filtering — is a normal HTML form submission or link
 * click that triggers a full page reload/redirect handled here.
 */
@Controller
public class ExpenseController {

    // The list of allowed categories, reused across the dashboard, add and edit forms
    public static final List<String> CATEGORIES = List.of(
            "Food", "Transport", "Shopping", "Entertainment", "Bills", "Education", "Other"
    );

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // ---------------------------------------------------------------
    // Feature 3: Dashboard (Home Page)
    // ---------------------------------------------------------------
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalToday", expenseService.getTotalSpentToday());
        model.addAttribute("totalWeek", expenseService.getTotalSpentThisWeek());
        model.addAttribute("totalMonth", expenseService.getTotalSpentThisMonth());
        model.addAttribute("transactionCount", expenseService.getTotalTransactionCount());
        model.addAttribute("categoryBreakdown", expenseService.getCategoryBreakdown());
        model.addAttribute("recentExpenses", expenseService.getRecentExpenses(5));

        // Feature 7: Budget alert
        model.addAttribute("isOverBudget", expenseService.isOverBudget());
        model.addAttribute("budgetLimit", ExpenseService.MONTHLY_BUDGET_LIMIT);

        return "index"; // renders templates/index.html
    }

    // ---------------------------------------------------------------
    // Feature 1: Add Expense
    // ---------------------------------------------------------------
    @GetMapping("/expenses/add")
    public String showAddForm(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", CATEGORIES);
        return "add-expense"; // renders templates/add-expense.html
    }

    @PostMapping("/expenses/add")
    public String addExpense(@Valid @ModelAttribute("expense") Expense expense,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Validation failed (e.g. empty description, amount <= 0) — redisplay the form with error messages
            model.addAttribute("categories", CATEGORIES);
            return "add-expense";
        }
        expenseService.save(expense);
        redirectAttributes.addFlashAttribute("successMessage", "Expense added successfully!");
        return "redirect:/expenses";
    }

    // ---------------------------------------------------------------
    // Feature 2 + 6: View All Expenses (with category filter)
    // ---------------------------------------------------------------
    @GetMapping("/expenses")
    public String listExpenses(@RequestParam(name = "category", required = false, defaultValue = "All") String category,
                                Model model) {
        List<Expense> expenses = expenseService.findByCategory(category);
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();

        model.addAttribute("expenses", expenses);
        model.addAttribute("total", total);
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("selectedCategory", category);
        return "expenses"; // renders templates/expenses.html
    }

    // ---------------------------------------------------------------
    // Feature 4: Edit Expense
    // ---------------------------------------------------------------
    @GetMapping("/expenses/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("expense", expenseService.findById(id));
        model.addAttribute("categories", CATEGORIES);
        return "edit-expense"; // renders templates/edit-expense.html
    }

    @PostMapping("/expenses/edit/{id}")
    public String updateExpense(@PathVariable Long id,
                                 @Valid @ModelAttribute("expense") Expense expense,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", CATEGORIES);
            return "edit-expense";
        }
        expense.setId(id); // make sure we update the existing row, not create a new one
        expenseService.save(expense);
        redirectAttributes.addFlashAttribute("successMessage", "Expense updated successfully!");
        return "redirect:/expenses";
    }

    // ---------------------------------------------------------------
    // Feature 5: Delete Expense
    // ---------------------------------------------------------------
    @GetMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        expenseService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Expense deleted successfully!");
        return "redirect:/expenses";
    }
}
