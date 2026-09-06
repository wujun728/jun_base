// @ts-check
import globals from "globals";
import pluginVue from "eslint-plugin-vue";
import tseslint from "typescript-eslint";
import stylistic from "@stylistic/eslint-plugin";

export default tseslint.config(
  {
    ignores: [
      "unpackage/",
      "uni_modules/",
      "node_modules/"
    ]
  },
  {
    files:
      ["**/*.{js,mjs,cjs,ts,vue}"],
    languageOptions:
    {
      globals: globals.browser
    }
  },
  ...tseslint.configs.recommended,
  ...pluginVue.configs["flat/essential"],
  {
    files: ["**/*.vue"],
    languageOptions: {
      parserOptions: {
        parser: tseslint.parser
      }
    }
  },
  stylistic.configs.customize({
    indent: 2,
    quotes: "double",
    semi: true,
    commaDangle: "only-multiline",
    braceStyle: "1tbs"
  }),
  {
    rules: {
      "@typescript-eslint/no-explicit-any": "off"
    }
  }
);
