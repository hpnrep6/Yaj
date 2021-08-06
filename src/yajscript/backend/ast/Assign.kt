package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

open class Assign (target : Var, value : Node) : Node() {
    val left = target
    val right = value

    override fun visit(visitor : Visitor) {
        return visitor.visitAssign(this)
    }

    override fun toString(): String {
        return "Assign($left, $right)"
    }
}

class PointerAssign(target: Var, value: Node): Assign(target, value) {
    override fun visit(visitor: Visitor) {
        return visitor.visitPointerAssign(this)
    }
}