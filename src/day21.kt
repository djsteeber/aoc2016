/**
 * Created by dsteeber on 12/21/2016.
 */
package day21

import utils.Queue
import utils.Stack
import java.io.File

/*
The computer system you're breaking into uses a weird scrambling function to store its passwords. It shouldn't be much trouble to create your own scrambled password so you can add it to the system; you just have to implement the scrambler.

The scrambling function is a series of operations (the exact list is provided in your puzzle input). Starting with the password to be scrambled, apply each operation in succession to the string.
The individual operations behave as follows:

swap position X with position Y means that the letters at indexes X and Y (counting from 0) should be swapped.
swap letter X with letter Y means that the letters X and Y should be swapped (regardless of where they appear in the string).
rotate left/right X steps means that the whole string should be rotated; for example, one right rotation would turn abcd into dabc.
rotate based on position of letter X means that the whole string should be rotated to the right based on the index of letter X (counting from 0) as determined before this instruction does any rotations. Once the index is determined, rotate the string to the right one time, plus a number of times equal to that index, plus one additional time if the index was at least 4.
reverse positions X through Y means that the span of letters at indexes X through Y (including the letters at X and Y) should be reversed in order.
move position X to position Y means that the letter which is at index X should be removed from the string, then inserted such that it ends up at index Y.
For example, suppose you start with abcde and perform the following operations:

swap position 4 with position 0 swaps the first and last letters, producing the input for the next step, ebcda.
swap letter d with letter b swaps the positions of d and b: edcba.
reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
rotate left 1 step shifts all letters left one position, causing the first letter to wrap to the end of the string: bcdea.
move position 1 to position 4 removes the letter at position 1 (c), then inserts it at position 4 (the end of the string): bdeac.
move position 3 to position 0 removes the letter at position 3 (a), then inserts it at position 0 (the front of the string): abdec.
rotate based on position of letter b finds the index of letter b (1), then rotates the string right once plus a number of times equal to that index (2): ecabd.
rotate based on position of letter d finds the index of letter d (4), then rotates the string right once, plus a number of times equal to that index, plus an additional time because the index was at least 4, for a total of 6 right rotations: decab.
After these steps, the resulting scrambled password is decab.

Now, you just need to generate a new scrambled password and you can access the system. Given the list of scrambling operations in your puzzle input, what is the result of scrambling abcdefgh?

 */


class Scrambler() {

    private fun rotateLeft(str: String, count: Int): String {
        val queue = Queue<Char>()

        for (c in str.toCharArray()) {
            queue.enqueue(c)
        }

        for (inx in 1..count) {
            queue.enqueue(queue.dequeue()!!)
        }

        var str = ""
        while (! queue.isEmpty()) {
            str += queue.dequeue()!!.toString()
        }
        return str
    }

    private fun rotateRight(str: String, count: Int): String {
        val chars = str.toCharArray()
        val queue = Queue<Char>()

        for (inx in chars.size-1 downTo 0) {
            queue.enqueue(chars[inx])
        }

        for (inx in 1..count) {
            queue.enqueue(queue.dequeue()!!)
        }

        var str = ""
        while (! queue.isEmpty()) {
            str = queue.dequeue()!!.toString() + str
        }
        return str


    }

    //rotate based on position of letter e
    private fun rotateBased(str: String, letter: String): String {
        var count = str.indexOf(letter)

        if (count >= 4) {
            count++
        }
        count++

        return rotateRight(str, count)
    }


    private fun findOriginalPosition(str: String, letter: String): Int {
        val currentPos = str.indexOf(letter)  //fgebchda -> gebchdaf
        var originalPos = currentPos

        for (inx in 0..str.length-1) {
            val calcPos = if (inx >= 4)  inx+inx+1+1 else inx+inx+1
            if ((calcPos == currentPos) || ((calcPos % str.length) == currentPos)) {
                originalPos = inx
                break
            }
        }

        return originalPos
    }

    private fun invertRotateBased(str: String, letter: String): String {
        var tempStr = str
        var originalPosition = findOriginalPosition(tempStr, letter)
        var currentPostion = str.indexOf(letter)
        val count = (((currentPostion + str.length) - originalPosition) % str.length)
        tempStr = rotateLeft(str, count)


        return tempStr

    }

    private fun swapPosition(str: String, x: Int, y: Int): String {
        val chars = str.toCharArray()
        val tempChar = chars[x]
        chars[x] = chars[y]
        chars[y] = tempChar

        return chars.fold(""){ str, a -> str + a}
    }

    private fun swapLetter(str: String, a: Char, b: Char): String {
        val chars = str.toCharArray()

        for (inx in 0..chars.size-1) {
            if (chars[inx] == a) {
                chars[inx] = b
            } else if (chars[inx] == b) {
                chars[inx] = a
            }
        }

        return chars.fold(""){ str, a -> str + a}
    }

