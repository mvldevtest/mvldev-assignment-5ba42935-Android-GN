package com.mvl.mvl_assignment.ui.view.fragments

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.data.model.response.GetListAddress
import com.mvl.mvl_assignment.databinding.FragmentRequestBookBinding
import com.mvl.mvl_assignment.helper.PrefDelegate
import com.mvl.mvl_assignment.helper.SharePrefStorage
import com.mvl.mvl_assignment.ui.view.interfaces.BookNavigator
import com.mvl.mvl_assignment.ui.viewmodel.RequestBookFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class RequestBookFragment : Fragment(),BookNavigator {
    private lateinit var navController: NavController

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val viewModel:RequestBookFragmentViewModel by viewModels()
    private lateinit var binding: FragmentRequestBookBinding
    var mContext: Context?=null
    var sharePrefStorage: SharePrefStorage = SharePrefStorage()
    var id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PrefDelegate.init(requireActivity())
        binding = FragmentRequestBookBinding.inflate(inflater)
        //retainInstance = true
        binding.lifecycleOwner = this
        binding.requestBookViewModel = viewModel

        mContext = requireActivity()
        initDeclaration(binding)
        viewModel.bookNavigator=this

        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    sharePrefStorage.mapBLocation=""
                    sharePrefStorage.mapALocation=""
                    sharePrefStorage.mapQBValue=""
                    sharePrefStorage.mapQAValue=""
                    sharePrefStorage.lngAValue=""
                    sharePrefStorage.lngBValue=""
                    sharePrefStorage.Loc_NickName_A=""
                    sharePrefStorage.Loc_NickName_b=""
                    findNavController().navigate(RequestBookFragmentDirections.actionMapFragment())
                    // if you want onBackPressed() to be called as normal afterwards

                }
            }
        )
        return binding.root
    }



    private fun initDeclaration(binding: FragmentRequestBookBinding) {
        val args = requireArguments().getString("id")
        if(args!=null){
            id=args
        }
        viewModel.onLocationRequestAPI(id)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RequestBookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSuccess(getAddress: GetListAddress) {

    }

    override fun onErrorMsg(errorMsg: String) {

    }

    override fun navigation() {
        findNavController().navigate(RequestBookFragmentDirections.actionListFragment("yearlistffragment"))
    }

}