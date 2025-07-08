package com.learn.tictoctoy

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.learn.tictoctoy.databinding.ActivityMainBinding
import com.learn.tictoctoy.viewmodel.MainViewModel
import kotlinx.coroutines.launch

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

        binding.newGame.setOnClickListener {
            viewModel.resetGame()
            resetBoardUI()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ownActionHistory.collect { actions ->
                    actions.forEach { actionMap ->
                        val position = actionMap["position"]
                        val action = actionMap["action"]
                        updateValue(position, action)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.computerActionHistory.collect { actions ->
                    actions.forEach { actionMap ->
                        val position = actionMap["position"]
                        val action = actionMap["action"]
                        updateValue(position, action)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.winner.collect { winnerText ->
                    binding.showResult.text =
//                        if (winnerText.isNotEmpty()) winnerText else "Game Running"
                        winnerText.ifEmpty { "Game Running" }
                }
            }
        }
    }

    private fun setupClickListeners() {
        textViewPairs.forEach { (textView, id) ->
            textView.setOnClickListener {
                viewModel.setOwnAction(id)
            }
        }
    }

    private fun updateValue(position: String?, action: String?) {
        if (position == null || action == null) return
        textViewPairs.find { it.second == position }?.first?.let { textView ->
            textView.text = action
            textView.isClickable = false
        }
    }

    private fun resetBoardUI() {
        textViewPairs.forEach { (textView, _) ->
            textView.text = ""
            textView.isClickable = true
        }
        binding.showResult.text = "Game Running"
    }

    private fun getTextViewIdPairs(): List<Pair<TextView, String>> {
        return listOf(
            binding.tv11 to "tv11", binding.tv12 to "tv12", binding.tv13 to "tv13",
            binding.tv21 to "tv21", binding.tv22 to "tv22", binding.tv23 to "tv23",
            binding.tv31 to "tv31", binding.tv32 to "tv32", binding.tv33 to "tv33"
        )
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}