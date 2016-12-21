import java.io.File
import java.util.*

/**
 * Created by dsteeber on 12/4/2016.
 */



fun String.aocDecode(): String {
    var map = HashMap<Char, Int>()

    for (c in this.toCharArray()) {
        if (map[c] == null) {
            map[c] = 1
        } else {
            map[c] = map[c]!! + 1
        }
    }

    var results: List<Pair<Char, Int>> = map.toList().sortedWith(Comparator { lhs, rhs ->
        when {
            lhs.second > rhs.second -> -1
            lhs.second < rhs.second -> 1
            else -> lhs.first.toString().compareTo(rhs.first.toString())
        }
    })

    var str = ""
    for (i in 0..4) {
        str = str + results[i].first
    }
    return str
}

class Room (encName: String) {

    val sector: Int
    val name: String
    val checkSum: String
    val fullName: String

    init {
        var parts = encName.split("-", "[", "]").filter({it != ""})

        checkSum = parts.last()
        parts = parts.dropLast(1)

        sector = parts.last().toInt()
        parts = parts.dropLast(1)

        fullName = parts.reduceRight { total, next -> total + next  }
        name = fullName.aocDecode()
    }

    override fun toString(): String {
        return "Room ( sector:${sector}, name:${name}, checkSum:${checkSum}) valid: ${isValid()}"
    }


    fun isValid(): Boolean {
        return checkSum == name
    }

    fun descyptedName(): String {
        var chars = fullName.toCharArray()

        val aVal = 'a'.toInt()

        var str: String = ""
        var e: Int = 0
        for (c in chars) {
            e = (((c.toInt() - aVal) + sector) % 26) + aVal
            str = str + e.toChar()
        }

        return str
    }

}

fun main(args: Array<String>) {
    val lines = File(".\\resources\\day04.data").readLines().map{it.trim()}

    var sum: Int = 0

    for (line in lines) {
        var r = Room(line)
        if (r.isValid()) {
            sum += r.sector
            println(r.descyptedName())
            println(Room(line))
        }

    }
    println("sum of sector = ${sum}")
}