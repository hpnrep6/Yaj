package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class GetProcedure(name: String): Node() {
    val name = name

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitProcCall(this)
    }

    override fun toString(): String {
        return "ProcCall{$name}"
    }
}