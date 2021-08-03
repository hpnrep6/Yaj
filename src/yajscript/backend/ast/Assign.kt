package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class Assign (target : Node, value : Node) : Node() {
    val left = target
    val right = value

    override fun visit(visitor : Visitor) {

    }

    override fun toString(): String {
        return "Assign($left, $right)"
    }
}