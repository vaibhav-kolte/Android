package com.learn.tictoctoy

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.learn.tictoctoy.databinding.ActivityMainBinding
import com.learn.tictoctoy.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var textViewPairs: List<Pair<TextView, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textViewPairs = getTextViewIdPairs()

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.ownAction.observe(this) { actionMap ->
            val position = actionMap["position"]
            val action = actionMap["action"]
            Log.d(TAG, "Player move: $position -> $action")
            updateValue(position, action)
        }

        viewModel.computerAction.observe(this) { actionMap ->
            val position = actionMap["position"]
            val action = actionMap["action"]
            Log.d(TAG, "Computer move: $position -> $action")
            updateValue(position, action)
        }

        viewModel.winner.observe(this) { hasWinner ->
            Log.d(TAG, "Game ended: Winner = $hasWinner")
            binding.showResult.text = if (hasWinner) "Game Over: Win" else "Game Running"
        }
    }

    private fun setupClickListeners() {
        textViewPairs.forEach { (textView, id) ->
            textView.setOnClickListener {
                viewModel.setOwnAction(id)
            }
        }

        binding.newGame.setOnClickListener {
            viewModel.resetGame()
            textViewPairs.forEach { (textView, id) ->
                textView.text = ""
            }
        }
    }

    private fun updateValue(position: String?, action: String?) {
        if (position == null || action == null) return

        textViewPairs.find { it.second == position }?.first?.let { view ->
            view.text = action
            view.isClickable = false
        }
    }

    private fun getTextViewIdPairs(): List<Pair<TextView, String>> {
        return listOf(
            binding.tv11 to "tv11",
            binding.tv12 to "tv12",
            binding.tv13 to "tv13",
            binding.tv21 to "tv21",
            binding.tv22 to "tv22",
            binding.tv23 to "tv23",
            binding.tv31 to "tv31",
            binding.tv32 to "tv32",
            binding.tv33 to "tv33"
        )
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}