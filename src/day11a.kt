package day11a

import utils.Flyweight
import utils.Queue
import java.util.SortedSet
import kotlin.comparisons.compareBy


/**
 * Created by dsteeber on 12/12/2016.

You come upon a column of four floors that have been entirely sealed off from the rest of the building except
for a small dedicated lobby. There are some radiation warnings and a big sign which reads
"Radioisotope Testing Facility".

According to the project status board, this facility is currently being used to experiment
with Radioisotope Thermoelectric Generators (RTGs, or simply "generators") that are designed to be paired
with specially-constructed microchips. Basically, an RTG is a highly radioactive rock
that generates electricity through heat.

The experimental RTGs have poor radiation containment, so they're dangerously radioactive.
The chips are prototypes and don't have normal radiation shielding, but they do have the ability to generate
an electromagnetic radiation shield when powered. Unfortunately, they can only be powered by their corresponding RTG.
An RTG powering a microchip is still dangerous to other microchips.

In other words, if a chip is ever left in the same area as another RTG, and it's not connected to its own RTG,
the chip will be fried. Therefore, it is assumed that you will follow procedure and keep chips connected
to their corresponding RTG when they're in the same room, and away from other RTGs otherwise.

These microchips sound very interesting and useful to your current activities, and you'd like to try to retrieve them.
The fourth floor of the facility has an assembling machine which can make a self-contained, shielded computer for you
to take with you - that is, if you can bring it all of the RTGs and microchips.

Within the radiation-shielded part of the facility (in which it's safe to have these pre-assembly RTGs),
there is an elevator that can move between the four floors. Its capacity rating means it can carry
at most yourself and two RTGs or microchips in any combination. (They're rigged to some
heavy diagnostic equipment - the assembling machine will detach it for you.) As a security measure,
the elevator will only function if it contains at least one RTG or microchip. The elevator always stops
on each floor to recharge, and this takes long enough that the items within it and the items
on that floor can irradiate each other. (You can prevent this if a Microchip and its Generator end up
on the same floor in this way, as they can be connected while the elevator is recharging.)

You make some notes of the locations of each component of interest (your puzzle input).
Before you don a hazmat suit and start moving things around, you'd like to have an idea of what you need to do.

When you enter the containment area, you and the elevator will start on the first floor.

For example, suppose the isolated area has the following arrangement:

The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
The second floor contains a hydrogen generator.
The third floor contains a lithium generator.
The fourth floor contains nothing relevant.
As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for Lithium, M for Microchip,
and G for Generator), the initial state looks like this:

F4 .  .  .  .  .
F3 .  .  .  LG .
F2 .  HG .  .  .
F1 E  .  HM .  LM
Then, to get everything up to the assembling machine on the fourth floor, the following steps could be taken:

Bring the Hydrogen-compatible Microchip to the second floor, which is safe because it can get power
from the Hydrogen Generator:

F4 .  .  .  .  .
F3 .  .  .  LG .
F2 E  HG HM .  .
F1 .  .  .  .  LM
Bring both Hydrogen-related items to the third floor, which is safe
because the Hydrogen-compatible microchip is getting power from its generator:

F4 .  .  .  .  .
F3 E  HG HM LG .
F2 .  .  .  .  .
F1 .  .  .  .  LM
Leave the Hydrogen Generator on floor three, but bring the Hydrogen-compatible Microchip back down with you
so you can still use the elevator:

F4 .  .  .  .  .
F3 .  HG .  LG .
F2 E  .  HM .  .
F1 .  .  .  .  LM
At the first floor, grab the Lithium-compatible Microchip, which is safe because Microchips don't affect each other:

F4 .  .  .  .  .
F3 .  HG .  LG .
F2 .  .  .  .  .
F1 E  .  HM .  LM
Bring both Microchips up one floor, where there is nothing to fry them:

F4 .  .  .  .  .
F3 .  HG .  LG .
F2 E  .  HM .  LM
F1 .  .  .  .  .
Bring both Microchips up again to floor three, where they can be temporarily connected
to their corresponding generators while the elevator recharges, preventing either of them from being fried:

F4 .  .  .  .  .
F3 E  HG HM LG LM
F2 .  .  .  .  .
F1 .  .  .  .  .
Bring both Microchips to the fourth floor:

F4 E  .  HM .  LM
F3 .  HG .  LG .
F2 .  .  .  .  .
F1 .  .  .  .  .
Leave the Lithium-compatible microchip on the fourth floor, but bring the Hydrogen-compatible one
so you can still use the elevator; this is safe because although the Lithium Generator is on the destination floor,
you can connect Hydrogen-compatible microchip to the Hydrogen Generator there:

F4 .  .  .  .  LM
F3 E  HG HM LG .
F2 .  .  .  .  .
F1 .  .  .  .  .
Bring both Generators up to the fourth floor, which is safe because you can connect the Lithium-compatible Microchip
to the Lithium Generator upon arrival:

F4 E  HG .  LG LM
F3 .  .  HM .  .
F2 .  .  .  .  .
F1 .  .  .  .  .
Bring the Lithium Microchip with you to the third floor so you can use the elevator:

F4 .  HG .  LG .
F3 E  .  HM .  LM
F2 .  .  .  .  .
F1 .  .  .  .  .
Bring both Microchips to the fourth floor:

F4 E  HG HM LG LM
F3 .  .  .  .  .
F2 .  .  .  .  .
F1 .  .  .  .  .
In this arrangement, it takes 11 steps to collect all of the objects at the fourth floor for assembly.
Each elevator stop counts as one step, even if nothing is added to or removed from it.)

In your situation, what is the minimum number of steps required to bring all of the objects to the fourth floor?
 */

