package yajscript.backend.ast

import yajscript.backend.ast.visitor.Visitor
import yajscript.backend.type.Type
import kotlin.String

class String (value : String): Value() {
    override val value = value

    constructor(cast: Type): this(cast.toString()) {

    }

    override fun visit(visitor : Visitor) {

    }

    override fun toPrint(): kotlin.String {
        return value
    }

    override fun toString(): kotlin.String {
        return "\"${value}\""
    }
}