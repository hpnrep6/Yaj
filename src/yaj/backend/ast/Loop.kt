package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor

class While(condition: Node, scene: Scene): Node() {
    val condition = condition
    val scene = scene

    override fun visit(visitor: Visitor): Unit {
        return visitor.visitWhile(this)
    }

    override fun toString(): kotlin.String {
        return "While($condition,\n$scene)"
    }

}