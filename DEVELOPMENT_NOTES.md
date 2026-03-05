# DEVELOPMENT NOTES

## 1. Overall Approach

The goal of this assignment was to build a RESTful backend service that evaluates loan applications and returns an approval or rejection decision along with risk classification and loan offer details.


The application flow is as follows:

1. Client submits a loan application via the REST endpoint.
2. The request is validated using **Bean Validation (JSR-380)** annotations.
3. The request is processed by the **LoanService**, which:

    * Evaluates eligibility rules
    * Classifies applicant risk
    * Calculates EMI and loan offer if eligible
4. The decision is stored using **JPA Repository** for audit purposes.
5. The API returns a structured response with the decision result.

---

## 2. Architecture and Structure

The project follows a **layered architecture**:

```
controller → service → repository → database
```

**Packages used:**

* `controller` – Handles HTTP requests and responses
* `service` – Contains business logic for loan evaluation
* `domain` – Contains entities, DTOs, and enums
* `repository` – JPA repositories for persistence
* `util` – Utility classes such as EMI calculator
* `exception` – Global exception handling

This separation improves maintainability, readability, and testability.

---

## 3. Key Design Decisions

### 1. Layered Architecture

Separating controller, service, and repository layers ensures clean separation of concerns and makes the system easier to extend and test.

### 2. Use of BigDecimal for Financial Calculations

All financial values (loan amount, EMI, interest rate) use **BigDecimal** to avoid floating-point precision errors.

### 3. Validation using Bean Validation

Input validation is implemented using annotations such as:

* `@NotNull`
* `@Min`
* `@Max`
* `@Valid`

This ensures invalid requests are rejected early before business logic execution.

### 4. Risk Classification via Enum

Risk levels are represented using an enum:

```
LOW
MEDIUM
HIGH
```

This improves type safety and makes the logic easier to maintain.

### 5. Centralized Error Handling

A **Global Exception Handler** (`@ControllerAdvice`) is used to standardize error responses for validation and JSON parsing errors.

### 6. Use of JPA Repository

The application uses **Spring Data JPA** with an embedded **H2 database** to store loan application decisions for auditing.

---

## 4. Business Rules Implemented

The service evaluates the following rules:

1. **Minimum Credit Score**

    * Applications with credit score < 600 are rejected.

2. **Age + Loan Tenure Limit**

    * Age + loan tenure must not exceed 65 years.

3. **EMI Affordability**

    * EMI must not exceed **60% of the applicant’s monthly income**.

4. **Risk Classification**

    * Credit score ≥ 750 → LOW risk
    * Credit score 650–749 → MEDIUM risk
    * Credit score < 650 → HIGH risk

5. **Interest Rate Determination**

    * LOW risk → lower interest rate
    * MEDIUM risk → moderate interest rate
    * HIGH risk → higher interest rate

---

## 5. Trade-offs Considered

### 1. Simplicity vs Full Production Design

The system is intentionally simple to focus on core business logic rather than building a full microservice architecture.

### 2. In-memory Database

An **H2 in-memory database** was used for simplicity and easy testing instead of an external database.

### 3. Service-Level Business Logic

All eligibility and evaluation logic is centralized in the service layer to keep controllers lightweight.

---

## 6. Assumptions Made

* Monthly income provided in the request is the applicant's net income.
* Interest rate rules are simplified and based only on risk classification.
* Only one loan offer is generated per application.
* Loan tenure is provided in months.
* EMI calculations assume a fixed interest rate over the loan tenure.

---

## 7. Testing Strategy

Unit tests were implemented for the core logic including:

* **EMI calculation**
* **Risk classification**
* **Eligibility rules**
* **Approval and rejection scenarios**

Mockito is used to mock repository interactions to ensure business logic tests remain isolated.

---

## 8. Improvements With More Time

If more time were available, the following improvements could be implemented:

### 1. API Documentation

Add **Swagger/OpenAPI documentation** for easier API exploration.

### 2. Integration Tests

Add integration tests using **SpringBootTest** to verify the complete request-to-database flow.

### 3. Configuration Externalization

Move business rules (interest rates, eligibility limits) to configuration files or a rule engine.

### 4. Logging and Monitoring

Add structured logging using **SLF4J** and integrate monitoring tools.

### 5. Better Error Response Format

Introduce a standardized API error response structure with error codes and messages.

### 6. Persistence Improvements

Use a persistent database (PostgreSQL/MySQL) instead of H2 for production readiness.

### 7. Security

Add authentication and authorization to secure the API endpoints.

