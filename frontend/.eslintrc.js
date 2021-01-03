module.exports = {
    root: true,
    env: {
        browser: true,
        es2021: true,
    },
    extends: [
        'plugin:vue/essential',
        'airbnb-base',
    ],
    parserOptions: {
        ecmaVersion: 12,
        sourceType: 'module',
        parser: 'babel-eslint',
    },
    plugins: [
        'vue',
    ],
    rules: {
        indent: ['warn', 4],
        'no-console': 0,
        'no-unused-vars': [2, {
            vars: 'all',
            args: 'none',
        }],
        "comma-dangle": 0, // comma on last line of object
        quotes: 0, // single / double quotes
        semi: 0, // require semi-colon for every line
        "no-restricted-syntax": 0,
        "default-case": 0, // switch must have a default case
        "object-property-newline": 0, // all fields must on newlines
        "arrow-parens": 0,
        "no-param-reassign": 0, // assign to param
        'vue/valid-v-slot': ['error', {
            allowModifiers: true,
        }],
        'vue/no-side-effects-in-computed-properties': 0
    },
};
