package yajscript.backend.ast

import yajscript.backend.type.Double
import yajscript.backend.ast.Number
import yajscript.backend.ast.visitor.Visitor
import kotlin.String

abstract class Binary (left : Node, operator : (Double, Double) -> Double, right : Node) : Node() {
    val left = left
    val right = right
    val operator = operator


    override fun toString(): kotlin.String {
        return "${this::class.java.simpleName}($left, $right)"
    }
}

abstract class Unary (operator : (Double) -> Double, right : Node) : Node() {
    val right = right
    val operator = operator

    override fun toString(): kotlin.String {
        return "${this::class.java.simpleName}($right)"
    }
}

fun add (a : Double, b : Double) : Double {
    return Double(a.value + b.value)
}

fun sub (a : Double, b : Double) : Double {
    return Double(a.value - b.value)
}

fun mult (a : Double, b : Double) : Double {
    return Double(a.value * b.value)
}

fun div (a : Double, b : Double) : Double {
    return Double(a.value / b.value)
}

fun mod(a: Double, b: Double) : Double {
    return Double(a.value % b.value)
}

fun neg (a : Double) : Double {
    return Double(-a.value)
}

fun pos (a : Double) : Double {
    return Double(a.value)
}

class Add (left : Node, right : Node) : Binary (left, ::add, right) {
    override fun visit(visitor : Visitor) {

    }
}

class Subtract (left : Node, right : Node) : Binary (left, ::sub, right) {
    override fun visit(visitor : Visitor) {

    }
}

class Multiply (left : Node, right : Node) : Binary (left, ::mult, right) {
    override fun visit(visitor : Visitor) {

    }
}

class Divide (left : Node, right : Node) : Binary (left, ::div, right) {
    override fun visit(visitor : Visitor) {

    }
}

class Modulo (left: Node, right: Node) : Binary(left, ::mod, right) {
    override fun visit(visitor : Visitor) {

    }
}

class Negative (right : Node) : Unary(::neg, right) {
    override fun visit(visitor : Visitor) {

    }
}

class Positive (right: Node) : Unary(::pos, right) {
    override fun visit(visitor : Visitor) {

    }
}

