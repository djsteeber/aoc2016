/**
 * Created by dsteeber on 12/2/2016.
 */
/**
 * Created by dsteeber on 12/2/2016.
 */

import java.io.File
import kotlin.io.*

open class EnumCompanion<T, V>(private val valueMap: Map<T, V>) {
    fun fromInt(type: T) = valueMap[type]
}

fun Int.abs(): Int = Math.abs(this)


data class Location(var x: Int = 0, var y: Int = 0) {
    fun distanceFrom(loc: Location): Int {
        return (loc.x - x).abs() + (loc.y + y).abs()
    }
}

enum class Direction(val compassHeading: Int = 0) {
    NORTH(0), EAST(90), SOUTH(180), WEST(270);

    companion object : EnumCompanion<Int, Direction>(Direction.values().associateBy(Direction::compassHeading))
}

enum class Rotation(val title: String, val rotationValue: Int) {
    L("LEFT", -90), R("RIGHT", 90);
}

data class Person(val location: Location = Location(0,0), var direction: Direction = Direction.NORTH) {
    fun turn (rotation: Rotation) {
        val compassHeading = ((direction.compassHeading + rotation.rotationValue) + 360) % 360

        direction = Direction.fromInt(compassHeading) ?: direction
    }

    fun move(distance: Int) {
        when (direction) {
            Direction.NORTH -> {
                location.y = location.y + distance
            }
            Direction.SOUTH -> {
                location.y = location.y - distance
            }
            Direction.EAST -> {
                location.x = location.x + distance
            }
            Direction.WEST -> {
                location.x = location.x - distance
            }
        }
    }

    fun turnAndMove(rotation: Rotation, distance: Int) {
        turn(rotation)
        move(distance)
    }

}

fun main(args: Array<String>) {
    val person = Person()

    var dataStr = File(".\\resources\\day01.data").readText()

    var data: List<String> = dataStr.split(",").map{it.trim()}


    for (step in data) {
        var turn = Rotation.valueOf(step.substring(0..0))
        var dist: Int = step.substring(1).toInt()
        person.turnAndMove(turn, dist)
        println(step)
        println(person)
    }

    println(person)

    var distance = person.location.distanceFrom(Location())
    println("Person traveled ${distance} blocks")

}