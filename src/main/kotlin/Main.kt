fun main() {
    while (true) {
        val input = readln()
        if (input.contains("\\d".toRegex())) {
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
            println(sum)
        } else if (input == "/exit") {
            println("Bye!")
            break
        } else if (input == "/help") {
            println("The program calculates the sum of numbers")
        } else if (input.matches("/.*".toRegex())) {
            println("Unknown Command")
        } else if (input.isNotEmpty()) {
            println("Invalid expression")
        }
    }
}