package com.example.daynightmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.daynightmode.databinding.FragmentFirstBinding
import com.example.daynightmode.skin.AppThemeType
import com.example.daynightmode.skin.AppThemeController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BaseVMFragment<FirstViewModel>() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createVm()
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = mVm
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.origin.setOnClickListener {
            AppThemeController.changeSkin(AppThemeType.DEFAULT)
        }
        binding.red.setOnClickListener {
            AppThemeController.changeSkin(AppThemeType.RED)
        }
        binding.green.setOnClickListener {
            AppThemeController.changeSkin(AppThemeType.GREEN)
        }
        binding.originTheme.text = themText()
        binding.originTheme.setOnClickListener {
            AppThemeController.changeCompanion()
            binding.originTheme.text = themText()
        }

    }

    private fun themText(): String {
        val isShowCompanion = AppThemeController.isShowCompanion
        return if (isShowCompanion) {
            "当前为伴生：点击切换为默认"
        } else {
            "当前为默认：点击切换为伴生"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}