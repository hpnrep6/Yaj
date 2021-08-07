package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class Bool(value: Boolean): Value() {
    override val value = value

    override fun visit(visitor: Visitor): Bool {
        return visitor.visitBool(this)
    }

    override fun toPrint(): kotlin.String {
        return value.toString()
    }

    override fun toString(): String {
        return value.toString()
    }
}