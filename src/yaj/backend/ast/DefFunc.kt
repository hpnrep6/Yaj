package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class DefFunc (target : Identifier, body : Scene, scope: Scope) : Func() {
    val left = target
    val right = body

    override val scope = scope

    override fun visit(visitor : Visitor): kotlin.String {
        return ""
    }

    override fun toString(): String {
        return "FuncDef{$left}($right)"
    }
}