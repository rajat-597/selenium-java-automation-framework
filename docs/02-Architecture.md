# Framework Architecture

## Current structure

```text
src/main/java/com/rajat/automation/
├── core/
│   ├── config/
│   ├── driver/
│   ├── listeners/
│   ├── logging/
│   └── reporting/
├── ui/
│   ├── pages/
│   ├── components/
│   └── actions/
├── data/
├── api/
└── database/

src/test/java/com/rajat/automation/tests/
├── ui/
└── endtoend/
```

This structure is the current starting point. We will add folders/classes only when a real responsibility requires them.

## Driver design

```text
BaseTest
   ↓
DriverManager
   ↓
ThreadLocal<WebDriver>
   ↑
DriverFactory
```

DriverFactory creates the browser driver. DriverManager manages driver access and lifecycle. ThreadLocal keeps the current driver reference isolated per thread. BaseTest handles common test lifecycle. Tests do not create WebDriver directly.

## Page Objects

Page Objects contain locators and page behavior/actions. They receive an existing WebDriver and do not create browsers. Assertions remain in tests.

For now, pages are created directly, for example:

```java
new LoginPage(driver);
```

A PageManager will be introduced only if centralized page creation later solves a real problem.

## Locators

Use normal `By` locators rather than PageFactory.

Prefer stable locators such as:
1. `data-testid` or another stable test attribute
2. stable unique ID
3. meaningful/accessibility attributes
4. CSS
5. XPath when required

XPath is acceptable; brittle structural XPath should be avoided.

## UI components

Reusable application components such as Header and Sidebar belong under `ui/components/`.

## UI actions

Common UI-specific technical actions belong under `ui/actions/`.

Do not create a large generic `Utils` class.

## Waits

Use condition-based explicit waits as the normal synchronization strategy. Avoid `Thread.sleep()` as the normal mechanism.

## Configuration

Configuration precedence:

```text
default config
    ↓
environment variables
    ↓
Maven runtime parameters
```

Example:

```text
mvn test -Dbrowser=edge -Denv=uat
```

Secrets must come from environment/CI secret storage or appropriate secret management and must not be committed.

## Test data

Use static data for stable business values and dynamic data for uniqueness/isolation.

Use a test-scoped context for values such as customerId, email and orderId when a scenario needs to share state across API/UI/DB steps.

Avoid shared mutable static test data.

## API and database

API and DB are supporting capabilities inside this Selenium project.

Typical flow:

```text
API creates data
    ↓
Selenium verifies/uses it
    ↓
DB validates state
```

The project will not become a separate full API or DB framework.

## Test organization

Folders organize code. TestNG groups classify execution.

Example:

```text
Login   → smoke
Search  → regression
Payment → smoke + regression
```

Browser is runtime configuration rather than separate XML files for every browser/group combination.

Examples:

```text
mvn test -Dgroups=smoke -Dbrowser=chrome
mvn test -Dgroups=regression -Dbrowser=edge
```

## Parallel execution

Parallel execution is supported but controlled. The concurrency level should be configurable according to application and infrastructure capacity.

Each parallel execution must have isolated driver/session state and appropriate test data.

## Retry

Retry is configurable and should mainly address potentially transient failures.

Genuine assertion/business failures should not be blindly retried.

A test that passes after retry should remain clearly identified as retried/potentially flaky.

## Failure handling

A TestNG listener centrally captures failure diagnostics.

Useful evidence includes:
- screenshot
- exception/stack trace
- relevant logs
- current URL
- browser
- environment

The listener orchestrates diagnostics and reporting rather than becoming a giant class.

## Reporting

Reports should show:
- passed/failed/skipped
- browser
- environment
- duration
- failure reason
- failure evidence
- retry/flaky information
