package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class GetProcedure(name: String, scope: Scope): Node() {
    val name = name
    val scope = scope

    override fun visit(visitor: Visitor): Node? {
        return visitor.visitProcCall(this)
    }

    override fun toString(): String {
        return "ProcCall{$name}"
    }
}