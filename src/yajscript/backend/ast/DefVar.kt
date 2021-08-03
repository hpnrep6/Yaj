package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class DefVar (target : Identifier, scope: Scope) : Var() {
    val left = target

    override val scope = scope

    override fun visit(visitor : Visitor): kotlin.String {
        return ""
    }

    override fun toString(): String {
        return "VarDef{$left}"
    }
}