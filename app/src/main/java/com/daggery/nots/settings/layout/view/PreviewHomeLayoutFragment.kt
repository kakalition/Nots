package com.daggery.nots.settings.layout.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentPreviewHomeLayoutBinding
import com.daggery.nots.settings.layout.utils.PreviewHomeLayoutUtils
import com.daggery.nots.settings.layout.utils.bind

class PreviewHomeLayoutFragment : Fragment() {

    private var _viewBinding: FragmentPreviewHomeLayoutBinding? = null
    internal val viewBinding get() = _viewBinding!!

    private var _fragmentUtils: PreviewHomeLayoutUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    internal val args: PreviewHomeLayoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentPreviewHomeLayoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = PreviewHomeLayoutUtils(this)

        fragmentUtils.bindsToolbar()

        val noteItemTileRes = if (args.homeLayoutKey == 0) {
            R.drawable.bg_note_item_filled
        } else { R.drawable.bg_note_item_outlined }

        with(viewBinding) {
            previewOne.bind(fragmentUtils, "One", noteItemTileRes)
            previewTwo.bind(fragmentUtils, "Two", noteItemTileRes)
            previewThree.bind(fragmentUtils, "Three", noteItemTileRes)
            previewFour.bind(fragmentUtils, "Four", noteItemTileRes)
        }
    }
}