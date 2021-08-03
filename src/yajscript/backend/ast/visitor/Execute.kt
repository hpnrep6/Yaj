package yajscript.backend.ast.visitor

import yajscript.backend.ast.*
import yajscript.backend.ast.Number
import yajscript.backend.type.Double

class Execute : Visitor() {

    override fun visitBinary(node : Binary): Number {
        return node.operator(node.left.visit(this) as Number, node.right.visit(this) as Number)
    }

    override fun visitUnary(node : Unary): Number {
        return node.operator(node.right.visit(this) as Number)
    }

    override fun visitNumber(node: Number): Number {
        return node
    }

    override fun visitNumVariable(node: Var): Number {
        TODO("Not yet implemented")
    }


    override fun visitVarDef(node : DefVar) {

    }
    override fun visitFuncDef(node : DefFunc) {

    }
    override fun visitVarAssign(node : Assign) {

    }
    override fun visitFuncCall(node : Node) {

    }


    override fun visitIf(node : Node) {

    }
    override fun visitElif(node : Node) {

    }
    override fun visitElse(node : Node) {

    }


    override fun visitFor(node : Node) {

    }
    override fun visitWhile(node : Node) {

    }
}