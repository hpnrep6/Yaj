package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor

abstract class Var : Node() {
    abstract val scope: Scope

    abstract override fun visit(visitor: Visitor) : Any
}