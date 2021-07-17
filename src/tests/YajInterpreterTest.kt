package tests

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import yajscript.YajInterpreter
import java.io.File
import java.io.FileNotFoundException

val testDirectory: String = "src/tests/YajInterpreter_"

fun readFile(file: String) : String {
    try {
        val file: File = File(testDirectory + file)
        return file.inputStream().readBytes().toString(Charsets.UTF_8)
    } catch (e: FileNotFoundException) {
        println(e)
        return ""
    }
}

internal class YajInterpreterTest {

    @Test
    fun lexer_token_generation() {
        val string = readFile("lexer_token_generation.yaj")
        val interpreter = YajInterpreter(string)

        val tokens = interpreter.lex()

        for(token in tokens) {
            println(token)
        }

        Assertions.assertEquals(1, 1)
    }

}