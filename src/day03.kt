import java.io.File

/**
 * Created by dsteeber on 12/3/2016.
 */


data class Triangle(val x: Int, val y: Int, val z: Int) {

    fun isValid(): Boolean {
        return ((x + y) > z) && ((y + z) > x) && ((x + z) > y)
    }

}


fun main(args: Array<String>) {
    val lines = File(".\\resources\\day03.data").readLines().map{it.trim()}

    var counter = 0


    for (line in lines) {
        var points: List<Int> = line.split(" ").filter{it != ""}.map{it.toInt()}

        var t = Triangle(points[0], points[1], points[2])
        if (t.isValid()) {
            counter++
        }
    }

    println("The number of invalid triangles = ${counter}")
    println("Total number of triangles = ${lines.size}")

    /* part 2 */
    var t1: Triangle? = null
    var t2: Triangle? = null
    var t3: Triangle? = null
    counter = 0
    for (line in lines) {
        var points: List<Int> = line.split(" ").filter{it != ""}.map{it.toInt()}

        if (t1 == null) {
            t1 = Triangle(points[0], points[1], points[2])
        } else if (t2 == null) {
            t2 = Triangle(points[0], points[1], points[2])

        } else {
            t3 = Triangle(points[0], points[1], points[2])

            if (Triangle(t1.x, t2.x,t3.x).isValid()) {
                counter++;
            }
            if (Triangle(t1.y, t2.y,t3.y).isValid()) {
                counter++;
            }
            if (Triangle(t1.z, t2.z,t3.z).isValid()) {
                counter++;
            }
            t1 = null
            t2 = null
        }
    }
    println("The number of invalid triangles = ${counter}")
    println("Total number of triangles = ${lines.size}")



}