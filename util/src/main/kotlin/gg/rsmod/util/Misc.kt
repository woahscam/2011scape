package gg.rsmod.util

import java.text.NumberFormat
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Misc {

    val DIRECTION_DELTA_X = intArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)
    val DIRECTION_DELTA_Z = intArrayOf(-1, -1, -1, 0, 0, 1, 1, 1)

    fun getNpcMoveDirection(dd: Int): Int {
        return if (dd < 0) {
            -1
        } else getNpcMoveDirection(
            DIRECTION_DELTA_X[dd],
            DIRECTION_DELTA_Z[dd]
        )
    }

    /**
     * Gets the 32 bit value.
     *
     * @return the 32 bit value
     */
    fun get32BitValue(array: BooleanArray, trueCondition: Boolean): Int {
        var value = 0
        for (index in 1 until array.size + 1) {
            if (array[index - 1] == trueCondition) {
                value += 1 shl index
            }
        }
        return value
    }

    private fun getNpcMoveDirection(dx: Int, dy: Int): Int {
        if (dx == 0 && dy > 0) {
            return 0
        }
        if (dx > 0 && dy > 0) {
            return 1
        }
        if (dx > 0 && dy == 0) {
            return 2
        }
        if (dx > 0 && dy < 0) {
            return 3
        }
        if (dx == 0 && dy < 0) {
            return 4
        }
        if (dx < 0 && dy < 0) {
            return 5
        }
        if (dx < 0 && dy == 0) {
            return 6
        }
        return if (dx < 0 && dy > 0) {
            7
        } else -1
    }
    fun getPlayerWalkingDirection(dx: Int, dy: Int): Int {
        if (dx == -1 && dy == -1) {
            return 0
        }
        if (dx == 0 && dy == -1) {
            return 1
        }
        if (dx == 1 && dy == -1) {
            return 2
        }
        if (dx == -1 && dy == 0) {
            return 3
        }
        if (dx == 1 && dy == 0) {
            return 4
        }
        if (dx == -1 && dy == 1) {
            return 5
        }
        if (dx == 0 && dy == 1) {
            return 6
        }
        return if (dx == 1 && dy == 1) {
            7
        } else -1
    }

    fun getPlayerRunningDirection(dx: Int, dy: Int): Int {
        if (dx == -2 && dy == -2)
            return 0
        if (dx == -1 && dy == -2)
            return 1
        if (dx == 0 && dy == -2)
            return 2
        if (dx == 1 && dy == -2)
            return 3
        if (dx == 2 && dy == -2)
            return 4
        if (dx == -2 && dy == -1)
            return 5
        if (dx == 2 && dy == -1)
            return 6
        if (dx == -2 && dy == 0)
            return 7
        if (dx == 2 && dy == 0)
            return 8
        if (dx == -2 && dy == 1)
            return 9
        if (dx == 2 && dy == 1)
            return 10
        if (dx == -2 && dy == 2)
            return 11
        if (dx == -1 && dy == 2)
            return 12
        if (dx == 0 && dy == 2)
            return 13
        if (dx == 1 && dy == 2)
            return 14
        return if (dx == 2 && dy == 2) 15 else -1
    }

    /**
     * Formats the string as display name.
     * @param name The string to format.
     * @return The formatted name.
     */
    fun formatForDisplay(name: String): String {
        val newName = StringBuilder()
        var wasSpace = true
        for (c in name.replace("_".toRegex(), " ").lowercase()) {
            if (wasSpace) {
                newName.append(c.uppercase())
                wasSpace = false
            } else {
                newName.append(c)
            }
            if (c == ' ') {
                wasSpace = true
            }
        }
        return newName.toString()
    }

    /**
     * Produces either "a" or "an" in the context
     * of a string. For example
     *
     * You've just advanced an Attack level, or
     * You've just advanced a Strength level
     */
    fun formatForVowel(string: String) : String {
        val initialChar = Character.toLowerCase(string.toCharArray().first())
        val vowel = initialChar == 'a' || initialChar == 'e' || initialChar == 'i' || initialChar == 'o' || initialChar == 'u'
        return if(vowel) "an" else "a"
    }

    /**
     * Returns a pluralized version of the input word by adding an "s" or "ies" if necessary.
     *
     * @param word the input word to pluralize
     * @return the pluralized version of the input word
     */
    fun pluralize(word: String): String {
        // if the word already ends with an "s", return it as-is
        if (word.endsWith("s")) {
            return word
        }
        // if the word ends with a "y", replace the "y" with "ies" to make it plural
        return if (word.endsWith("y")) {
            word.substring(0, word.length - 1) + "ies"
        }
        // for all other cases, simply add an "s" to make the word plural
        else {
            word + "s"
        }
    }


    /**
     * Returns the given [string] formatted with an indefinite article "a" or "an",
     * or "some" if the last character is "s".
     * If an [amount] is provided greater than 1, returns the string with the amount
     * formatted using a number formatter, followed by the string.
     */
    fun formatWithIndefiniteArticle(string: String, amount: Int = 0): String {
        val initialChar = Character.toLowerCase(string.firstOrNull() ?: ' ')
        val lastChar = Character.toLowerCase(string.lastOrNull() ?: ' ')
        val vowel = initialChar in setOf('a', 'e', 'i', 'o', 'u')
        val some = lastChar == 's'

        return when {
            amount > 1 -> "${NumberFormat.getInstance().format(amount)} ${pluralize(string)}"
            vowel -> "an $string"
            some -> "some $string"
            else -> "a $string"
        }
    }


    /**
     * Formats the string for sentences
     */
    fun formatSentence(ct: String): String? {
        var ctb: String? = ""
        if (ct.length > 1) {
            var canCap = true
            var forceCap = true
            for (i in 0 until ct.length) {
                var ctls = ct.substring(i, i + 1)
                if (ctls == " ") {
                    canCap = true
                }
                if (ctls == "." || ctls == "?" || ctls == "!") {
                    forceCap = true
                }
                if (Character.isLowerCase(ctls[0])) {
                    if (forceCap) {
                        ctls = ctls.uppercase(Locale.getDefault())
                        canCap = false
                        forceCap = false
                    }
                    if (canCap) {
                        canCap = false
                        forceCap = false
                    }
                } else if (Character.isUpperCase(ctls[0])) {
                    if (!canCap && !forceCap) {
                        ctls = ctls.lowercase(Locale.getDefault())
                    } else {
                        canCap = false
                        forceCap = false
                    }
                } else if(Character.isDigit(ctls[0])) {
                    canCap = false
                    forceCap = false
                }
                ctb += ctls
            }
            return ctb
        }
        return if (ct.length == 1) {
            ct.uppercase(Locale.getDefault())
        } else ct
    }

}