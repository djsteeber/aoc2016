import java.io.File
import java.util.*
import kotlin.comparisons.compareBy


/**
 * Created by dsteeber on 12/12/2016.
TODO Still need to complete this
You come upon a column of four floors that have     companion object {
val flyWeight = Flyweight<Node>()
}


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

data class BuildingItem(val id: Int, val name: String, val itemType: BuildingItem.ItemType) {
    enum class ItemType {
        GENERATOR,
        MICROCHIP
    }
}

/*
create a breadth first search with paring.  Need to determine if a state has been reached before, and if so, ignore the path
 */


//TODO:  Removing the buildingItem.copy as I these are immutable and should not really need another instantation
fun SortedSet<BuildingItem>.copy() = setOf<BuildingItem>().toSortedSet(compareBy { it.id }).let { copy ->
    forEachIndexed { i, buildingItem -> copy.add(buildingItem) }
    copy
}


fun Array<SortedSet<BuildingItem>>.copy() = Array(size) { get(it).copy() }

fun SortedSet<BuildingItem>.toStateString() = map { it.id }.fold(""){ total, b -> "$total $b".trim()}
fun Array<SortedSet<BuildingItem>>.toStateString() = map { it.toStateString() }.fold("") { total ,b -> "$total [$b]".trim()}

class Building(val floors: Array<SortedSet<BuildingItem>>, val elevator: SortedSet<BuildingItem>) {

    fun getState(currentFloors: List<SortedSet<BuildingItem>>, elevatorFloor: Int = 0): String {
        // need to sort the floors
        return "{ elevator: $elevatorFloor, currentFloors: $currentFloors }"
    }

    fun countMoves(endState: String): Int {
        var map: MutableMap<String, Int> = mutableMapOf()

        return 0
    }
}


fun createFloors(initialState: Boolean = true, test: Boolean = false): Array<SortedSet<BuildingItem>> {
    val rtn =
    if (initialState) {
        if (test) {
            arrayOf(
                    mutableSetOf(
                            BuildingItem(1, "H", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(2,"L", BuildingItem.ItemType.MICROCHIP)
                    ).toSortedSet(compareBy { it.id }),
                    mutableSetOf(
                            BuildingItem(3,"H", BuildingItem.ItemType.GENERATOR)
                    ).toSortedSet(compareBy { it.id }),
                    mutableSetOf(
                            BuildingItem(4,"L", BuildingItem.ItemType.GENERATOR)
                    ).toSortedSet(compareBy { it.id }),
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id })
            )
        } else {
            arrayOf(
                    mutableSetOf(
                            BuildingItem(1, "polonium", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(2, "thulium", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(3,"promethium", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(4,"ruthenium", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(5,"cobolt", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(6,"cobolt", BuildingItem.ItemType.MICROCHIP)
                    ).toSortedSet(compareBy { it.id }),
                    mutableSetOf(
                            BuildingItem(8,"polonium", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(7,"promethium", BuildingItem.ItemType.MICROCHIP)
                    ).toSortedSet(compareBy { it.id }),
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id }),
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id })
            )
        }
    } else {
        if (test) {
            arrayOf(
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id }),
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id }),
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id }),
                    mutableSetOf(
                            BuildingItem(1, "H", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(2,"L", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(3,"H", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(4,"L", BuildingItem.ItemType.GENERATOR)
                    ).toSortedSet(compareBy { it.id })
            )

        } else {
            arrayOf(
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id }),
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id }),
                    mutableSetOf<BuildingItem>().toSortedSet(compareBy { it.id }),
                    mutableSetOf(
                            BuildingItem(1, "polonium", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(2, "thulium", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(3,"promethium", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(4,"ruthenium", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(5,"cobolt", BuildingItem.ItemType.GENERATOR),
                            BuildingItem(6,"cobolt", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(7,"polonium", BuildingItem.ItemType.MICROCHIP),
                            BuildingItem(8,"promethium", BuildingItem.ItemType.MICROCHIP))
                            .toSortedSet(compareBy { it.id })
            )
        }
    }
    return rtn
}

fun main(args: Array<String>) {
    println("day11")
    val start = System.currentTimeMillis()

    val floors = createFloors(initialState = true, test = true)
    val elevator = sortedSetOf<BuildingItem>().toSortedSet(compareBy { it.id })

    val building = Building(floors, elevator)

    val endState = createFloors(initialState = false, test = true).toStateString()
    val count = building.countMoves(endState)


    val floor2 = floors.copy()

    println("current state")
    println(floors.toStateString())
    println("desired state")
    println(endState)
    println("took $count moves")

    val end = System.currentTimeMillis()
    println("run time: ${end - start}ms")
}