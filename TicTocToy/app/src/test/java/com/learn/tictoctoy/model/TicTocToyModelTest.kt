package com.learn.tictoctoy.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TicTocToyModelTest {
    private lateinit var model: TicTocToyModel

    @Before
    fun setUp() {
        model = TicTocToyModel()
    }

    @Test
    fun `resetGame should clear all cells`() {
        // Arrange: mark some cells
        model.updateOwnChoice("tv11", "X")
        model.updateOwnChoice("tv22", "O")
        model.updateOwnChoice("tv33", "X")

        // Act: reset the game
        model.resetGame()

        // Assert: all cells should be reset
        val field = model.javaClass.getDeclaredField("views")
        field.isAccessible = true
        val views = field.get(model) as Map<*, *>

        views.forEach { (key, cell) ->
            val cellObj = cell as TicTocToyModel.Cell
            assertEquals("Cell $key should have empty value", "", cellObj.value)
            assertFalse("Cell $key should not be marked", cellObj.marked)
        }
    }

}