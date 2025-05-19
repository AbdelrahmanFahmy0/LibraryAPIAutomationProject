![Library API Automation Demo](docs/demo.gif)

# 📚 Library API Automation

This project automates the testing of a real **Library API** that manages Books, Households, Users, and Wishlists. The suite performs full **CRUD operations** including **partial updates**, validates **responses**, and generates detailed **Allure reports** for visibility.

---

## 🛠️ Tools & Technologies

-  **Java**
-  **Rest Assured**
-  **TestNG**
-  **Maven**
-  **Allure Reporting**
-  **Log4j Logging**
-  **JSON Schema Validator**


### ⚙️ Prerequisites

- Java Development Kit (JDK) installed
- IDE (eg: IntelliJ IDEA, Eclipse)
- Maven installed
---

## 📄 Features Covered

Each resource (Books, Households, Users, Wishlists) supports:

- 🔹 Create
- 🔹 Retrieve (by ID)
- 🔹 Retrieve all
- 🔹 Update (full)
- 🔹 Update (partial)
- 🔹 Delete

---

## 📁 Project Structure

```bash
src/
├── main/
│ ├── java/
│ │ ├── Listeners/ # TestNG listeners and hooks
│ │ ├── Models/ # POJOs / Data models for API requests/responses
│ │ └── Utils/ # Utility classes: schema validation, logging, helpers
│ └── resources/
│ ├── allure.properties # Allure configuration
│ └── log4j.properties # Log4j configuration
│
└── test/
├── java/
│ ├── Base/ # Base test class and common setup
│ ├── Books/ # Tests for Books API
│ ├── Households/ # Tests for Households API
│ ├── Users/ # Tests for Users API
│ └── Wishlists/ # Tests for Wishlists API
└── resources/
├── Data/ # Test data files (JSON, properties, etc.)
└── Schemas/ # JSON schemas for response validation
```

---

## 🚀 How to Run

Before running the tests, ensure you have installed all the [⚙️ Prerequisites](#-Prerequisites).

1. **Clone the repository**
   
   ```bash
   git clone https://github.com/AbdelrahmanFahmy0/LibraryAPIAutomationProject.git
   cd LibraryAPIAutomationProject
   ```
3. **Run the tests**
   
   ```bash
   mvn clean test
   ```
5. **Generate Allure Report**
   
   ```bash
   allure serve Test-Outputs/allure-results
   ```

---

## ✅ Sample Validations

- ✔️ **Status code and response time**
- ✔️ **Field-level value assertions**
- ✔️ **JSON Schema validation**

---

## 🤝 Contributions

Contributions are welcome! Please fork the repository and create a pull request.
