package yajscript

import yajscript.backend.Lexer
import yajscript.backend.Parser
import yajscript.backend.Interpreter

class YajInterpreter (source : String) {
    val source = source

    private var lexer : Lexer = Lexer()
    private var parser : Parser = Parser()
    private var interpreter : Interpreter = Interpreter()

    public final fun lex() {

    }

    public final fun parse() {

    }

    public final fun interpret() {

    }

    public final fun run() {
        lex()
        parse()
        interpret()
    }

    public fun out(message : String) {
        println(message)
    }

    public fun errorOut(message : String) {
        out(message)
    }
}