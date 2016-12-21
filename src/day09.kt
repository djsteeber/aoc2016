import java.io.File
import java.util.ArrayList
import java.util.LinkedList
import java.util.StringTokenizer

/**
 * Created by dsteeber on 12/9/2016.
 * s
 */

/*
Wandering around a secure area, you come across a datalink port to a new part of the network.
After briefly scanning it for interesting files, you find one file in particular that catches your attention.
It's compressed with an experimental format, but fortunately, the documentation for the format is nearby.

The format compresses a sequence of characters. Whitespace is ignored.
To indicate that some sequence should be repeated, a marker is added to the file, like (10x2).
To decompress this marker, take the subsequent 10 characters and repeat them 2 times.
Then, continue reading the file after the repeated data. The marker itself is not included in the decompressed output.

If parentheses or other characters appear within the data referenced by a marker,
that's okay - treat it like normal data, not a marker, and then resume looking for markers
after the decompressed section.

For example:

ADVENT contains no markers and decompresses to itself with no changes, resulting in a decompressed length of 6.
A(1x5)BC repeats only the B a total of 5 times, becoming ABBBBBC for a decompressed length of 7.
(3x3)XYZ becomes XYZXYZXYZ for a decompressed length of 9.
A(2x2)BCD(2x2)EFG doubles the BC and EF, becoming ABCBCDEFEFG for a decompressed length of 11.
(6x1)(1x3)A simply becomes (1x3)A - the (1x3) looks like a marker, but because it's within a data section
of another marker, it is not treated any differently from the A that comes after it. It has a decompressed length of 6.
X(8x2)(3x3)ABCY becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18), because the decompressed data
from the (8x2) marker (the (3x3)ABC) is skipped and not processed further.
What is the decompressed length of the file (your puzzle input)? Don't count whitespace.
 */


open class Token(val value: String) {
    override fun toString(): String {
        return value
    }

    open fun size(): Int {
        return value.length
    }
}


class MarkerToken(val width: Int, val repeatCount: Int, value: String) : Token(value) {


    override fun toString(): String {
        val str = value.substring(0..width - 1).repeat(repeatCount) + value.substring(width)

        return str
    }

    override fun size(): Int {
        return 0
    }
}


@Suppress("NAME_SHADOWING")
class EBTokenizer {

    // the parts alternate between marker and non marker.
    //Need to check the length of the string after the marker is long enough to satisfy the decompression.
    // if not, the then current part and the next part need to be combined
    fun parse(str: String, cascadingMarkers: Boolean = false): List<Token> {

        val tokens = ArrayList<Token>()

        val st = StringTokenizer(str, "(x)", true)

        while (st.hasMoreTokens()) {
            val tkn = st.nextToken()

            if (tkn == "(") {
                val width = st.nextToken().toInt()
                st.nextToken()  // ingore the
                val repeatCount = st.nextToken().toInt()
                st.nextToken()  // ignore the )
                if (!cascadingMarkers) {
                    var value = st.nextToken()

                    while (width > value.length) {
                        value += st.nextToken()
                    }

                    tokens.add(MarkerToken(width, repeatCount, value))
                }
            } else {
                tokens.add(Token(tkn))
            }
        }
        return tokens
    }

    fun computeDecryptedSize(data: String, cascadingMarkers: Boolean = false): Int {

        var decyptedSize = 0
        val stack = Stack<MarkerToken>()

        val st = StringTokenizer(data, "(x)", true)

        while (st.hasMoreTokens()) {
            val tkn = st.nextToken()

            if (tkn == "(") {
                val width = st.nextToken().toInt()
                st.nextToken()  // ingore the
                val repeatCount = st.nextToken().toInt()
                st.nextToken()  // ignore the )
                if (cascadingMarkers) {
                    stack.push(MarkerToken(width, repeatCount, ""))
                } else {
                    var value = st.nextToken()

                    while (width > value.length) {
                        value += st.nextToken()
                    }

                    decyptedSize += MarkerToken(width, repeatCount, value).toString().length

                }
            } else {
                var str = tkn
                while (!stack.isEmpty()) {
                    var markerToken = stack.pop()

                    markerToken = MarkerToken(markerToken.width, markerToken.repeatCount, str)
                    str = markerToken.toString()

                }
                decyptedSize += str.length

            }
        }
        return decyptedSize
    }


    fun computeDecryptedSizeV2(data: String, cascadingMarkers: Boolean = false): Int {

        var decyptedSize = 0
        val stack = Stack<Token>()

        val st = StringTokenizer(data, "(x)", true)

        while (st.hasMoreTokens()) {
            val tkn = st.nextToken()

            if (tkn == "(") {
                val width = st.nextToken().toInt()
                st.nextToken()  // ingore the
                val repeatCount = st.nextToken().toInt()
                st.nextToken()  // ignore the )
                if (cascadingMarkers) {
                    stack.push(MarkerToken(width, repeatCount, ""))
                } else {
                    var value = st.nextToken()

                    while (width > value.length) {
                        value += st.nextToken()
                    }
                    stack.push(MarkerToken(width, repeatCount, value))
                }
            } else {
                stack.push(Token(tkn))
            }
        }

        //   X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY
        while (!stack.isEmpty()) {
            val token = stack.pop()
            if (token is MarkerToken) {
                decyptedSize += (token.width * (token.repeatCount - 1)) + token.value.length
            } else {
                decyptedSize += token.value.length
            }
        }


        return decyptedSize
    }

}


//   X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY
fun computeSize(line: String, offset: Int = 0, accumulator: Long = 0): Long {
    if (offset >= line.length) {
        return accumulator
    } else if (line[offset] == '(') {
        val endPoint = line.indexOf(')', offset)
        val xPoint = line.indexOf('x', offset)
        val width = line.substring((offset+1)..xPoint-1).toInt()
        val repeatCount = line.substring((xPoint+1)..endPoint-1).toInt()

        //println(" width = ${width}, repeatCount = ${repeatCount}")

        var subLine = line.substring(endPoint+1..endPoint+width)
//        val line = "X(8x2)(3x3)ABCY"  // should equal 20
        val subLineSize = computeSize(subLine) * repeatCount

        return computeSize(line, endPoint+width+1, accumulator + subLineSize)

    } else {
        val leftParenInx = line.indexOf('(', offset)
        if (leftParenInx == -1) {
            return accumulator + line.length.toLong() - offset.toLong()
        }

        return computeSize(line, leftParenInx, accumulator + (leftParenInx - offset))
    }

    return accumulator
}

fun main(args: Array<String>) {
    val line = File(".\\resources\\day09.data").readText()

    //val line = "X(8x2)(3x3)ABCY"  // should equal 20

    var size: Long = computeSize(line)

    println("size = ${size}")


/*
    val parser = EBTokenizer()
    val lst = parser.parse(line)

    var strList: List<String> = lst.map { it.toString() }

    var str = ""
    for (s in strList) {
        str += s
    }


    println("size of list = " + lst.size)
    println("lenth of the input = " + line.length)
    println("length of decompressed input = " + str.length)
    println("length of decompressed input (method 2) = " + parser.computeDecryptedSizeV2(line))

    println()

    println("length of decompressed input (method 2) cascade = " + parser.computeDecryptedSizeV2(line, true))
*/

}