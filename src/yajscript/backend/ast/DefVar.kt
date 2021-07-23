package yajscript.backend.ast

class DefVar (target : Variable, value : Value) {
    val left = target
    val right = value
}