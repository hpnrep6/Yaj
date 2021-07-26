package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor

class Assign (target : Node, value : Node) : Node() {
    val left = target
    val right = value

    override fun visit(visitor : Visitor) {

    }
}