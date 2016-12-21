/**
 * Created by dsteeber on 12/5/2016.
 */

/*


You are faced with a security door designed by Easter Bunny engineers
that seem to have acquired most of their security knowledge by watching hacking movies.

The eight-character password for the door is generated one character at a time
by finding the MD5 hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).

A hash indicates the next character in the password if its hexadecimal representation starts with five zeroes.
If it does, the sixth character in the hash is the next character of the password.

For example, if the Door ID is abc:

The first index which produces a hash that starts with five zeroes is 3231929, which we find by hashing abc3231929;
the sixth character of the hash, and thus the first character of the password, is 1.
5017308 produces the next interesting hash, which starts with 000008f82...,
so the second character of the password is 8.
The third time a hash starts with five zeroes is for abc5278568, discovering the character f.
In this example, after continuing this search a total of eight times, the password is 18f47a30.

Given the actual Door ID, what is the password?

Your puzzle input is ugkcyxxp.

 */

import java.math.BigInteger
import java.security.*


class BunnyDecoder(val prefix: String) {
    private val EMPTY_CHAR: Char = '\u0000'
    private var counter = 0;
    private var d = MessageDigest.getInstance("MD5")

    fun reset() {
        counter = 0
    }

    private fun nextHash(): String {
        var hashText:String = "11111"
        while (! hashText.startsWith("00000")) {
            val code = prefix + counter.toString()
            counter++
            var ba = d.digest(code.toByteArray())

            var bigInt = BigInteger(1, ba)
            hashText = bigInt.toString(16)

            while (hashText.length < 32) {
                hashText = "0" + hashText
            }
        }
        return hashText
    }

    fun findCode(size: Int = 8): String {
        var str: String = ""
        for(i in 1..size) {
            str = str + nextHash()[5].toString()
        }
        return str;
    }

    fun findCode2(size: Int = 8): String {
        var result = CharArray(size)

        while (result.contains(EMPTY_CHAR)) {
            var code = nextHash()
            var posChar: Char = code[5]
            if ((posChar >= '0') && (posChar <= '7')) {
                var inx = posChar.toString().toInt()
                if (result[inx] == EMPTY_CHAR) {
                    result[inx] = code[6]
                }
            }
        }

        var rtnString = ""
        for (i in 0..result.size-1) {
            rtnString = rtnString + result[i]
        }

        return rtnString
    }
    override fun toString(): String {
        return "BunnyDecoder (prefix : {$prefix), counter: ${counter}"
    }

}

fun main(args: Array<String>) {
    var decoder = BunnyDecoder("ugkcyxxp")
    println(decoder.findCode(8))

    decoder.reset()
    var decodedMessage = decoder.findCode2(8)
    println(decodedMessage)
}
