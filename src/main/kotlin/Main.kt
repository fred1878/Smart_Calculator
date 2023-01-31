import java.math.BigInteger
import java.util.*
import kotlin.math.pow

fun main() {
    Calculator().start()
}

class Calculator {
    private val variables = mutableMapOf<String, BigInteger>()

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

    private fun processInput(input: String) {
        try {
            if (input.contains('=')) {
                assignVariable(input)
            } else {
                println(calculateRPN(convertToRPN(input.replace("\\s".toRegex(), ""))))
            }
        } catch (e: Exception) {
            println("Invalid Expression")
        }
    }

    private fun convertToRPN(input: String): Stack<Token> {
        val operator = Stack<Token>()
        val output = Stack<Token>()

        var cleanInput = input.replace("\\+{2,}".toRegex(), "+")
        cleanInput = "-{2,}".toRegex().replace(cleanInput) { r -> if (r.value.length % 2 == 0) "+" else "-" }

        val tokens = cleanInput.trim().split("(?<=op)|(?=op)".replace("op", "[-+*/()]").toRegex())
            .filter { it.isNotEmpty() }.map { it }.toMutableList()

        while (tokens.isNotEmpty()) {
            val token = Token(tokens.removeFirst())

            if (token.isNumber()) {
                output.add(token)
            } else if (token.isOperator()) {
                while (operator.isNotEmpty() && token.getPrecidence() <= operator.peek()
                        .getPrecidence() && !operator.peek().isLeftBracket()
                ) {
                    output.add(operator.pop())
                }
                operator.add(token)
            } else if (token.isLeftBracket()) {
                operator.add(token)
            } else if (token.isRightBracket()) {
                while (!operator.peek().isLeftBracket()) {
                    try {
                        output.add(operator.pop())
                    } catch (e: Exception) {
                        println("Invalid Expression")
                    }
                }
                if (operator.isNotEmpty() && operator.peek().isLeftBracket()) {
                    operator.pop()
                }
            } else {
                try {
                    output.add(Token(getVariableValue(token.toString()).toString()))
                } catch (e:Exception) {
                    println("Unknown variable")
                }
            }
        }
        if (operator.contains(Token(")")) || operator.contains(Token("("))) {
            println("Invalid Expression")
        }

        while (operator.isNotEmpty()) {
            output.add(operator.pop())
        }
        return output
    }

    private fun calculateRPN(input: Stack<Token>): BigInteger {
        val stack = Stack<Token>()

        for (token in input) {
            if (token.isNumber()) {
                stack.push(token)
            } else if (token.isOperator()) {
                val b = stack.pop().toBigInteger()
                val a = stack.pop().toBigInteger()

                when (token.toString()) {
                    "+" -> stack.push(Token((a + b).toString()))
                    "-" -> stack.push(Token((a - b).toString()))
                    "*" -> stack.push(Token((a * b).toString()))
                    "/" -> stack.push(Token((a / b).toString()))
                    "^" -> stack.push(Token((a.pow(b.toInt()).toBigDecimal().toBigInteger().toString())))
                }
            }
        }
        if (stack.size != 1) {
            println("Stack is not correct size, contains $stack")
        }

        return stack.pop().toBigInteger()
    }

    private fun assignVariable(input: String) {
        val identifier = input.substringBefore('=').trim()
        val value = input.substringAfter('=').trim()
        if (!identifier.matches("[a-zA-Z]*".toRegex())) {
            println("Invalid identifier")
            return
        }
        if (!value.matches("-?\\d*".toRegex()) && !value.matches("[a-zA-Z]*".toRegex())) {
            println("Invalid assignment")
            return
        }
        variables[identifier] = getVariableValue(value)
    }

    private fun getVariableValue(value: String): BigInteger {
        return value.toBigIntegerOrNull() ?: variables[value] ?: throw Exception()
    }

    data class Token(val token: String) {
        fun isNumber(): Boolean {
            return token.matches("-?\\d+".toRegex())
        }

        fun isOperator(): Boolean {
            return token in "+-/*^"
        }

        fun isLeftBracket(): Boolean {
            return token in "("
        }

        fun isRightBracket(): Boolean {
            return token in ")"
        }

        override fun toString(): String {
            return token
        }

        fun toBigInteger(): BigInteger {
            return token.toBigInteger()
        }

        fun getPrecidence(): Int {
            return when (token) {
                "^" -> 4
                "/" -> 3
                "*" -> 3
                "+" -> 2
                "-" -> 2
                else -> 0
            }
        }
    }
}