/* eslint-disable no-console */
const fs = require('fs');
const path = require('path');

function rmSafe(targetPath) {
  try {
    fs.rmSync(targetPath, { recursive: true, force: true });
    console.log(`[cleanup] removed: ${path.relative(process.cwd(), targetPath)}`);
  } catch (err) {
    console.warn(`[cleanup] failed to remove: ${targetPath}`);
    console.warn(err);
  }
}

function main() {
  const cwd = process.cwd();

  const paths = [
    path.join(cwd, 'dist'),
    path.join(cwd, 'coverage'),
    path.join(cwd, '.eslintcache'),
    path.join(cwd, 'node_modules', '.vite'),
  ];

  // Remove dist-* directories (e.g. dist-test, dist-prod)
  try {
    const entries = fs.readdirSync(cwd, { withFileTypes: true });
    entries
      .filter((e) => e.isDirectory() && e.name.startsWith('dist-'))
      .forEach((e) => paths.push(path.join(cwd, e.name)));
  } catch {
    // ignore
  }

  paths.forEach((p) => rmSafe(p));
}

main();

