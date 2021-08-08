package yaj.backend.parser

import yaj.YajInterpreter
import yaj.backend.Error
import yaj.backend.Token
import yaj.backend.TokenType
import yaj.backend.ast.Node
import yaj.backend.ast.*
import yaj.backend.ast.Number
import yaj.backend.type.Double
import yaj.backend.type.Identifier
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

    fun scene(parent: Scope? = null): Scene {
        val nodes = mutableListOf<Node>()
        val scope = Scope(parent)

        if (atEnd())
            return Scene(nodes, scope)

        if (tokens[index].type == TokenType.BRACE_L ||
            tokens[index].type == TokenType.START_OF) {
            ++index
        }

        do {
            skipWhitespace()

            if (atEnd())
                break

            when (tokens[index].type) {
                TokenType.VAR_DEF -> {
                    ++index
                    val node = varDef(scope) ?: continue

                    nodes.add(node)
                }

                TokenType.IDENTIFIER -> {
                    val funcCall = funcCall(scope)

                    if (funcCall != null) {
                        nodes.add(funcCall)
                        continue
                    }

                    val node = varGet(scope) ?: continue

                    nodes.add(node)
                }

                TokenType.OUT -> {
                    ++index
                    val out = out(scope) ?: continue

                    nodes.add(out)
                }

                TokenType.IF -> {
                    ++index
                    consume(TokenType.PAREN_L) ?: continue

                    val boolExpr = boolExpr(scope)

                    consume(TokenType.PAREN_R) ?: continue

                    val scene = scene(scope)

                    var otherwise: Scene? = null

                    if (tokenIs(TokenType.ELSE, 0, false)) {
                        consume(TokenType.ELSE)

                        otherwise = scene(scope)
                    }

                    nodes.add(If(boolExpr, scene, otherwise))
                }

                TokenType.WHILE -> {
                    ++index
                    consume(TokenType.PAREN_L) ?: continue

                    val boolExpr = boolExpr(scope)

                    consume(TokenType.PAREN_R) ?: continue

                    val scene = scene(scope)

                    nodes.add(While(boolExpr, scene))
                }

                TokenType.PROC -> {
                    ++index
                    val name = consume(TokenType.IDENTIFIER) ?: continue

                    val scene = scene(scope)

                    nodes.add(
                        DefProcedure((name.value as Identifier).value, scene, scope)
                    )
                }

                TokenType.FUNC -> {
                    ++index

                    val function = func(scope) ?: continue

                    nodes.add(function)
                }

                TokenType.RETURN -> {
                    ++index

                    if (tokenIs(TokenType.NEW_LINE, 0, false) ||
                        tokenIs(TokenType.SEMICOLON, 0, false)) {
                        nodes.add(Return(Number(0.0)))
                        continue
                    }

                    val expr = findOperation(scope) ?: continue

                    nodes.add(Return(expr))
                }

                TokenType.EOF,
                TokenType.BRACE_R -> {
                    ++index
                    break
                }

                else -> {
                    error()
                    ++index
                }
            }

        } while (curIs(TokenType.SEMICOLON, false) || curIs(TokenType.NEW_LINE, false))

        return Scene(nodes, scope)
    }

    fun funcCall(scope: Scope): Node? {
        if (tokenIs(TokenType.PAREN_L, 1, false)) {
            val id = tokens[index++].value as Identifier
            if (tokenIs(TokenType.PAREN_R, 1, false)) {
                val returnValue = GetProcedure(
                    id.value,
                    scope
                )
                consume(TokenType.PAREN_L)
                consume(TokenType.PAREN_R)
                return returnValue
            } else {
                ++index
                val parameters = mutableListOf<Node>()

                do {
                    val operation = findOperation(scope) ?: break

                    parameters.add(operation)

                    if (curIs(TokenType.PAREN_R, false)) {
                        ++index
                        break
                    }

                    consume(TokenType.COMMA)
                } while (true)

                return GetFunc(
                    id.value,
                    scope,
                    parameters
                )
            }
        }
        return null
    }

    fun func(scope: Scope): Node? {
        val name = consume(TokenType.IDENTIFIER) ?: return null

        consume(TokenType.PAREN_L) ?: return null

        // No parameters: is procedure
        if (tokenIs(TokenType.PAREN_R, 0, false)) {
            ++index
            val scene = scene(scope)

            return DefProcedure((name.value as Identifier).value, scene, scope)
        }

        val parameterList = mutableListOf<String>()

        do {
            val parameter = consume(TokenType.IDENTIFIER) ?: return null

            parameterList.add(
                (parameter.value as Identifier).value
            )

            if (!curIs(TokenType.COMMA, false)) {
                break
            } else {
                ++index
            }
        } while (true)

        consume(TokenType.PAREN_R)

        val scene = scene(scope)

        return DefFunc(
            (name.value as Identifier).value,
            parameterList,
            scene,
            scope
        )
    }

    fun varDef(scope: Scope): Node? {
        val name = consume(TokenType.IDENTIFIER) ?: return null

        val definition = DefVar(((name.value as yaj.backend.type.Identifier).value), scope)

        return assign(definition, scope)
    }

    fun varGet(scope: Scope): Node? {
        val variable = getVar(scope)

        return assign(variable, scope)
    }

    fun getVar(scope: Scope): GetVar {
        return GetVar(
            (tokens[index++].value as yaj.backend.type.Identifier).value,
            scope
        )
    }


    fun assign(variable: Var, scope: Scope): Node? {
        if (curIs(TokenType.ASSIGN_V, false)) {
            consume(TokenType.ASSIGN_V) ?: return null

        } else {
            return Assign(variable, Number(0.0))
        }

        when (tokens[index].type) {
            TokenType.IDENTIFIER -> {
                if (!atEnd()) {
                    val findOp = findOperation(scope, 0) ?: return null
                    return Assign(variable, findOp)
                } else {
                    return Assign(variable, Number(0.0))
                }
            }

            else -> {
                val findOp = findOperation(scope, 0) ?: return null

                return Assign(variable, findOp)
            }
        }

    }

    fun findOperation(scope: Scope, indexOffset: Int = 0): Node? {
        var offset = indexOffset

        // Skip tokens that give no hint to type of operation
        while (!atEnd(offset)) {
            if (tokens[index + offset].type == TokenType.PAREN_L) {
                ++offset
            } else {
                break
            }
        }

        // Get hinted type of operation based on token
        when (tokens[index + offset].type) {
            TokenType.DOUBLE -> {
                var findOp = findOperation(scope, offset + 1)

                if (findOp == null) {
                    return expr(scope)
                }
                else {
                    return findOp
                }
            }

            TokenType.IDENTIFIER -> {
                var findOp = findOperation(scope, offset + 1)

                if (findOp == null) {
                    return getVar(scope)
                }
                else {
                    return findOp
                }
            }

            TokenType.ADD,
            TokenType.SUB,
            TokenType.MULT,
            TokenType.DIV,
            TokenType.POW,
            TokenType.MOD -> {
                val evaluated = expr(scope)

                return evaluated
            }

            TokenType.STRING -> {
                return stringConcat(scope)
            }

            TokenType.BOOL,
            TokenType.NOT,
            TokenType.OR,
            TokenType.AND,
            TokenType.EQUALS,
            TokenType.NOT_EQUALS,
            TokenType.GREATER_EQUALS,
            TokenType.GREATER,
            TokenType.LESS,
            TokenType.LESS_EQUALS -> {
                return boolExpr(scope)
            }

            else -> {
                return null
            }
        }
    }

    fun out(scope: Scope): Print? {
        consume(TokenType.PAREN_L) ?: return null

        if (!atEnd()) {
            var str : Node

            str = stringConcat(scope)

            consume(TokenType.PAREN_R) ?: return null

            return Print(str)
        }

        return null
    }

    /**
     * String
     */

    fun string(scope: Scope): Node? {
        when (tokens[index].type) {
            TokenType.STRING -> {
                return String(
                    (tokens[index++].value as yaj.backend.type.String).value
                )
            }

            TokenType.IDENTIFIER -> {
                val funcCall = funcCall(scope)

                if (funcCall != null) {
                    return funcCall
                }

                return getVar(scope)
            }

            TokenType.DOUBLE -> {
                return String(
                    formatNumber((tokens[index++].value as Double).value)
                )
            }
            else -> {
                error()
                return null
            }
        }
    }

    fun stringConcat(scope: Scope): Node {
        var root = string(scope)

        if (root == null) {
            error()
            return String("")
        }

        if (curIs(TokenType.ADD, false)) {
            ++index

            if (!atEnd()) {
                when (tokens[index].type) {
                    TokenType.STRING,
                    TokenType.IDENTIFIER,
                    TokenType.DOUBLE -> {
                        return StringConcat(root, stringConcat(scope))
                    }
                }
            } else {
                error(-1)
            }
        }

        return root
    }


    /**
     * Boolean
     */

    fun bool(scope: Scope): Node {
        if (atEnd()) {
            error(-1)
            return Bool(false)
        }

        when (tokens[index].type) {
            TokenType.NOT -> {
                ++index
                return Not(bool(scope))
            }

            TokenType.BOOL -> {
                return Bool((tokens[index++].value as yaj.backend.type.Bool).value)
            }

            TokenType.PAREN_L -> {
                ++index
                var node = boolExpr(scope)
                consume(TokenType.PAREN_R) ?: return Bool(false)

                return node
            }

            TokenType.IDENTIFIER -> {
                val funcCall = funcCall(scope)

                if (funcCall != null) {
                    return funcCall
                }

                return getVar(scope)
            }

            TokenType.DOUBLE -> {
                return expr(scope)
            }

            TokenType.STRING -> {
                return stringConcat(scope)
            }

            else -> {
                error()
                ++index
                return Bool(false)
            }
        }
    }

    fun boolExpr(scope: Scope): Node {
        val root = bool(scope)

        if (!atEnd())
            when (tokens[index].type) {
                TokenType.OR -> {
                    ++index
                    return Or(root, boolExpr(scope))
                }

                TokenType.AND -> {
                    ++index
                    return And(root, boolExpr(scope))
                }

                TokenType.PAREN_R,
                TokenType.NEW_LINE,
                TokenType.SEMICOLON,
                TokenType.COMMA -> {
                    return root
                }

                TokenType.EQUALS -> {
                    ++index
                    return Equals(root, boolExpr(scope))
                }

                TokenType.NOT_EQUALS -> {
                    ++index
                    return NotEquals(root, boolExpr(scope))
                }

                TokenType.GREATER_EQUALS -> {
                    ++index
                    return GreaterEquals(root, boolExpr(scope))
                }

                TokenType.LESS_EQUALS -> {
                    ++index
                    return LessEquals(root, boolExpr(scope))
                }

                TokenType.LESS -> {
                    ++index
                    return Less(root, boolExpr(scope))
                }

                TokenType.GREATER -> {
                    ++index
                    return Greater(root, boolExpr(scope))
                }

                else -> {
                    error()
                }
            }

        return root
    }

    /**
     * Number
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
                TokenType.SEMICOLON,

                TokenType.NOT,
                TokenType.OR,
                TokenType.AND,
                TokenType.EQUALS,
                TokenType.NOT_EQUALS,
                TokenType.GREATER_EQUALS,
                TokenType.GREATER,
                TokenType.LESS,
                TokenType.LESS_EQUALS,
                TokenType.COMMA, -> {
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
                val funcCall = funcCall(scope)

                if (funcCall != null) {
                    return funcCall
                }

                return getVar(scope)
            }

            TokenType.PAREN_L -> {
                ++index;
                var node = expr(scope)
                consume(TokenType.PAREN_R)
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