package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor

class DefFunc (target : Function, body : Scene) : Node() {
    val left = target
    val right = body

    override fun visit(visitor : Visitor) {

    }
}