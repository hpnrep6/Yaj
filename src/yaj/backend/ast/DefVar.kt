package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class DefVar(name : String, scope: Scope) : Var() {
    val name = name

    override val scope = scope

    override fun visit(visitor : Visitor): String {
        return visitor.visitVarDef(this)
    }

    override fun toString(): String {
        return "VarDef{$name}"
    }
}