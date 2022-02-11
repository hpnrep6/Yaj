package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

abstract class Cast(expression: Node): Node() {
    val expression = expression

    override fun toPrint(): String {
        return expression.toPrint()
    }

    override fun toString(): String {
        return "${this::class.java.simpleName}(${expression})"
    }
}

class CastNum(expression: Node): Cast(expression) {
    override fun visit(visitor: Visitor): Any? {
        return visitor.visitCastNum(this)
    }

}

class CastBool(expression: Node): Cast(expression) {
    override fun visit(visitor: Visitor): Any? {
        return visitor.visitCastBool(this)
    }

}

class CastString(expression: Node): Cast(expression) {
    override fun visit(visitor: Visitor): Any? {
        return visitor.visitCastString(this)
    }

}