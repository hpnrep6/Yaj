## Yaj

An AST interpreter for the Yaj programming language.
  
## Features
 
- Types
  - Number (Kotlin double-precision floating point)
  - String (Kotlin strings)
  - Bool (Kotlin boolean)
- Control structures
  - If
  - Else
  - Or (Syntactic sugar for `else if`)
- Expressions
  - Maths
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
- Scope
  - Variable shadowing
  - Outer scope variable lookup
- Output
  - Out (Defaults to kotlin's `println` function)
 
#### TODO:
- Counted loops
- "Expression variable assignement"
- Functions
- Structs
- Classes
- Runtime error reporting

## Extended Backus-Naur form Grammar
```
alpha = ? ASCII characters A to Z, a to z, À and onwards ? ;

numerical = ? ASCII characters 0 to 9 ? ;

alphanumerical = alpha | numerical ;

new_line = ? new line character ? ;

all_characters = ? all visible characters ? - new_line ;

string_definition = "'" | """ ;

string_regular = string_definition, all_characters - string_definition, string_definition ;

string_multiline = "\`", (all_characters | new_line) - "\`" , "\`" ;

number = numerical, { numerical | "." } ;

identifier = alphanumerical, { alphanumerical } ;

assign = identifier, ":=", expr | boolExpr | stringConcat | identifier;

var_decl = "var", assign ;

num = "+" | "-" | number | identifier | ("(", expr, ")") ;

add_sub = num, \[ ("+" | "-"), mult_div] ;
  
mult_div = num, \[ ("\*", "/", "%"), num] ;

expr = mult_div, add_sub ;

bool = "true" | "false" ;

comparison = "=" | ">" | ">=" | "<" | "<=" ;

bool_op_unary = ("!", bool) | bool ;

bool_op_bin = bool, ("&" | "|"), bool_op_unary ;

boolExpr = bool_op_unary, \[{bool_op_bin}] ;

stringConcat = (string | identifier | number), ["+", stringConcat] ;

out = "Out", "(", stringConcat, ")" ;

if_statement = "if", "(", boolExpr, ")", "{", scene ;

while_loop = "while", "(", boolExpr, ")", "{", scene ;

scene = [{var_decl | assign | out | if_statement | while_loop}], ("}" | ? EOF ? ) ;

program = scene ;
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
| `src/yaj/backend/parser`       | Parser to generate abstract syntax tree |
