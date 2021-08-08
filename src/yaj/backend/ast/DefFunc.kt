package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import kotlin.String

class DefFunc (name: String, variables: MutableList<String>, scene: Scene, scope: Scope): Node() {
    val name = name
    val scene = scene
    val variables = variables
    val scope = scope

    override fun visit(visitor : Visitor): Unit {
        return visitor.visitFuncDef(this)
    }

    override fun toString(): String {
        val params = StringBuilder()

        params.append(variables[0])

        for (i in 1 until variables.size) {
            params.append(", ")
            params.append(variables[i])
        }
        return "FuncDef{$name}($params,\n$scene)"
    }
}