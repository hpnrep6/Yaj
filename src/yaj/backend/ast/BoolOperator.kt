package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

open class BoolBinary(left: Node, right: Node, operator: (Bool, Bool) -> Bool): Node() {
    val left = left
    val right = right
    val operator = operator

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitBoolBinary(this)
    }

    override fun toString(): kotlin.String {
        return "${this::class.java.simpleName}($left, $right)"
    }
}

open class BoolUnary(left: Node, operator: (Bool) -> Bool): Node() {
    val left = left
    val operator = operator

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitBoolUnary(this)
    }

    override fun toString(): kotlin.String{
        return "${this::class.java.simpleName}($left)"
    }
}

private fun or(a: Bool, b: Bool): Bool {
    return Bool(a.value || b.value)
}

private fun and(a: Bool, b: Bool): Bool {
    return Bool(a.value && b.value)
}

private fun not(a: Bool): Bool {
    return Bool(!a.value)
}

class Or(left: Node, right: Node): BoolBinary(left, right, ::or)

class And(left: Node, right: Node): BoolBinary(left, right, ::and)

class Not(left: Node): BoolUnary(left, ::not)

open class BoolComparison(left: Node, right: Node, equals: Boolean): Node() {
    val left = left
    val right = right
    val equals = equals

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitBoolComparison(this)
    }

    override fun toString(): String {
        return "${this::class.java.simpleName}($left, $right)"
    }
}

class Equals(left: Node, right: Node): BoolComparison(left, right, true)

class NotEquals(left: Node, right: Node): BoolComparison(left, right, false)

open class NumComparison(left: Node, right: Node, operator: (Number, Number) -> Boolean): Node() {
    val left = left
    val right = right
    val operator = operator

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitNumComparison(this)
    }

    override fun toString(): String {
        return "${this::class.java.simpleName}($left, $right)"
    }
}

class GreaterEquals(left: Node, right: Node): NumComparison(left, right, ::greaterEquals)

class Greater(left: Node, right: Node): NumComparison(left, right, ::greater)

class LessEquals(left: Node, right: Node): NumComparison(left, right, ::lessEquals)

class Less(left: Node, right: Node): NumComparison(left, right, ::less)

private fun greater(a: Number, b: Number): Boolean {
    return a.value > b.value
}

private fun greaterEquals(a: Number, b: Number): Boolean {
    return a.value >= b.value
}

private fun less(a: Number, b: Number): Boolean {
    return a.value < b.value
}

private fun lessEquals(a: Number, b: Number): Boolean {
    return a.value <= b.value
}