package com.pichaeljanson.diceroll

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pichaeljanson.diceroll.databinding.DiceRollingFragmentBinding

class DiceRollingFragment : Fragment() {

    private lateinit var binding: DiceRollingFragmentBinding

    private lateinit var viewModelFactory: DiceViewModelFactory

    private lateinit var viewModel: DiceViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dice_rolling_fragment, container, false)

        viewModelFactory = DiceViewModelFactory(6)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DiceViewModel::class.java)

        binding.diceVm = viewModel
        binding.lifecycleOwner = this

        initRollVibration()

        return binding.root
    }

// region Vibration

    private fun initRollVibration(){
        viewModel.apply {
            eventVibrate.observe(this@DiceRollingFragment, Observer { vibType ->
                if (vibType == VibrationType.ROLL){
                    vibrate(vibType.pattern)
                    handledVibration()
                }
            })
        }
    }

    private fun vibrate(pattern: LongArray){
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                it.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                // Depreciated in API 26
                it.vibrate(pattern, -1)
            }
        }
    }

    // endregion
}