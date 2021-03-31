package com.baokiin.demo

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlin.math.abs

class AdapterItem(val list:MutableList<DataItem>, val x:(Int, String)->Unit) :
    RecyclerView.Adapter<AdapterItem.ViewHodel>() {
    private var posXDown = 0f
    private var posYDown = 0f
    inner class ViewHodel(v: View): RecyclerView.ViewHolder(v)
    {
        private var onclick:ListenerOnclick? = null
        val checkBox: View =v.checkLayout
        val name: TextView = v.txtName
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
        val view = LayoutInflater.from(parent.context).inflate( R.layout.item_layout,parent,false)
        return ViewHodel(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        holder.setMyOnClick {  pos ->
            if(!list[pos].check){
                list[pos].check = true
                holder.checkBox.visibility = View.VISIBLE
            }
            else{
                list[pos].check = false
                holder.checkBox.visibility = View.GONE
            }
            x(pos,list[position].name)
        }
        holder.name.text = list[position].name
        if(list[position].check)
            holder.checkBox.visibility = View.VISIBLE
        else
            holder.checkBox.visibility = View.GONE
    }

}
