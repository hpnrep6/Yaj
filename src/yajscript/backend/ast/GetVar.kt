package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class GetVar(name: String, scope: Scope): Var() {
    val name = name
    override val scope = scope

    override fun visit(visitor: Visitor): String {
        TODO("Not yet implemented")
    }

    override fun toPrint(): String {
        var value = scope.getVar(name) ?: return ""

        return value.toPrint()
    }

    override fun toString(): String {
        return "VarLookup{$name}"
    }
}