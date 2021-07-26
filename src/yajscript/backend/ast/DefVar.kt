package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor

class DefVar (target : Variable, value : Value) : Node() {
    val left = target
    val right = value

    override fun visit(visitor : Visitor) {

    }
}