/**
 * Created by dsteeber on 12/21/2016.
 */
package day22

import java.io.File

/*
You gain access to a massive storage cluster arranged in a grid; each storage node is only connected
to the four nodes directly adjacent to it (three if the node is on an edge, two if it's in a corner).

You can directly access data only on node /dev/grid/node-x0-y0, but you can perform some limited actions
on the other nodes:

You can get the disk usage of all nodes (via df). The result of doing this is in your puzzle input.
You can instruct a node to move (not copy) all of its data to an adjacent node
(if the destination node has enough space to receive the data). The sending node is left empty after this operation.
Nodes are named by their position: the node named node-x10-y10 is adjacent
to nodes node-x9-y10, node-x11-y10, node-x10-y9, and node-x10-y11.

Before you begin, you need to understand the arrangement of data on these nodes.
Even though you can only move data between directly connected nodes, you're going to need
to rearrange a lot of the data to get access to the data you need. Therefore, you need to work out how
you might be able to shift data around.

To do this, you'd like to count the number of viable pairs of nodes. A viable pair is any two nodes (A,B),
regardless of whether they are directly connected, such that:

Node A is not empty (its Used is not zero).
Nodes A and B are not the same node.
The data on node A (its Used) would fit on node B (its Avail).
How many viable pairs of nodes are there?

---Part 2----

 */


//Filesystem              Size  Used  Avail  Use%
//dev/grid/node-x0-y0     88T   67T    21T   76%



data class DevNode(val name: String, val size: Int, val used: Int, val avail: Int, val usedPct: Int) {
    fun canAcceptAllData(devNode: DevNode): Boolean {
        return (this.used <= devNode.avail) && (this.used > 0)
    }
}



class DevNodeMatrx(filePath: String) {
    private val devNodes: MutableList<MutableList<DevNode>> = mutableListOf()

    init {
        // guarantee x is sorted in order add .sorted() to the end
        // also this assumes no gaps in x
        var lines: List<String> = File(filePath).readLines().filter { it.startsWith("/dev") }.sorted()

        for (item in lines) {
            val parts: List<String> = item.split(' ').map(String::trim).filter(String::isNotBlank)
            val (x,y)= parts[0].split('-').map(String::trim).filter { (it.startsWith("x") || it.startsWith("y")) }
                    .map{ it.substring(1).toInt() }
            val (size, used, avail, usedPct) = parts.subList(1, parts.size).map { it.trim('T', '%', ' ').toInt() }

            (devNodes.size..y).forEach { devNodes.add(mutableListOf()) }

            devNodes[y].add(DevNode(parts[0], size, used, avail, usedPct))
        }
    }

    //960 is the count
    fun getAvailablePairs() : Set<Pair<DevNode,DevNode>> {
        var set: MutableSet<Pair<DevNode, DevNode>> = mutableSetOf()

        val inx = 0
        val total = devNodes.size * devNodes[0].size
        for (nodeA in devNodes.flatten().subList(0, total)) {
            // fetch the list starting at inx since the items before inx have already been addressed
            // filter based on the nodes not being the same, it does not alread exist and nodaA can accept the data
            devNodes.flatten().subList(inx, total)
                    .filter{ (nodeA != it) && (! set.contains(Pair(nodeA, it)) && nodeA.canAcceptAllData(it)) }
                    .forEach { set.add(Pair(nodeA, it)) }
        }

        return set
    }
}

fun main(args: Array<String>) {
    println("day22")

    val start = System.currentTimeMillis()
    var devNodeMatrix = DevNodeMatrx(".\\resources\\day22.data")

    println("count = ${devNodeMatrix.getAvailablePairs().count()}")
    val end = System.currentTimeMillis()

    println("run time = ${end - start}ms")

}