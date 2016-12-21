import java.io.File
import java.util.*

/**
 * Created by dsteeber on 12/7/2016.
 */


/*
While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7,
of course; IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).

An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character sequence
which consists of a pair of two different characters followed by the reverse of that pair, such as xyyx or abba.
However, the IP also must not have an ABBA within any hypernet sequences, which are contained by square brackets.

For example:

abba[mnop]qrst supports TLS (abba outside square brackets).
abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
How many IPs in your puzzle input support TLS?
 */


fun String.isBABOf(str: String): Boolean {
    if (this.length != 3) return false
    if (str.length != 3) return false

    return ((this[0] == str[1]) && (str[1] == this[2]) && (str[0] == this[1]) && (this[1] == str[2]))
}

class IPV7Addr(val ipStr: String) {

    private fun isABBA(str: String): Boolean {
        var chars = str.toCharArray()

        if (chars.size < 4) return false

        for (i in 0..chars.size-4) {
            if ((chars[i] != chars[i+1]) && (chars[i] == chars[i+3]) && (chars[i+1] == chars[i+2])) return true
        }
        return false
    }

    private fun getABAList(str: String): List<String> {
        var returnList = ArrayList<String>()

        var chars = str.toCharArray()

        if (chars.size < 3) return returnList

        for (i in 0..chars.size-3) {
            if ((chars[i] != chars[i+1]) && (chars[i] == chars[i+2])) {
                returnList.add("" + chars[i] + chars[i+1] + chars[i+2])
            }
        }
        return returnList
    }


    fun supportsSSL(): Boolean {

        var abaList = ArrayList<String>()
        var babList = ArrayList<String>()

        var rtnVal = false

        var st = StringTokenizer(ipStr, "[]", true)

        var hypernetSeq = false

        while (st.hasMoreTokens()) {
            var token: String = st.nextToken()
            if (token == "[") {
                hypernetSeq = true
            } else if (token == "]"){
                hypernetSeq = false
            } else {
                var lst = getABAList(token)
                if (hypernetSeq) {
                    babList.addAll(lst)
                } else {
                    abaList.addAll(lst)
                }
            }
        }

        if (abaList.isEmpty()) return false
        if (babList.isEmpty()) return false

        for (aba in abaList) {
            for (bab in babList) {
                if (bab.isBABOf(aba)) {
                    return true
                }
            }
        }

        return false
    }

    fun supportsTLS(): Boolean {

        var rtnVal = false

        var st = StringTokenizer(ipStr, "[]", true)

        var hypernetSeq = false

        while (st.hasMoreTokens()) {
            var token: String = st.nextToken()
            if (token == "[") {
                hypernetSeq = true
            } else if (token == "]"){
                hypernetSeq = false
            } else {
                var abba = isABBA(token)
                // break on the first hypernetSeq that is abba
                if (hypernetSeq && abba) return false
                rtnVal = rtnVal || abba
            }
        }

        return rtnVal
    }
}


fun main(args: Array<String>) {
    val lines = File(".\\resources\\day07.data").readLines()

    val tls = lines.filter{IPV7Addr(it).supportsTLS()}
    println("TLS count = ${tls.size}")

    val ssl = lines.filter{IPV7Addr(it).supportsSSL()}
    println("SSL count = ${ssl.size}")

/*

    for (line in lines) {
        var ip7 = IPV7Addr(line)
        println("$line TLS:  ${ip7.supportsTLS()}")
    }
    */
}