package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class Scope() {
    val funcs = mapOf<kotlin.String, Scene>()

    val vars = mapOf<kotlin.String, Node>()
}

class Scene(nodes: MutableList<Node>, scope: Scope): Node() {
    val nodes = nodes

    val scope = scope

    override fun visit(visitor: Visitor): Any? {
        return null
    }

    override fun toString(): String {
        var stringBuilder = StringBuilder()

        stringBuilder.append("Scene(\n")

        for (node in nodes) {
            stringBuilder.append(node.toString())

            stringBuilder.append("\n")
        }

        stringBuilder.append(")")
//
//        stringBuilder.append("Variables(\n")
//
//        stringBuilder.append(scope.funcs.toString())
//
//        stringBuilder.append(")\n")
//
//        stringBuilder.append("Functions(\n")
//
//        stringBuilder.append(scope.vars.toString())
//
//        stringBuilder.append(")\n)")

        return stringBuilder.toString()
    }
}