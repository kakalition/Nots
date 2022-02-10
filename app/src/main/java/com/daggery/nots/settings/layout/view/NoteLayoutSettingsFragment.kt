package com.daggery.nots.settings.layout.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.nots.MainViewModel
import com.daggery.nots.databinding.FragmentNoteLayoutSettingsBinding
import com.daggery.nots.settings.layout.utils.NoteLayoutSettingsUtils

class NoteLayoutSettingsFragment : Fragment() {

    private var _viewBinding: FragmentNoteLayoutSettingsBinding? = null
    val viewBinding get() = _viewBinding!!

    val viewModel: MainViewModel by activityViewModels()

    private var _fragmentUtils: NoteLayoutSettingsUtils? = null
    val fragmentUtils get() = _fragmentUtils!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentNoteLayoutSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = NoteLayoutSettingsUtils(this)

        with(fragmentUtils) {
            bindsToolbar()
        }
    }
}