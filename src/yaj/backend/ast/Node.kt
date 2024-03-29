package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

abstract class Node {
    abstract fun visit(visitor : Visitor) : Any?

    open fun toPrint(): String {
        return this::class.toString()
    }

    open fun equals(other: Node): Boolean {
        return other == this
    }
}