package yajscript.backend

class Error(message : String, row : String, line: Int, column : Int) {
    val message = message
    val row = row
    val line = line
    val column = column

    fun print(out : (String) -> Unit) {

        out("Error: $message at line $line index $column")
        out("$row")

        var whitespace = StringBuilder()

        for (i in 1..column) {
            whitespace.append('_')
        }

        whitespace.append('^')
        whitespace.append('_')

        out(whitespace.toString())
        out("")
    }
}