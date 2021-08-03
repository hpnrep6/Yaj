package yajscript.backend.ast

import yajscript.backend.Token
import yajscript.backend.ast.visitor.Visitor
import yajscript.backend.type.Identifier
import kotlin.String

class Identifier(name: String) : Node() {
    val name = name

    override fun visit(visitor : Visitor) {

    }

    override fun toString(): String {
        return name
    }
}