package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class String (value : String): Value() {
    override val value = value

    override fun visit(visitor : Visitor) {

    }

    override fun toString(): kotlin.String {
        return "\"${value}\""
    }
}