# 💰 Smart Expense Tracker

A simple, fast web app for students and young earners to answer one question:
**"Where did my money go this week?"** — without the complexity of apps like
CRED or Walnut.

Built for a college hackathon.

## Problem It Solves

Students and young earners often lose track of daily spending and slide into
overspending without realizing it. This app gives them a dead-simple way to
log expenses and immediately see a summary: today, this week, this month, and
broken down by category.

## Tech Stack

| Layer      | Technology                          |
|------------|--------------------------------------|
| Backend    | Java 17 + Spring Boot 3.3            |
| Frontend   | Thymeleaf + Bootstrap 5 (no JavaScript at all) |
| Database   | H2 in-memory database                |
| ORM        | Spring Data JPA / Hibernate          |
| Build Tool | Maven                                |

Every interaction (add, edit, delete, filter) is a plain HTML form submission
or link click handled entirely on the server — there is no JavaScript
anywhere in this project, by design.

## Features

1. **Add Expense** — log a description, amount, category, and date.
2. **View All Expenses** — a sortable table (newest first) with a running total.
3. **Dashboard** — total spent today / this week / this month, transaction
   count, a category-wise breakdown table, and the 5 most recent expenses.
4. **Edit Expense** — update any existing entry.
5. **Delete Expense** — remove an entry.
6. **Filter by Category** — narrow the "All Expenses" table to one category.
7. **Budget Alert** — a red banner appears on the dashboard once this month's
   spending passes ₹5,000 (see `ExpenseService.MONTHLY_BUDGET_LIMIT`).

## Project Structure

```
smart-expense-tracker/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/hackathon/expensetracker/
    │   ├── ExpenseTrackerApplication.java   # entry point + sample data seeding
    │   ├── model/Expense.java               # JPA entity
    │   ├── repository/ExpenseRepository.java# Spring Data JPA queries
    │   ├── service/ExpenseService.java      # business logic / calculations
    │   └── controller/ExpenseController.java# HTTP routes
    └── resources/
        ├── application.properties
        ├── templates/
        │   ├── fragments.html    # shared navbar
        │   ├── index.html        # dashboard
        │   ├── add-expense.html
        │   ├── expenses.html
        │   └── edit-expense.html
        └── static/css/style.css
```

## How to Run

**Requirements:** JDK 17+ and Maven (or use the included Maven Wrapper if you
add one — this project uses plain `mvn`).

```bash
cd smart-expense-tracker
mvn spring-boot:run
```

Then open **http://localhost:8080** in your browser.

The app starts with 10 sample expenses already loaded (via a
`CommandLineRunner`) so the dashboard and tables aren't empty on first launch.
Since H2 is in-memory, this sample data resets every time you restart the app.

### H2 Console (optional, for the demo/judges)

Visit **http://localhost:8080/h2-console** while the app is running.
- JDBC URL: `jdbc:h2:mem:expensedb`
- Username: `sa`
- Password: *(leave blank)*

This lets you show the actual database table and run raw SQL live during a
demo — a nice touch for a hackathon.

## Building a JAR

```bash
mvn clean package
java -jar target/expense-tracker.jar
```

## Screenshots (what to capture for your submission)

Since this is a generated project, add your own screenshots after running it
locally:
1. **Dashboard** — showing the 4 summary cards, category breakdown, and
   recent expenses.
2. **Add Expense form** — with the category dropdown open.
3. **All Expenses table** — with the category filter applied.
4. **Budget alert banner** — after adding enough expenses to cross ₹5,000
   in the current month.

## Possible Next Steps

- Make the ₹5,000 budget limit user-configurable via a settings page.
- Add pagination to the expenses table for large datasets.
- Add basic charts (would require introducing a small amount of JS or a
  server-rendered chart image, since the current build is JS-free by design).
- Add user accounts so multiple people can track expenses separately.
