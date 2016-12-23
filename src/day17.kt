/**
 * Created by dsteeber on 12/21/2016.
 */
package day17

import utils.MD5Hasher
import utils.Queue

/*
You're trying to access a secure vault protected by a 4x4 grid of small rooms connected by doors.
You start in the top-left room (marked S), and you can access the vault (marked V) once you reach the bottom-right room:

#########
#S| | | #
#-#-#-#-#
# | | | #
#-#-#-#-#
# | | | #
#-#-#-#-#
# | | |
####### V
Fixed walls are marked with #, and doors are marked with - or |.

The doors in your current room are either open or closed (and locked) based on the hexadecimal MD5 hash
of a passcode (your puzzle input) followed by a sequence of uppercase characters representing
the path you have taken so far (U for up, D for down, L for left, and R for right).

Only the first four characters of the hash are used; they represent, respectively,
the doors up, down, left, and right from your current position.
Any b, c, d, e, or f means that the corresponding door is open; any other character (any number or a) means
that the corresponding door is closed and locked.

To access the vault, all you need to do is reach the bottom-right room; reaching this room opens the vault
and all doors in the maze.

For example, suppose the passcode is hijkl. Initially, you have taken no steps, and so your path is empty:
you simply find the MD5 hash of hijkl alone. The first four characters of this hash are ced9,
which indicate that up is open (c), down is open (e), left is open (d), and right is closed and locked (9).
Because you start in the top-left corner, there are no "up" or "left" doors to be open, so your only choice is down.

Next, having gone only one step (down, or D), you find the hash of hijklD. This produces f2bc, which indicates
that you can go back up, left (but that's a wall), or right. Going right means hashing hijklDR to get 5745 -
all doors closed and locked. However, going up instead is worthwhile: even though it returns you
to the room you started in, your path would then be DU, opening a different set of doors.

After going DU (and then hashing hijklDU to get 528e), only the right door is open; after going DUR, all doors lock.
(Fortunately, your actual passcode is not hijkl).

Passcodes actually used by Easter Bunny Vault Security do allow access to the vault if you know the right path.
For example:

If your passcode were ihgpwlah, the shortest path would be DDRRRD.
With kglvqrro, the shortest path would be DDUDRLRRUDRD.
With ulqzkmiv, the shortest would be DRURDRUDDLLDLUURRDULRLDUUDDDRR.
Given your vault's passcode, what is the shortest path (the actual path, not just the length) to reach the vault?

Your puzzle input is udskfozm.

-------------Part 2 -------------------
You're curious how robust this security solution really is, and so you decide to find longer and longer paths
which still provide access to the vault. You remember that paths always end the first time
they reach the bottom-right room (that is, they can never pass through it, only end in it).

For example:

If your passcode were ihgpwlah, the longest path would take 370 steps.
With kglvqrro, the longest path would be 492 steps long.
With ulqzkmiv, the longest path would be 830 steps long.
What is the length of the longest path that reaches the vault?

Your puzzle input is still udskfozm.
 */

data class Room(val num: Int) {


    var up: Room? = null
    set(room) {
        field = room
        if ((room != null) && (room.down == null)) {
            room.down = this
        }
    }
    var down: Room? = null
    set(room) {
        field = room
        if ((room != null) && (room.up == null)) {
            room.up = this
        }
    }
    var left: Room? = null
        set(room) {
            field = room
            if ((room != null) && (room.right == null)) {
                room.right = this
            }
        }
    var right: Room? = null
        set(room) {
            field = room
            if ((room != null) && (room.left == null)) {
                room.left = this
            }
        }

    constructor(num: Int, up: Room? = null, down: Room? = null, left: Room? = null, right: Room? = null) : this(num) {
        this.up = up
        this.down = down
        this.left = left
        this.right = right
    }

    fun getFarLeft(): Room {
        if (this.left == null) {
            return this
        } else {
            return this.left!!.getFarLeft()
        }
    }
    fun getFarUp(): Room {
        if (this.up == null) {
            return this
        } else {
            return this.up!!.getFarUp()
        }
    }

    fun toStringRepresentation(): String {

        var str = ""

        var row: Room? = this
        while (row != null) {
            var col: Room? = row
            while (col != null) {
                str += col.num.toString() + " "
                col = col.right
            }
            row = row.down
            str += "\n"
        }

        return str
    }

}

