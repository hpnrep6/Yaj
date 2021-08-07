package yajscript.backend

import yajscript.YajInterpreter
import yajscript.backend.type.*
import yajscript.backend.type.Double
import yajscript.backend.type.String
import yajscript.backend.Error
import kotlin.math.exp

/**
 * Initialises keywords hashmap for keyword identification
 */
fun initialistKeywords() : HashMap<kotlin.String, TokenType> {
    val keywordMap: HashMap<kotlin.String, TokenType> = HashMap<kotlin.String, TokenType>()

    keywordMap.put("var", TokenType.VAR_DEF)
    keywordMap.put("func", TokenType.FUNC_DEF)
    keywordMap.put("proc", TokenType.PROC)
    keywordMap.put("return", TokenType.RETURN)
    keywordMap.put("exit", TokenType.EXIT)
    keywordMap.put("Error", TokenType.ERROR)
    keywordMap.put("CriticalError", TokenType.ERROR_CRIT)
    keywordMap.put("Out", TokenType.OUT)

    keywordMap.put("if", TokenType.IF)
    keywordMap.put("else", TokenType.ELSE)
    keywordMap.put("or", TokenType.OR_CONTROL)
    keywordMap.put("for", TokenType.FOR)
    keywordMap.put("while", TokenType.WHILE)
    keywordMap.put("forever", TokenType.FOREVER)

    return keywordMap
}

/**
 * Keyword hashmap
 */
val keywords: HashMap<kotlin.String, TokenType> = initialistKeywords()

/**
 * Lexer class. Generates tokens from string input.
 */
class Lexer(interpreter: YajInterpreter) {
    val output = interpreter

    var errors: MutableList<Error> = mutableListOf<Error>()
    var tokens: MutableList<Token> = mutableListOf<Token>()

    var index: Int = 0
    var col: Int = 0
    var line: Int = 0

    var source: kotlin.String = ""

    /**
     * Resets previous token generation variables
     */
    fun reset() {
        errors.clear()
        tokens.clear()
    }

    fun getLine() : kotlin.String {
        if (splitLines == null) {
            splitLines = source.split("\n")
        }
        return splitLines!![line]
    }

    var splitLines: List<kotlin.String>? = null

    fun unexpectedTokenError(offset : Int = 0) {
        errors.add(
            Error("Unexpected token", getLine(), line, col)
        )
    }

    fun stringEndOfLineError(offset : Int = 0) {
        errors.add(
            Error("Unterminated string literal", getLine(), line, col + offset)
        )
    }

    /**
     * @param offset Offset to check at
     *
     * @return at end of string
     */
    fun atEnd(offset : Int = 0) : Boolean {
        return index + offset >= source.length
    }

    /**
     * Called to record that a new line has been reached
     */
    fun newLine() {
        line++
        col = 0
    }

    /**
     * Increment index and column counters
     */
    fun increment(amount : Int = 1) {
        index += amount
        col += amount
    }

    /**
     * Add a token
     *
     * @param type Type of token
     */
    fun addToken(type : TokenType) {
        addToken(type, None(), line, col)
    }

    /**
     * Add a double token
     *
     * @param value Value of double
     */
    fun addToken(value : kotlin.Double) {
        addToken(TokenType.DOUBLE, Double(value), line, col)
    }

    /**
     * Add a string token
     *
     * @param value Value of string
     */
    fun addToken(value : String) {
        addToken(TokenType.STRING, value, line, col)
    }

    /**
     * Add a token
     *
     * @param type Type of token
     * @param value Value of token
     */
    fun addToken(type : TokenType, value : Type) {
        addToken(type, value, line, col)
    }

    /**
     * Add a token
     *
     * @param type Type of token
     * @param value Value of token
     * @param line Line token is on
     * @param column Column token is on
     */
    fun addToken(type : TokenType, value : Type, line : Int = this.line, column : Int = this.col) {
        tokens.add(Token(
            type, value, line, column
        ))
    }

