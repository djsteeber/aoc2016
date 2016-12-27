/**
 * Created by dsteeber on 12/21/2016.
 */
package day19

import utils.Queue

/*
The Elves contact you over a highly secure emergency channel. Back at the North Pole,
the Elves are busy misunderstanding White Elephant parties.

Each Elf brings a present. They all sit in a circle, numbered starting with position 1.
Then, starting with the first Elf, they take turns stealing all the presents from the Elf to their left.
An Elf with no presents is removed from the circle and does not take turns.

For example, with five Elves (numbered 1 to 5):

  1
5   2
 4 3
Elf 1 takes Elf 2's present.
Elf 2 has no presents and is skipped.
Elf 3 takes Elf 4's present.
Elf 4 has no presents and is also skipped.
Elf 5 takes Elf 1's two presents.
Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
Elf 3 takes Elf 5's three presents.
So, with five Elves, the Elf that sits starting in position 3 gets all the presents.

With the number of Elves given in your puzzle input, which Elf gets all the presents?

Your puzzle input is 3014603.
 */



data class Elf(val num: Int, var numPresents: Int = 1)


fun whoIsLeft(numElves: Int) : Elf {
    var queue =  Queue<Elf>()
    (1..numElves).forEach { queue.enqueue(Elf(it)) }

    while (queue.size > 1) {
        val elf1 = queue.dequeue()!!
        val elf2 = queue.dequeue()!!

        elf1.numPresents += elf2.numPresents
        queue.enqueue(elf1)
    }

    return queue.dequeue()!!

}

fun whoIsLeftAcross(numElves: Int) : Elf {
    val queue =  Queue<Elf>()
    val queue2 = Queue<Elf>()
    val middle = (numElves / 2).toInt()

    (1..middle).forEach { queue.enqueue(Elf(it)) }
    (middle+1..numElves).forEach { queue2.enqueue(Elf(it)) }
    println("queue created")

    var totalSize = numElves
    while (totalSize > 1) {
        val elf1 = queue.dequeue()!!
        val elf2 = queue2.dequeue()!!

        elf1.numPresents += elf2.numPresents
        queue2.enqueue(elf1)

        if (queue2.size > queue.size + 1) {
            queue.enqueue(queue2.dequeue()!!)
        }
        totalSize--
    }

    return queue2.dequeue()!!
}



fun main(args: Array<String>) {
    println("day19")
    var queue =  Queue<Elf>()

    var numElves = 5
    var elf = whoIsLeft(numElves)
    println("With ${numElves} elves, ${elf} is the one you want to be when stealing left")

    elf = whoIsLeftAcross(numElves)
    println("With ${numElves} elves, ${elf} is the one you want to be when stealing across")

    numElves = 3014603
//    elf = whoIsLeft(numElves)
//    println("With ${numElves} elves, ${elf} is the one you want to be when stealing left")

    elf = whoIsLeftAcross(numElves)
    println("With ${numElves} elves, ${elf} is the one you want to be when stealing across")


}