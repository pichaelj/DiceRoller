package com.pichaeljanson.diceroll

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pichaeljanson.diceroll.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModelFactory: DiceViewModelFactory

    private lateinit var viewModel: DiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModelFactory = DiceViewModelFactory(6)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DiceViewModel::class.java)

        binding.diceVm = viewModel
        binding.lifecycleOwner = this

        initRollVibration()
    }

    // region Vibration

    private fun initRollVibration(){
        viewModel.apply {
            eventVibrate.observe(this@MainActivity, Observer { vibType ->
                if (vibType == VibrationType.ROLL){
                    vibrate(vibType.pattern)
                    handledVibration()
                }
            })
        }
    }

    private fun vibrate(pattern: LongArray){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

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
