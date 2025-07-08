package com.learn.tictoctoy.model

import android.util.Log
class TicTocToyModel {

    data class Cell(var value: String = "", var marked: Boolean = false)

    private val views = mutableMapOf(
        "tv11" to Cell(),
        "tv12" to Cell(),
        "tv13" to Cell(),
        "tv21" to Cell(),
        "tv22" to Cell(),
        "tv23" to Cell(),
        "tv31" to Cell(),
        "tv32" to Cell(),
        "tv33" to Cell(),
    )

    fun updateOwnChoice(option: String, choice: String) {
        views[option]?.apply {
            value = choice
            marked = true
        }
    }

    fun getComputerChoice(): String {
        val unmarkedCells = views.filter { !it.value.marked }.keys
        if (unmarkedCells.isEmpty()) return ""
        val randomKey = unmarkedCells.random()
        updateOwnChoice(randomKey, "O")
        Log.d(TAG, "Random key with value false: $randomKey")
        return randomKey
    }

    fun checkResult(choice: String): Winner {
        Log.d(TAG, "Checking for winner: $choice")

        val winningCombinations = arrayOf(
            arrayOf("tv11", "tv12", "tv13"),
            arrayOf("tv21", "tv22", "tv23"),
            arrayOf("tv31", "tv32", "tv33"),
            arrayOf("tv11", "tv21", "tv31"),
            arrayOf("tv12", "tv22", "tv32"),
            arrayOf("tv13", "tv23", "tv33"),
            arrayOf("tv11", "tv22", "tv33"),
            arrayOf("tv13", "tv22", "tv31")
        )

        for (combination in winningCombinations) {
            if (combination.all { views[it]?.value == choice }) {
                Log.d(TAG, "$choice wins with ${combination.joinToString()}")
                return Winner(choice, true)
            }
        }
        return Winner("", false)
    }

    fun resetGame() {
        views.keys.forEach { key ->
            views[key]?.apply {
                value = ""
                marked = false
            }
        }
    }

    companion object {
        private const val TAG = "TicTocToyModel"
    }
}