package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class GetFunc(name: String, scope: Scope, parameters: MutableList<Node>): Node() {
    val name = name
    val scope = scope
    val parameters = parameters

    override fun visit(visitor: Visitor): Node? {
        return visitor.visitFuncCall(this)
    }

    override fun toString(): String {
        val params = StringBuilder()

        params.append(parameters[0])

        for (i in 1 until parameters.size) {
            params.append(", ")
            params.append(parameters[i])
        }
        return "FuncCall{$name}($params)"
    }
}