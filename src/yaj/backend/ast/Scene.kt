package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class Scope(parent: Scope?) {
    val parent = parent

    val funcs = hashMapOf<String, Scene>()

    val vars = hashMapOf<String, Node>()

    var nestedLevel: Int = 0

    init {
        var parent = parent

        while (parent != null) {
            parent = parent.parent
            ++nestedLevel
        }
    }

    fun addVar(name: String, value: Node) {
        vars[name] = value
    }

    fun addFunc(name: String, value: Scene) {
        vars[name] = value
    }

    fun getVar(name: String): Node {
        val attempt = vars[name]

        if (parent == null) {
            return attempt!!
        }

        if (attempt == null) {
            return parent.getVar(name)
        }

        return attempt
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

        var indent = StringBuilder()

        for (i in 1..scope.nestedLevel) {
            indent.append("    ")
        }

        var indentStr = indent.toString()

        stringBuilder.append(indentStr)

        stringBuilder.append("Scene(\n")

        for (node in nodes) {
            stringBuilder.append(indentStr)
            stringBuilder.append(node.toString())

            stringBuilder.append("\n")
        }
        stringBuilder.append(indentStr)
        stringBuilder.append(")")


        if (scope.funcs.isNotEmpty() || scope.vars.isNotEmpty()){
            stringBuilder.append("\n${indentStr}Variables(")

            stringBuilder.append(indentStr)
            stringBuilder.append(hashMapToString(scope.vars))

            stringBuilder.append(indentStr)
            stringBuilder.append("\n)\n")

            stringBuilder.append(indentStr)
            stringBuilder.append("Functions(")

            stringBuilder.append(indentStr)
            stringBuilder.append(hashMapToString(scope.funcs))

            stringBuilder.append("\n$indentStr)")
        }

        return stringBuilder.toString()
    }
}

private fun <K, T> hashMapToString(hashmap: HashMap<K, T>): String {
    var string = StringBuilder()

    for ((key, value) in hashmap) {
        string.append("\n$key = $value")
    }

    return string.toString()
}