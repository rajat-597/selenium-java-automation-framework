# ADR-002 — Driver Management

## Decision
Use:
```text
DriverFactory → creates driver
DriverManager → manages driver
ThreadLocal<WebDriver> → isolates driver reference
BaseTest → handles common lifecycle
```

## Why
Parallel executions need isolated browser sessions.

## Important clarification
ThreadLocal does not create a driver. It only provides thread-local storage for the driver reference.
