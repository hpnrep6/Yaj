package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor

class Print(node: Node): Node() {
    val node = node

    override fun visit(visitor: Visitor): Any? {
        TODO("Not yet implemented")
    }

    override fun toString(): kotlin.String {
        return "Output($node)"
    }
}