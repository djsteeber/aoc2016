import com.sun.xml.internal.fastinfoset.util.StringArray
import java.io.File
import java.util.*

/**
 * Created by dsteeber on 12/2/2016.
 */

data class KeyPadNum(val num: String) {

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

var keyPad: HashMap<String, KeyPadNum> = HashMap<String, KeyPadNum>()

/*
1 2 3
4 5 6
7 8 9
 */
fun HashMap<String, KeyPadNum>.buildKeyPad() {

    for (i in 1..9) {
        this[i.toString()] = KeyPadNum(i.toString())
    }
    this["1"]!!.setAdjacent(right = this["2"], down = this["4"])
    this["2"]!!.setAdjacent(left = this["1"], right = this["3"], down = this["5"])
    this["3"]!!.setAdjacent(left = this["2"], down = this["6"])
    this["4"]!!.setAdjacent(up = this["1"], right = this["5"], down = this["7"])
    this["5"]!!.setAdjacent(up = this["2"], down = this["8"], left = this["4"], right = this["6"])
    this["6"]!!.setAdjacent(up = this["3"], left = this["5"], down = this["9"])
    this["7"]!!.setAdjacent(up = this["4"], right = this["8"])
    this["8"]!!.setAdjacent(left = this["7"], up = this["5"], right = this["9"])
    this["9"]!!.setAdjacent(left = this["8"], up = this["6"])
}

/*
      1
    2 3 4
  5 6 7 8 9
    A B C
      D
 */
fun HashMap<String, KeyPadNum>.buildKeyPad2() {

    for (i in 1..9) {
        this[i.toString()] = KeyPadNum(i.toString())
    }
    for (a in 'A'..'D') {
        this[a.toString()] = KeyPadNum(a.toString())
    }

    this["1"]!!.setAdjacent(down = this["3"])
    this["2"]!!.setAdjacent(right = this["3"], down = this["6"])
    this["3"]!!.setAdjacent(up = this["1"], left = this["2"], right = this["4"], down = this["7"])
    this["4"]!!.setAdjacent(left = this["3"], down = this["8"])
    this["5"]!!.setAdjacent(right = this["6"])
    this["6"]!!.setAdjacent(up = this["2"], left = this["5"], down = this["A"], right = this["7"])
    this["7"]!!.setAdjacent(left = this["6"], up = this["3"], right = this["8"], down = this["B"])
    this["8"]!!.setAdjacent(left = this["7"], up = this["4"], right = this["9"], down = this["C"])
    this["9"]!!.setAdjacent(left = this["8"])

    this["A"]!!.setAdjacent(up = this["6"], right = this["B"])
    this["B"]!!.setAdjacent(left = this["A"], up = this["7"], right = this["C"], down = this["D"])
    this["C"]!!.setAdjacent(left = this["B"], up = this["8"])
    this["D"]!!.setAdjacent(up = this["B"])
}

fun main(args: Array<String>) {
    var lines = File(".\\resources\\day02.data").readLines()

    var keyPad: HashMap<String, KeyPadNum> = HashMap<String, KeyPadNum>()
    keyPad.buildKeyPad()

    print(" code for 1-9 key pad = ")
    var key: KeyPadNum = keyPad["5"]!!
    for (line in lines) {
        key = key.findKey(line.trim())
        print("${key.num}")
    }
    println()

    var keyPad2: HashMap<String, KeyPadNum> = HashMap<String, KeyPadNum>()
    keyPad2.buildKeyPad2()

    print(" code for 1-D key pad = ")
    key = keyPad2["5"]!!
    for (line in lines) {
        key = key.findKey(line.trim())
        print("${key.num}")
    }
    println()



}