import java.io.File

/**
 * Created by dsteeber on 12/8/2016.
 */

/*
You come across a door implementing what you can only assume is an implementation of two-factor authentication after a long game of requirements telephone.

To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.

Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it works. Now you just have to work out what the screen would have displayed.

The magnetic strip on the card you swiped encodes a series of instructions for the screen;
these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off,
 and is capable of three somewhat peculiar operations:

rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels.
Pixels that would fall off the right end appear at the left end of the row.
rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels.
Pixels that would fall off the bottom appear at the top of the column.
For example, here is a simple sequence on a smaller screen:

rect 3x2 creates a small rectangle in the top-left corner:

###....
###....
.......
rotate column x=1 by 1 rotates the second column down by one pixel:

#.#....
###....
.#.....
rotate row y=0 by 4 rotates the top row right by four pixels:

....#.#
###....
.#.....
rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to wrap back to the top:

.#..#.#
#.#....
.#.....
As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen market. That's what the advertisement on the back of the display tries to convince you, anyway.

There seems to be an intermediate check of the voltage used by the display: after you swipe your card, if the screen did work, how many pixels should be lit?

To begin, get your puzzle input.
 */


/*
Internal notes:  Would use a lexxer and BNF to describe language
Have classes that are instructions and the instructions modify the screen
 */

fun BooleanArray.rotate(count: Int) {
    var iterationCount = count
    while (iterationCount > 0) {
        var b = this[size-1]
        for (inx in size-1 downTo 1) {
            this[inx] = this[inx-1]
        }
        this[0] = b
        iterationCount--
    }
}

fun BooleanArray.countTrue(): Int {
    return this.filter( {it} ).size
}

class Screen(val width: Int, val height: Int) {

    var pixels = Array(height, {BooleanArray(width)})


    fun rect(width: Int, height: Int) {
        for (x in 0..height-1) {
            var row = pixels[x]
            for (y in 0..width-1) {
                row[y] = true
            }
        }
    }

    fun rotateRow(rowInx: Int, count: Int) {
        // shift all pixels in rowInx over by the count
        /*
        var tempRow = BooleanArray(width)
        var row = pixels[rowInx]
        for (inx in 0..width-count-1) {
            tempRow[inx+count] = row[inx]
        }
        pixels[rowInx] = tempRow
        */
        pixels[rowInx].rotate(count)
    }



    fun rotateColumn(colInx: Int, count: Int) {
        var tempCol = BooleanArray(height)

        for (inx in 0..pixels.size-1) {
            tempCol[inx] = pixels[inx][colInx]
        }

        tempCol.rotate(count)

        for (inx in 0..pixels.size-1) {
            pixels[inx][colInx] = tempCol[inx]
        }
    }

    fun processInstruction(instruction: String) {
        val parts = instruction.split(" ")

        if (parts.size == 0) return

        if (parts[0] == "rect") {
            val dims = parts[1].split('x').map({it.toInt()})
            rect(dims[0], dims[1])
        } else if (parts[0] == "rotate") {
            //rotate row y=0 by 3
            val inx = parts[2].split('=').last().toInt()
            val count = parts.last().toInt()
            if (parts[1] == "row") {
                rotateRow(inx, count)
            } else if (parts[1] == "column") {
                rotateColumn(inx, count)
            }
        }
    }

    fun countPixels(): Int {
        var count = 0

        for (row in pixels) {
            count += row.countTrue()
        }

        return count
    }

    override fun toString(): String {
        var str: String = ""
        for (row in pixels) {
            for (pixel in row) {
                if (pixel) {
                    str +=  "*"
                } else {
                    str += " "
                }
            }
            str += "\n"
        }
        return str
    }
}


fun test() {


    var screen = Screen(7,3)


    screen.processInstruction("rect 3x2")
    println(screen.toString())

    screen.processInstruction("rotate column x=1 by 1")
    println(screen.toString())

    screen.processInstruction("rotate row y=0 by 4")
    println(screen.toString())


    screen.processInstruction("rotate column x=1 by 1")
    println(screen.toString())


}

fun main(args: Array<String>) {
    var lines = File(".\\resources\\day08.data").readLines()
    var screen = Screen(50,6)

    for (line in lines) {
        screen.processInstruction(line)
    }

    println(screen)

    println(" pixel count = " + screen.countPixels().toString())

}