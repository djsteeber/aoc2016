import java.util.*

/**
 * Created by dsteeber on 12/6/2016.
 */

/*
        var results: List<Pair<Char, Int>> = map.toList().sortedWith(Comparator { lhs, rhs ->
            when {
                lhs.second > rhs.second -> -1
                lhs.second < rhs.second -> 1
                else -> lhs.first.toString().compareTo(rhs.first.toString())
            }
        })

 */

class CharCountMap() {

    private val map: HashMap<Char, Int> = HashMap()

    enum class SORT_DIRECTION(val valnum: Int) {
        NONE(0),
        ASCENDING(1),
        DECENTING(-1)
    }

    enum class SORT_FIELD {
        KEY,
        VALUE
    }

    fun sort(primarySortField: SORT_FIELD = SORT_FIELD.KEY, keySort: SORT_DIRECTION = SORT_DIRECTION.ASCENDING,
             valueSort: SORT_DIRECTION = SORT_DIRECTION.ASCENDING): List<Pair<Char, Int>> {
        // return a sorted list

        if (map.isEmpty()) {
            return emptyList()
        }

        /* key first */
        var results: List<Pair<Char, Int>> = when (primarySortField) {
            SORT_FIELD.KEY -> {
                map.toList().sortedWith(Comparator { lhs, rhs ->
                    when {
                        lhs.first.toString().compareTo(rhs.first.toString()) == 0 -> {
                            if (lhs.second > rhs.second) {
                                (-1 * valueSort.valnum)
                            } else if (lhs.second < rhs.second) {
                                (1 * valueSort.valnum)
                            } else {
                                0
                            }
                        }
                        else -> (lhs.first.toString().compareTo(rhs.first.toString()) * keySort.valnum)
                    }
                })
            }
            SORT_FIELD.VALUE -> {
                map.toList().sortedWith(Comparator { lhs, rhs ->
                    when {
                        lhs.second > rhs.second -> (1 * valueSort.valnum)
                        lhs.second < rhs.second -> (-1 * valueSort.valnum)
                        else -> (lhs.first.toString().compareTo(rhs.first.toString()) * keySort.valnum)
                    }
                })
            }
        }

        return results;
    }

    fun clear() {
        map.clear()
    }

    fun addCharacters(str: String = "") {
        for (c in str.toCharArray()) {
            if (map[c] == null) {
                map[c] = 1
            } else {
                map[c] = map[c]!! + 1
            }
        }
    }


}

fun main(args: Array<String>) {
    var ccm: CharCountMap = CharCountMap()

    ccm.addCharacters("aabbcccdd")

    val x = ccm.sort(primarySortField = CharCountMap.SORT_FIELD.VALUE, valueSort = CharCountMap.SORT_DIRECTION.DECENTING)

    println(x)


}