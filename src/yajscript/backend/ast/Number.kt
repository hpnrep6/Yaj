package yajscript.backend.ast

import yajscript.backend.type.Double

class Number (value : Double) : Value() {
    override val value = value
}