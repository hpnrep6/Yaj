package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class DefProcedure(name: String, scene: Scene): Node() {
    val name = name
    val scene = scene

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitProcDef(this)
    }

    override fun toString(): String {
        return "Procedure{$name}(\n$scene)"
    }
}