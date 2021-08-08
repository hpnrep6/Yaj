package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class If(booleanOperation: Node, scene: Scene, otherwise: Scene? = null): Node() {
    val operation = booleanOperation
    val scene = scene
    val otherwise = otherwise // else, but that's a reserved keyword in kotlin

    override fun visit(visitor: Visitor): Node? {
        return visitor.visitIf(this)
    }

    override fun toString(): String {
        if (otherwise == null)
            return "If($operation,\n$scene)"
        else
            return "If($operation,\n$scene,\nElse(\n$otherwise)"
    }
}