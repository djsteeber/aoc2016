import java.util.*

/**
 * Created by dsteeber on 12/19/2016.
 */


class Flyweight<T>() {

    var cache: MutableList<T> = mutableListOf()

    fun fetch(item: T) : T {
        if (cache.contains(item)) {
            return cache.find { it == item }!!
        }
        cache.add(item)

        return item
    }

}

fun Int.countBits(): Int {

    var num = this
    var count = 0
    while (num > 0) {
        if ((num and 0x0001) == 1) {
            count++
        }
        num = num shr 1
    }
    return count
}

data class Node(val x: Int, val y: Int, var distance: Int = -1, var parent: Node? = null) {

    fun getNeighbors(): List<Node> {
        return arrayOf(Node(this.x + 1, y, parent = this),
                Node(this.x - 1, y, parent = this),
                Node(this.x, y + 1, parent = this),
                Node(this.x, y - 1, parent = this)
        ).filter { it.isValid() }
    }


    fun isValid(): Boolean {
        if ((x < 0) || (y < 0)) return false

        if ((parent != null) && (parent!!.parent == this)) {
            return false
        }

        val favoriteNum = 1350
        val num = (x * x + 3 * x + 2 * x * y + y + y * y) + favoriteNum

        return (num.countBits() % 2) == 0
    }

    operator override fun equals(node: Any?): Boolean {
        return ((node != null) && (node is Node) && (x == node.x) && (y == node.y))
    }

    override fun toString(): String {
        return "[Node(x=${x}, y=${y}, distance=${distance})]"
    }
}




class Queue<T> {
    private val storage = LinkedList<T>()

    fun enqueue(item: T) {
        storage.add(item)
    }

    fun dequeue(): T?  = storage.removeFirst()

    val size: Int
        get() {
            return storage.size
        }

    fun isEmpty(): Boolean = (size == 0)

}


/*
How many locations (distinct x,y coordinates, including your starting location) can you reach in at most 50 steps?


 */


fun partA() {
    val flyweight = Flyweight<Node>()
    val queue: Queue<Node> = Queue<Node>()
    val root = flyweight.fetch(Node(x = 1,y = 1, distance = 0))

    val neighbors = root.getNeighbors()


    println(neighbors)
    queue.enqueue(root)

    while (! queue.isEmpty()) {
        val current = queue.dequeue()!!
        //println("checking node ${current}")
        if ((current.x == 31) && (current.y == 39)) {
            println("found it current = ${current}")

            break
        }
        for (n in current.getNeighbors()) {
            val node = flyweight.fetch(n)
            if (node.distance == -1) {
                node.distance = current.distance + 1
                node.parent = current
                queue.enqueue(node)
            }
        }
    }

}

fun partB(steps: Int) {
    var count = 0;
    val flyweight = Flyweight<Node>()
    val queue: Queue<Node> = Queue<Node>()
    val root = flyweight.fetch(Node(x = 1,y = 1, distance = 0))

    val neighbors = root.getNeighbors()


    println(neighbors)
    queue.enqueue(root)

    while (! queue.isEmpty()) {
        val current = queue.dequeue()!!

        if (current.distance <= steps) {
            count++
            println(current)

            if (current.distance < steps) {
                for (n in current.getNeighbors()) {
                    val node = flyweight.fetch(n)
                    if (node.distance == -1) {
                        node.distance = current.distance + 1
                        node.parent = current
                        queue.enqueue(node)
                    }
                }
            }
        }

    }

    println("Count of nodes you can travel to in ${steps} steps = ${count}")
}


fun main(args: Array<String>) {

    partA()

    partB(50)


}