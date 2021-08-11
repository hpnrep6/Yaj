package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor

class StringConcat(left: Node, right: Node): Node() {
    val left = left
    val right = right

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitStringConcat(this)
    }

    override fun toPrint(): kotlin.String {
        return "${left.toPrint()}${right.toPrint()}"
    }

    override fun toString(): kotlin.String {
        return "Concat($left, $right)"
    }

}