package yajscript.backend

enum class TokenType {
// Variable types
    LITERAL,
    NULL,

// Variables reference
    VAR_NAME,
    FUNC_NAME,

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
    SEMIOLON,       // ;
    PERIOD,         // .
    PERIOD_DOUBLE,  // ..
    QUOTE_DOUBLE,   // "
    QUOTE_SINGLE,   // '

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
    FOREVER,        // forever

// Keywords
    FUNC,           // func
    PROC,           // proc
    RETURN,         // return
    EXIT,           // exit
    ERROR,          // Error()
    ERROR_CRIT,     // CriticalError()
    OUT,            // Out() ; Print

// Boolean
    NOT,            // !
    OR,             // ||
    AND,            // &&

// Other
    EOF             // End of file
}

class Token {
    // Types
    val NULL = "null"

    // Assignment
    val ASSIGN_P = ":"
    val ASSIGN_V = ":="

    // Punctuation
    val BRACE_L = "{"
    val BRACE_R = "}"
    val BRACK_L = "["
    val BRACK_R = "]"
    val PAREN_L = "("
    val PAREN_R = ")"
    val COMMA = ","
    val SEMIOLON = ";"
    val PERIOD = "."
    val PERIOD_DOUBLE = ".."
    val QUOTE_DOUBLE = "\""
    val QUOTE_SINGLE = "'"

    // Math
    val ADD = "+"
    val SUB = "-"
    val MULT = "*"
    val DIV = "/"
    val MOD = "%"
    val POW = "^"

    // Control flow
    val IF = "if"
    val ELSE = "else"
    val OR_CONTROL = "or"
    val FOR = "for"
    val WHILE = "while"
    val FOREVER = "forever"

    // Keywords
    val FUNC = "func"
    val PROC = "proc"
    val RETURN = "return"
    val EXIT = "exit"
    val ERROR = "Error"
    val ERROR_CRIT = "CriticalError"
    val OUT = "Out"

    // Boolean
    val NOT = "!"
    val OR = "||"
    val AND = "&&"
}