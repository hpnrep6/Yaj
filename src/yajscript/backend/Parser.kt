package yajscript.backend

import yajscript.YajInterpreter

class Parser(interpreter: YajInterpreter) {

    val output = interpreter

    lateinit var tokens : MutableList<Token>

    val errors: MutableList<Error> = mutableListOf<Error>()

    private fun reset() {
        errors.clear()

    }

    fun parse(tokens: MutableList<Token>) {
        this.tokens = tokens
        reset()


    }
}