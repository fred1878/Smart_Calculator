import java.util.*

fun main() {
    Calculator().start()
}

class Calculator {
    private val variables = mutableMapOf<String, Int>()

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

    private fun processInput(input:String){
        try {
            if (input.contains('=')) {
                assignVariable(input)
            } else {
                println(calculate(input))
            }
        } catch (e:Exception) {
            println("Unknown variable")
        }
    }

    private fun assignVariable(input: String) {
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
        variables[identifier] = getVariableValue(value)
    }

    private fun getVariableValue(value:String):Int {
        return value.toIntOrNull() ?: variables[value] ?: throw Exception()
    }

    private fun calculate(input:String):Int {
        var nums = Stack<Int>()
        var operations = Stack<String>()
        val chars = input.split(" ").toMutableList()

        while (chars.isNotEmpty()){
            var char = chars.removeFirst()

            when {
                char.matches("(--)*".toRegex()) -> char = "+"
                char.matches("-*".toRegex()) -> char = "-"
                char.matches("\\+*".toRegex()) -> char = "+"
            }

            if (char in "+-") {
                operations.push(char)
            } else {
                if (operations.isEmpty()) {
                    nums.push(getVariableValue(char))
                } else {
                    try {
                        when (operations.pop()) {
                            "+" -> nums.push(nums.pop() + getVariableValue(char))
                            "-" -> nums.push(nums.pop() - getVariableValue(char))
                        }
                    } catch (e: Exception) {
                        println("Invalid expression")
                    }
                }
            }
        }
        return nums.pop()
    }
}