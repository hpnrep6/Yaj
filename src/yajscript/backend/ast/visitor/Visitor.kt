package yajscript.backend.ast.visitor

import yajscript.backend.ast.*
import yajscript.backend.ast.Number
import yajscript.backend.ast.String

abstract class Visitor {
    abstract fun visitScene(node: Scene): Node?

    abstract fun visitBinary(node : Binary): Number
    abstract fun visitUnary(node : Unary): Number
    abstract fun visitNumber(node: Number): Number
    abstract fun visitNumVariable(node: Var): Number

    abstract fun visitStringConcat(node: StringConcat): String
    abstract fun visitString(node: String): String

    abstract fun visitVarDef(node : DefVar): kotlin.String
    abstract fun visitVarGet(node: GetVar): Node
    abstract fun visitFuncDef(node : DefFunc): Unit
    abstract fun visitAssign(node : Assign): Unit
    abstract fun visitPointerAssign(node: PointerAssign): Unit
    abstract fun visitFuncCall(node : Node): Any?

    abstract fun visitIf(node : Node): Any?
    abstract fun visitElif(node : Node): Any?
    abstract fun visitElse(node : Node): Any?

    abstract fun visitFor(node : Node): Any?
    abstract fun visitWhile(node : Node): Any?
}