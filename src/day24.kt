/**
 * Created by dsteeber on 12/21/2016.
 */
package day24

import utils.Flyweight
import utils.Queue
import java.io.File



// UGLY, need to fix this.

// answer is 502 and if you return to zero it is 724, different path
/*
You've finally met your match; the doors that provide access to the roof are locked tight,
and all of the controls and related electronics are inaccessible. You simply can't reach them.

The robot that cleans the air ducts, however, can.

It's not a very fast little robot, but you reconfigure it to be able to interface with some of the exposed wires
that have been routed through the HVAC system. If you can direct it to each of those locations, you should be able
to bypass the security controls.

You extract the duct layout for this area from some blueprints you acquired and create a map
with the relevant locations marked (your puzzle input).
0 is your current location, from which the cleaning robot embarks; the other numbers are (in no particular order)
the locations the robot needs to visit at least once each. Walls are marked as #, and open passages are marked as ..
Numbers behave like open passages.

For example, suppose you have a map like the following:

###########
#0.1.....2#
#.#######.#
#4.......3#
###########
To reach all of the points of interest as quickly as possible, you would have the robot take the following path:
0 to 4 (2 steps)
4 to 1 (4 steps; it can't move diagonally)
1 to 2 (6 steps)
2 to 3 (2 steps)
Since the robot isn't very fast, you need to find it the shortest route. This path is the fewest steps
(in the above example, a total of 14) required to start at 0 and then visit every other location at least once.

Given your actual map, and starting from location 0, what is the fewest number of steps required
to visit every non-0 number marked on the map at least once?
 */


class Coordinate(val x: Int, val y: Int) {
    fun up(): Coordinate = Coordinate(x,y-1)
    fun down(): Coordinate = Coordinate(x,y+1)
    fun left(): Coordinate = Coordinate(x-1,y)
    fun right(): Coordinate = Coordinate(x+1,y)

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        return when(other) {
            null -> { false }
            is Coordinate -> {
                ((this.x == other.x) && (this.y == other.y))
            }
            else -> { false }
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}


fun List<CharArray>.find(c: Char): Coordinate? {
    var y: Int = 0
    this.forEach {
        val x = it.indexOfFirst { it == c }
        if (x != -1) {
            return Coordinate(x, y)
        } else {
            y++
        }
    }
    return null
}

operator fun List<CharArray>.get(c: Coordinate): Char {
    return this[c.y][c.x]
}


class Node(val mapRepresentation: List<CharArray>, val location: Coordinate, val symbol: Char,var parent: Node? = null) {

    var distance: Int = 0

    fun isWall(): Boolean = (symbol == '#')
    fun isOpenSpace(): Boolean = (symbol == '.')
    fun isNumberedSpace(): Boolean = ((!isWall()) && (! isOpenSpace()))

    companion object {
        val flyWeight = Flyweight<Node>()
    }

    /**
     * assume the map has walls at all of the boarders.  If not you would need to make sure x and y are withing bounds
     */
    fun getNeighbors(): List<Node> {
        val adjCoords: Array<Coordinate> = arrayOf(location.up(),location.right(),location.down(),location.left())

        val rtn: List<Node> = adjCoords.map{ flyWeight.fetch(Node(mapRepresentation, it, mapRepresentation[it], this))}.filter(Node::isValidNeighbor)

        return rtn
    }


    private fun isValidNeighbor(): Boolean {
        if (mapRepresentation.isEmpty()) return false
        if ((location.x < 0) && (location.y < 0)) return false
        if ((location.y >= mapRepresentation.size) && (location.x >= mapRepresentation[0].size)) return false

        if ((parent != null) && (parent!!.parent == this)) return false

        if (isWall()) return false
        //if (symbol == '0') return false

        //if (isNumberedSpace() && (symbol.toString().toInt() > 4)) return false

        return true
    }

    fun clearDistance() {
        distance = 0
        flyWeight.clear()
        parent = null
    }

    override fun toString(): String {
        return "Node: [ location: $location, symbol: $symbol, distance $distance]"
    }

    operator override fun equals(other: Any?): Boolean {
        val rtn =
        when (other) {
            null -> { false}
            is Node -> { location == other.location}
            else -> { false }
        }
        return rtn

    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

}


data class NodeDistance(val from: Char, val to: Char, val distance: Int)

class FloorGrid(data: List<String>) {
    val mapRepresentation: List<CharArray> = data.map{ it.toCharArray()}


    fun findDistance(startSymbol: Char, endSynmbol: Char): Int {

        val startCoord = mapRepresentation.find(startSymbol)
        val endCoord = mapRepresentation.find(endSynmbol)

        if ((startCoord == null ) || (endCoord == null)) {
            return -1
        }
        val start = Node.flyWeight.fetch(Node(mapRepresentation, startCoord, startSymbol))

        val distance: Int = -1
        val queue = Queue<Node>()

        start!!.clearDistance()
        queue.enqueue(start)

        while (queue.isNotEmpty()) {
            val current = queue.dequeue()!!

            if (current.symbol == endSynmbol) {
                return current.distance
            }

            // This is put in a else block because once you hit this node, it doesnt pay calculating beyound
            for (node in current.getNeighbors()) {
                if (node.distance == 0) {
                    node.distance = current.distance + 1

                    queue.enqueue(node)
                }
            }

        }

        return distance
    }

    fun findMinDistance(): Int? {
        val set = findAllDistances()

        val pointCount = set.map{it.from}.distinct().count()

        return calcTotalDistance('0', set, pointCount)
    }

    fun findAllDistances(): Set<NodeDistance> {

        val set: MutableSet<NodeDistance> = mutableSetOf()

        for (i in 0..9) {
            val iChar = i.toString()[0]
            for (j in i+1..9) {
                val jChar = j.toString()[0]
                val distance = findDistance(iChar, jChar)
                if (distance > -1) {
                    println("$i -> $j = $distance")
                    set.add(NodeDistance(iChar, jChar, distance))
//                    if (i != 0) {
                        set.add(NodeDistance(jChar, iChar, distance))
//                    }
                }
            }
        }

        return set
    }
//> 490 < 574
    fun calcTotalDistance(symbol: Char, set: Set<NodeDistance>, pointCount: Int, pathTaken: String = "", accumDistance: Int = 0): Int? {

        if ((symbol != '0') && pathTaken.contains(symbol)) return null

        if (pathTaken.length == pointCount)
            println("calcTotalDisnance symbol=$symbol, pathTaken=$pathTaken, distance=$accumDistance")
        val children = set.filter{ (it.from == symbol) && (! pathTaken.contains(it.from.toString()))}

        if (pathTaken.length == pointCount) {
            return accumDistance
        } else if (children.isEmpty()) {
            return null
        }

        var minDistance = Int.MAX_VALUE
        for (child in children) {
            val distance = calcTotalDistance(child.to, set, pointCount, pathTaken + symbol.toChar(), accumDistance + child.distance)

            if ((distance != null) && (distance!! < minDistance)) {
                minDistance = distance!!
            }
        }

        return minDistance
    }

}


fun main(args: Array<String>) {
    println("day24")

    val data = File(".\\resources\\day24.data").readLines()

    val floorGrid = FloorGrid(data)

    val distance = floorGrid.findMinDistance()

    if (distance == null) {
        println("no path found")
    } else {
        println("min distance = ${distance!!}")
    }


}