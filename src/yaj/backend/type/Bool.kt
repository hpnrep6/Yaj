package yaj.backend.type

class Bool (value : Boolean) : Type() {
    var value = value

    override fun toString() : kotlin.String {
        return value.toString()
    }
}