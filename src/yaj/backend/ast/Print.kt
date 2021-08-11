package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor

class Print(node: Node): Node() {
    val node = node

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitPrint(this)
    }

    override fun toString(): kotlin.String {
        return "Output($node)"
    }
}