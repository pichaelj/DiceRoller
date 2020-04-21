package com.pichaeljanson.diceroll

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pichaeljanson.diceroll.databinding.DiceRollingFragmentBinding
import timber.log.Timber

class DiceRollingFragment : Fragment(), RollListener {

    private val diceRollingVm: DiceRollingViewModel by viewModels() {
        DiceRollingViewModelFactory(this)
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
        binding.lifecycleOwner = this

        initRollVibration()

        return binding.root
    }

    // region Options Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dice_rolling_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit -> diceRollingVm.toggleEditMode()
        }

        return super.onOptionsItemSelected(item)
    }

    // endregion

    // region RollListener implementation

    override fun onRoll() {
        for (frag in childFragmentManager.fragments){
            if (frag is RollListener) {
                frag.onRoll()
            }
        }
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