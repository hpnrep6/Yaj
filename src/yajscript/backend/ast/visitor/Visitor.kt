package yajscript.backend.ast.visitor

import yajscript.backend.ast.Binary
import yajscript.backend.ast.Node
import yajscript.backend.ast.Unary

abstract class Visitor {

    abstract fun visitBinary(node : Binary)
    abstract fun visitUnary(node : Unary)

    abstract fun visitVarDecl(node : Node)
    abstract fun visitFuncDecl(node : Node)
    abstract fun visitVarAssign(node : Node)
    abstract fun visitFuncCall(node : Node)

    abstract fun visitIf(node : Node)
    abstract fun visitElif(node : Node)
    abstract fun visitElse(node : Node)

    abstract fun visitFor(node : Node)
    abstract fun visitWhile(node : Node)

}