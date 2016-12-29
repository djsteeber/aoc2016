package day25

import java.io.File
import java.util.*


/*
You open the door and find yourself on the roof. The city sprawls away from you for miles and miles.

There's not much time now - it's already Christmas, but you're nowhere near the North Pole,
much too far to deliver these stars to the sleigh in time.

However, maybe the huge antenna up here can offer a solution. After all, the sleigh doesn't need the stars, exactly;
it needs the timing data they provide, and you happen to have a massive signal generator right here.

You connect the stars you have to your prototype computer, connect that to the antenna, and begin the transmission.

Nothing happens.

You call the service number printed on the side of the antenna and quickly explain the situation.
"I'm not sure what kind of equipment you have connected over there," he says,
"but you need a clock signal." You try to explain that this is a signal for a clock.

"No, no, a clock signal - timing information so the antenna computer knows how to read the data you're sending it.
An endless, alternating pattern of 0, 1, 0, 1, 0, 1, 0, 1, 0, 1...." He trails off.

You ask if the antenna can handle a clock signal at the frequency you would need to use for the data from the stars.
"There's no way it can! The only antenna we've installed capable of that is on top
of a top-secret Easter Bunny installation, and you're definitely not-" You hang up the phone.

You've extracted the antenna's clock signal generation assembunny code (your puzzle input);
it looks mostly compatible with code you worked on just recently.

This antenna code, being a signal generator, uses one extra instruction:

out x transmits x (either an integer or the value of a register) as the next value for the clock signal.
The code takes a value (via register a) that describes the signal to generate,
but you're not sure how it's used. You'll have to find the input to produce the right signal through experimentation.

What is the lowest positive integer that can be used to initialize register a and cause the code to output a clock signal of 0, 1, 0, 1... repeating forever?
 */

class Registers {
    private val registers = HashMap<String, Int>()

    private val ALL_REG_NAMES = "abcdefghijklmnopqrstuvwxyz"
    private fun isValidRegisterName(name: String): Boolean = ALL_REG_NAMES.contains(name)


    fun get(registerName: String): Int =
            if (isValidRegisterName(registerName)) {
                registers[registerName] ?: 0
            } else {
                registerName.toInt()
            }

    fun put(registerName: String, value: Int) {
        registers[registerName] = value
    }

    fun put(registerName: String, valueOrRegisterName: String) {
        if (isValidRegisterName(valueOrRegisterName)) {
            put(registerName, get(valueOrRegisterName))
        } else {
            put(registerName, valueOrRegisterName.toInt())
        }
    }

    fun clear() {
        registers.clear()
    }

}


class BAInterpreter(program: List<String>) {
    val registers =  Registers()

    private val instructionSet: MutableList<List<String>> = mutableListOf()
    init {
        program.forEach { instructionSet.add( it.split(' ').map(String::trim)) }
    }


    private fun getRegisterValueOrValue(valueOrName: String): Int {
        val value = try {
            valueOrName.toInt()
        } catch (ex: NumberFormatException) {
            registers.get(valueOrName)
        }
        return value
    }

    fun exec(): Boolean {
        var outCount = 0

        var currentOutValue: Int = 2  // set to 1 because first value needs to be 0

        var instructionSetPointer = 0
        while (instructionSetPointer < instructionSet.size) {
            val instruction = instructionSet[instructionSetPointer]


            //println("{${instruction}} instructionSetPointer ${instructionSetPointer}")
            when (instruction[0]) {
                "cpy" -> {
                    registers.put(instruction[2], instruction[1])
                    instructionSetPointer++
                }
                "inc" -> {
                    registers.put(instruction[1], registers.get(instruction[1]) + 1)
                    instructionSetPointer++
                }
                "dec" -> {
                    registers.put(instruction[1], registers.get(instruction[1]) - 1)
                    instructionSetPointer++
                }
                "jnz" -> {
                    if (registers.get(instruction[1]) == 0) {
                        instructionSetPointer++
                    } else {
                        instructionSetPointer += getRegisterValueOrValue(instruction[2])
                    }
                }
                "out" -> {
                    val outValue = registers.get(instruction[1])
                    //print(outValue.toString())
                    if ((currentOutValue != outValue) && ((outValue == 1) || (outValue == 0))) {
                        currentOutValue = outValue
                        outCount++
                        if (outCount > 100000) {  // this is a hack,  you should check the register patter to see if it repeats
                            return true
                        }
                        // note, after 5 or six iterations, check to see if the register values match or repeat
                    } else {
                        return false
                    }
                    instructionSetPointer++
                }
            }
        }
        return false
    }
}



//9227737
fun main(args: Array<String>) {
    val instructionSet: List<String> = File(".\\resources\\day25.data").readLines()


    val baInterpreter = BAInterpreter(instructionSet)

    var i = 0
    var start = System.currentTimeMillis()
    while (true) {
        baInterpreter.registers.clear()
        baInterpreter.registers.put("a",i)
        val found = baInterpreter.exec()
        if ((i % 100) == 0) {
            val end = System.currentTimeMillis()
            println("running with a = $i: time = ${end - start}")
            start = end
        }
        if (found) {
            println("Found register a possiblity at $i")
            break
        } else {
            i++
        }
    }

}
