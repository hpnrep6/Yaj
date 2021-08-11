package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor

abstract class Binary (left : Node, operator : (Number, Number) -> Number, right : Node) : Node() {
    val left = left
    val right = right
    val operator = operator

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitBinary(this)
    }

    override fun toString(): kotlin.String {
        return "${this::class.java.simpleName}($left, $right)"
    }
}

abstract class Unary (operator : (Number) -> Number, right : Node) : Node() {
    val right = right
    val operator = operator

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitUnary(this)
    }

    override fun toString(): kotlin.String {
        return "${this::class.java.simpleName}($right)"
    }
}

fun add (a : Number, b : Number) : Number {
    return Number(a.value + b.value)
}

fun sub (a : Number, b : Number) : Number {
    return Number(a.value - b.value)
}

fun mult (a : Number, b : Number) : Number {
    return Number(a.value * b.value)
}

fun div (a : Number, b : Number) : Number {
    return Number(a.value / b.value)
}

fun mod(a: Number, b: Number) : Number {
    return Number(a.value % b.value)
}

fun neg (a : Number) : Number {
    return Number(-a.value)
}

fun pos (a : Number) : Number {
    return Number(a.value)
}

class Add (left : Node, right : Node) : Binary (left, ::add, right) {

}

class Subtract (left : Node, right : Node) : Binary (left, ::sub, right) {

}

class Multiply (left : Node, right : Node) : Binary (left, ::mult, right) {

}

class Divide (left : Node, right : Node) : Binary (left, ::div, right) {

}

class Modulo (left: Node, right: Node) : Binary(left, ::mod, right) {

}

class Negative (right : Node) : Unary(::neg, right) {

}

class Positive (right: Node) : Unary(::pos, right) {

}
