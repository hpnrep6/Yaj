package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class DefVar(name : String) : Var() {
    val name = name

    override fun visit(visitor : Visitor): Any? {
        return visitor.visitVarDef(this)
    }

    override fun toString(): String {
        return "VarDef{$name}"
    }
}