package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class GetFunc(name: Identifier, scope: Scope): Var() {
    val name = name
    override val scope = scope

    override fun visit(visitor: Visitor): String {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "FuncLookup{$name}"
    }
}