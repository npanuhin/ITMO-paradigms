"use strict";

// Utility functions
function createExpression(obj, evaluate, toString, diff) {
    obj.prototype.evaluate = evaluate;
    obj.prototype.toString = toString;
    obj.prototype.diff = diff;
}


// Expression functions
function Const(value) {
    this.value = value;
}

// let CONSTS = Object.assign({}, ...[0, 1, 2].map(i => ({[i]: new Const(i)})));
let CONSTS = [0, 1, 2].reduce((obj, num) => {
    obj[num] = new Const(num);
    return obj;
}, {});

createExpression(Const,
    function() {
        return Number(this.value);
    },
    function() {
        return String(this.value);
    },
    () => CONSTS[0]
);


const VARS = {
    "x": 0,
    "y": 1,
    "z": 2
};

function Variable(varName) {
    this.varName = varName;
    this.varIndex = VARS[varName];
}

createExpression(Variable,
    function(...vars) {
        return vars[this.varIndex];
    },
    function() {
        return this.varName;
    },
    function(diffVar) {
        return this.varName === diffVar ? CONSTS[1] : CONSTS[0];
    }
);

function Operation(...args) {
    this.args = args;
}

createExpression(Operation,
    function(...params) {
        return this.operation(...this.args.map(f => f.evaluate(...params)));
    },
    function() {
        return this.args.join(' ') + ' ' + this.operand;
    },
    function(diffVar) {
        return this.diffFunc(diffVar, ...this.args)
    }
);

function createOperation(operand, operation, diffFunc) {
    function Obj(...args) {
        Operation.call(this, ...args);
    };
    Obj.prototype = Object.create(Operation.prototype);
    Obj.prototype.operand = operand;
    Obj.prototype.operation = operation;
    Obj.prototype.diffFunc = diffFunc;
    return Obj;
}

const Add = createOperation("+", (a, b) => a + b,
    (diffVar, a, b) => new Add(a.diff(diffVar), b.diff(diffVar))
);

const Subtract = createOperation("-", (a, b) => a - b,
    (diffVar, a, b) => new Subtract(a.diff(diffVar), b.diff(diffVar))
);

const Multiply = createOperation("*", (a, b) => a * b,
    (diffVar, a, b) => new Add(
        new Multiply(a.diff(diffVar), b),
        new Multiply(a, b.diff(diffVar))
    )
);

const Divide = createOperation("/", (a, b) => a / b,
    (diffVar, a, b) => new Divide(
        new Subtract(
            new Multiply(a.diff(diffVar), b),
            new Multiply(a, b.diff(diffVar))
        ),
        new Multiply(b, b)
    )
);

const Negate = createOperation("negate", a => -a,
    (diffVar, a) => new Negate(a.diff(diffVar))
);

const Gauss = createOperation("gauss", (a, b, c, x) => a * Math.exp(-((x - b) * (x - b)) / (2 * c * c)),
    (diffVar, a, b, c, x) => {
        const substr = new Subtract(x, b);
        return new Add(
            new Gauss(a.diff(diffVar), b, c, x),
            new Multiply(
                new Gauss(a, b, c, x),
                new Negate(
                    new Divide(
                        new Multiply(substr, substr),
                        new Multiply(
                            CONSTS[2],
                            new Multiply(c, c)
                        )
                    )
                ).diff(diffVar)
            )
        )
    }
);

const OPERATIONS = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "gauss": Gauss
};


// RPN parser
function parse(expression) {
    let stack = [];
    expression.split(' ').filter(x => x.length).forEach(item => {
        if (item in OPERATIONS) {
            const curOperation = OPERATIONS[item];
            stack.push(new curOperation(...stack.splice(-curOperation.prototype.operation.length)));

        } else if (item in VARS) {
            stack.push(new Variable(item));

        } else {
            stack.push(new Const(Number(item)));
        }
    });
    return stack.pop();
}


// Main
const test_expr = new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
);
console.log(test_expr.evaluate(5, 0, 0));
