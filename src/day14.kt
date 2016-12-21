/**
 * Created by dsteeber on 12/20/2016.
 */
package day14

import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

/*
once a set of three is found, the you need to check the rest of the strings for 5 of the same letter
i.e. if aaa is found then a string with aaaaa must be found to count
 */

class MD5Hasher() {
    private val d = MessageDigest.getInstance("MD5")

    fun hash(data: String): String {
        val ba = d.digest(data.toByteArray())

        val bigInt = BigInteger(1, ba)
        var hashText = bigInt.toString(16)

        while (hashText.length < 32) {
            hashText = "0" + hashText
        }
        return hashText
    }
}
fun String.findRepeatingCharIndex(size: Int): Int? {
    if (size > length) {
        return null
    }

    for (i in 0..this.length-size) {
        val c = this[i]
        val found = (1..size-1).none { c != this[i + it] }
        if (found) {
            return i
        }
    }
    return null
}


class Queue<T> {
    private val storage = LinkedList<T>()

    fun enqueue(item: T) {
        storage.add(item)
    }

    fun dequeue(): T?  = storage.removeFirst()

    val size: Int
        get() {
            return storage.size
        }

    fun isEmpty(): Boolean = (size == 0)

    fun peek(inx: Int): T? {
        if (inx >= storage.size) {
            return null
        }
        return storage[inx]
    }
}


class BunnyKeyGenerator(val salt: String) {
    val md5 = MD5Hasher()
    var counter = 0   //generateSequence(0, {it + 1})
    var md5List = Queue<String>()

    var index = -1

    init {
        fillHashList()
    }

    private fun fillHashList() {
        while (md5List.size < 1000) {
            md5List.enqueue(md5.hash(salt + counter.toString()))
            counter++
        }
    }

    fun getNextKey(): String? {
        var found = false
        var item: String? = null

        while (! found) {
            item = md5List.dequeue()!!
            index++
            fillHashList()
            val itemIndex = item.findRepeatingCharIndex(3)
            if (itemIndex != null) {
                val searchStr = item[itemIndex].toString().repeat(5)
                for (i in 1..1000) {
                    val lhItem = md5List.peek(i)
                    if ((lhItem != null) && (lhItem.contains(searchStr))) {
                        found = true
                        break
                    }
                }
            }
        }

        return item
    }
}


/*
take MD5 of a prearranged salt

 */

fun main(args: Array<String>) {
    val bkg = BunnyKeyGenerator("abc")

    for (i in 1..64) {
        val key = bkg.getNextKey()
        println(key)
    }

    println("index = ${bkg.index}")

    // test answer should be 39
}