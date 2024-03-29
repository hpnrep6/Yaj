package tests

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import yaj.YajInterpreter
import yaj.backend.ast.visitor.Execute
import java.io.File
import java.io.FileNotFoundException
import kotlin.text.StringBuilder

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
    fun lexer_token_generation_errors_count() {
        val string = readFile("lexer_token_generation.yaj")
        val interpreter = YajInterpreter(string)

        val tokens = interpreter.lex()

        assertEquals(false, interpreter.hasEncounteredError())
    }

    val lexer_token_generation_content_expected = """
        Token of type: START_OF with value: {None} at line: 0, column: 0
        Token of type: FUNC with value: {None} at line: 0, column: 3
        Token of type: IDENTIFIER with value: {test} at line: 0, column: 8
        Token of type: PAREN_L with value: {None} at line: 0, column: 9
        Token of type: PAREN_R with value: {None} at line: 0, column: 10
        Token of type: BRACE_L with value: {None} at line: 0, column: 12
        Token of type: NEW_LINE with value: {None} at line: 0, column: 13
        Token of type: VAR_DEF with value: {None} at line: 1, column: 7
        Token of type: IDENTIFIER with value: {iii} at line: 1, column: 11
        Token of type: ASSIGN_P with value: {None} at line: 1, column: 12
        Token of type: DOUBLE with value: {0.0} at line: 1, column: 15
        Token of type: NEW_LINE with value: {None} at line: 1, column: 15
        Token of type: VAR_DEF with value: {None} at line: 2, column: 7
        Token of type: IDENTIFIER with value: {cc} at line: 2, column: 10
        Token of type: ASSIGN_P with value: {None} at line: 2, column: 11
        Token of type: DOUBLE with value: {3.0} at line: 2, column: 14
        Token of type: NEW_LINE with value: {None} at line: 2, column: 14
        Token of type: NEW_LINE with value: {None} at line: 3, column: 1
        Token of type: VAR_DEF with value: {None} at line: 4, column: 7
        Token of type: IDENTIFIER with value: {ge} at line: 4, column: 10
        Token of type: ASSIGN_P with value: {None} at line: 4, column: 11
        Token of type: STRING with value: {str} at line: 4, column: 17
        Token of type: NEW_LINE with value: {None} at line: 4, column: 18
        Token of type: VAR_DEF with value: {None} at line: 5, column: 7
        Token of type: IDENTIFIER with value: {t} at line: 5, column: 9
        Token of type: ASSIGN_V with value: {None} at line: 5, column: 10
        Token of type: IDENTIFIER with value: {g} at line: 5, column: 13
        Token of type: NEW_LINE with value: {None} at line: 5, column: 14
        Token of type: BRACE_R with value: {None} at line: 6, column: 1
        Token of type: NEW_LINE with value: {None} at line: 6, column: 2
        Token of type: EOF with value: {None} at line: 6, column: 2
    """.trimIndent().replace("\n", "")

    @Test
    fun lexer_token_generation_content() {
        val string = readFile("lexer_token_generation.yaj")
        val interpreter = YajInterpreter(string)

        val tokens = interpreter.lex()

        val strb = StringBuilder()

        for(token in tokens) {
            strb.append(token.toString())
        }

        assertEquals(lexer_token_generation_content_expected, strb.toString())
    }

    val lexer_token_string_errors_expected = """
        Error: Unterminated string literal at line 0 index 15
        var a := "strin
        _______________^_

        Error: Unterminated string literal at line 2 index 13
        "test test te
        _____________^_

        Error: Unterminated string literal at line 4 index 8
        'test te
        ________^_

        Error: Unterminated string literal at line 15 index 13
        "def in def"
        _____________^_


    """.trimIndent()

    class outputWrangler(input : String) : YajInterpreter(input) {
        val errors = StringBuilder()
        val output = StringBuilder()

        override fun errorOut(error : String) {
            errors.append(error)
            errors.append("\n")
        }

        override fun out(message: String) {
            output.append(message)
            output.append("\n")
        }
    }

    @Test
    fun lexer_token_string_errors() {
        val string = readFile("lexer_token_string_error.yaj")
        val interpreter = outputWrangler(string)

        interpreter.lex()

        assertEquals(lexer_token_string_errors_expected, interpreter.errors.toString())
    }

    val parser_ast_expr_expected = """
        Scene(
        Assign(VarDef{test}, Add(23.0, Positive(3.0)))
        Assign(VarDef{d}, Add(Multiply(2.0, VarLookup{test}), Negative(5.0)))
        Assign(VarDef{stringTest}, Concat("string", " test"))
        Assign(VarDef{stringConcat}, Concat("stringtest is ", VarLookup{stringTest}))
        Assign(VarDef{str}, Concat("add the two! ->", Concat(VarLookup{stringConcat}, VarLookup{stringTest})))
        )
    """.trimIndent()

    @Test
    fun parser_ast_expr() {
        val string = readFile("parser_ast_expr.yaj")
        val interpreter = YajInterpreter(string)

        var tokens = interpreter.lex()

        var node = interpreter.parse(tokens)

        var exec = Execute(interpreter)

        node.visit(exec)

        assertEquals(parser_ast_expr_expected, node.toString())
    }

    val interpreter_operations_expected = """
        0
        1
        2
        3
        5
        8
        13
        21
        34
        55
        
    """.trimIndent()

    @Test
    fun interpreter_operations() {
        val string = readFile("interpreter_operations.yaj")
        val interpreter = outputWrangler(string)

        var tokens = interpreter.lex()

        var node = interpreter.parse(tokens)

        var exec = Execute(interpreter)

        node.visit(exec)

        assertEquals(interpreter_operations_expected,interpreter.output.toString())
    }

    val interpreter_procedures_expected = """
        30
        32
        34
        36
        
    """.trimIndent()

    @Test
    fun interpreter_procedures() {
        val string = readFile("interpreter_procedures.yaj")
        val interpreter = outputWrangler(string)

        var tokens = interpreter.lex()

        var node = interpreter.parse(tokens)

        var exec = Execute(interpreter)

        node.visit(exec)

        assertEquals(interpreter_procedures_expected, interpreter.output.toString())
    }

    val interpreter_functions_expected = """
        277 is the value of test
        369 is the value of test
        465 is the value of test
        565 is the value of test
        669 is the value of test
        777 is the value of test
        889 is the value of test
        1005 is the value of test
        1125 is the value of test
        
    """.trimIndent()

    @Test
    fun interpreter_functions() {
        val string = readFile("interpreter_functions.yaj")
        val interpreter = outputWrangler(string)

        var tokens = interpreter.lex()

        var node = interpreter.parse(tokens)

        var exec = Execute(interpreter)

        node.visit(exec)

        assertEquals(interpreter_functions_expected, interpreter.output.toString())
    }

    val interpreter_recursion_expected = """
        Fib sequence at index 0 is 0
        Fib sequence at index 1 is 1
        Fib sequence at index 2 is 1
        Fib sequence at index 3 is 2
        Fib sequence at index 4 is 3
        Fib sequence at index 5 is 5
        Fib sequence at index 6 is 8
        Fib sequence at index 7 is 13
        Fib sequence at index 8 is 21
        Fib sequence at index 9 is 34
        
    """.trimIndent()

    @Test
    fun interpreter_recursion() {
        val string = readFile("interpreter_recursion.yaj")
        val interpreter = outputWrangler(string)

        var tokens = interpreter.lex()

        var node = interpreter.parse(tokens)

        var exec = Execute(interpreter)

        node.visit(exec)

        assertEquals(interpreter_recursion_expected, interpreter.output.toString())
    }

    val interpreter_casting_expected = """
        eee223
        3true
        true
        
    """.trimIndent()

    @Test
    fun interpreter_casting() {
        val string = readFile("parser_casting.yaj");
        val interpreter = outputWrangler(string)

        var tokens = interpreter.lex()

        var node = interpreter.parse(tokens)

        var exec = Execute(interpreter)

        node.visit(exec)

        assertEquals(interpreter_casting_expected, interpreter.output.toString())
    }
}