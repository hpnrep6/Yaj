package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import kotlin.String

class Number (value : Double) : Value() {
    override val value = value

    override fun visit(visitor : Visitor): Number {
        return visitor.visitNumber(this)
    }

    override fun toPrint(): String {
        return formatNumber(value)
    }

    override fun toString(): String {
        return value.toString()
    }
}

fun formatNumber(num: Double): String {
    if (hasFract(num)) {
        return num.toString()
    } else {
        return num.toLong().toString()
    }
}

private fun hasFract(num: Double): Boolean {
    val whole = num.toLong()
    val fract = num - whole.toDouble()
    return fract != 0.0
}