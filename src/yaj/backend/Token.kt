package yaj.backend

import yaj.backend.type.Type

enum class TokenType {
// Variable types
    NULL,           // null value

// Variables reference
    IDENTIFIER,

// Assignment
    ASSIGN_P,       // :    Assign pointer
    ASSIGN_V,       // :=   Assign value

// Punctuation
    BRACE_L,        // {
    BRACE_R,        // }
    BRACK_L,        // [
    BRACK_R,        // ]
    PAREN_L,        // (
    PAREN_R,        // )
    COMMA,          // ,
    SEMICOLON,      // ;
    PERIOD,         // .
    PERIOD_DOUBLE,  // ..
    QUOTE_DOUBLE,   // "
    QUOTE_SINGLE,   // '
    BACKTICK,       // `

// Whitespace
    NEW_LINE,       // "\n"

// Math
    ADD,            // +
    SUB,            // -
    MULT,           // *
    DIV,            // /
    MOD,            // %
    POW,            // ^

// Control flow
    IF,             // if
    ELSE,           // else
    OR_CONTROL,     // or ; else if / elif
    FOR,            // for
    WHILE,          // while
    FOREVER,        // forever ; while(true)

// Keywords
    VAR_DEF,        // var
    FUNC,           // func
    PROC,           // proc
    RETURN,         // return
    EXIT,           // exit
    ERROR,          // Error()
    ERROR_CRIT,     // CriticalError()
    OUT,            // Out() ; Print

// Literals
    DOUBLE,         // 123.45
    STRING,         // " ... "
    BOOL,           // true / false
    NONE_LIT,       // none ; null

// Casting
    CAST_NUM,       // Num
    CAST_STR,       // String
    CAST_BOOL,      // Bool

// Boolean
    NOT,            // !
    OR,             // |
    AND,            // &
    // Comparison
    EQUALS,         // =
    NOT_EQUALS,     // !=
    GREATER_EQUALS, // >=
    LESS_EQUALS,    // <=
    LESS,           // <
    GREATER,        // >

// Other
    START_OF,       // Start of file
    EOF             // End of file
}

class Token(type: TokenType, value: Type, line: Int, column: Int) {
    val type = type
    val value = value
    val line = line
    val column = column

    override fun toString() : String {
        return "Token of type: $type with value: {$value} at line: $line, column: $column"
    }
}