data class BuildingItem(val id: Int, val name: String, val itemType: BuildingItem.ItemType, var floorLocation: Int = 0): Comparable<BuildingItem> {
    enum class ItemType(val str: String) {
        GENERATOR("G"),
        MICROCHIP("M"),
        ELEVATOR("E");

        override fun toString(): String {
            return str
        }
    }

    override fun equals(other: Any?): Boolean {
        val rtn =
        when (other) {
            null -> { false }
            is BuildingItem -> { id == other.id}
            else -> { false }
        }
        return rtn
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun compareTo(other: BuildingItem): Int {
        if (this == other) return 0
        if (floorLocation < other.floorLocation) return -1
        if (floorLocation == other.floorLocation) return id.compareTo(other.id)

        return 1
        //return id.compareTo(other.id)
    }

    override fun toString(): String {
        return "$name-$itemType:$floorLocation"
    }
}

fun SortedSet<BuildingItem>.copy() = setOf<BuildingItem>().toSortedSet(compareBy { it.id }).let { copy ->
    forEachIndexed { i, buildingItem -> copy.add(buildingItem.copy()) }
    copy
}

fun SortedSet<BuildingItem>.getItem(itemType: BuildingItem.ItemType): BuildingItem = filter{ it.itemType == itemType}.first()

fun SortedSet<BuildingItem>.toDisplayString(): String = this.sortedBy { it.id }.map{ it.toString()}.reduce{ a,b -> a + " " + b}


fun equivalent(a: SortedSet<BuildingItem>, b: SortedSet<BuildingItem>): Boolean {
    var tmpB = b.toMutableSet()
    for (item in a) {
        tmpB.removeAll { (it.id == item.id) && (it.floorLocation == item.floorLocation) }
    }
    if (tmpB.isEmpty()) return true
/*
    if (tmpB.size != 2) return false

    var tmpA = a.filter { it.itemType != BuildingItem.ItemType.ELEVATOR }.toMutableSet()
    for (item in b.filter { it.itemType != BuildingItem.ItemType.ELEVATOR }) {
        tmpA.removeAll { (it.id == item.id) && (it.floorLocation == item.floorLocation) }
    }
    if (tmpA.size != 2) return false

    // check interchangability
    if ((tmpA.first().id == tmpB.first().id) && (tmpA.last().id == tmpB.last().id)) {
        val rtn =
            ((tmpA.first().floorLocation == tmpB.last().floorLocation)
                && (tmpA.last().floorLocation == tmpB.first().floorLocation))
        //if (rtn) println("thowing out equivalend")
        return rtn
    } else if ((tmpA.first().id == tmpB.last().id) && (tmpA.last().id == tmpB.first().id)) {
        val rtn = ((tmpA.first().floorLocation == tmpB.first().floorLocation)
                && (tmpA.last().floorLocation == tmpB.last().floorLocation))
        //if (rtn) println("thowing out equivalend")
        return rtn
    }
*/
    return false
}

class BFSNode(val items: SortedSet<BuildingItem>, var parent: BFSNode? = null, var distance: Int = 0) {

    companion object {
        val flyWeight = Flyweight<BFSNode>()
    }

    val  NUMBER_OF_FLOORS = 4

    // only need to check the 2 floors affected
    fun isValid(value: SortedSet<BuildingItem>, floors: IntArray): Boolean {

        for (i in floors) {
            var chips = value.filter { (it.floorLocation == i) && (it.itemType == BuildingItem.ItemType.MICROCHIP) }
            val generators = value.filter { (it.floorLocation == i) && (it.itemType == BuildingItem.ItemType.GENERATOR) }

            for (generator in generators) {
                // keep the chips that do not match the generator
                chips = chips.filter { it.name != generator.name }
            }

            // if there are chips left and there are generators then it is fried
            if (chips.isNotEmpty() && generators.isNotEmpty()) {
                return false
            }
        }

        return true
    }

