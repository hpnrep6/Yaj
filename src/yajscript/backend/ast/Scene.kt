package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class Scope() {
    val funcs = hashMapOf<String, Scene>()

    val vars = hashMapOf<String, Node>()

    fun addVar(name: String, value: Node) {
        vars[name] = value
    }

    fun addFunc(name: String, value: Scene) {
        vars[name] = value
    }

    fun getVar(name: String): Node {
        return vars[name]!!
    }

    fun getFunc(name: String): Node? {
        return funcs[name]!!
    }
}

class Scene(nodes: MutableList<Node>, scope: Scope): Node() {
    val nodes = nodes

    val scope = scope

    override fun visit(visitor: Visitor): Node? {
        return visitor.visitScene(this)
    }

    override fun toString(): String {
        var stringBuilder = StringBuilder()

        stringBuilder.append("Scene(\n")

        for (node in nodes) {
            stringBuilder.append(node.toString())

            stringBuilder.append("\n")
        }

        stringBuilder.append(")")

        if (scope.funcs.isNotEmpty() || scope.vars.isNotEmpty()){
            stringBuilder.append("\nVariables(\n")

            stringBuilder.append(scope.vars.toString())

            stringBuilder.append("\n)\n")

            stringBuilder.append("Functions(\n")

            stringBuilder.append(scope.funcs.toString())

            stringBuilder.append("\n)")
        }

        return stringBuilder.toString()
    }
}