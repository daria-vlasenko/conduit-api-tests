# Conduit API Test Automation

API test suite for the Conduit (RealWorld) application — a Medium-like blogging platform 
with JWT-based authentication.

## Tech Stack
- Java 17
- REST Assured
- JUnit 5
- Allure Report
- Maven

## What's covered
- User registration & login (positive / negative)
- JWT-based authorization for protected endpoints
- Article creation, retrieval, and access control
- Comment creation
- Negative scenarios: invalid credentials, unauthorized access, ownership checks

## How to run
```bash
mvn clean test
```
Against a custom backend:
```bash
mvn clean test -Dbase.url=https://your-api-url
```

## Test reports
```bash
mvn allure:report
```
Report is generated in `target/site/allure-maven-plugin`.

## Notes
Tests run against a public Conduit demo instance (`node-express-conduit.appspot.com`). 
Response codes for negative scenarios are validated against an expected range (e.g. 
401/403/422), since public demo deployments of Conduit differ slightly in their error 
handling implementation.

## CI/CD
[![CI](https://github.com/daria-vlasenko/conduit-api-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/daria-vlasenko/conduit-api-tests/actions)

Runs automatically on every push via GitHub Actions.
