import java.io.File
import java.util.*

/**
 * Created by dsteeber on 12/15/2016.
 */


class Registers {
    private val registers = HashMap<String, Int>()

    fun get(registerName: String): Int {
        var returnValue = 0

        try {
            returnValue = registerName.toInt()
        } catch (e: NumberFormatException) {
            if (registers[registerName] == null) {
                registers[registerName] = 0
            }
            returnValue = registers[registerName]!!
        }

        return returnValue
    }

    fun put(registerName: String, value: Int) {
       registers[registerName] = value
    }

    fun put(registerName: String, valueOrRegisterName: String) {
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




fun main(args: Array<String>) {
    val instructionSet: List<String> = File(".\\resources\\day12.data").readLines()
    val registers =  Registers()
    var instructionSetPointer = 0
    registers.put("c", 1)

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
                    instructionSetPointer += args[1].toInt()
                }
            }
        }
    }


    println("the value in register a is " + registers.get("a"))

}
