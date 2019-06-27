package com.bluetree.jetpacksample.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluetree.jetpacksample.R

/**
 * A simple [Fragment] subclass.
 *
 */
class BlankFragment2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //通过getArguments的方式获取参数
        val text = arguments?.getString("showText")
        view.findViewById<TextView>(R.id.tv).setText(text)

    }


}