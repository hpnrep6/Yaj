package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class If(booleanOperation: Node, scene: Scene): Node() {
    val operation = booleanOperation
    val scene = scene

    override fun visit(visitor: Visitor) {

    }

    override fun toString(): String {
        return "Conditional($operation,\n$scene)"
    }
}