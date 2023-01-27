fun main() {
    Calculator().start()
}

class Calculator {
    val variables = mutableMapOf<String, Int>()

    fun start() {
        while (true) {

            val input = readln()
            if (input.isEmpty()) {
                continue
            }
            if (input == "/exit") {
                println("Bye!")
                break
            } else if (input == "/help") {
                println("The program calculates the sum of numbers")
            } else if (input.matches("/.*".toRegex())) {
                println("Unknown Command")
            } else {
                processInput(input)
            }
        }
    }

    fun processInput(input:String){
        try {
            if (input.contains('=')) {
                assignVariable(input)
            } else if (input.trim().matches("[a-zA-Z]*".toRegex())) {
                println(variables[input])
            } else {
                println(calculate(input))
            }
        } catch (e:Exception) {
            println("Invalid expression")
        }
    }
    fun assignVariable(input: String) {
        val identifier = input.substringBefore('=').trim()
        val value = input.substringAfter('=').trim()
        if (!identifier.matches("[a-zA-Z]*".toRegex())) {
            println("Invalid identifier")
            return
        }
        if (!value.matches("\\d*".toRegex()) && !value.matches("[a-zA-Z]*".toRegex())) {
            println("Invalid assignment")
            return
        }
        variables[identifier] = assignVariableValue(value)
    }

    fun assignVariableValue(value:String):Int {
        return value.toIntOrNull() ?: variables[value] ?: throw Exception()
    }

    fun calculate(input:String):Int {
        var sum = 0
        var operation = "+"
        val chars = input.split(" ").toMutableList()

        while (chars.isNotEmpty()){
            var char = chars.removeFirst()

            when {
                char.matches("(--)*".toRegex()) -> char = "+"
                char.matches("-*".toRegex()) -> char = "-"
                char.matches("\\+*".toRegex()) -> char = "+"
            }

            if (char in "+-") {
                operation = char
            } else {
                try {
                    when (operation) {
                        "+" -> sum += char.toInt()
                        "-" -> sum -= char.toInt()
                    }
                } catch (e:Exception){
                    println("Invalid expression")
                }
            }
        }
        return sum
    }

}
