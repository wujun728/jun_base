// Prettier配置文档：https://prettier.io/docs/en/options.html
module.exports = {
    printWidth: 180,
    tabWidth: 4,
    useTabs: false,
    semi: true,
    singleQuote: true,
    quoteProps: 'as-needed',
    jsxSingleQuote: false,
    trailingComma: 'none',
    bracketSpacing: true,
    bracketSameLine: false,
    arrowParens: 'always',
    proseWrap: 'preserve',
    htmlWhitespaceSensitivity: 'ignore',
    vueIndentScriptAndStyle: false,
    endOfLine: 'lf',
    embeddedLanguageFormatting: 'auto',
    singleAttributePerLine: false,
    //自定义文件后缀对应的parser
    parsers: {
        '.nvue': 'vue',
        '.ux': 'vue'
    }
};