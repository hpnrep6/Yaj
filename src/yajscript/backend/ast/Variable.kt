package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import yajscript.backend.type.Identifier

class Variable (name : Identifier) : Node() {
    val name = name

    override fun visit(visitor : Visitor) {

    }
}