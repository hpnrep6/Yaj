package yaj.backend.type

class String (string : kotlin.String) : Type() {
    var value = string

    override fun toString() : kotlin.String {
        return value
    }
}