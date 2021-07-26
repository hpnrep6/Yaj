package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor

abstract class Node {
    abstract fun visit(visitor : Visitor)
}