    private fun reversePostions(str: String, start: Int, end: Int): String {
        val chars = str.toMutableList()

        var newSeq: List<Char> = listOf()

        if (start == 0) {
            if (end == chars.size - 1) {
                newSeq = chars.reversed()
            } else {
                newSeq = chars.subList(0, end+1).reversed() + chars.subList(end+1, chars.size - 1)
            }
        } else {
            if (end == chars.size - 1) {
                newSeq = chars.subList(0,start) + chars.subList(start, end+1).reversed()
            } else {
                newSeq = chars.subList(0,start) + chars.subList(start, end+1).reversed() + chars.subList(end+1, chars.size)
            }
        }

        return newSeq.fold(""){ str, a -> str + a}
    }

    private fun movePostion(str: String, posAt: Int, posTo: Int): String {
        val chars = str.toMutableList()
        val c = chars.removeAt(posAt)
        chars.add(posTo, c)
        return chars.fold(""){ str, a -> str + a}

    }

    fun applyInstruction(str: String, instruction: String): String {
        val parts = instruction.split(" ")

        if (parts[0] == "swap") {
            if (parts[1] == "letter") {
                return swapLetter(str, parts[2].toCharArray()[0], parts[5].toCharArray()[0])
            } else if (parts[1] == "position") {
                return swapPosition(str, parts[2].toInt(), parts[5].toInt())
            }
        } else if (parts[0] == "move") {
            if (parts[1] == "position") {
                return movePostion(str, parts[2].toInt(), parts[5].toInt())
            }
        } else if (parts[0] == "reverse") {
            if (parts[1] == "positions") {
                return reversePostions(str, parts[2].toInt(), parts[4].toInt())
            }
        } else if (parts[0] == "rotate") {
            if (parts[1] == "based") {
                return rotateBased(str, parts[6])
            } else if (parts[1] == "right") {
                return rotateRight(str, parts[2].toInt())
            } else if (parts[1] == "left") {
                return rotateLeft(str, parts[2].toInt())
            }
        }


        throw Exception("Unkown instruction ${instruction}")
    }


    fun revertInstruction(str: String, instruction: String): String {
        val parts = instruction.split(" ")

        if (parts[0] == "swap") {
            if (parts[1] == "letter") {
                return swapLetter(str, parts[2].toCharArray()[0], parts[5].toCharArray()[0])  // same as apply
            } else if (parts[1] == "position") {
                return swapPosition(str, parts[2].toInt(), parts[5].toInt()) // same as apply
            }
        } else if (parts[0] == "move") {
            if (parts[1] == "position") {
                return movePostion(str, parts[5].toInt(), parts[2].toInt()) // just reversed the parameters
            }
        } else if (parts[0] == "reverse") {
            if (parts[1] == "positions") {
                return reversePostions(str, parts[2].toInt(), parts[4].toInt()) // same as apply
            }
        } else if (parts[0] == "rotate") {
            if (parts[1] == "based") {
                return invertRotateBased(str, parts[6]) //TODO  this if different and need to rework
            } else if (parts[1] == "left") {
                return rotateRight(str, parts[2].toInt()) // rotate right should rotate back left
            } else if (parts[1] == "right") {
                return rotateLeft(str, parts[2].toInt()) // rotate left should rotate back right
            }
        }


        throw Exception("Unkown instruction ${instruction}")
    }
}





fun main(args: Array<String>) {
    val scrambler = Scrambler()

    //val answerStack = Stack<String>()
/*
    swap position 4 with position 0 swaps the first and last letters, producing the input for the next step, ebcda.
    swap letter d with letter b swaps the positions of d and b: edcba.
    reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
    rotate left 1 step shifts all letters left one position, causing the first letter to wrap to the end of the string: bcdea.
    move position 1 to position 4 removes the letter at position 1 (c), then inserts it at position 4 (the end of the string): bdeac.
    move position 3 to position 0 removes the letter at position 3 (a), then inserts it at position 0 (the front of the string): abdec.
    rotate based on position of letter b finds the index of letter b (1), then rotates the string right once plus a number of times equal to that index (2): ecabd.
    rotate based on position of letter d finds the index of letter d (4), then rotates the string right once, plus a number of times equal to that index, plus an additional time because the index was at least 4, for a total of 6 right rotations: decab.
    After these steps, the resulting scrambled password is decab.

*/
    val lines = File(".\\resources\\day21.data").readLines()
    var str = "abcdefgh"

/*
    val lines: List<String> = listOf("swap position 4 with position 0",
            "swap letter d with letter b",
            "reverse positions 0 through 4",
            "rotate left 1",
            "move position 1 to position 4",
            "move position 3 to position 0",
            "rotate based on position of letter b",
            "rotate based on position of letter d"
            )
    var str = "abcde"
*/
    //answerStack.push(str)
    for (line in lines) {
        str = scrambler.applyInstruction(str, line)
        println("'${line}' produces ->' ${str}'")
        //answerStack.push(str)
    }
    println("--------")
println("starting with ${str}")

    // need to unscramble
    str = "fbgdceah"

    //answerStack.pop()
    for (line in lines.reversed()) {
        str = scrambler.revertInstruction(str, line)
        println("'${line}' produces ->' ${str}'")
  /*
        val prevAnswer = answerStack.pop()!!

        if (str != prevAnswer) {
            println("******* ${prevAnswer} does not match ${str}")
        }
        */
    }


    println("day21")
}