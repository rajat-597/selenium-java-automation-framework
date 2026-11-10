# 40 Framework Questions — Final Decisions

This document contains the final conclusions from our framework discussions. It intentionally does not include the back-and-forth discussion.

## 1. What is the purpose of our framework?
**Conclusion:** The framework will provide reusable infrastructure to create, execute, maintain, debug, and report automated tests consistently.

**Why:** We want a framework that can grow with the project instead of putting everything directly inside test classes.

## 2. What should the overall architecture look like?
**Conclusion:** Separate responsibilities into core framework, UI, test data, API support, database support, and tests.

```text
core/
ui/
data/
api/
database/
tests/
```

**Why:** It keeps responsibilities clear and makes the framework easier to maintain.

## 3. What is POM?
**Conclusion:** Page Object Model keeps page locators and page-related actions together.

**Why:** Tests should focus on business scenarios instead of low-level Selenium code.

## 4. What should be inside a Page Object?
**Conclusion:** A Page Object contains locators and UI behavior/actions.

**Why:** This keeps UI implementation details away from test classes.

**Example:**
```java
loginPage.login(username, password);
```

## 5. Where should assertions be written?
**Conclusion:** Assertions remain in test classes.

**Why:** The Page Object performs the action or returns the state; the test decides whether the result is correct.

## 6. Should Page Objects create WebDriver?
**Conclusion:** No. Page Objects receive an existing WebDriver.

**Why:** Browser creation and page interaction are different responsibilities.

## 7. Should we use PageFactory?
**Conclusion:** We will use normal `By` locators instead of PageFactory for this framework.

**Why:** `By` gives explicit control over element lookup and works naturally with our explicit wait/UI action approach.

## 8. Why use `By`?
**Conclusion:** A `By` stores the locator, and Selenium finds the element when required.

```java
private By username = By.id("username");
```

**Why:** It keeps element lookup explicit and works well with dynamic pages and waits.

## 9. Should we create a PageManager?
**Conclusion:** Not initially. We will directly create pages:
```java
new LoginPage(driver);
```

**Why:** We do not currently have enough pages to justify another abstraction. PageManager can be introduced later if it solves a real problem.

## 10. Where should WebDriver be created?
**Conclusion:** `DriverFactory` creates the WebDriver.

**Why:** Browser-specific creation logic should stay in one place.

## 11. What is DriverManager responsible for?
**Conclusion:** `DriverManager` manages the current WebDriver and its lifecycle/access.

**Why:** Tests should not directly manage driver creation and cleanup.

## 12. What is ThreadLocal responsible for?
**Conclusion:** `ThreadLocal<WebDriver>` keeps the driver reference isolated for each thread.

**Important:** ThreadLocal does not create drivers.

**Why:** It prevents parallel executions from accidentally using the same browser session.

## 13. What is the final driver architecture?
**Conclusion:**
```text
BaseTest
   ↓
DriverManager
   ↓
ThreadLocal<WebDriver>
   ↑
DriverFactory
```

**Why:** This gives centralized driver management with isolated browser sessions for parallel execution.

## 14. What should BaseTest contain?
**Conclusion:** BaseTest remains thin and mainly handles common test setup, teardown, and driver lifecycle.

**Why:** We do not want BaseTest to become a dumping ground for API, DB, logging, reporting, and other unrelated responsibilities.

## 15. Should we create a huge Utils class?
**Conclusion:** No.

**Why:** Functionality should belong to the area it actually supports.

```text
UI actions → ui/actions
UI components → ui/components
Configuration → core/config
Driver → core/driver
```

## 16. Where should reusable UI components go?
**Conclusion:** Reusable components such as Header and Sidebar go under `ui/components/`.

**Why:** The same UI component may be used by multiple pages.

## 17. Where should generic UI actions go?
**Conclusion:** UI-specific reusable actions go under `ui/actions/UIActions.java`.

**Why:** Common click/type/wait behavior can be centralized without mixing it with unrelated utilities.

## 18. What wait strategy will we use?
**Conclusion:** Use condition-based explicit waits.

Examples:
```text
element visible
element clickable
element present
URL changed
text present
alert present
```

**Why:** Explicit waits synchronize with the actual application condition.

## 19. Should we use `Thread.sleep()`?
**Conclusion:** No, not as the normal synchronization mechanism.

**Why:** Fixed waits make tests slower and can still be unreliable.

## 20. What locator strategy will we use?
**Conclusion:** Prefer stable and meaningful locators.

```text
data-testid
↓
stable ID
↓
meaningful/accessibility attributes
↓
CSS
↓
XPath when required
```

**Why:** Stable locators reduce maintenance and flaky tests.

## 21. Is XPath bad?
**Conclusion:** No.

**Why:** XPath is useful when a suitable stable locator is unavailable. The problem is brittle structural XPath.

