package yajscript.backend.ast

class Assign (target : Node, value : Node) : Node() {
    val left = target
    val right = value
}