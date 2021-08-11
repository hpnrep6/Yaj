package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class GetVar(name: String): Var() {
    val name = name

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitVarGet(this)
    }

    override fun toPrint(): String {
        return toString()
    }

    override fun toString(): String {
        return "VarLookup{$name}"
    }
}