package yaj.backend.ast.visitor

import yaj.YajInterpreter
import yaj.backend.ast.*
import yaj.backend.ast.Number

open class Execute(interpreter: YajInterpreter, parent: Scope? = null): Visitor() {
    val interpreter = interpreter
    val scope = Scope(parent)

    /**
     * Scene
     */

    override fun visitScene(node: Scene): Node? {
        var nodes = node.nodes
        val visitor = Execute(interpreter, scope)

        if (node.params.isNotEmpty()) {
            for (i in 0 until node.params.size) {
                visitor.scope.addVar(
                    node.params[i].name, node.params[i].node
                )
            }

            node.params.clear()
        }

        for (line in nodes) {
            val value = line.visit(visitor)

            if (value != Unit && value != null) {
                visitor.scope.clear()
                return value as Node?
            }
        }

        visitor.scope.clear()

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
        val output = (node.visit(Printer(interpreter, scope)))!!

        if (output::class != String::class) {
            return yaj.backend.ast.String((output as Node).toPrint())
        } else {
            yaj.backend.ast.String(output as String)
        }

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
        return scope.getVar(node.name)
    }

    override fun visitAssign(node : Assign) {
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
        val output = (node.node.visit(Printer(interpreter, scope)))!!

        if (output::class != String::class) {
            interpreter.out((output as Node).toPrint())
        } else {
            interpreter.out(output as String)
        }
    }


    /**
     * Function
     */
    override fun visitProcDef(node: DefProcedure) {
        scope.addFunc(node.name, node.scene)
    }

    override fun visitProcCall(node: GetProcedure): Node? {
        val scene = scope.getFunc(node.name) ?: return null

        return scene.visit(this) as Node?
    }

    override fun visitFuncDef(node : DefFunc) {
        scope.addFunc(node.name, node)
    }

    override fun visitFuncCall(node : GetFunc): Node? {
        val name = node.name

        val funcGet = scope.getFunc(name) ?: return null
        val func = funcGet as DefFunc

        val args = node.parameters
        val params = func.variables

        if (params.size != args.size) {
            return null
        }

        for (i in 0 until args.size) {
            func.scene.params.add(
                FuncParam(params[i], args[i].visit(this) as Node)
            )
        }

        val returnValue = func.scene.visit(this)

        return returnValue as Node?
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
            returnValue = scene.visit(this) as Node?
            if (returnValue != null) {
                break
            }
        }

        return returnValue
    }

    override fun visitCastNum(node: CastNum): yaj.backend.ast.Number {
        val expression = node.expression
        val evaluated = expression.visit(this)

        var value = (evaluated as Value).value

        if (value is Boolean) {
            if (value) {
                value = 1
            } else {
                value = 0
            }
        } else if (value is kotlin.String) {
            value = 0
        }
        return yaj.backend.ast.Number(value as Double)
    }

    override fun visitCastString(node: CastString): yaj.backend.ast.String {
        val expression = node.expression
        val evaluated = expression.visit(this)

        var value = (evaluated as Value).value

        if (value is Double || value is Boolean) {
            value = value.toString()
        }
        return yaj.backend.ast.String(value as kotlin.String)
    }

    override fun visitCastBool(node: CastBool): yaj.backend.ast.Bool {
        val expression = node.expression
        val evaluated = expression.visit(this)

        var value = (evaluated as Value).value

        if (value is Double) {
            if (value != 0) {
                value = false
            } else {
                value = true
            }
        } else if (value is kotlin.String) {
            value = value.lowercase() != "false"
        }
        return yaj.backend.ast.Bool((evaluated as Value).value as Boolean)
    }
}