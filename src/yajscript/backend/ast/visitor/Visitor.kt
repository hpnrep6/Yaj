package yajscript.backend.ast.visitor

import yajscript.backend.ast.*
import yajscript.backend.ast.Number

abstract class Visitor {

    abstract fun visitBinary(node : Binary): Number
    abstract fun visitUnary(node : Unary): Number
    abstract fun visitNumber(node: Number): Number
    abstract fun visitNumVariable(node: Var): Number

    abstract fun visitVarDef(node : DefVar): Any?
    abstract fun visitFuncDef(node : DefFunc): Any?
    abstract fun visitVarAssign(node : Assign): Any?
    abstract fun visitFuncCall(node : Node): Any?

    abstract fun visitIf(node : Node): Any?
    abstract fun visitElif(node : Node): Any?
    abstract fun visitElse(node : Node): Any?

    abstract fun visitFor(node : Node): Any?
    abstract fun visitWhile(node : Node): Any?

}