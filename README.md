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

## Grammar

\<scene>:

\<variable-declaration>:
  
\<assign>:  
  
\<expr>:
  
\<number>:  
  
\<add-sub>:
  
\<mult_div>:  

## File structure

| Directory                      | Description                             |
| ------------------------------ | --------------------------------------- |
| `src/yaj/`                     | Interpreter files                       |
| `src/tests/`                   | Interpreter tests                       |
| `src/yaj/backend/`             | Core files                              |
| `src/yaj/backend/type/`        | Token variable types                    |
| `src/yaj/backend/ast/`         | Abstract syntax tree files              |
| `src/yaj/backend/ast/visitor/` | Abstract syntax tree visitor            |
| `src/yaj/backend/parse/r`      | Parser to generate abstract syntax tree |
