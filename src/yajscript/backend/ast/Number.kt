package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import yajscript.backend.type.Double

class Number (value : Double) : Value() {
    override val value = value

    override fun visit(visitor : Visitor) {

    }
}