# AI Development Guidelines

## 1. Project Goal

This project is a scalable Java + Selenium + TestNG automation framework.

The framework should support:
- Web UI automation
- Cross-browser execution
- Parallel execution
- Test data management
- API support using Rest Assured
- Database validation using JDBC
- Logging
- Reporting
- Failure diagnostics
- Screenshots
- Retry handling
- CI/CD execution

API and database capabilities are supporting capabilities of the Selenium framework,
not separate API/DB frameworks.

---

## 2. Architecture Rules

Follow the documented architecture in:

docs/01-PRD.md
docs/02-Architecture.md
docs/03-Rules.md
docs/04-Phases.md
docs/learning/40-Final-Questions-and-Decisions.md

Do not introduce a new architecture or design pattern without explaining why.

Do not create abstractions just because they are considered "best practice".

Prefer the simplest implementation that satisfies the current requirement.

---

## 3. Package Responsibilities

core/
Framework infrastructure

core/driver/
WebDriver creation and lifecycle

core/config/
Configuration management

core/listeners/
TestNG listeners

core/logging/
Logging

core/reporting/
Reporting

ui/pages/
Page Objects

ui/components/
Reusable UI components

ui/actions/
Generic UI actions

data/
Test data generation and management

api/
Rest Assured supporting capability

database/
JDBC supporting capability

tests/
Actual test scenarios

Do not place code in a generic Utils class if a more meaningful
responsibility-based location exists.

---

## 4. Selenium Rules

Use Selenium WebDriver.

Use TestNG.

Use normal By locators instead of PageFactory.

Do not create WebDriver inside Page Objects.

Page Objects receive an existing WebDriver.

Page Objects contain:
- Locators
- UI actions
- Page behavior

Assertions belong in test classes.

Avoid Thread.sleep() for normal synchronization.

Use condition-based explicit waits where required.

Prefer stable locators:
1. data-testid
2. stable ID
3. meaningful accessibility attributes
4. CSS
5. XPath when necessary

Avoid brittle structural XPath.

---

## 5. Driver Management

Use DriverFactory for driver creation.

Use DriverManager for driver lifecycle/access.

Use ThreadLocal<WebDriver> for parallel isolation.

Architecture:

BaseTest
↓
DriverManager
↓
ThreadLocal<WebDriver>
↑
DriverFactory

Never use a single shared mutable WebDriver across parallel tests.

---

## 6. Test Independence

Tests should be independently executable.

Do not make one test depend on another test's execution.

Avoid shared mutable static test data.

Use TestContext for test-specific values such as:

customerId
email
orderId
etc.

Dynamic test data should be generated centrally.

Prefer UUID-based uniqueness where appropriate.

---

## 7. Test Data

Use static data when appropriate.

Use dynamic data when uniqueness/isolation is required.

API or DB may be used to prepare test data instead of performing
unnecessary UI setup.

Test-created data should normally be cleaned up after execution.

Cleanup must not hide the original test failure.

---

## 8. API and Database

API:
- Use Rest Assured
- Keep API usage focused on supporting UI/E2E tests
- Do not build a separate API framework here

Database:
- Use JDBC
- Use it for setup/validation/cleanup where appropriate
- Use PreparedStatement for parameterized queries
- Always close database resources properly

Example:

API creates customer
↓
UI verifies customer
↓
DB verifies customer

---

## 9. Failure Handling

When a test fails, collect useful evidence:

- Screenshot
- Exception
- Stack trace
- Current URL
- Browser
- Environment
- Logs
- Relevant test data/context

Use a centralized TestNG listener.

Do not duplicate screenshot/failure-handling code inside every test.

---

## 10. Retry

Retry must be configurable.

Do not blindly retry every failure.

Retry may be useful for potentially transient failures.

Functional/assertion failures should normally not be hidden by retries.

If the original test fails but retry passes, report it as a
potential flaky test.

---

## 11. Logging

Logs should explain what happened.

Use meaningful logs.

Do not log every technical operation unnecessarily.

Use appropriate levels such as:
INFO
DEBUG
WARN
ERROR

Sensitive information such as passwords/tokens must never be logged.

---

## 12. Configuration

Support:

Default configuration
↓
Environment variables
↓
Maven runtime parameters

More specific configuration overrides less specific configuration.

Example:

mvn test -Dbrowser=edge -Denv=qa

Secrets must come from environment variables, CI secrets,
or an appropriate secret-management mechanism.

Never hardcode credentials.

---

## 13. AI Development Rules

Before changing code:

1. Understand the existing architecture.
2. Read relevant documentation.
3. Identify the exact requirement.
4. Explain the proposed change when the change is architectural.
5. Implement the smallest appropriate solution.
6. Run relevant tests.
7. Analyze failures.
8. Fix only the actual problem.
9. Do not introduce unrelated refactoring.
10. Update documentation when an architectural decision changes.

AI must NOT:

- Invent new frameworks without justification
- Add unnecessary design patterns
- Create unnecessary interfaces
- Create unnecessary factories/managers
- Create giant utility classes
- Rewrite working code unnecessarily
- Change architecture without approval
- Hardcode credentials
- Hide test failures
- Add sleeps simply to make tests pass
- Disable tests just because they fail

---

## 14. Learning Rule

The developer must understand the code generated by AI.

For every significant implementation, AI should be able to explain:

- What was changed
- Why it was changed
- Which architectural rule it follows
- How it works
- How it is tested
- What could fail
- How to debug it

AI is an implementation assistant, not the owner of architecture.

---

## 15. Validation Rule

Never assume generated code is correct.

Validate through:

Compile
→ Run test
→ Inspect result
→ Intentionally test failure scenarios when useful
→ Debug
→ Improve
→ Document

A solution is considered complete only after it has been validated.