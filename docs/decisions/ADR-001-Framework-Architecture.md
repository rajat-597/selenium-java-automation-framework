# ADR-001 — Framework Architecture

## Decision
Use separated responsibilities for:
- core infrastructure
- UI pages/components/actions
- test data
- supporting API
- supporting database
- tests

## Why
This keeps business tests readable and makes technical infrastructure reusable.

## Trade-off
There are more files and folders than a simple script project, but the structure becomes easier to maintain as the framework grows.

## Rule
Do not add abstractions only for appearance. Add them when they solve a real problem.
