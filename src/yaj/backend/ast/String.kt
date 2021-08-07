package yaj.backend.ast

import yaj.backend.ast.visitor.Visitor
import yaj.backend.type.Type
import kotlin.String

class String (value : String): Value() {
    override val value = value

    constructor(cast: Type): this(cast.toString()) {

    }

    override fun visit(visitor : Visitor): yaj.backend.ast.String {
        return visitor.visitString(this)
    }

    override fun toPrint(): kotlin.String {
        return value
    }

    override fun toString(): kotlin.String {
        return "\"${value}\""
    }
}