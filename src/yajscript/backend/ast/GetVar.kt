package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class GetVar(name: String, scope: Scope): Var() {
    val name = name
    override val scope = scope

    override fun visit(visitor: Visitor): Node {
        return visitor.visitVarGet(this)
    }

    override fun toPrint(): String {
        var value = scope.getVar(name)

        return value.toPrint()
    }

    override fun toString(): String {
        return "VarLookup{$name}"
    }
}