package com.learn.tictoctoy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.tictoctoy.model.TicTocToyModel


class MainViewModel : ViewModel() {

    private val ticTocToyModel = TicTocToyModel()

    private val _ownAction = MutableLiveData<Map<String, String>>()
    val ownAction: LiveData<Map<String, String>> = _ownAction

    private val _computerAction = MutableLiveData<Map<String, String>>()
    val computerAction: LiveData<Map<String, String>> = _computerAction

    private val _winner = MutableLiveData(false)
    val winner: LiveData<Boolean> = _winner

    fun setOwnAction(position: String) {
        if (_winner.value == true) return

        _ownAction.value = mapOf(
            "position" to position,
            "action" to "X"
        )
        ticTocToyModel.updateOwnChoice(position, "X")
        val winnerResult = ticTocToyModel.checkResult("X")
        if (winnerResult.win) {
            _winner.value = true
        } else {
            handleComputerTurn()
        }
    }

    fun handleComputerTurn() {
        val computerPosition = ticTocToyModel.getComputerChoice()
        if (computerPosition.isEmpty()) return

        _computerAction.value = mapOf(
            "position" to computerPosition,
            "action" to "O"
        )
        val winnerResult = ticTocToyModel.checkResult("O")
        if (winnerResult.win) {
            _winner.value = true
        }
    }

    fun resetGame() {
        ticTocToyModel.resetGame()
        _ownAction.value = emptyMap()
        _computerAction.value = emptyMap()
        _winner.value = false
    }

}