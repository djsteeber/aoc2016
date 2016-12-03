import com.sun.xml.internal.fastinfoset.util.StringArray
import java.io.File
import java.util.*

/**
 * Created by dsteeber on 12/2/2016.
 */

data class KeyPadNum(val num: Int) {

    var up: KeyPadNum = this
    var down: KeyPadNum = this
    var left: KeyPadNum = this
    var right: KeyPadNum = this

    fun move(): KeyPadNum {
        return up
    }

    fun setAdjacent(up: KeyPadNum? = null, down: KeyPadNum? = null, left: KeyPadNum? = null, right: KeyPadNum? = null) {
        this.up = up ?: this.up
        this.down = down ?: this.down
        this.left = left ?: this.left
        this.right = right ?: this.right
    }

    fun findKey(str: String = ""): KeyPadNum {
        if (str.length == 0) {
            return this
        } else {
            val moveChar = str[0]
            val moveRest = str.substring(1)
            val key = when (moveChar) {
                'U' -> {
                    this.up.findKey(moveRest)
                }
                'D' -> {
                    this.down.findKey(moveRest)
                }
                'L' -> {
                    this.left.findKey(moveRest)
                }
                'R' -> {
                    this.right.findKey(moveRest)
                }
                else -> {
                    println("Error, invalid move '$moveChar'")
                    throw Exception("Invalid move instruction $moveChar")
                }

            }
            return key
        }
    }
}

var keyPad: HashMap<Int, KeyPadNum> = HashMap<Int, KeyPadNum>()

/*
1 2 3
4 5 6
7 8 9
 */
fun HashMap<Int, KeyPadNum>.buildKeyPad() {
    for (i in 1..9) {
        this[i] = KeyPadNum(i)
    }
    this[1]!!.setAdjacent(right = this[2], down = this[4])
    this[2]!!.setAdjacent(left = this[1], right = this[3], down = this[5])
    this[3]!!.setAdjacent(left = this[2], down = this[6])
    this[4]!!.setAdjacent(up = this[1], right = this[5], down = this[7])
    this[5]!!.setAdjacent(up = this[2], down = this[8], left = this[4], right = this[6])
    this[6]!!.setAdjacent(up = this[3], left = this[5], down = this[9])
    this[7]!!.setAdjacent(up = this[4], right = this[8])
    this[8]!!.setAdjacent(left = this[7], up = this[5], right = this[9])
    this[9]!!.setAdjacent(left = this[8], up = this[6])

}


fun main(args: Array<String>) {
    var keyPad: HashMap<Int, KeyPadNum> = HashMap<Int, KeyPadNum>()
    keyPad.buildKeyPad()

    var lines = File(".\\resources\\day02.data").readLines()

    var key: KeyPadNum = keyPad[5]!!
    for (line in lines) {
        key = key.findKey(line.trim())
        print("${key.num}")
    }
    println()

}