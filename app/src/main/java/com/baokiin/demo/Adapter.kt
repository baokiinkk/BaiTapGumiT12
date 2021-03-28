package com.baokiin.demo

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlin.math.abs

class Adapter(val list:MutableList<DataItem>, val x:(Int)->Unit) :
    RecyclerView.Adapter<Adapter.ViewHodel>() {
    private var posXDown = 0f
    private var posYDown = 0f
    var countCheck = 0
    inner class ViewHodel(v: View): RecyclerView.ViewHolder(v)
    {
        private var onclick:ListenerOnclick? = null
        val checkBox: View =v.checkLayout
        val name = v.txtName
        fun setMyOnClick (action: (View, Int) -> Unit = {_,_ ->}) {
            onclick = object : ListenerOnclick {
                override fun onItemClick(view: View, position: Int) {
                    super.onItemClick(view, position)
                    action(view,position )
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
        val viewHodel=ViewHodel(view)
        return viewHodel
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHodel, position: Int) {
        holder.setMyOnClick { view, pos ->
            if(list[pos].check != true){
                list[pos].check = true
                countCheck++
                holder.checkBox.visibility = View.VISIBLE
            }
            else{
                list[pos].check = false
                countCheck--
                holder.checkBox.visibility = View.GONE
            }
            x(pos)
        }
        holder.name.text = list[position].name
        if(list[position].check == true)
            holder.checkBox.visibility = View.VISIBLE
        else
            holder.checkBox.visibility = View.GONE
    }

}
