/**
 * Created by dsteeber on 12/20/2016.
 */
package day15


/*
The halls open into an interior plaza containing a large kinetic sculpture. The sculpture is in a sealed enclosure
and seems to involve a set of identical spherical capsules that are carried to the top and allowed to bounce
through the maze of spinning pieces.

Part of the sculpture is even interactive! When a button is pressed, a capsule is dropped and tries to fall
through slots in a set of rotating discs to finally go through a little hole at the bottom
and come out of the sculpture. If any of the slots aren't aligned with the capsule as it passes, the capsule
bounces off the disc and soars away. You feel compelled to get one of those capsules.

The discs pause their motion each second and come in different sizes; they seem to each have
a fixed number of positions at which they stop. You decide to call the position with the slot 0,
and count up for each position it reaches next.

Furthermore, the discs are spaced out so that after you push the button, one second elapses
before the first disc is reached, and one second elapses as the capsule passes from one disc to the one below it.
So, if you push the button at time=100, then the capsule reaches the top disc at time=101, the second disc at time=102,
the third disc at time=103, and so on.

The button will only drop a capsule at an integer time - no fractional seconds allowed.

For example, at time=0, suppose you see the following arrangement:

Disc #1 has 5 positions; at time=0, it is at position 4.
Disc #2 has 2 positions; at time=0, it is at position 1.
If you press the button exactly at time=0, the capsule would start to fall; it would reach the first disc at time=1.
Since the first disc was at position 4 at time=0, by time=1 it has ticked one position forward. As a five-position disc,
the next position is 0, and the capsule falls through the slot.

Then, at time=2, the capsule reaches the second disc. The second disc has ticked forward two positions
at this point: it started at position 1, then continued to position 0, and finally ended up at position 1 again.
Because there's only a slot at position 0, the capsule bounces away.

If, however, you wait until time=5 to push the button, then when the capsule reaches each disc,
the first disc will have ticked forward 5+1 = 6 times (to position 0), and the second disc will have
ticked forward 5+2 = 7 times (also to position 0). In this case, the capsule would fall through the discs
and come out of the machine.

However, your situation has more than two discs; you've noted their positions in your puzzle input.
What is the first time you can press the button to get a capsule?
 */


class Disc(val num: Int, val slotNum: Int, val initialPosition: Int, val next: Disc? = null) {

    fun receive(time: Int): Boolean {
        // note add a second to the equation below because a second ticks from the time
        // the caller calls receive to the time the receive is processed.
        val slot = (initialPosition + time + 1) % slotNum

        if (slot != 0) return false
        if (next == null) return true

        return next.receive(time+1)
    }
}


fun main(args: Array<String>) {
    println("day 15")

    // disks
    val disks = Disc(1,7,0,Disc(2,13,0,Disc(3,3,2,Disc(4,5,2,Disc(5,17,0,Disc(6,19,7))))))

    var passThru = false
    var time = 0
    while (! passThru) {
        passThru = disks.receive(time)
        if (! passThru) {
            time++
        }
    }

    println("press button at time ${time}")

    val disks2 = Disc(1,7,0,Disc(2,13,0,Disc(3,3,2,Disc(4,5,2,Disc(5,17,0,Disc(6,19,7, Disc(7,11,0)))))))
    passThru = false
    time = 0
    while (! passThru) {
        passThru = disks2.receive(time)
        if (! passThru) {
            time++
        }
    }

    println("press button again at time ${time}")




    /*
       Disc #1 has 7 positions; at time=0, it is at position 0.
       Disc #2 has 13 positions; at time=0, it is at position 0.
       Disc #3 has 3 positions; at time=0, it is at position 2.
       Disc #4 has 5 positions; at time=0, it is at position 2.
       Disc #5 has 17 positions; at time=0, it is at position 0.
       Disc #6 has 19 positio ns; at time=0, it is at position 7.
       */
}