    /**
     * Check if next character is of character indicated
     *
     * @param expected Expected character
     * @param offset Offset from next index to check from
     */
    fun peekIs(expected : Char, offset: Int = 0) : Boolean {
        if (atEnd(1 + offset)) {
            return false
        }

        return source[index + 1 + offset] == expected
    }

    /**
     * Check if next character satisfies the given function
     *
     * @param expected Function to check if character is expected
     * @param offset Offset from next index to check
     */
    fun peekIs(expected : (Char) -> Boolean, offset: Int = 0) : Boolean {
        if (atEnd(1 + offset)) {
            return false
        }

        return expected(source[index + 1 + offset])
    }

    /**
     * Check if current character is of character indicated
     *
     * @param expected Expected character
     */
    fun curIs(expected: Char) : Boolean {
        return peekIs(expected, -1)
    }

    /**
     * Check if current character satisfies the given function
     *
     * @param expected Function to check if character is expected
     */
    fun curIs(expected: (Char) -> Boolean) : Boolean {
        return peekIs(expected, -1)
    }

    /**
     * Numerical digit check
     *
     * @return Whether character is a numerical digit
     */
    fun isDigit(char : Char) : Boolean {
        return char in '0'..'9'
    }

    /**
     * Alphabetical check
     *
     * @return Whether character is in the allowed alphabet
     */
    fun isAlpha(char: Char) : Boolean {
        return char in 'A'..'Z' || // Capital letters
               char in 'a'..'z' || // Lowercase letters
               char in 'À'..'ö' || // Latin
               char in 'ø'..'ʸ' || // Latin
               char >= 'Ā'         // Everything else
    }

    /**
     * @return Whether character is alphanumerical
     */
    fun isAlphaNumerical(char : Char) : Boolean {
        return isAlpha(char) || isDigit(char)
    }

    /**
     * Skip over blank spaces
     */
    fun skipWhitespace() {
        while (!atEnd()) {
            if (source[index] == ' ') {
                increment()
            } else {
                return
            }
        }
    }

    /**
     * Create number token
     */
    fun handleDigit() {
        val startIndex = index
        var endIndex = index
        var consumedSeparator = false

        while (true) {
            if (peekIs(::isDigit)) {

            } else if (peekIs(output.digitSeparator)) { // default '.'
                if (consumedSeparator) {
                    break
                } else {
                    consumedSeparator = true
                }
            } else {
                break
            }

            endIndex++
            increment()
        }

        // Skip to char after number for substring function and to parse next char. Example:
        // 1123.42
        //        ^ skip to here

        endIndex++
        increment()

        var numberString : kotlin.String = source.substring(startIndex, endIndex)
        var number : kotlin.Double = numberString.toDouble()
        addToken(number)
    }

    /**
     * Generate a keyword token, if there is one
     *
     * @param word String to check for keyword
     * @return whether or not a keyword was found
     */
    fun handleKeyword(word : kotlin.String) : Boolean {
        val possibleKeyword : TokenType? = keywords.get(word)

        if (possibleKeyword == null) {
            return false
        } else {
            addToken(possibleKeyword, None(), line, col - 1)
            return true
        }
    }

    /**
     * Create literal token
     *
     * @param word String of possible literal
     * @return whether a literal token has been created
     */
    fun handleLiteral(word : kotlin.String) : Boolean {
        when (word) {
            "true" -> {
                addToken(TokenType.BOOL, Bool(true), line, col - 1)
            }
            "false" -> {
                addToken(TokenType.BOOL, Bool(false), line, col - 1)
            }
            "none" -> {
                addToken(TokenType.NONE_LIT, None(), line, col - 1)
            }
            else -> {
                return false
            }
        }

        return true
    }

