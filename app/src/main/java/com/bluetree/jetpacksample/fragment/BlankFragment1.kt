package com.bluetree.jetpacksample.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

import com.bluetree.jetpacksample.R
import com.bluetree.jetpacksample.utils.LogUtils

open class BlankFragment1 : Fragment(),View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn ->  Navigation.findNavController(getView()!!).navigate(R.id.action_blankFragment1_to_blankFragment2)
            R.id.btn1 -> Navigation.findNavController(getView()!!).navigate(R.id.action_blankFragment1_to_blankFragment22)
            R.id.btn2 ->  {
                //动态带参，获取参数在BlankFragment2
                val args = Bundle()
                args.putString("showText","fragment4")
                Navigation.findNavController(getView()!!).navigate(R.id.action_blankFragment1_to_blankFragment23,args)
            }
            else -> LogUtils.i(this,"click ${v.id}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn).setOnClickListener(this)
        view.findViewById<Button>(R.id.btn1).setOnClickListener(this)
        view.findViewById<Button>(R.id.btn2).setOnClickListener(this)
    }

}
