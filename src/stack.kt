import java.util.*

/**
 * Created by dsteeber on 12/14/2016.
 */
class Stack<T> {
    var linkedList = LinkedList<T>()
    fun push(item: T) {
        linkedList.addLast(item)
    }

    fun pop(): T {
        return linkedList.removeLast()
    }

    fun isEmpty(): Boolean {
        return (linkedList.size == 0)
    }

    fun clear() {
        linkedList.clear()
    }

    val size:Int = linkedList.size
}

