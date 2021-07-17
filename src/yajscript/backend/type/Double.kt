package yajscript.backend.type

class Double (value : kotlin.Double) : Type() {
    var value = value

    override fun toString() : kotlin.String {
        return value.toString()
    }
}