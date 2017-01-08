/**
 * Created by dsteeber on 12/23/2016.
 */
package utils

import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

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


class Queue<T> {
    private val storage = LinkedList<T>()

    fun enqueue(item: T) {
        storage.add(item)
    }

    fun dequeue(): T?  = storage.removeFirst()

    fun remove(inx: Int) : T? = storage.removeAt(inx)

    fun peek(inx: Int): T? = storage[inx]

    val size: Int
        get() {
            return storage.size
        }

    fun isEmpty(): Boolean = (size == 0)
    fun isNotEmpty(): Boolean = (size > 0)

}

class Stack<T>() {
    private val storage = LinkedList<T>()

    val size: Int
        get() {
            return storage.size
        }

    fun push(value: T) {
        storage.addLast(value)
    }

    fun isEmpty(): Boolean = (size == 0)
    fun isNotEmpty(): Boolean = (size > 0)

    fun pop(): T? {
        var rtn: T? = null

        if (! isEmpty()) {
            rtn = storage.removeLast()
        }
        return rtn
    }
}

class Flyweight<T>() {

    private var cache: MutableSet<T> = mutableSetOf()

    fun fetch(item: T) : T {
//        if (cache.contains(item)) {
//            return cache.find { it == item }!!
//        }
        val b = cache.add(item)

//        return item
        if (b) {
            return item
        }
        return cache.find { it == item }!!

    }

    fun clear() {
        cache.clear()
    }

    fun contains(item: T) = cache.contains(item)

}
