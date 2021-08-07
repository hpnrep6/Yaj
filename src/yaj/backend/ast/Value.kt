package yaj.backend.ast

abstract class Value : Node() {
    abstract val value : Any
}