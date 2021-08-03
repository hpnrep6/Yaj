package yajscript.backend.ast.visitor

import yajscript.backend.ast.*

abstract class Visitor {

    abstract fun visitBinary(node : Binary)
    abstract fun visitUnary(node : Unary)

    abstract fun visitVarDecl(node : DefVar)
    abstract fun visitFuncDecl(node : DefFunc)
    abstract fun visitVarAssign(node : Assign)
    abstract fun visitFuncCall(node : Node)

    abstract fun visitIf(node : Node)
    abstract fun visitElif(node : Node)
    abstract fun visitElse(node : Node)

    abstract fun visitFor(node : Node)
    abstract fun visitWhile(node : Node)

}