    /**
     * Handle literal, keyword, and identifier tokens
     */
    fun handleWord() {
        val startIndex = index
        var endIndex = index

        while (curIs(::isAlphaNumerical)) {
            endIndex++
            increment()
        }

        val name : kotlin.String = source.substring(startIndex, endIndex)

        if (handleLiteral(name)) {
            return
        }

        if (handleKeyword(name)) {
            return
        }

        addToken(TokenType.IDENTIFIER, Identifier(name), line, col - 1)
    }

    /**
     * Handle string token
     *
     * @param endChar Character to end string at
     * @param multiLine Whether it is a multiline string
     */
    fun handleString(endChar : Char, multiLine : Boolean = false) {
        // Skip past ', ", or `
        increment()

        val startIndex = index
        var endIndex = index

        while (!curIs(endChar)) {
            if (peekIs('\n')) {
                if (!multiLine) {
                    stringEndOfLineError(
                        if (line == 0)
                            1
                        else
                            0
                    )
                    break
                } else {
                    newLine()
                }
            } else if (atEnd()) {
                stringEndOfLineError(
                // Equivalent to:
//                 if (line == 0)
//                     0
//                 else
//                     -1
                // Assuming line is always positive
                    (-line).coerceAtLeast(-1)
                )
                return
            }

            endIndex++
            increment()
        }

        val string : kotlin.String = source.substring(startIndex, endIndex)
        addToken(String(string))

        increment()
    }

    /**
     * All encompassing function to generate a token
     */
    fun generateToken() {
        var current: Char = source[index]

        // Dynamic length tokens

        if (isAlpha(current)) {
            return handleWord()
        }

        if (isDigit(current)) {
            return handleDigit()
        }

        if (current == '"' || current == '\'') {
            return handleString(current, false)
        }

        if (current == '`') {
            return handleString(current, true)
        }

        // Static length tokens

        var increment : Int = 1

        when (current) {
            // Punctuation
            '{' -> addToken(TokenType.BRACE_L)
            '}' -> addToken(TokenType.BRACE_R)
            '[' -> addToken(TokenType.BRACK_L)
            ']' -> addToken(TokenType.BRACK_R)
            '(' -> addToken(TokenType.PAREN_L)
            ')' -> addToken(TokenType.PAREN_R)
            ',' -> addToken(TokenType.COMMA)
            ';' -> addToken(TokenType.SEMICOLON)
            '.' -> {
                if (peekIs('.')) {
                    addToken(TokenType.PERIOD_DOUBLE)
                    increment = 2
                } else
                    addToken(TokenType.PERIOD)
            }

            // Line
            '\n' -> {
                addToken(TokenType.NEW_LINE)
                newLine()
            }

            // Math
            '+' -> addToken(TokenType.ADD)
            '-' -> addToken(TokenType.SUB)
            '*' -> addToken(TokenType.MULT)
            '/' -> addToken(TokenType.DIV)
            '%' -> addToken(TokenType.MOD)
            '^' -> addToken(TokenType.POW)

            // Boolean
            '!' -> {
                if (peekIs('=')) {
                    addToken(TokenType.NOT_EQUALS)
                    increment = 2
                } else {
                    addToken(TokenType.NOT)
                }
            }
            '|' -> addToken(TokenType.OR)
            '&' -> addToken(TokenType.AND)
            '=' -> addToken(TokenType.EQUALS)

            // Assign
            ':' -> {
                if (peekIs('=')) {
                    addToken(TokenType.ASSIGN_V)
                    increment = 2
                } else {
                    addToken(TokenType.ASSIGN_P)
                }
            }

            // No suitable token found
            else -> {
                unexpectedTokenError()
            }
        }

        increment(increment)
    }

    /**
     * Generate tokens for entire input string
     *
     * @param source Source string to generate tokens for
     */
    fun lex(source: kotlin.String): MutableList<Token> {
        this.source = source
        reset()

        while (!atEnd()) {
            skipWhitespace()

            // Check if whitespace skipped brings index to end
            if (!atEnd())
                generateToken()
        }

        return tokens
    }
}