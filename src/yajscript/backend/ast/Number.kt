package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class Number (value : Double) : Value() {
    override val value = value

    override fun visit(visitor : Visitor): Number {
        return visitor.visitNumber(this)
    }

    override fun toPrint(): String {
        return value.toString()
    }

    override fun toString(): String {
        return value.toString()
    }
}