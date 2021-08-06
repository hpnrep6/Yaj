package yajscript.backend.ast.visitor

import yajscript.backend.ast.*
import yajscript.backend.ast.Number
import yajscript.backend.type.Double

class Execute : Visitor() {

    override fun visitScene(node: Scene): Node? {
        var nodes = node.nodes

        for (line in nodes) {
            line.visit(this)
        }

        return null
    }

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


    override fun visitStringConcat(node: StringConcat): yajscript.backend.ast.String {

        return yajscript.backend.ast.String(
            node.toPrint()
        )
    }

    override fun visitString(node: yajscript.backend.ast.String): yajscript.backend.ast.String {
        return node
    }


    override fun visitVarDef(node : DefVar): String {
        return node.name
    }

    override fun visitVarGet(node: GetVar): Node {
        var scope = node.scope

        return scope.getVar(node.name)
    }

    override fun visitAssign(node : Assign) {
        var scope = node.left.scope
        var name = node.left.visit(this)

        var value = node.right.visit(this)

        scope.addVar(name as String, value as Node)
    }

    override fun visitPointerAssign(node: PointerAssign) {
        var scope = node.left.scope
        var name = node.left.visit(this)

        var value = node.right

        scope.addVar(name as String, value)
    }

    override fun visitFuncDef(node : DefFunc) {

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