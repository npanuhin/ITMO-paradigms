"use strict";

// Utility functions
function createExpression(obj, evaluate, toString, diff, prefix = toString, postfix = toString) {
    obj.prototype.evaluate = evaluate;
    obj.prototype.toString = toString;
    obj.prototype.diff = diff;
    obj.prototype.prefix = prefix;
    obj.prototype.postfix = postfix;
}

// Expression functions
function Const(value) {
    this.value = value;
}

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);

createExpression(Const,
    function() {
        return Number(this.value);
    },
    function() {
        return String(this.value);
    },
    () => Const.ZERO
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
        return this.varName === diffVar ? Const.ONE : Const.ZERO;
    }
);

function Operation(...args) {
    this.args = args;
}

createExpression(Operation,
    function(...params) {
        return this.operation(...this.args.map(x => x.evaluate(...params)));
    },
    function() {
        return this.args.join(' ') + ' ' + this.operand;
    },
    function(diffVar) {
        return this.diffFunc(diffVar, ...this.args)
    },
    function() {
        return '(' + this.operand + ' ' + this.args.map(x => x.prefix()).join(' ') + ')';
    },
    function() {
        return '(' + this.args.map(x => x.postfix()).join(' ') + ' ' + this.operand + ')';
    }
);

function createOperation(operand, operation, diffFunc) {
    function Obj(...args) {
        Operation.call(this, ...args);
    }
    Obj.prototype = Object.create(Operation.prototype);
    Obj.prototype.constructor = Obj;
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
        const subtr = new Subtract(x, b);
        return new Add(
            new Gauss(a.diff(diffVar), b, c, x),
            new Multiply(
                new Gauss(a, b, c, x),
                new Negate(
                    new Divide(
                        new Multiply(subtr, subtr),
                        new Multiply(
                            Const.TWO,
                            new Multiply(c, c)
                        )
                    )
                ).diff(diffVar)
            )
        );
    }
);

const Exp = createOperation(null, a => Math.exp(a),
    (diffVar, a) => new Multiply(a.diff(diffVar), new Exp(a))
);

const sumexp = args => args.reduce((a, b) => a + Math.exp(b), 0)
const Sumexp = createOperation("sumexp", (...args) => sumexp(args),
    (diffVar, ...args) => args.reduce((sum, operation) => new Add(sum, (new Exp(operation)).diff(diffVar)), Const.ZERO)
);

const Softmax = createOperation("softmax", (...args) => Math.exp(args[0]) / sumexp(args),
    (diffVar, ...args) => (new Divide(new Exp(args[0]), new Sumexp(...args))).diff(diffVar)
);

const OPERATIONS = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "gauss": Gauss,
    "sumexp": Sumexp,
    "softmax": Softmax
};


// RPN parser
function parse(expression) {
    const stack = [];
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


// ---------------------------------------------------- Homework 8 -----------------------------------------------------

// Errors
function ParsingError(message) {
    this.message = message;
}
ParsingError.prototype = Object.create(Error.prototype);
ParsingError.prototype.constructor = ParsingError;
ParsingError.prototype.name = "ParsingError";


function createParsingError(name, buildMessage) {
    function Error(...args) {
        ParsingError.call(this, buildMessage(...args));
    }
    Error.prototype = Object.create(ParsingError.prototype);
    Error.prototype.constructor = ParsingError;
    Error.prototype.name = name;
    return Error;
}

const MissClosingParenthesisError = createParsingError("MissClosingParenthesisError",
    (pos, foundToken) => `Expected ) at pos ${pos} but found ${foundToken}`
);

const UnknownOperationError = createParsingError("UnknownOperationError",
    (pos, foundToken) => `Invalid operation token at pos ${pos}, found ${foundToken}`
);

const UnexpectedTokenError = createParsingError("UnexpectedTokenError",
    (pos, foundToken) => `Unexpected token ${foundToken} at pos ${pos}`
);

const MissingArgsError = createParsingError("MissingArgsError",
    (pos, operand, foundCount, expectedCount) =>
    `Expected ${expectedCount} arguments for operation ${operand} at pos ${pos} but found ${foundCount}`
);


// Parser
function Parser(source, separators) {
    let _pos = 0;
    this.getPos = () => _pos;
    this.cur = () => source[_pos];
    this.next = () => source[_pos++];
    this.move = (i = 1) => _pos += i;
    this.getSource = () => source;
    this.isSeparator = () => separators.includes(source[_pos]);
}

Parser.prototype.hasNext = function() {
    return this.getPos() + 1 <= this.getSource().length;
};

Parser.prototype.skipWhitespaces = function() {
    while (this.hasNext() && this.cur() === ' ') {
        this.move();
    }
};

Parser.prototype.expectToken = function(expectedToken) {
    this.skipWhitespaces();
    return this.getSource().startsWith(expectedToken, this.getPos());
};

Parser.prototype.parseToken = function() {
    this.skipWhitespaces();
    if (this.hasNext() && this.isSeparator()) {
        return this.next();
    }
    let token = "";
    while (this.hasNext() && !this.isSeparator()) {
        token += this.next();
    }
    return token;
};

Parser.prototype.getToken = function() {
    const token = this.parseToken();
    this.move(-token.length);
    return token;
};


const ParsingMode = Object.freeze({PREFIX: "prefix", POSTFIX: "postfix"});

function parseExpression(expression, mode) {
    expression = expression.trim();
    const parser = new Parser(expression, [' ', '(', ')']);

    function parseToken(token) {
        if (token === '(') {
            const result = parseExpression();
            token = parser.parseToken();
            if (token !== ')') {
                throw new MissClosingParenthesisError(parser.getPos(), token);
            }
            return result;
        } else if (token in VARS) {
            return new Variable(token);
        } else if (token && !isNaN(Number(token))) {
            return new Const(token);
        } else {
            throw new UnexpectedTokenError(parser.getPos(), token);
        }
    }

    function parseArgs() {
        const operationArgs = [];
        while (parser.hasNext() && !parser.expectToken(')') && !(parser.getToken() in OPERATIONS)) {
            operationArgs.push(parseToken(parser.parseToken()))
        }
        return operationArgs;
    }

    function parseOperation() {
        const token = parser.parseToken();
        if (token in OPERATIONS) {
            return OPERATIONS[token];
        }
        throw new UnknownOperationError(parser.getPos(), token);
    }

    function parseExpression() {
        let operationArgs, curOperation;
        if (mode === ParsingMode.POSTFIX) {
            operationArgs = parseArgs();
            curOperation = parseOperation();
        } else {
            curOperation = parseOperation();
            operationArgs = parseArgs();
        }
        const argsLen = curOperation.prototype.operation.length;
        if (argsLen !== 0 && argsLen !== operationArgs.length) {
            throw new MissingArgsError(parser.getPos(), curOperation.prototype.operand, operationArgs.length, argsLen);
        }
        return new curOperation(...operationArgs);
    }

    function parse() {
        const result = parseToken(parser.parseToken());
        if (parser.hasNext()) {
            throw new ParsingError("Unexpected symbols at the end of expression");
        }
        return result;
    }

    return parse();
}

const parsePostfix = expression => parseExpression(expression, ParsingMode.POSTFIX);
const parsePrefix = expression => parseExpression(expression, ParsingMode.PREFIX);
// const parsePrefix = parse;
