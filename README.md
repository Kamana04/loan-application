# Loan Application Service

## Overview

This project is a **Spring Boot REST API** that evaluates loan applications and returns a decision based on predefined business rules.
The service analyzes applicant details, calculates loan affordability, determines risk classification, and generates a loan offer when the application is approved.

The system also stores loan decisions for auditing purposes.

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 In-Memory Database
* Maven
* JUnit 5 & Mockito (Unit Testing)

---

## Project Structure

```
src/main/java
 ├── controller
 │    └── LoanApplicationController
 ├── service
 │    └── LoanService
 ├── repository
 │    └── LoanApplicationRepository
 ├── domain
 │    ├── Applicant
 │    ├── LoanDetails
 │    ├── LoanApplicationRequest
 │    ├── LoanApplicationResponse
 │    └── enums
 ├── util
 │    └── EmiCalculator
 └── exception
      └── GlobalExceptionHandler
```

---

## How to Run the Application

### 1. Clone the Repository

```bash
git clone <repository-url>
cd loan-service
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on:

```
http://localhost:8080
```

---

## H2 Database Console

The application uses an **H2 in-memory database**.

Access it at:

```
http://localhost:8080/h2-console
```

Connection details:

| Property | Value               |
| -------- | ------------------- |
| JDBC URL | jdbc:h2:mem:loan-db |
| Username | sa                  |
| Password | (empty)             |

---

## API Endpoint

### Evaluate Loan Application

```
POST /applications
```

---

## Sample Request

```json
{
  "applicant": {
    "name": "Rahul",
    "age": 30,
    "monthlyIncome": 80000,
    "employmentType": "SALARIED",
    "creditScore": 720
  },
  "loan": {
    "amount": 500000,
    "tenureMonths": 36,
    "purpose": "PERSONAL"
  }
}
```

---

## Sample Approved Response

```json
{
  "applicationId": "a8c2f3b4-5e2a-4f98-9a2c-1234567890ab",
  "status": "APPROVED",
  "riskBand": "MEDIUM",
  "offer": {
    "interestRate": 12.0,
    "tenureMonths": 36,
    "emi": 16607.45,
    "totalPayable": 597868.20
  }
}
```

---

## Sample Rejected Response

```json
{
   "applicationId": "9d8fbbcf-b224-41a5-b2c5-b8a5ca1ab144",
   "applicationStatus": "REJECTED",
   "riskBand": null,
   "loanOffer": null,
   "rejectionReasons": [
      "CREDIT_SCORE_BELOW_MINIMUM"
   ]
}
```

---

## Business Rules Implemented

1. **Credit Score Requirement**

    * Applications with credit score < 600 are rejected.

2. **Age + Tenure Limit**

    * Applicant age + loan tenure must not exceed 65 years.

3. **EMI Affordability**

    * EMI must not exceed **60% of the applicant’s monthly income**.

4. **Risk Classification**

    * Credit Score ≥ 750 → LOW risk
    * Credit Score 650–749 → MEDIUM risk
    * Credit Score < 650 → HIGH risk

5. **Interest Rate Determination**

    * LOW risk → lower interest rate
    * MEDIUM risk → moderate interest rate
    * HIGH risk → higher interest rate

---

## Unit Testing

Unit tests cover:

* EMI calculation
* Risk classification
* Eligibility rules
* Approval and rejection scenarios

Run tests using:

```bash
mvn test
```

---

## Development Notes

Please refer to:

```
DEVELOPMENT_NOTES.md
```

for details on:

* Architecture decisions
* Trade-offs
* Assumptions
* Future improvements

---

## Future Improvements

* Add Swagger/OpenAPI documentation
* Add integration tests
* Add persistent database (PostgreSQL/MySQL)
* Introduce authentication and authorization
* Improve logging and monitoring

---

## Author

Backend implementation for loan application using Spring Boot.
