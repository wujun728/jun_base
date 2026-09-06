# Contributing Guide

Thanks for taking the time to contribute!

## Getting Started

- Node.js: `>= 18`
- Install dependencies:
  - `npm ci` (recommended when `package-lock.json` exists)
  - or `npm install`
- Start dev server: `npm run dev`

## Project Scripts

- `npm run dev` - local development
- `npm run build` - production build (`--mode prod`)
- `npm run build:test` - test build (`--mode test`)
- `npm run preview` - preview build output

## Making Changes

1. Create a branch from `master`
2. Keep PRs focused and small when possible
3. Avoid unrelated formatting changes in the same PR

## Code Style

- Prefer existing patterns in the codebase
- If you touch a file, keep formatting consistent with surrounding code

## Submitting a Pull Request

Before opening a PR:

- Ensure `npm run build` passes locally
- Update docs if behavior/config changed (README, env examples, etc.)

## Reporting Bugs / Requesting Features

Please use GitHub Issues and follow the templates.

