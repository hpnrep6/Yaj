package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class Scope(parent: Scope?) {
    val parent = parent

    val funcs = hashMapOf<String, Node>()

    val vars = hashMapOf<String, Node>()

    var nestedLevel: Int = 0

    init {
        var parent = parent

        while (parent != null) {
            parent = parent.parent
            ++nestedLevel
        }
    }

    fun clear() {
        funcs.clear()
        vars.clear()
    }

    fun addVar(name: String, value: Node) {
        vars[name] = value
    }

    fun addFunc(name: String, value: Node) {
        funcs[name] = value
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

    fun getVarScope(name: String): Scope? {
        val attempt = vars[name]

        if (attempt != null) {
            return this
        }

        if (parent == null) {
            return null
        }

        var parent = parent

        while (true) {
            if (parent == null) {
                return null
            }

            var testParent = parent.vars[name]

            if (testParent != null) {
                return parent
            }
            parent = parent.parent
        }
    }

    fun getFunc(name: String): Node? {
        val attempt = funcs[name]

        if (parent == null) {
            return attempt!!
        }

        if (attempt == null) {
            return parent.getFunc(name)
        }

        return attempt
    }

    fun getFuncScope(name: String): Scope? {
        val attempt = funcs[name]

        if (attempt != null) {
            return this
        }

        if (parent == null) {
            return null
        }

        var parent = parent

        while (true) {
            if (parent == null) {
                return null
            }

            var testParent = parent.funcs[name]

            if (testParent != null) {
                return parent
            }
            parent = parent.parent
        }
    }

    override fun toString(): String {
        return "Scope(\n$vars,\n$funcs)"
    }
}

class Scene(nodes: MutableList<Node>): Node() {
    val nodes = nodes

    val params: MutableList<FuncParam> = mutableListOf<FuncParam>()

    override fun visit(visitor: Visitor): Any? {
        return visitor.visitScene(this)
    }

    override fun toString(): String {
        var stringBuilder = StringBuilder()

        var indent = StringBuilder()

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