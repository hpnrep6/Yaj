package yajscript.backend.ast

import yajscript.backend.type.Double
import yajscript.backend.ast.Number

open class Binary (left : Number, operator : (Double, Double) -> Double, right : Number) : Node() {
    val left = left
    val right = right
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

class Add (left : Number, right : Number) : Binary (left, ::add, right) {

}

class Subtract (left : Number, right : Number) : Binary (left, ::sub, right) {

}

class Multiply (left : Number, right : Number) : Binary (left, ::mult, right) {

}

class Divide (left : Number, right : Number) : Binary (left, ::div, right) {

}