package com.baokiin.demo

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_navi.view.*
import kotlin.math.abs

class AdapterNavi(val list:MutableList<String>, val x:(Int)->Unit) :
    RecyclerView.Adapter<AdapterNavi.ViewHodel>() {
    private var posXDown = 0f
    private var posYDown = 0f
    inner class ViewHodel(v: View): RecyclerView.ViewHolder(v)
    {
        private var onclick:ListenerOnclick? = null
        val name: TextView = v.txtTitleNavi
        fun setMyOnClick (action: (Int) -> Unit = {_ ->}) {
            onclick = object : ListenerOnclick {
                override fun onItemClick(view: View, position: Int) {
                    super.onItemClick(view, position)
                    action(position )
                }
            }
        }
        private fun onClick(view:View){
            view.setOnTouchListener { v, event ->
                v.performClick()
                when(event.action){
                    MotionEvent.ACTION_UP ->{
                        if(abs(event.rawX - posXDown) <10f && abs(event.rawY - posYDown) <10f){
                            onclick?.onItemClick(v, adapterPosition)
                        }

                    }
                    MotionEvent.ACTION_DOWN ->{
                        posXDown = event.rawX
                        posYDown = event.rawY
                    }
                }
                true
            }
        }
        init {
            onClick(itemView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodel {
        val view = LayoutInflater.from(parent.context).inflate( R.layout.item_navi,parent,false)
        return ViewHodel(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        holder.setMyOnClick {  pos ->
            x(pos)
        }
        holder.name.text = list[position]
    }

}
