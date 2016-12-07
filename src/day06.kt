import java.io.File
import java.util.*


/**
 * Created by dsteeber on 12/6/2016.
 */


/*
Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed,
 and protocol in situations like this is to switch to a simple repetition code to get the message through.

In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your puzzle input),
 but the data seems quite corrupted - almost too badly to recover. Almost.

All you need to do is figure out which character is most frequent for each position.
For example, suppose you had recorded the following messages:

eedadn
drvtee
eandsr
raavrd
atevrs
tsrnev
sdttsa
rasrtv
nssdts
ntnada
svetve
tesnvt
vntsnd
vrdear
dvrsen
enarar
The most common character in the first column is e; in the second, a; in the third, s, and so on.
Combining these characters returns the error-corrected message, easter.

Given the recording in your puzzle input, what is the error-corrected version of the message being sent?

 */


class CharacterCollector {
    var map = HashMap<Char, Int>()

    fun collect(c: Char) {
        if (map[c] == null) {
            map[c] = 1
        } else {
            map[c] = map[c]!! + 1
        }
    }

    fun mostCommon(): Char {
        if (map.isEmpty()) {
            return ' '
        }

        var results: List<Pair<Char, Int>> = map.toList().sortedWith(Comparator { lhs, rhs ->
            when {
                lhs.second > rhs.second -> -1
                lhs.second < rhs.second -> 1
                else -> lhs.first.toString().compareTo(rhs.first.toString())
            }
        })


        return results.first().first
    }

    fun leastCommon(): Char {
        if (map.isEmpty()) {
            return ' '
        }

        var results: List<Pair<Char, Int>> = map.toList().sortedWith(Comparator { lhs, rhs ->
            when {
                lhs.second > rhs.second -> 1
                lhs.second < rhs.second -> -1
                else -> lhs.first.toString().compareTo(rhs.first.toString())
            }
        })


        return results.first().first
    }
}

fun ArrayList<CharacterCollector>.mostCommon(): String {
    var str: String = ""

    for (col in this) {
        str += col.mostCommon()
    }

    return str
}

fun ArrayList<CharacterCollector>.leastCommon(): String {
    var str: String = ""

    for (col in this) {
        str += col.leastCommon()
    }

    return str
}

fun ArrayList<CharacterCollector>.collect(str: String) {
    var inx = 0
    for (c in str.toCharArray()) {
        if (inx < this.size) {
            this[inx].collect(c)
        }
        inx++
    }
}

fun main(args: Array<String>) {
    val lines: List<String> = File(".\\resources\\day06.data").readLines()

    if (lines.size == 0) {
        return
    }

    val width = lines.first().length

    val collectors: ArrayList<CharacterCollector> = arrayListOf(CharacterCollector())
    // start with 2 because 1 is already in the array
    for (i in 2..width) {
        collectors.add(CharacterCollector())
    }

    for (line in lines) {
        collectors.collect(line)
    }

    println("Most common  = " + collectors.mostCommon())
    println("Least common = " + collectors.leastCommon())
}