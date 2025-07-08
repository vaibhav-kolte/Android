package com.learn.tictoctoy.viewmodel

import androidx.lifecycle.ViewModel
import com.learn.tictoctoy.model.TicTocToyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private var ticTocToyModel = TicTocToyModel()

    private val _ownActionHistory = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val ownActionHistory: StateFlow<List<Map<String, String>>> = _ownActionHistory

    private val _computerActionHistory = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val computerActionHistory: StateFlow<List<Map<String, String>>> = _computerActionHistory

    private val _winner = MutableStateFlow("")
    val winner: StateFlow<String> = _winner

    fun setOwnAction(position: String) {
        if (_winner.value.isNotEmpty()) return

        val action = mapOf(
            "position" to position,
            "action" to "X"
        )
        ticTocToyModel.updateOwnChoice(position, "X")
        _ownActionHistory.value = _ownActionHistory.value + action

        val winnerResult = ticTocToyModel.checkResult("X")
        if (winnerResult.win) {
            _winner.value = "Player (${winnerResult.choice}) Wins!"
        } else {
            handleComputerTurn()
        }
    }

    fun handleComputerTurn() {
        val position = ticTocToyModel.getComputerChoice()
        if (position.isEmpty()) {
            _winner.value = "Game draw"
            return
        }

        val action = mapOf(
            "position" to position,
            "action" to "O"
        )
        _computerActionHistory.value = _computerActionHistory.value + action

        val winnerResult = ticTocToyModel.checkResult("O")
        if (winnerResult.win) {
            _winner.value = "Computer (${winnerResult.choice}) Wins!"
        }
    }

    fun resetGame() {
        ticTocToyModel.resetGame()
        _ownActionHistory.value = emptyList()
        _computerActionHistory.value = emptyList()
        _winner.value = ""
    }
}