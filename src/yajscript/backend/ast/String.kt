package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import yajscript.backend.type.String

class String (value : String): Value() {
    override val value = value

    override fun visit(visitor : Visitor) {

    }
}