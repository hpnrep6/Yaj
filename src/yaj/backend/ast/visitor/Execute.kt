package yaj.backend.ast.visitor

import yaj.YajInterpreter
import yaj.backend.ast.*
import yaj.backend.ast.Number

class Execute(interpreter: YajInterpreter): Visitor() {
    val interpreter = interpreter

    /**
     * Scene
     */
    override fun visitScene(node: Scene): Node? {
        var nodes = node.nodes

        for (line in nodes) {
            line.visit(this)
        }

        return null
    }

    /**
     * Number
     */
    override fun visitBinary(node : Binary): Number {
        return node.operator(node.left.visit(this) as Number, node.right.visit(this) as Number)
    }

    override fun visitUnary(node : Unary): Number {
        return node.operator(node.right.visit(this) as Number)
    }

    override fun visitNumber(node: Number): Number {
        return node
    }


    /**
     * String
     */
    override fun visitStringConcat(node: StringConcat): yaj.backend.ast.String {

        return yaj.backend.ast.String(
            node.toPrint()
        )
    }

    override fun visitString(node: yaj.backend.ast.String): yaj.backend.ast.String {
        return node
    }


    /**
     * Boolean
     */
    override fun visitBool(node: Bool): Bool {
        return node
    }

    override fun visitBoolBinary(node: BoolBinary): Bool {
        return node.operator(node.left.visit(this) as Bool, node.right.visit(this) as Bool)
    }

    override fun visitBoolUnary(node: BoolUnary): Bool {
        return node.operator(node.left.visit(this) as Bool)
    }

    override fun visitBoolComparison(node: BoolComparison): Bool {
        return Bool((node.left.visit(this) as Node).compare(node.right.visit(this) as Node))
    }

    override fun visitNumComparison(node: NumComparison): Bool {
        return Bool(node.operator(node.left.visit(this) as Number, node.right.visit(this) as Number))
    }


    /**
     * Assignment
     */

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

        // Similar to assign, but do not precalculate the value
        var value = node.right

        scope.addVar(name as String, value)
    }


    override fun visitPrint(node: Print) {
        interpreter.out((node.node.visit(this) as Node).toPrint())
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