package com.bluetree.jetpacksample.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bluetree.jetpacksample.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CustomViewFragment : Fragment() {
    private var param1: String? = null
    private var param2: Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var layoutId = R.layout.fragment_custom_draw_a_circle
        when (param2) {
            R.id.nav_draw_point -> {
                layoutId = R.layout.fragment_custom_draw_a_circle
            }
            R.id.nav_draw_line ->{
                layoutId = R.layout.fragment_custom_draw_many_circle
            }
            R.id.nav_draw_tree ->{
                layoutId = R.layout.activity_tree_view
            }
        }
        return inflater.inflate(layoutId, container, false)
    }


    companion object {

        val ARG_PARAM1 = "page_index"
        val ARG_PARAM2 = "param2"


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
                CustomViewFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putInt(ARG_PARAM2, param2)
                    }
                }
    }
}
