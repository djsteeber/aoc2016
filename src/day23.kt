/**
 * Created by dsteeber on 12/21/2016.
 */
package day23

import java.io.File
import java.util.*



/**
 * Created by dsteeber on 12/15/2016.
 */


class Registers {
    private val registers = HashMap<String, Int>()

    private fun isValidRegisterName(name: String): Boolean {
        return ("abcdefghijklmnopqrstuvwxyz".indexOf(name) >= 0)
    }

    fun get(registerName: String): Int {
        var returnValue = 0

        try {
            returnValue = registerName.toInt()
        } catch (e: NumberFormatException) {
            if (isValidRegisterName(registerName)) {
                if (registers[registerName] == null) {
                    registers[registerName] = 0
                }
                returnValue = registers[registerName]!!
            }
        }

        return returnValue
    }

    fun put(registerName: String, value: Int) {
        if (isValidRegisterName(registerName)) {
            registers[registerName] = value
        }
    }

    fun put(registerName: String, valueOrRegisterName: String) {
        if (isValidRegisterName(registerName)) {
            try {
                val value = valueOrRegisterName.toInt()
                put(registerName, value)
            } catch (e: NumberFormatException) {
                put(registerName, get(valueOrRegisterName))
                if (registers[registerName] == null) {
                    registers[registerName] = 0
                }
            }
        }
    }

    fun clear() {
        registers.clear()
    }

}


class BAInterpreter(val program: List<String>) {
    val registers =  Registers()


    private fun getRegisterValueOrValue(valueOrName: String): Int {
        val value = try {
            valueOrName.toInt()
        } catch (ex: NumberFormatException) {
            registers.get(valueOrName)
        }
        return value
    }

    fun exec() {
        val instructionSet: MutableList<String> = mutableListOf()
        instructionSet.addAll(program)

        var instructionSetPointer = 0
        while (instructionSetPointer < instructionSet.size) {
            val instruction = instructionSet[instructionSetPointer]

            val parts = instruction.split(' ').map{ it.trim() }
            val fn = parts[0]
            val args = parts.slice(1..parts.size-1)
            //println("{${instruction}} instructionSetPointer ${instructionSetPointer}")
            when (fn) {
                "cpy" -> {
                    registers.put(args[1], args[0])
                    instructionSetPointer++
                }
                "inc" -> {
                    registers.put(args[0], registers.get(args[0]) + 1)
                    instructionSetPointer++
                }
                "dec" -> {
                    registers.put(args[0], registers.get(args[0]) - 1)
                    instructionSetPointer++
                }
                "jnz" -> {
                    if (registers.get(args[0]) == 0) {
                        instructionSetPointer++
                    } else {
                        instructionSetPointer += getRegisterValueOrValue(args[1])
                    }
                }
                "tgl" -> {
                    var tglInstPtr = instructionSetPointer
                    val count = getRegisterValueOrValue(args[0])
                    tglInstPtr +=  count
                    if ((tglInstPtr >=0) &&(tglInstPtr < instructionSet.size)) {
                        // get the instruction at tglInstrPtr
                        val tglInstruction = instructionSet[tglInstPtr]
                        val tglArgs = tglInstruction.split(' ').map{ it.trim() }

                        if (tglArgs.size == 2) {
                            if (tglArgs[0] == "inc") {
                                instructionSet[tglInstPtr] = "dec" + " " + tglArgs[1]
                            } else {
                                instructionSet[tglInstPtr] = "inc" + " " + tglArgs[1]
                            }
                        } else {
                            if (tglArgs[0] == "jnz") {
                                instructionSet[tglInstPtr] = "cpy" + " " + tglArgs[1] + " " + tglArgs[2]
                            } else {
                                instructionSet[tglInstPtr] = "jnz" + " " + tglArgs[1] + " " + tglArgs[2]
                            }
                        }
                    }
                    instructionSetPointer++

                }
            }
        }
        //instructionSet.forEach { println(it) }
    }
}



//9227737
fun main(args: Array<String>) {
    val instructionSet: List<String> = File(".\\resources\\day23.data").readLines()

    val instructionSet2: List<String> = listOf(
            "cpy 2 a",
            "tgl a",
            "tgl a",
            "tgl a",
            "cpy 1 a",
            "dec a",
            "dec a")

    val baInterpreter = BAInterpreter(instructionSet)
    baInterpreter.registers.put("a",7)
    baInterpreter.exec()
    println("the value in register a is " + baInterpreter.registers.get("a"))


    baInterpreter.registers.clear()
    baInterpreter.registers.put("a",12)
    baInterpreter.exec()
    println("PART 2:  the value in register a is " + baInterpreter.registers.get("a"))

}
