package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

abstract class Node {
    abstract fun visit(visitor : Visitor) : Any?

    open fun toPrint(): String {
        return this::class.toString()
    }

    open fun compare(other: Node): Boolean {
        return other == this
    }
}