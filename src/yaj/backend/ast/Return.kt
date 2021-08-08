package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor

class Return(value: Node): Node() {
    val value = value

    override fun visit(visitor: Visitor): Node {
        return visitor.visitReturn(this)
    }

    override fun toString(): kotlin.String {
        return "Return($value)"
    }
}