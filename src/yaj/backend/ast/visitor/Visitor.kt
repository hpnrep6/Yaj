package yaj.backend.ast.visitor

import yaj.backend.ast.*
import yaj.backend.ast.Number
import yaj.backend.ast.String

abstract class Visitor {
    abstract fun visitScene(node: Scene): Any?
    abstract fun visitReturn(node: Return): Any?

    abstract fun visitBinary(node : Binary): Any?
    abstract fun visitUnary(node : Unary): Any?
    abstract fun visitNumber(node: Number): Any?

    abstract fun visitStringConcat(node: StringConcat): Any?
    abstract fun visitString(node: String): Any?

    abstract fun visitBool(node: Bool): Any?
    abstract fun visitBoolBinary(node: BoolBinary): Any?
    abstract fun visitBoolUnary(node: BoolUnary): Any?
    abstract fun visitBoolComparison(node: BoolComparison): Any?
    abstract fun visitNumComparison(node: NumComparison): Any?

    abstract fun visitVarDef(node : DefVar): Any?
    abstract fun visitVarGet(node: GetVar): Any?
    abstract fun visitAssign(node : Assign): Any?
    abstract fun visitPointerAssign(node: PointerAssign): Any?

    abstract fun visitPrint(node: Print): Any?

    abstract fun visitProcCall(node: GetProcedure): Any?
    abstract fun visitProcDef(node: DefProcedure): Any?
    abstract fun visitFuncCall(node : GetFunc): Any?
    abstract fun visitFuncDef(node : DefFunc): Any?

    abstract fun visitIf(node : If): Any?

    abstract fun visitFor(node : Node): Any?
    abstract fun visitWhile(node : While): Any?

    abstract fun visitCastNum(node: CastNum): Any?
    abstract fun visitCastString(node: CastString): Any?
    abstract fun visitCastBool(node: CastBool): Any?
}