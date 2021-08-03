package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class Number (value : kotlin.Double) : Value() {
    override val value = value

    override fun visit(visitor : Visitor) {

    }

    override fun toString(): String {
        return value.toString()
    }
}