class PathFinder(val pathType: PathType = PathFinder.PathType.SHORTEST) {
    private val md5 = MD5Hasher()
    private data class PossibleRoute(val room: Room, val passcode: String, val currentPath: String)

    enum class PathType {
        SHORTEST,
        LONGEST
    }

    private val openCodes = listOf('b','c','d','e','f')

    //up down left right
    private fun getPossibleRoutes(room: Room, passcode: String, currentPath: String): List<PossibleRoute> {
        val routes: MutableList<PossibleRoute> = mutableListOf()
        val codes = md5.hash(passcode + currentPath).subSequence(0..3)

        if (openCodes.contains(codes[0]) && (room.up != null)) {
            routes.add(PossibleRoute(room.up!!, passcode, currentPath + "U"))
        }
        if (openCodes.contains(codes[1]) && (room.down != null)) {
            routes.add(PossibleRoute(room.down!!, passcode, currentPath + "D"))
        }
        if (openCodes.contains(codes[2]) && (room.left != null)) {
            routes.add(PossibleRoute(room.left!!, passcode, currentPath + "L"))
        }
        if (openCodes.contains(codes[3]) && (room.right != null)) {
            routes.add(PossibleRoute(room.right!!, passcode, currentPath + "R"))
        }

        return routes
    }

    // Possible rework
    // this would be interesting using robots to walk the path.  when there is more than one path, then clone the robot
    // and let them take a direction
    fun findPathTo(currentRoom: Room, num: Int, passcode: String, currentPath: String = "", recursionLevel: Int = 0): String? {
        if (currentRoom.num == num) {
            return currentPath
        }

        //println("I am trying to get to ${num} and am in ${currentRoom.num}.  My current path is ${currentPath} at recursionLevel = ${recursionLevel}")

        val pathList: MutableList<String> = mutableListOf()

        val possibleRoutes = getPossibleRoutes(currentRoom, passcode, currentPath)
        if (! possibleRoutes.isEmpty()) {
            possibleRoutes.forEach {
                //println("  going to try ${it.room}  ${it.currentPath}")
                val path = findPathTo(it.room, num, passcode, it.currentPath, recursionLevel+1)
                if (path != null) {
                    pathList.add(path)
                }
            }
        }

        if (pathList.isEmpty()) {
            return null
        }

        var rtnPath = when(this.pathType) {
            PathType.SHORTEST -> {
                var shortestPath: String? = null
                // would love to change this to reduce
                pathList.forEach {
                    if (shortestPath == null) {
                        shortestPath = it
                    } else if (it.isNotEmpty()) {
                        shortestPath = if (shortestPath!!.length < it.length) shortestPath else it
                    }
                }
                shortestPath
            }
            PathType.LONGEST -> {
                var longestPath: String? = null
                // would love to change this to reduce
                pathList.forEach {
                    if (longestPath == null) {
                        longestPath = it
                    } else if (it.isNotEmpty()) {
                        longestPath = if (longestPath!!.length > it.length) longestPath else it
                    }
                }
                longestPath
            }
        }

        return rtnPath
    }

}


fun createRooms(width: Int, height: Int): Room {
    var counter = 0

    var room: Room? = null
    var roomAbove: Room? = null
    for (y in 1..height) {
        room = null
        for (x in 1..width) {
            counter++
            if (room == null) {
                room = Room(counter, up = roomAbove)
            } else {
                if (roomAbove != null) {
                    roomAbove = roomAbove.right
                }
                room.right = Room(counter, left = room, up = roomAbove)
                room = room.right
            }
        }
        roomAbove = room!!.getFarLeft()
    }

    return room!!.getFarLeft().getFarUp()
}



fun main(args: Array<String>) {
    println("day17")

    val room: Room = createRooms(4,4)

    println(room.toStringRepresentation())

    val passcode = "udskfozm"

    val pathFinder = PathFinder()
    val path = pathFinder.findPathTo(room, 16, passcode)
    println("the shortest path is ${path}")

    val pathFinder2 = PathFinder(PathFinder.PathType.LONGEST)
    val path2 = pathFinder2.findPathTo(room, 16, passcode)
    println("the length of the longest path is ${path2!!.length}")



}