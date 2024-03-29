## Yaj Interpreter

An abstract syntax tree interpreter.
  
## Features
 
- Types
  - Number (Double-precision floating points)
  - String (Strings)
  - Bool (Booleans)
  - Basic type casting
- Control structures
  - If
  - Else
  - Or (Syntactic sugar for `else if`)
- Expressions
  - Arithmetic
  - Boolean
    - Boolean operations
    - Boolean comparisons
  - String concatenation
  - Variable lookup
- Variables
  - Variable initialisation
  - Variable assignment
- Loops
  - While
- Procedures
  - Procedure initialisation
  - Procedure calls
  - Procedure return values
- Functions
  - Function initilisation
  - Function parameters
  - Function calls
  - Function return values
- Comments
  - Single-line comments
- Scope
  - Variable shadowing
  - Outer scope variable lookup
- Output
  - Out (Defaults to kotlin's `println` function)
  - Cast to string
- Debug
  - Abstract syntax tree
    - Print AST

#### Todo
- For loop syntactic sugar
- Runtime error reporting (currently only reports errors in the lexer and parser stages)

## Example

### Input
```
,, Fibonacci sequence
func fib(iter) {
    if (iter <= 1) {
        return iter
    }

    return (fib(iter - 1) + fib(iter - 2))
}

var b := 0

while (b < 10) {
    Out("Fib sequence at index " + b + " is " + fib(b))
    b := b + 1
}
```

### Output
```
Fib sequence at index 0 is 0
Fib sequence at index 1 is 1
Fib sequence at index 2 is 1
Fib sequence at index 3 is 2
Fib sequence at index 4 is 3
Fib sequence at index 5 is 5
Fib sequence at index 6 is 8
Fib sequence at index 7 is 13
Fib sequence at index 8 is 21
Fib sequence at index 9 is 34
```

### API Example

```
import yaj.YajInterpreter

fun main(args: Array<String>) {
  val sourceString = readFromFile("example.yaj") // Function to read source code as string
  
  val interpreter = YajInterpreter(sourceString) // Create interpreter object
  
  interpreter.run() // Execute the yaj source code
}
```

## Extended Backus-Naur form Grammar
```
(* characters *)
alpha = ? ASCII characters A to Z, a to z, À and onwards ? ;
numerical = ? ASCII characters 0 to 9 ? ;
alphanumerical = alpha | numerical ;
new_line = ? new line character ? ;
all_characters = ? all visible characters ? - new_line ;
endl = new_line | ";" ;

(* string *)
string_definition = "'" | """ ;
string_regular = string_definition, all_characters - string_definition, string_definition ;
string_multiline = "`", (all_characters | new_line) - "`" , "`" ;

(* number *)
number = numerical, { numerical }, ["."], { numerical } ;

(* variables *)
identifier = alphanumerical, { alphanumerical } ;
operation = expr | boolExpr | stringConcat | identifier ;
assign = identifier, ":=", operation ;
var_decl = "var", assign ;

(* type casting *)
castBool = "Bool", "(", boolExpr, ")" ;
castNum = "Num", "(", expr, ")" ;
castString = "String", "(", stringConcat, ")" ;
cast = castBool | castNum | castString ;

(* arithmetic *)
num = ( number | identifier | function_call | cast | ("(", expr, ")") ) | ( ( "+" | "-" ), num ) ;
add_sub = num, [ ("+" | "-"), mult_div] ;
mult_div = num, [ ("*", "/", "%"), num] ;
expr = mult_div, add_sub ;

(* boolean/logical *)
bool = "true" | "false" ;
comparison = "=" | ">" | ">=" | "<" | "<=" ;
bool_op_unary = ["!"], bool | function_call | identifier | bool_op_unary | cast ;
bool_op_bin = bool, ("&" | "|"), bool_op_unary ;
boolExpr = bool_op_unary, [{bool_op_bin}] ;

(* string manipulation *)
stringConcat = (string | identifier | number | cast | function_call), ["+", stringConcat] ;

(* output *)
out = "Out", "(", stringConcat, ")" ;

(* code comments *)
comment = ",," ;

(* conditional statements *)
if_statement = "if", "(", boolExpr, ")", "{", scene, [else_statement] ;
else_statement = "else", scene ;

(* loops *)
while_loop = "while", "(", boolExpr, ")", "{", scene ;

(* functions *)
procedure_decl = "proc", identifier, ["()"], "{", scene ;
procedure_call = identifier, "(", ")" ;
function_decl = "func", identifier, "(", identifier, { ",", identifier } , ")", "{", scene ;
function_call = identifier, "(", operation, [{ ",", operation }], ")" ;
return = "return", "(", operation, ")" ;

(* main program *)
scene = [{(
    var_decl | 
    assign | 
    out | 
    if_statement | 
    while_loop | 
    procedure_decl |
    procedure_call |
    function_decl |
    procedure_call
    ), 
    endl }], 
    ("}" | ? EOF ? ) ;
    
yaj_program = scene ;
```

## File structure

| Directory                      | Description                             |
| ------------------------------ | --------------------------------------- |
| `src/yaj/`                     | Interpreter files                       |
| `src/tests/`                   | Interpreter tests                       |
| `src/yaj/backend/`             | Core files                              |
| `src/yaj/backend/type/`        | Token variable types                    |
| `src/yaj/backend/ast/`         | Abstract syntax tree files              |
| `src/yaj/backend/ast/visitor/` | Abstract syntax tree visitor            |
| `src/yaj/backend/parser/`      | Parser to generate abstract syntax tree |
