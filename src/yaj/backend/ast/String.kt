package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import yaj.backend.type.Type
import kotlin.String

class String (value : String): Value() {
    override val value = value

    constructor(cast: Type): this(cast.toString()) {

    }

    override fun visit(visitor : Visitor): Any? {
        return visitor.visitString(this)
    }

    override fun toPrint(): kotlin.String {
        return value
    }

    override fun toString(): kotlin.String {
        return "\"${value}\""
    }

    override fun equals(other: Node): Boolean {
        if (other::class != this::class) {
            return false
        }

        val asStr = other as yaj.backend.ast.String
        return asStr.value == this.value
    }
}