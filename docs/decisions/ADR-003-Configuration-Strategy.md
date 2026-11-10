# ADR-003 — Configuration Strategy

## Decision
Use:
```text
Default config
→ Environment variables
→ Maven runtime parameters
```

More specific values override less specific values.

## Example
```text
mvn test -Dbrowser=edge -Denv=uat
```

## Secrets
Secrets must come from environment/CI secret storage or appropriate secret management and must not be committed to Git.
