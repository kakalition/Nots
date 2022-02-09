package com.daggery.nots.settings.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentPreviewThemeBinding
import com.daggery.nots.databinding.ListItemNoteBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.utils.GeneralUtils
import com.daggery.nots.utils.ThemeEnum.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: REFACTOR
class PreviewThemeFragment : Fragment() {

    private val generalUtils = GeneralUtils()
    private var _viewBinding: FragmentPreviewThemeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private var themeKey: Int = 0
    private val args: PreviewThemeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lateinit var context: Context
        val localInflater: LayoutInflater = if(args.themeEnum == AZALEA) {
            context = ContextThemeWrapper(activity, R.style.AzaleaTheme)
            themeKey = R.style.AzaleaTheme
            inflater.cloneInContext(context)
        } else if(args.themeEnum == NORD) {
            context = ContextThemeWrapper(activity, R.style.NordTheme)
            themeKey = R.style.NordTheme
            inflater.cloneInContext(context)
        } else {
            context = ContextThemeWrapper(activity, R.style.DefaultDarkTheme)
            themeKey = R.style.DefaultDarkTheme
            inflater.cloneInContext(context)
        }
        _viewBinding = FragmentPreviewThemeBinding.inflate(localInflater, container, false)
        prepareStatusBar(context)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.previewBinding.toolbarBinding.apply {
            toolbarTitle.text = "Preview"
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        viewBinding.previewBinding.previewOne.bind("One")
        viewBinding.previewBinding.previewTwo.bind("Two")
        viewBinding.previewBinding.previewThree.bind("Three")
        viewBinding.previewBinding.previewFour.bind("Four")

        viewBinding.previewBinding.applyThemeButton.setOnClickListener { _ ->
            applyTheme()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun prepareStatusBar(context: Context) {
        generalUtils.prepareStatusBar(requireActivity(), context, themeKey)
    }

    private fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(date)
    }

    private fun ListItemNoteBinding.bind(number: String) {
        noteTitle.text = "Preview $number"
        noteBody.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
            .plus("tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis ")
            .plus("nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
        noteDate.text = getCurrentDate()
        listItemLayout.setBackgroundResource(R.drawable.bg_note_item)
    }

    private fun applyTheme() {
        val themeKey = when(args.themeEnum) {
            DEFAULT_DARK -> R.style.DefaultDarkTheme
            NORD -> R.style.NordTheme
            AZALEA -> R.style.AzaleaTheme
        }
        (requireActivity() as MainActivity).updateTheme(themeKey)
    }
}