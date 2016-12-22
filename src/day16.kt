/**
 * Created by dsteeber on 12/21/2016.
 */
package day16


/*
You're done scanning this part of the network, but you've left traces of your presence. You need to overwrite some disks with random-looking data to cover your tracks and update the local security system with a new checksum for those disks.

For the data to not be suspicious, it needs to have certain properties; purely random data will be detected as tampering. To generate appropriate random data, you'll need to use a modified dragon curve.

Start with an appropriate initial state (your puzzle input). Then, so long as you don't have enough data yet to fill the disk, repeat the following steps:

Call the data you have at this point "a".
Make a copy of "a"; call this copy "b".
Reverse the order of the characters in "b".
In "b", replace all instances of 0 with 1 and all 1s with 0.
The resulting data is "a", then a single 0, then "b".
For example, after a single step of this process,

1 becomes 100.
0 becomes 001.
11111 becomes 11111000000.
111100001010 becomes 1111000010100101011110000.
Repeat these steps until you have enough data to fill the desired disk.

Once the data has been generated, you also need to create a checksum of that data. Calculate the checksum only for the data that fits on the disk, even if you generated more data than that in the previous step.

The checksum for some given data is created by considering each non-overlapping pair of characters in the input data. If the two characters match (00 or 11), the next checksum character is a 1. If the characters do not match (01 or 10), the next checksum character is a 0. This should produce a new string which is exactly half as long as the original. If the length of the checksum is even, repeat the process until you end up with a checksum with an odd length.

For example, suppose we want to fill a disk of length 12, and when we finally generate a string of at least length 12, the first 12 characters are 110010110100. To generate its checksum:

Consider each pair: 11, 00, 10, 11, 01, 00.
These are same, same, different, same, different, same, producing 110101.
The resulting string has length 6, which is even, so we repeat the process.
The pairs are 11 (same), 01 (different), 01 (different).
This produces the checksum 100, which has an odd length, so we stop.
Therefore, the checksum for 110010110100 is 100.

Combining all of these steps together, suppose you want to fill a disk of length 20 using an initial state of 10000:

Because 10000 is too short, we first use the modified dragon curve to make it longer.
After one round, it becomes 10000011110 (11 characters), still too short.
After two rounds, it becomes 10000011110010000111110 (23 characters), which is enough.
Since we only need 20, but we have 23, we get rid of all but the first 20 characters: 10000011110010000111.
Next, we start calculating the checksum; after one round, we have 0111110101, which 10 characters long (even), so we continue.
After two rounds, we have 01100, which is 5 characters long (odd), so we are done.
In this example, the correct checksum would therefore be 01100.

The first disk you have to fill has length 272. Using the initial state in your puzzle input, what is the correct checksum?

Your puzzle answer was 00000100100001100.


The second disk you have to fill has length 35651584. Again using the initial state in your puzzle input, what is the correct checksum for this disk?

Observations:
  initial version was done with recursion.  Might have worked but needed to make sure only reference to data was passed
  when generating random characters

  Decided to move from strings to MutableList<Char>  The conversion in and out of strings was killing the performance

  instead of truncating in genRandomChars checkSum could limit by the size number

  no need to convert genRandomChars into a string.
 */


// change to iterative
fun genRandomChars(str: String, size: Int): List<Char> {

    var ap: MutableList<Char> = str.toMutableList()

    while (ap.size < size) {
        val bp = ap.reversed().map {
            when (it) {
                '0' -> '1'
                '1' -> '0'
                else -> it
            }
        }
        ap.add('0')
        ap.addAll(bp)
    }

    if  (ap.size > size) {
        return ap.dropLast(ap.size - size)
    } else {
        return ap
    }
}

fun genCheckSum(charArray: List<Char>): List<Char> {

    if ((charArray.size %2) == 1) {
        error("I don't know what to do with an odd string")
        return emptyList()
    }

    var source: MutableList<Char> = mutableListOf()
    source.addAll(charArray)
    var checkSum: MutableList<Char> = mutableListOf()
    while ((checkSum.size % 2) == 0) {
        checkSum.clear()
        for (inx in 0..source.size-1 step 2) {
            if (source[inx] == source[inx+1]) {
                checkSum.add('1')
            } else {
                checkSum.add('0')
            }
        }
        source.clear()
        source.addAll(checkSum)
    }

    return checkSum
}


fun main(args: Array<String>) {
    println("day 16")

    var randomString = genRandomChars("1", 3)
    println(randomString)


    randomString = genRandomChars("0", 3)
    println(randomString)


    randomString = genRandomChars("11111", 11)
    println(randomString)

    randomString = genRandomChars("111100001010", 25)
    println(randomString)


    println(genCheckSum("110010110100".toList()))

    println(genCheckSum(genRandomChars("10000", 20)))

    val puzzleInput = "11011110011011101"
    val checkSum = genCheckSum(genRandomChars(puzzleInput, 272))
    val checkSumStr = checkSum.fold(""){ str, c -> str + c}
    println("puzzle answer is ${checkSumStr}")

    //part 2
    val checkSumPart2 = genCheckSum(genRandomChars(puzzleInput, 35651584))
    val checkSumPart2Str = checkSumPart2.fold(""){str, c -> str + c}

    println("puzzle answer for part 2 is ${checkSumPart2Str}")
}