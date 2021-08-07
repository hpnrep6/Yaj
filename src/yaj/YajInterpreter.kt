package yaj

import yaj.backend.Lexer
import yaj.backend.parser.Parser
import yaj.backend.Interpreter
import yaj.backend.Token
import yaj.backend.ast.Node

open class YajInterpreter (source : String) {
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

    public final fun parse(tokens: MutableList<Token>) : Node {
        var ast = parser.parse(tokens, source)

        if (parser.errors.size > 0) {
            encounteredError = true
            for (error in parser.errors) {
                error.print(::errorOut)
            }
        }

        return ast
    }

    public final fun interpret() {

    }

    public final fun run() : Boolean {
        tokens = lex()
        if (encounteredError) {
            return false
        }

        parse(tokens)
        if (encounteredError) {
            return false
        }

        interpret()
        if (encounteredError) {
            return false
        }

        return true
    }

    public open fun out(message : String) {
        println(message)
    }

    public open fun errorOut(message : String) {
        out(message)
    }

    public final fun hasEncounteredError() : Boolean {
        return encounteredError
    }
}