## 22. How should configuration work?
**Conclusion:** Support:
```text
Default config
   ↓
Environment variables
   ↓
Maven runtime parameters
```

The more specific value overrides the previous one.

**Example:**
```text
mvn test -Dbrowser=edge -Denv=uat
```

## 23. Where should secrets be stored?
**Conclusion:** Secrets such as passwords and tokens come from environment variables/CI secrets or appropriate secret management.

**Why:** Secrets must not be committed to Git.

## 24. How should test data be handled?
**Conclusion:** Use both static and dynamic data.

**Static:** stable business values.

**Dynamic:** values that need uniqueness such as email, customer ID, etc.

## 25. How should dynamic data be generated?
**Conclusion:** Use a centralized test-data generation approach rather than generating random values manually inside every test.

**Why:** It keeps data generation consistent and reusable.

## 26. Should tests depend on other tests?
**Conclusion:** No. Tests should be independent wherever practical.

**Why:** A test should not fail simply because another test failed or ran in a different order.

## 27. How should we handle test data during parallel execution?
**Conclusion:** Each test should have isolated data.

**Why:** Shared mutable test data can be overwritten by another parallel test.

## 28. Should we use static variables for test data?
**Conclusion:** Avoid shared mutable static test data.

**Why:** Parallel tests can overwrite each other's values.

Use test-scoped context for values such as:
```text
customerId
email
orderId
```

## 29. What is TestContext?
**Conclusion:** `TestContext` holds data belonging to the current test execution.

**Why:** It allows API → UI → DB parts of the same scenario to share data without unsafe static variables.

## 30. How should API be used in this Selenium framework?
**Conclusion:** API is a supporting capability, not a separate full API framework.

**Example:**
```text
API creates customer
       ↓
UI verifies customer
       ↓
DB verifies customer
```

**Why:** API setup can be faster and more reliable than creating all preconditions through UI.

## 31. How should database functionality be used?
**Conclusion:** Database access is supporting functionality, mainly for setup and validation.

**Why:** We want realistic end-to-end testing without turning this project into a separate database automation framework.

## 32. How should tests be organized?
**Conclusion:** Use technology-first organization with business areas inside it.

```text
tests/
  ui/
    login/
    customer/
    order/
  endtoend/
```

**Why:** It keeps the project organized without creating unnecessary folders from day one.

## 33. What is the difference between folders and TestNG groups?
**Conclusion:** Folders show where the test belongs. TestNG groups show when/why it should run.

Example:
```text
Login   → smoke
Search  → regression
Payment → smoke + regression
```

## 34. Do we need separate TestNG XML files for every browser/group combination?
**Conclusion:** No. Browser should normally be passed as runtime configuration.

```text
mvn test -Dgroups=smoke -Dbrowser=chrome
mvn test -Dgroups=smoke -Dbrowser=firefox
mvn test -Dgroups=regression -Dbrowser=edge
```

**Why:** This scales better than creating files such as `smoke-chrome.xml` and `smoke-firefox.xml`.

## 35. How should parallel execution work?
**Conclusion:** Support controlled parallel execution rather than running everything simultaneously.

**Why:** The application, database and infrastructure may not support unlimited concurrent sessions.

The concurrency level should be configurable.

## 36. How should retry work?
**Conclusion:** Retry should be configurable and used mainly for potentially transient failures.

**Why:** We do not want retry to hide genuine application or automation defects.

If a test fails and passes on retry, the report should clearly show that it was retried/potentially flaky.

## 37. How should failure screenshots be captured?
**Conclusion:** Use a centralized TestNG listener.

```text
Test fails
   ↓
TestNG Listener
   ↓
Screenshot + diagnostics
```

**Why:** We do not want screenshot code inside every test.

## 38. What should failure diagnostics contain?
**Conclusion:** Capture useful evidence such as:
```text
Screenshot
Exception/stack trace
Relevant logs
Current URL
Browser
Environment
```

**Why:** The goal is not only to know that the test failed, but to understand why it failed.

## 39. What should logging and reporting do?
**Conclusion:**

**Logging:** helps us understand what happened during execution.

**Reporting:** presents the final execution result.

Reports should show:
```text
Passed
Failed
Skipped
Browser
Environment
Duration
Failure reason
Screenshot/logs for failure
Retry/flaky information
```

Avoid excessive logs and keep detailed logging available when debugging is required.

## 40. How should we think about automation and debugging?
**Conclusion:** Do not automate everything. Select tests based on value, stability, repetition, determinism, frequency and maintenance cost.

For failures, first identify whether the issue is related to:
```text
Business/assertion
Locator
Synchronization
Test data
Application
Environment
Network
Browser
Framework
```

**Why:** The goal is not just to write Selenium scripts. We want to learn how to build, maintain, troubleshoot and improve a scalable automation framework.
