package yajscript

import yajscript.backend.Lexer
import yajscript.backend.Parser
import yajscript.backend.Interpreter
import yajscript.backend.Token

class YajInterpreter (source : String) {
    val source = source
    val digitSeparator = '.'

    private var lexer : Lexer = Lexer(this)
    private var parser : Parser = Parser(this)
    private var interpreter : Interpreter = Interpreter(this)

    var encounteredError = false

    lateinit var tokens : MutableList<Token>

    public final fun lex(): MutableList<Token> {
        var tokens: MutableList<Token> = lexer.lex(source)

        if (lexer.errors.size > 0) {
            encounteredError = true
            for (error in lexer.errors) {
                error.print(::errorOut)
            }
        }
        
        return tokens
    }

    public final fun parse() {
        var ast = parser.parse(tokens)

        if (parser.errors.size > 0) {
            encounteredError = true
            for (error in parser.errors) {
                error.print(::errorOut)
            }
        }
    }

    public final fun interpret() {

    }

    public final fun run() : Boolean {
        tokens = lex()
        if (encounteredError) {
            return false
        }

        parse()
        if (encounteredError) {
            return false
        }

        interpret()
        if (encounteredError) {
            return false
        }

        return true
    }

    public fun out(message : String) {
        println(message)
    }

    public fun errorOut(message : String) {
        out(message)
    }

    public fun hasEncounteredError() : Boolean {
        return encounteredError
    }
}