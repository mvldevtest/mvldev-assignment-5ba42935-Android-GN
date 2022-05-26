package com.mvl.mvl_assignment.ui.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.mvl.mvl_assignment.databinding.FragmentLocationNickNameBinding
import com.mvl.mvl_assignment.helper.PrefDelegate
import com.mvl.mvl_assignment.helper.SharePrefStorage
import com.mvl.mvl_assignment.ui.view.interfaces.NickNameNavigator
import com.mvl.mvl_assignment.ui.viewmodel.LocationNickNameFragmentViewmodel

import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



@AndroidEntryPoint
class LocationNickNameFragment : Fragment(), NickNameNavigator {

    private val locationNickNameViewModel: LocationNickNameFragmentViewmodel by viewModels()
    private lateinit var binding: FragmentLocationNickNameBinding
    var mContext:Context?=null
    var sharePrefStorage: SharePrefStorage = SharePrefStorage()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        PrefDelegate.init(requireActivity())
        binding = FragmentLocationNickNameBinding.inflate(inflater)
        //retainInstance = true
        binding.lifecycleOwner = this
        binding.nickNameFragmentViewModel = locationNickNameViewModel
        binding.nickNameFragmentViewModel?.nickNameNavigation = this
        mContext = requireActivity()
        initDeclaration(binding)
        // Inflate the layout for this fragment
        return binding.root
    }

    fun initDeclaration(binding: FragmentLocationNickNameBinding) {
        val args = requireArguments().getString("labelName")
        if(args!=null){
            locationNickNameViewModel.locationLabel.value=args
            if(args.equals("Location A",ignoreCase = true)) {
                locationNickNameViewModel.address.value ="Location A"+":"+sharePrefStorage.mapALocation
                locationNickNameViewModel.qualityValue.value ="Quality ari value :"+ sharePrefStorage.mapQAValue
            }else if (args.equals("Location B",ignoreCase = true)){
                locationNickNameViewModel.address.value ="Location B"+":"+ sharePrefStorage.mapBLocation
                locationNickNameViewModel.qualityValue.value ="Quality ari value :"+sharePrefStorage.mapQBValue
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocationNickNameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }

    override fun navigationToMapFragment(label: String) {
        findNavController().navigate(LocationNickNameFragmentDirections.actionMapFragment(label))

    }
}