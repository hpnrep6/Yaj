package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class GetFunc(name: String, parameters: MutableList<Node>): Node() {
    val name = name
    val parameters = parameters

    override fun visit(visitor: Visitor): Any? {
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