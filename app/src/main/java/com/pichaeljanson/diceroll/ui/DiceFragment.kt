package com.pichaeljanson.diceroll.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pichaeljanson.diceroll.DiceRollingFragment
import com.pichaeljanson.diceroll.RollListener
import com.pichaeljanson.diceroll.databinding.DiceFragmentBinding

class DiceFragment : Fragment(), RollListener {

    private val diceVm: DiceViewModel by viewModels() {
        DiceViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DiceFragmentBinding.inflate(
            inflater, container, false
        )

        binding.diceVm = diceVm
        binding.lifecycleOwner = this

        if (parentFragment is DiceRollingFragment) {
            parentFragment
        }

        return binding.root
    }

    override fun onRoll() {
        diceVm.roll()
    }
}