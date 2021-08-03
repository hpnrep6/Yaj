package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class Bool(value: Boolean): Value() {
    override val value = value

    override fun visit(visitor: Visitor): Boolean {
        TODO("Not yet implemented")
    }

    override fun toPrint(): kotlin.String {
        return value.toString()
    }

    override fun toString(): String {
        return value.toString()
    }
}