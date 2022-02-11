package yaj.backend.ast.visitor

import yaj.YajInterpreter
import yaj.backend.ast.*
import yaj.backend.ast.Number

class Printer(interpreter: YajInterpreter, scope: Scope): Visitor() {
    val interpreter = interpreter
    val scope = scope

    private fun convertToString(any: Any?): String {
        if (any == null) {
            return "null"
        } else if (any::class == String::class) {
            return any as String
        } else {
            return (any as Node).toPrint()
        }
    }

    override fun visitScene(node: Scene): String {
        return node.toPrint()
    }

    override fun visitReturn(node: Return): String {
        return node.toPrint()
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

    override fun visitNumber(node: Number): String {
        return node.toPrint()
    }


    /**
     * String
     */

    override fun visitStringConcat(node: StringConcat): yaj.backend.ast.String {

        return yaj.backend.ast.String(
            convertToString(node.left.visit(this)) + convertToString(node.right.visit(this))
        )
    }

    override fun visitString(node: yaj.backend.ast.String): String {
        return node.toPrint()
    }


    /**
     * Boolean
     */

    override fun visitBool(node: Bool): String {
        return node.toPrint()
    }

    override fun visitBoolBinary(node: BoolBinary): Bool {
        return node.operator(node.left.visit(this) as Bool, node.right.visit(this) as Bool)
    }

    override fun visitBoolUnary(node: BoolUnary): Bool {
        return node.operator(node.left.visit(this) as Bool)
    }

    override fun visitBoolComparison(node: BoolComparison): Bool {
        return Bool((node.left.visit(this) as Node).equals(node.right.visit(this) as Node))
    }

    override fun visitNumComparison(node: NumComparison): Bool {
        val value = node.operator(node.left.visit(this) as Number, node.right.visit(this) as Number)

        return Bool(value)
    }

    override fun visitVarDef(node: DefVar): String {
        return node.name
    }

    override fun visitVarGet(node: GetVar): String {
        return scope.getVar(node.name).toPrint()
    }

    override fun visitAssign(node: Assign): String {
        return node.toString()
    }

    override fun visitPointerAssign(node: PointerAssign): String {
        return node.toString()
    }

    override fun visitPrint(node: Print): String {
        return node.toString()
    }

    override fun visitProcCall(node: GetProcedure): Node? {
        val scene = scope.getFunc(node.name) ?: return null

        return scene.visit(this) as Node?
    }

    override fun visitProcDef(node: DefProcedure): String {
        return node.toString()
    }

    override fun visitFuncCall(node : GetFunc): Node? {
        val visitor = Execute(interpreter, scope)

        return node.visit(visitor) as Node?
    }

    override fun visitFuncDef(node: DefFunc): String {
        return node.toString()
    }

    override fun visitIf(node: If): String {
        return node.toString()
    }

    override fun visitFor(node: Node): String {
        return node.toString()
    }

    override fun visitWhile(node: While): String {
        return node.toString()
    }

    override fun visitCastBool(node: CastBool): Any? {
        return node.expression.visit(this)
    }

    override fun visitCastNum(node: CastNum): Any? {
        return node.expression.visit(this)
    }

    override fun visitCastString(node: CastString): Any? {
        return node.expression.visit(this)
    }
}