    fun isValidSet(value: SortedSet<BuildingItem>): Boolean {
        var chips = value.filter {  (it.itemType == BuildingItem.ItemType.MICROCHIP) }
        var chips2 = value.filter {  (it.itemType == BuildingItem.ItemType.MICROCHIP) }
        var generators = value.filter { (it.itemType == BuildingItem.ItemType.GENERATOR) }
        val generators2 = value.filter { (it.itemType == BuildingItem.ItemType.GENERATOR) }


        for (generator in generators2) {
            // keep the chips that do not match the generator
            chips = chips.filter { it.name != generator.name }
        }

        for (chip in chips2) {
            generators = generators.filter { it.name != chip.name }
        }

        // if there are chips left and there are generators then it is fried
        if (chips.isNotEmpty() && generators.isNotEmpty()) {
            return false
        }

        return true
    }

    private fun isValidElevatorFloor(num: Int): Boolean = ((num >= 1) && (num <= 4))

    private fun getNeighbors(nextFloorInc: Int, itemCount: Int = 1): Set<BFSNode> {
        val list: MutableSet<BFSNode> = mutableSetOf()
        val nextElevatorFloor = items.getItem(BuildingItem.ItemType.ELEVATOR).floorLocation + nextFloorInc
        if (! isValidElevatorFloor(nextElevatorFloor)) return list

        val copyItems = items.copy()
        val elevatorFloor = copyItems.getItem(BuildingItem.ItemType.ELEVATOR).floorLocation
        val itemsOnThisFloor = copyItems.filter{ (it.id != 0) && (it.floorLocation == elevatorFloor)}.toSortedSet()
        if (itemsOnThisFloor.isEmpty()) {
            return emptySet()
        }
        val elevator = copyItems.filter{it.itemType == BuildingItem.ItemType.ELEVATOR}.first()
        elevator.floorLocation += nextFloorInc
        val itemsOnNextFloor = copyItems.filter{ (it.id != 0) && (it.floorLocation == nextElevatorFloor)}.toSortedSet()


        val floors = intArrayOf(elevatorFloor, nextElevatorFloor)
        // collect the single items first, but only if inc is down stairs

        if (itemCount == 1) {
            //possible items that can move
            var movableItems = itemsOnThisFloor.filter { outter -> isValidSet(itemsOnThisFloor.minus(outter).toSortedSet())}
            movableItems = movableItems.filter { outter -> isValidSet(itemsOnNextFloor.plus(outter).toSortedSet())}
            val chips = movableItems.filter {it.itemType == BuildingItem.ItemType.MICROCHIP}



            if (chips.size > 1) {
               movableItems = movableItems.filter { it.itemType == BuildingItem.ItemType.GENERATOR }.plus(chips.first())
            }

            for (item in movableItems) {
                item.floorLocation += nextFloorInc
               // if (isValid(copyItems, floors)) {
                    val x = flyWeight.fetch(BFSNode(copyItems.copy(), this))

                    if ((x.parent != null) && (x.parent!!.parent != this)) {
                        list.add(x)
                    }
                //}
                item.floorLocation -= nextFloorInc
            }
        } else {
            var lastItem: BuildingItem? = null

            for (item in itemsOnThisFloor) {
                if (lastItem != null) {
                    val valid = isValidSet(itemsOnThisFloor.minus(item).minus(lastItem!!).toSortedSet())
                    && isValidSet(itemsOnNextFloor.plus(item).plus(lastItem!!).toSortedSet())
                    if (valid) {
                        item.floorLocation += nextFloorInc
                        lastItem.floorLocation += nextFloorInc
                        val x = flyWeight.fetch(BFSNode(copyItems.copy(), this))

                        if ((x.parent != null) && (x.parent!!.parent != this)) {
                            list.add(x)
                        }

                        lastItem.floorLocation -= nextFloorInc
                        item.floorLocation -= nextFloorInc
                    }

                }
                lastItem = item
            }
        }

        return list
    }


    fun getNeighbors(): Set<BFSNode> {

        val list: MutableSet<BFSNode> = mutableSetOf()

        // check if any of the floors below you have any items.  If not, then ignore down
        val elevatorFloor = items.getItem(BuildingItem.ItemType.ELEVATOR).floorLocation

        if (elevatorFloor < 4) {
            var doubleItems = getNeighbors(1,2)
            if (doubleItems.isNotEmpty()) {
                list.addAll(doubleItems)
            } else {
                val singleItems = getNeighbors(1,1)
                if (singleItems.isNotEmpty()) {
                    list.addAll(singleItems)
                }
            }
        }
        if ((elevatorFloor > 1) && ( items.filter { it.floorLocation < elevatorFloor }.size > 0)) {
            var singleItems = getNeighbors(-1,1)
            if (singleItems.isNotEmpty()) {
                list.addAll(singleItems)
            } else {
                val doubleItems = getNeighbors(-1,2)
                if (doubleItems.isNotEmpty()) {
                    list.addAll(doubleItems)
                }
            }
        } else {
            if (elevatorFloor > 2)
                println("skipping neighbor down $elevatorFloor")
        }

        return list
    }

