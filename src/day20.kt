/**
 * Created by dsteeber on 12/21/2016.
 */
package day20

import java.io.File

/*
You'd like to set up a small hidden computer here so you can use it to get back into the network later.
However, the corporate firewall only allows communication with certain external IP addresses.

You've retrieved the list of blocked IPs from the firewall, but the list seems to be messy and poorly maintained,
 and it's not clear which IPs are allowed. Also, rather than being written in dot-decimal notation,
 they are written as plain 32-bit integers, which can have any value from 0 through 4294967295, inclusive.

For example, suppose only the values 0 through 9 were valid, and that you retrieved the following blacklist:

5-8
0-2
4-7
The blacklist specifies ranges of IPs (inclusive of both the start and end value) that are not allowed.
Then, the only IPs that this firewall allows are 3 and 9, since those are the only numbers not in any range.

Given the list of blocked IPs you retrieved from the firewall (your puzzle input),
what is the lowest-valued IP that is not blocked?

 */


data class IP32BitRange(val low: Long, val high: Long)


fun main(args: Array<String>) {

    val lines = File(".\\resources\\day20.data").readLines()
    val ipRanges: MutableList<IP32BitRange> = mutableListOf()

    for (line in lines) {
        val lowHigh = line.split("-")
        ipRanges.add(IP32BitRange(lowHigh[0].toLong(), lowHigh[1].toLong()))
    }

    ipRanges.sortBy{ it.low }

    var range = IP32BitRange(ipRanges[0].low,ipRanges[0].high)

    // compress the ranges until you find one that is not in it
    var ipCount: Long = 0
    for (inx in 1..ipRanges.size-1) {
        if (ipRanges[inx].low > range.high+1) {
            println("found a range ${ipRanges[inx]}")
            println("current range ${range}")
            println("lowest non blocked IP = ${range.high + 1}")
            ipCount += ipRanges[inx].low - range.high - 1
            println(" count = ${ipCount}")

            if (ipRanges[inx].high > range.high) {
                range = IP32BitRange(range.low, ipRanges[inx].high)
            }

            // part one
            //break
        } else {
            if (ipRanges[inx].high > range.high) {
                range = IP32BitRange(range.low, ipRanges[inx].high)
            }
        }
    }


}