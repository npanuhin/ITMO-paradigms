"use strict";

// Utility functions
const average = (...values) => values.reduce((a, b) => a + b, 0) / values.length;

const median = (...values) => {
    values.sort((a, b) => a - b);
    const middle = Math.floor(values.length / 2);
    return values.length % 2 ? values[middle] : (values[middle - 1] + values[middle]) / 2;
};


// Expression functions
const cnst = value => () => value;

const pi = cnst(Math.PI);
const e = cnst(Math.E);

const CONSTS = {
    "pi": pi,
    "e": e
};

const variable = (variableName) => (...args) => args[VARS[variableName]];

const VARS = {
    "x": 0,
    "y": 1,
    "z": 2
};

const operation = operation => (...funcs) => (...params) => operation(...funcs.map(f => f(...params)));

const add = operation((x, y) => x + y);
const subtract = operation((x, y) => x - y);
const multiply = operation((x, y) => x * y);
const divide = operation((x, y) => x / y);
const negate = operation(x => -x);
const avg3 = operation(average);
const med5 = operation(median);

const OPERATIONS = {
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1],
    "avg3": [avg3, 3],
    "med5": [med5, 5]
};


// RPN parser
function parse(expression) {
    let stack = [];
    expression.split(' ').filter(x => x.length).forEach(item => {
        if (item in OPERATIONS) {
            const curOperation = OPERATIONS[item];
            stack.push(curOperation[0](...stack.splice(-curOperation[1])));

        } else if (item in VARS) {
            stack.push(variable(item));

        } else if (item in CONSTS) {
            stack.push(CONSTS[item]);

        } else {
            stack.push(cnst(Number(item)));
        }
    });
    return stack.pop();
}


// Main
const testExpr = parse("x x 2 - * x * 1 +");
for (let i = 0; i <= 10; i++) {
    console.log(testExpr(i));
}
