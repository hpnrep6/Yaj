package yaj.backend.ast.visitor

import yaj.backend.ast.*
import yaj.backend.ast.Number
import yaj.backend.ast.String

abstract class Visitor {
    abstract fun visitScene(node: Scene): Node?

    abstract fun visitBinary(node : Binary): Number
    abstract fun visitUnary(node : Unary): Number
    abstract fun visitNumber(node: Number): Number

    abstract fun visitStringConcat(node: StringConcat): String
    abstract fun visitString(node: String): String

    abstract fun visitBool(node: Bool): Bool
    abstract fun visitBoolBinary(node: BoolBinary): Bool
    abstract fun visitBoolUnary(node: BoolUnary): Bool
    abstract fun visitBoolComparison(node: BoolComparison): Bool
    abstract fun visitNumComparison(node: NumComparison): Bool

    abstract fun visitVarDef(node : DefVar): kotlin.String
    abstract fun visitVarGet(node: GetVar): Node
    abstract fun visitAssign(node : Assign): Unit
    abstract fun visitPointerAssign(node: PointerAssign): Unit

    abstract fun visitPrint(node: Print): Unit

    abstract fun visitFuncCall(node : Node): Any?
    abstract fun visitFuncDef(node : DefFunc): Unit

    abstract fun visitIf(node : If): Unit

    abstract fun visitFor(node : Node): Any?
    abstract fun visitWhile(node : Node): Any?
}