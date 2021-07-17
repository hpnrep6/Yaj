package yajscript.backend.type

class String (string : kotlin.String) : Type() {
    var value = string

    override fun toString() : kotlin.String {
        return value
    }
}