    operator override fun equals(other: Any?): Boolean {
        val rtn =
                when (other) {
                    null -> { false}
                    is BFSNode -> { equivalent(items, other.items) }
                    else -> { false }
                }
        return rtn

    }

    override fun hashCode(): Int {
        return items.toString().hashCode()
    }

}

fun countSteps(startState: SortedSet<BuildingItem>): Int {
    val queue = Queue<BFSNode>()

    queue.enqueue(BFSNode(startState))

    var lastDistance = -1
    var comparisons = 0
    var start = System.currentTimeMillis()
    while (queue.isNotEmpty()) {
        val current = queue.dequeue()!!

        comparisons++
        if (lastDistance != current.distance) {
            val timing = System.currentTimeMillis() - start
            println(" ${current.distance} time ${timing/1000.0}s $comparisons per comparison = ${timing/comparisons}ms")
            lastDistance = current.distance
            comparisons = 0
            start = System.currentTimeMillis()
        }
        //println("${current.items.toDisplayString()}  distance = ${current.distance}")

        if (current.items.size == current.items.filter { it.floorLocation == 4 }.size) {

            println()
            println("found the solution ")

            var c: BFSNode? = current
            while (c != null) {
                println(c.items.toDisplayString())

                c = c.parent
            }

            return current.distance
        }

        // get the neighbors
        for (node in current.getNeighbors()) {
            if (node.distance == 0) {
                node.distance = current.distance + 1

                queue.enqueue(node)
            }
        }
    }
    return -1
}

/**
 *
F4 .  .  .  .  .
F3 .  .  .  LG .
F2 .  HG .  .  .
F1 E  .  HM .  LM

 */

fun createItems(test: Boolean = false) : SortedSet<BuildingItem> {
    val rtn =
    if (test) {
        mutableSetOf(
                BuildingItem(0, "E", BuildingItem.ItemType.ELEVATOR,1),
                BuildingItem(1, "H", BuildingItem.ItemType.GENERATOR,2),
                BuildingItem(2, "H", BuildingItem.ItemType.MICROCHIP,1),
                BuildingItem(3, "L", BuildingItem.ItemType.GENERATOR,3),
                BuildingItem(4, "L", BuildingItem.ItemType.MICROCHIP,1)
        ).toSortedSet()
    } else {
        mutableSetOf(
                BuildingItem(0, "E", BuildingItem.ItemType.ELEVATOR,1),
                BuildingItem(1, "polonium", BuildingItem.ItemType.GENERATOR,1),
                BuildingItem(2, "thulium", BuildingItem.ItemType.GENERATOR,1),
                BuildingItem(3, "thulium", BuildingItem.ItemType.MICROCHIP,1),
                BuildingItem(4, "promethium", BuildingItem.ItemType.GENERATOR,1),
                BuildingItem(5, "ruthenium", BuildingItem.ItemType.GENERATOR,1),
                BuildingItem(6, "ruthenium", BuildingItem.ItemType.MICROCHIP,1),
                BuildingItem(7, "cobalt", BuildingItem.ItemType.GENERATOR,1),
                BuildingItem(8, "cobalt", BuildingItem.ItemType.MICROCHIP,1),
                BuildingItem(9, "polonium", BuildingItem.ItemType.MICROCHIP,2),
                BuildingItem(10, "promethium", BuildingItem.ItemType.MICROCHIP,2)
        ).toSortedSet()
/*
The first floor contains a polonium generator, a thulium generator, a thulium-compatible microchip, a promethium generator,
 a ruthenium generator, a ruthenium-compatible microchip, a cobalt generator, and a cobalt-compatible microchip.
The second floor contains a polonium-compatible microchip and a promethium-compatible microchip.
The third floor contains nothing relevant.
The fourth floor contains nothing relevant.
 */
    }

    return rtn
}

// formula is 2 * (n - 1) -1 to move n items up one floor
//TODO:  This runs forever  at count 14 and it is running at 216 seconds and growing.  Need to pair it down
//  change the algorithm to calc count to move up one floor, then the next, and calc the rest with lookup
fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val items = createItems(test = false)

    val steps = countSteps(items)

    println("steps = $steps")


    val timing = System.currentTimeMillis() - start

    println("runtime = $timing")


}