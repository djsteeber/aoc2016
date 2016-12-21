import java.io.File
import java.util.*

/**
 * Created by dsteeber on 12/12/2016.
 */

open class MicroChipReceiver(val id: String) {
    val chips = ArrayList<Int>()

    open fun addInstruction(instruction: String) {

    }

    fun giveMicroChip(microChip: Int) {
        chips.add(microChip)
    }

    open fun hasSomethingToDo(): Boolean = false

    open fun performWork(recievers: HashMap<String, MicroChipReceiver>) {

    }

    override fun toString(): String {
        if (chips.size == 0) {
            return ""
        }
        return "chips{" + chips.map{it.toString()}.reduce{total, item -> total + ", " + item} + "}"
    }

}

class Bot(id: String) : MicroChipReceiver(id) {

    var lowGiveID = ""
    var highGiveID = ""

    //bot 177 gives low to output 8 and high to bot 157
    override fun addInstruction(instruction: String) {
        var parts = instruction.split(' ')  // simple tokenizer, lexer would be better but over complicated
        lowGiveID = parts[5] + "-" + parts[6]
        highGiveID = parts[10] + "-" + parts[11]
    }

    override fun toString(): String {
        return "Bot[${id}] low -> ${lowGiveID}, high -> ${highGiveID}" + super.toString()
    }

    override fun hasSomethingToDo(): Boolean {
        return (chips.size > 1)
    }

    override fun performWork(recievers: HashMap<String, MicroChipReceiver>) {
        if (chips.size < 2) {
            println("*** Error, cannot perform work with < 2 chips")
            return
        }
        val sortedChips: List<Int> = chips.sorted()

        if ((sortedChips[0] == 17) && (sortedChips[1] == 61)) {
            println("*** I'm the bot you are looking for ${id}")

        }


//        println(" doing work " + this.toString())
        recievers[lowGiveID]!!.giveMicroChip(sortedChips[0])
        recievers[highGiveID]!!.giveMicroChip(sortedChips[1])
        chips.clear()

    }

}

class OutputBin(id: String) : MicroChipReceiver(id) {
    override fun toString(): String {
        return "Output[$id]" + super.toString()
    }
}

fun HashMap<String, MicroChipReceiver>.findOrCreate(id: String): MicroChipReceiver {
    var mcr: MicroChipReceiver? = this[id]

    if (mcr == null) {
        if (id.startsWith("bot")) {
            mcr = Bot(id)
        } else {
            mcr = OutputBin(id)
        }
        this[id] = mcr
    }
    return mcr
}

fun createID(prefix: String, id: String): String {
    return prefix + "-" + id
}

fun main(args: Array<String>) {
    val instructions = File(".\\resources\\day10.data").readLines()

    val receivers = HashMap<String, MicroChipReceiver>()


    for (instruction in instructions) {
        if (instruction.startsWith("bot 2")) {
            println()
        }
        val instParts = instruction.split(' ')
        if (instParts[0] == "bot") {
            val botId = createID("bot", instParts[1])
            val bot = receivers.findOrCreate(botId)


            receivers.findOrCreate(createID(instParts[5], instParts[6]))
            receivers.findOrCreate(createID(instParts[10],instParts[11]))
            bot.addInstruction(instruction)
        } else if (instParts[0] == "value") {
            //value 61 goes to bot 119
            val receiver = receivers.findOrCreate(createID(instParts[4], instParts[5]))
            receiver.giveMicroChip(instParts[1].toInt())
        }
    }

    receivers.values.forEach { println(it.toString()) }

    var workBots = receivers.values.filter{it.hasSomethingToDo()}
    while (workBots.size > 0) {
        workBots.forEach {it.performWork(receivers)}
        workBots = receivers.values.filter{it.hasSomethingToDo()}
    }

    val product: Int = receivers[createID("output", "0")]!!.chips[0] * receivers[createID("output", "1")]!!.chips[0] *
            receivers[createID("output", "2")]!!.chips[0]

    println(" product of output 0,1,2 is ${product}")

}