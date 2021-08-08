package yaj

import yaj.backend.Lexer
import yaj.backend.parser.Parser
import yaj.backend.Token
import yaj.backend.ast.Node
import yaj.backend.ast.visitor.Execute

open class YajInterpreter (source : String) {
    final val source = source
    val digitSeparator = '.'

    private var lexer : Lexer = Lexer(this)
    private var parser : Parser = Parser(this)
    private var interpreter : Execute = Execute(this)

    var encounteredError = false

    /**
     * Perform lexical analysis on input
     *
     * @return Tokens generated from string
     */
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

    /**
     * Parse the output of the lexical analysis
     *
     * @param tokens Tokens generated from lexer
     * @return Root node of parsed abstract syntax tree
     */
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

    /**
     * Walks the abstract syntax tree and walk
     *
     * @param root Root AST node from parser
     */
    public final fun interpret(root: Node) {
        root.visit(interpreter)
    }

    /**
     * Runs the program using the input.
     * Passes the input through the lexer, parser,
     * and AST visitor.
     */
    public final fun run() : Boolean {
        val tokens = lex()
        if (encounteredError) {
            return false
        }

        val node = parse(tokens)
        if (encounteredError) {
            return false
        }

        interpret(node)
        if (encounteredError) {
            return false
        }

        return true
    }

    /**
     * Output message handling, from the Yaj "Out" function
     *
     * @param message Output message
     */
    public open fun out(message : String) {
        println(message)
    }

    /**
     * Error output message handling
     *
     * @param message Error message
     */
    public open fun errorOut(message : String) {
        out(message)
    }

    /**
     * Whether an error has been encountered
     *
     * @return if an error has occured
     */
    public final fun hasEncounteredError() : Boolean {
        return encounteredError
    }
}