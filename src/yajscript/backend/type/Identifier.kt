package yajscript.backend.type

class Identifier (name: kotlin.String) : Type() {
    var value = name

    override fun toString() : kotlin.String {
        return value.toString()
    }
}