package yajscript.backend.parser

import yajscript.YajInterpreter
import yajscript.backend.Error
import yajscript.backend.Token
import yajscript.backend.TokenType
import yajscript.backend.ast.Node
import yajscript.backend.ast.*
import yajscript.backend.ast.Identifier
import yajscript.backend.ast.Number
import yajscript.backend.type.Double
import kotlin.test.currentStackTrace

class Parser(interpreter: YajInterpreter) {

    val output = interpreter
    lateinit var source: kotlin.String
    var sourceSplit: List<kotlin.String>? = null

    lateinit var tokens : MutableList<Token>

    val errors: MutableList<Error> = mutableListOf<Error>()

    var index: Int = 0

    private fun reset() {
        errors.clear()
        index = 0
    }

    fun atEnd(offset: Int = 0) : Boolean {
        return index + offset >= tokens.size
    }

    fun getLine(line: Int) : kotlin.String {
        if (sourceSplit == null) {
            sourceSplit = source.split("\n")
        }
        return sourceSplit!![line]
    }

    fun error(offset: Int = 0, expected: TokenType? = null) {
        var exp = if (expected == null) "" else ". Expected $expected"
//var a = currentStackTrace(); for (i in a) println(i.toString())

        var token = tokens[index + offset]
        errors.add(
            Error("Parser error: unexpected token$exp", getLine(token.line), token.line, token.column)
        )
    }

    fun EOFError() {
        var token = tokens[tokens.size - 1]

            Error("Parser error: Reached end of line while parsing", getLine(token.line), token.line, token.column).print(::println)

        errors.add(
            Error("Parser error: Reached end of line while parsing", getLine(token.line), token.line, token.column)
        )
    }

    fun consume(expected: TokenType): Token? {
        if (curIs(expected)) {
            return tokens[index++]
        } else {
            ++index
            return null
        }
    }

    fun curIs(expected: TokenType, error: Boolean = true): Boolean {
        return tokenIs(expected, 0, error)
    }

    fun tokenIs(expected: TokenType, offset: Int = 1, hasError: Boolean = true): Boolean {
        if (atEnd(offset)) {
            if (hasError)
                EOFError()
            return false
        }

        if (tokens[index + offset].type == expected) {
            return true
        } else {

            if (hasError) {
                error(0, expected)
            }
            return false
        }
    }

    fun skipWhitespace() {
        while (curIs(TokenType.SEMICOLON, false) || curIs(TokenType.NEW_LINE, false)) {
            index++
        }
    }

    fun parse(tokens: MutableList<Token>, source: String, sourceSplit: List<String>? = null) : Node {
        this.tokens = tokens
        this.source = source
        this.sourceSplit = sourceSplit
        reset()

        val root = scene()

        return root
    }

    fun scene(): Scene {
        val nodes = mutableListOf<Node>()
        val scope = Scope()

        do {
            skipWhitespace()

            when (tokens[index].type) {
                TokenType.VAR_DEF -> {
                    ++index
                    val node = varDef(scope) ?: continue

                    nodes.add(node)
                }

                TokenType.IDENTIFIER -> {
                    val variable = getVar(scope)

                    val assign = assign(variable, scope) ?: continue

                    nodes.add(assign)
                }


                else -> {
                    error()
                    ++index
                }
            }

        } while (curIs(TokenType.SEMICOLON, false) || curIs(TokenType.NEW_LINE, false))

        return Scene(nodes, scope)
    }

    fun varDef(scope: Scope): Node? {
        val name = consume(TokenType.IDENTIFIER) ?: return null

        val definition = DefVar(Identifier((name.value as yajscript.backend.type.Identifier).value), scope)

        return assign(definition, scope)
    }

    fun getVar(scope: Scope): GetVar {
        return GetVar(
            Identifier((tokens[index++].value as yajscript.backend.type.Identifier).value),
            scope
        )
    }

    fun assign(variable: Var, scope: Scope): Node? {
        if (curIs(TokenType.ASSIGN_V, false)) {
            consume(TokenType.ASSIGN_V) ?: return null

            when (tokens[index].type) {
                TokenType.DOUBLE,
                TokenType.IDENTIFIER,
                TokenType.ADD,
                TokenType.SUB -> {
                    val evaluated = expr(scope)

                    return Assign(variable, evaluated)
                }

                TokenType.STRING -> {
                    return Assign(variable, string())
                }

                TokenType.BOOL_LIT -> {
                    return Assign(variable, bool())
                }

                else -> {
                    return null
                }
            }
        } else {
            return Assign(variable, Number(0.0))
        }
    }

    /**
     * String
     */
    fun string(): yajscript.backend.ast.String {
        return String(
            (tokens[index++].value as yajscript.backend.type.String).value
        )
    }

    /**
     * Boolean
     */
    fun bool(): Bool {
        return Bool(
            (tokens[index++].value as yajscript.backend.type.Bool).value
        )
    }

    /**
     * Math
     */
    fun mult_div(scope: Scope) : Node {
        var root = num(scope)

        while (!atEnd()) {
            when (tokens[index].type) {
                TokenType.MULT -> {
                    ++index
                    root = Multiply(root, num(scope))
                }

                TokenType.DIV -> {
                    ++index
                    root = Divide(root, num(scope))
                }

                TokenType.MOD -> {
                    ++index
                    root = Modulo(root, num(scope))
                }

                TokenType.ADD,
                TokenType.SUB,
                TokenType.PAREN_R,
                TokenType.NEW_LINE,
                TokenType.SEMICOLON -> {
                    return root
                }

                else -> {
                    error(0)
                    return root
                }
            }
        }

        return root
    }

    fun add_sub(node: Node, scope: Scope) : Node {
        var root = node

        while (!atEnd()) {
            when (tokens[index].type) {
                TokenType.ADD -> {
                    ++index
                    root = Add(root, Positive(mult_div(scope)))
                }

                TokenType.SUB -> {
                    ++index
                    root = Add(root, Negative(mult_div(scope)))
                }

                else -> {
                    return root
                }
            }
        }

        return root
    }

    fun num(scope: Scope) : Node {
        if (atEnd()) {
            error(-1)
            return Number(0.0)
        }

        when (tokens[index].type) {
            TokenType.DOUBLE -> {
                return Number(((tokens[index++].value) as Double).value)
            }

            TokenType.IDENTIFIER -> {
                return getVar(scope)
            }

            TokenType.PAREN_L -> {
                ++index;
                var node = expr(scope)
                tokenIs(TokenType.PAREN_R)
                return node
            }

            TokenType.ADD -> {
                ++index

                return Positive(num(scope))
            }

            TokenType.SUB -> {
                ++index

                return Negative(num(scope))
            }

            else -> {
                if (index > 0) {
                    error()
                } else
                    error(0)

                return Number(0.0)
            }
        }
    }

    fun expr(scope: Scope) : Node {
        var root = mult_div(scope)
        return add_sub(root, scope)
    }
}