package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class DefProcedure(name: String, scene: Scene, scope: Scope): Node() {
    val name = name
    val scene = scene
    val scope = scope

    override fun visit(visitor: Visitor): Unit {
        return visitor.visitProcDef(this)
    }

    override fun toString(): String {
        return "Procedure{$name}(\n$scene)"
    }
}