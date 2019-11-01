package com.teamit.tictactoebackend.mapper

import com.teamit.tictactoebackend.exception.IllegalMoveException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StringPositionMapperTest {

    @Test
    fun shouldMapValidStringToPosition() {
        val pos = StringPositionMapper.mapPositionFromString("A")
        assertEquals(0, pos)
    }

    @Test
    fun shouldMapValidStringToPositionCaseInsensitive() {
        val pos = StringPositionMapper.mapPositionFromString("a")
        assertEquals(0, pos)
    }

    @Test
    fun shouldThrowIllegalMoveExceptionForInvalidStringPosition() {
        assertThrows(IllegalMoveException::class.java) {
            StringPositionMapper.mapPositionFromString("D")
        }
    }

    @Test
    fun shouldMapValidPositionToString() {
        val pos = StringPositionMapper.mapStringFromPosition(0)
        assertEquals("A", pos)
    }

    @Test
    fun shouldThrowIllegalMoveExceptionForInvalidIntPosition() {
        assertThrows(IllegalMoveException::class.java) {
            StringPositionMapper.mapStringFromPosition(4)
        }
    }
}