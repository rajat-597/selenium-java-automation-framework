# Automation Selection and Debugging

## Automation selection

We should not automate every manual test case.

Consider automation when the test is:
- repetitive
- stable
- deterministic
- frequently executed
- regression-prone
- valuable enough to justify maintenance

## Debugging approach

When a test fails, first classify the failure:

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

Then use evidence:

```text
Exception/stack trace
Logs
Screenshot
Current URL
Browser
Environment
Test data
```

The goal is to fix the issue at the correct layer rather than changing Selenium code blindly.
