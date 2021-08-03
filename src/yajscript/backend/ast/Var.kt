package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor

abstract class Var : Node() {
    abstract val scope: Scope

    abstract override fun visit(visitor: Visitor) : kotlin.String
}