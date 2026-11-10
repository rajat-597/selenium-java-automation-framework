# Framework Rules

1. Keep responsibilities separated.
2. Add abstractions only when they solve a real problem.
3. Keep tests focused on business scenarios.
4. Page Objects contain locators and UI behavior, not assertions.
5. Page Objects do not create WebDriver.
6. Do not share one mutable WebDriver across parallel executions.
7. ThreadLocal isolates driver references; it does not create drivers.
8. Keep BaseTest thin.
9. Do not create a giant Utils class.
10. Prefer explicit condition-based waits.
11. Avoid Thread.sleep() as normal synchronization.
12. Prefer stable locators.
13. Avoid shared mutable static test data.
14. Keep tests independent wherever practical.
15. Use static data for stable values and dynamic data for uniqueness.
16. Use API/DB as supporting capabilities, not as separate full frameworks in this project.
17. Never commit secrets.
18. Use configuration precedence: default config → environment variables → Maven runtime parameters.
19. Folders organize code; TestNG groups classify execution.
20. Browser should normally be runtime configuration.
21. Parallel execution must be controlled.
22. Retry must not hide genuine defects.
23. Failure diagnostics should be centralized.
24. Reports should retain useful failure evidence.
25. Documentation should stay aligned with the actual framework.
26. Prefer designs that are simple enough to explain clearly in an interview.
