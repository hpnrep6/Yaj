package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor

class StringConcat(left: Node, right: Node): Node() {
    val left = left
    val right = right

    override fun visit(visitor: Visitor): String {
        return visitor.visitStringConcat(this)
    }

    override fun toPrint(): kotlin.String {
        return "${left.toPrint()}${right.toPrint()}"
    }

    override fun toString(): kotlin.String {
        return "Concat($left, $right)"
    }

}