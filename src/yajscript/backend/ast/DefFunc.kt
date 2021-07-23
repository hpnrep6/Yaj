package yajscript.backend.ast

class DefFunc (target : Function, body : Scene) : Node() {
    val left = target
    val right = body
}