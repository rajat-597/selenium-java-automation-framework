# Mistakes and Lessons

1. POM is more than locators — it also encapsulates page behavior/actions.
2. ThreadLocal does not create drivers — it isolates a stored driver reference by thread.
3. Do not share one WebDriver in parallel execution — centralize driver management, not the mutable browser session.
4. Page Objects should not create browsers — browser creation belongs to driver infrastructure.
5. Assertions belong in tests — Page Objects expose behavior/state.
6. Do not create PageManager prematurely — direct page creation is enough while the project is small.
7. Do not create a giant Utils class — place functionality according to responsibility.
8. Keep BaseTest thin — do not put unrelated framework responsibilities there.
9. Retry is not a defect fix — it should not hide genuine failures.
10. Folders and TestNG groups have different purposes — folders organize code; groups control classification/execution.
11. Browser-specific suite files are unnecessary by default — use runtime browser parameters.
12. Configuration needs precedence — defaults → environment variables → Maven parameters.
13. Shared mutable static test data is dangerous — use test-scoped context.
14. API can be used for fast test setup — keep UI focused on the behavior under test.
15. Do not automate everything — select automation based on value and maintainability.
