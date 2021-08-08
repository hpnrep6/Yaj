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
            val value = line.visit(this)

            if (value != Unit && value != null) {
                return value as Node?
            }
        }

        // Clear all variables initialised in scope
        node.scope.clear()

        return null
    }

    override fun visitReturn(node: Return): Node {

        return node.value.visit(this) as Node
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
        return Bool((node.left.visit(this) as Node).equals(node.right.visit(this) as Node))
    }

    override fun visitNumComparison(node: NumComparison): Bool {
        val value = node.operator(node.left.visit(this) as Number, node.right.visit(this) as Number)

        return Bool(value)
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
        var value = node.right.visit(this)

        if (node.left::class == DefVar::class) {
            val name = node.left.visit(this)
            val nameString = name as String

            scope.addVar(nameString, value as Node)

        } else {
            val nameString = (node.left as GetVar).name

            var varScope = scope.getVarScope(nameString)
            if (varScope == null) {
                return
            } else {
                varScope.addVar(nameString, value as Node)
            }
        }
    }

    override fun visitPointerAssign(node: PointerAssign) {
        var scope = node.left.scope
        var value = node.right

        if (node.left::class == DefVar::class) {
            val name = node.left.visit(this)
            val nameString = name as String

            scope.addVar(nameString, value)

        } else {
            val nameString = (node.left as GetVar).name

            var varScope = scope.getVarScope(nameString)
            if (varScope == null) {
                return
            } else {
                varScope.addVar(nameString, value)
            }
        }
    }


    /**
     * Output
     */
    override fun visitPrint(node: Print) {
        interpreter.out((node.node.visit(this) as Node).toPrint())
    }


    /**
     * Function
     */
    override fun visitProcDef(node: DefProcedure) {
        val scope = node.scope

        scope.addFunc(node.name, node.scene)
    }

    override fun visitProcCall(node: GetProcedure): Node? {
        val scene = node.scope.getFunc(node.name) ?: return null

        return scene.visit(this) as Node?
    }

    override fun visitFuncDef(node : DefFunc) {
        val scope = node.scope

        scope.addFunc(node.name, node)
    }

    override fun visitFuncCall(node : GetFunc): Node? {
        val scope = node.scope
        val name = node.name

        val funcGet = scope.getFunc(name) ?: return null
        val func = funcGet as DefFunc

        val args = node.parameters
        val params = func.variables

        if (params.size != args.size) {
            return null
        }

        val funcScope = func.scene.scope

        for (i in 0 until args.size) {
            funcScope.addVar(
                params[i], args[i].visit(this) as Node
            )
        }

        val returnValue = func.scene.visit(this)

        return returnValue
    }


    /**
     * Conditional
     */
    override fun visitIf(node : If): Node? {
        val bool = node.operation.visit(this) as Bool

        if (bool.value) {
            return node.scene.visit(this) as Node?
        } else if (node.otherwise != null) {
            return node.otherwise.visit(this) as Node?
        }

        return null
    }


    /**
     * Loop
     */

    override fun visitFor(node : Node): Node? {
        TODO()
    }

    override fun visitWhile(node : While): Node? {
        val boolExpr = node.condition
        val scene = node.scene

        var returnValue: Node? = null

        while ((boolExpr.visit(this) as Bool).value) {
            returnValue = scene.visit(this)
            if (returnValue != null) {
                break
            }
        }

        return returnValue
    }
}