package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor

abstract class Func: Node() {
    abstract val scope: Scope

    abstract override fun visit(visitor: Visitor): kotlin.String
}