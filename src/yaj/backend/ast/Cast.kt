package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

abstract class Cast(expression: Node): Node() {

    override fun toString(): String {
        return ""
    }
}

class CastInt(expression: Node): Cast(expression) {
    override fun visit(visitor: Visitor): Any? {
        TODO("Not yet implemented")
    }

}

class CastBool(expression: Node): Cast(expression) {
    override fun visit(visitor: Visitor): Any? {
        TODO("Not yet implemented")
    }

}

class CastString(expression: Node): Cast(expression) {
    override fun visit(visitor: Visitor): Any? {
        TODO("Not yet implemented")
    }

}