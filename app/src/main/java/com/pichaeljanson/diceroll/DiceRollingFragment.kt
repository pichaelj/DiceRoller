package com.pichaeljanson.diceroll

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pichaeljanson.diceroll.databinding.DiceRollingFragmentBinding
import com.pichaeljanson.diceroll.ui.DiceViewModel
import com.pichaeljanson.diceroll.ui.DiceViewModelFactory

class DiceRollingFragment : Fragment() {

    private val diceRollingVm: DiceRollingViewModel by viewModels() {
        DiceRollingViewModelFactory()
    }

    private val  diceVm: DiceViewModel by viewModels() {
        DiceViewModelFactory()
    }

    private lateinit var binding: DiceRollingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.dice_rolling_fragment, container, false)

        binding.diceRollingVm = diceRollingVm
        binding.diceVm = diceVm
        binding.lifecycleOwner = this

        initEventMenuRefresh()
        initRollListeners()
        initRollVibration()

        return binding.root
    }

    // region Options Menu

    private fun initEventMenuRefresh() {
        diceVm.apply {
            eventMenuRefresh.observe(viewLifecycleOwner, Observer {
                if (it) {
                    activity?.invalidateOptionsMenu()
                    notifyEventMenuRefreshHandled()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dice_rolling_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.findItem(R.id.menu_item_add).isEnabled = diceVm.addDiceEnabled
        menu.findItem(R.id.menu_item_remove).isEnabled = diceVm.removeDiceEnabled
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_add -> diceVm.incrementDice()
            R.id.menu_item_remove -> diceVm.decrementDice()
        }

        return super.onOptionsItemSelected(item)
    }

    // endregion

    // region Rolling

    private fun initRollListeners() {
        diceRollingVm.startRollEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                onStartRoll()
                diceRollingVm.startRollEventHandled()
            }
        })

        diceRollingVm.finishRollEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                onFinishRoll()
                diceRollingVm.finishRollHandled()
            }
        })
    }

    private fun onStartRoll() {
        diceVm.startRoll()

        // Animate dice images
        val rotateAnim = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnim.duration = 250
        rotateAnim.repeatCount = Animation.INFINITE
        rotateAnim.repeatMode = Animation.RESTART

        val diceCount = diceVm.count

        if (diceCount > 0) {
            binding.dice0Iv.startAnimation(rotateAnim)
        }

        if (diceCount > 1) {
            binding.dice1Iv.startAnimation(rotateAnim)
        }

        if (diceCount > 2) {
            binding.dice2Iv.startAnimation(rotateAnim)
        }

        if (diceCount > 3) {
            binding.dice3Iv.startAnimation(rotateAnim)
        }

        if (diceCount > 4) {
            binding.dice4Iv.startAnimation(rotateAnim)
        }
    }

    private fun onFinishRoll() {
        diceVm.roll()

        // Stop animations
        binding.dice0Iv.clearAnimation()
        binding.dice1Iv.clearAnimation()
        binding.dice2Iv.clearAnimation()
        binding.dice3Iv.clearAnimation()
        binding.dice4Iv.clearAnimation()
    }

    // endregion

    // region Vibration

    private fun initRollVibration(){
        diceRollingVm.apply {
            eventVibrate.observe(viewLifecycleOwner, Observer { vibType ->
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