package com.teamit.tictactoebackend.mapper

import com.teamit.tictactoebackend.exception.IllegalMoveException

class StringPositionMapper {
    companion object {
        fun mapPositionFromString(pos: String): Int {
            return when (pos.toLowerCase()) {
                "a" -> 0
                "b" -> 1
                "c" -> 2
                else -> throw IllegalMoveException("Can not make move outside of game board")
            }
        }

        fun mapStringFromPosition(pos: Int): String {
            return when(pos) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                else -> throw IllegalMoveException("Can not make move outside of game board")
            }
        }
    }
}
