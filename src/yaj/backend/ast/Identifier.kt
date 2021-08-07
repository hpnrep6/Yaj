package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class Identifier(name: String) : Node() {
    val name = name

    override fun visit(visitor : Visitor) {

    }

    override fun toString(): String {
        return name
    }
}