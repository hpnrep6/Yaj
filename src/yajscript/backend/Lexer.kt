package yajscript.backend

import yajscript.YajInterpreter
import kotlin.math.exp

class Lexer(interpreter: YajInterpreter) {
    val output = interpreter

    var errors: MutableList<Error> = mutableListOf<Error>()
    var tokens: MutableList<Token> = mutableListOf<Token>()

    var index: Int = 0
    var col: Int = 0
    var line: Int = 0

    var source: String = ""

    fun reset() {
        errors.clear()
        tokens.clear()
    }

    fun getLocation() {

    }

    fun atEnd(offset : Int = 0) : Boolean {
        return index + offset > source.length
    }

    fun peekIs(expected : Char) : Boolean {
        if (atEnd(1)) {
            return false
        }

        return source[index + 1] == expected
    }

    fun peekIs(expected : (Char) -> Boolean) : Boolean {
        if (atEnd(1)) {
            return false
        }

        return expected(source[index + 1])
    }

    fun isDigit(char : Char) : Boolean {
        return false
    }

    fun isAlphaNumerical(char : Char) : Boolean {
        return false
    }

    fun isAlpha(char: Char) : Boolean {
        return false
    }

    fun skipWhitespace() {

    }

    fun handleDigit() {

    }

    /**
     * Generate a keyword token, if there is one
     *
     * @param word String to check for keyword
     * @return whether or not a keyword was found
     */
    fun handleKeyword(word : String) : Boolean {
        return true
    }

    fun handleWord() {
        handleKeyword("e")
    }

    fun handleSymbol() {

    }

    fun generateTokens(source: String): MutableList<Token> {
        this.source = source
        reset()

        return tokens
    }
}