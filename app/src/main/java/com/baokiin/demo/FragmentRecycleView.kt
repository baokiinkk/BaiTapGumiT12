package com.baokiin.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_recycle_view.view.*
import kotlinx.android.synthetic.main.title_layout.view.*
import java.util.*
import kotlin.math.abs

class FragmentRecycleView : Fragment() {

    private var onclick:ListenerOnclick? = null
    private var posXDown = 0f
    private var posYDown = 0f
    private var countCheck = 0
    private var positionItemChoose:Int? = null
    private val list:MutableList<DataItem> by lazy {
        mutableListOf<DataItem>()
    }
    private lateinit var adapter: AdapterItem
    init {
        for(i in 0..5)
            list.add(DataItem("Quốc Bảo $i",false))
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_recycle_view, container, false)


        // onClick vào item thì sẽ set title và pos
        adapter = AdapterItem(list){ pos, name->
            view.includeTitle.name.text = name
            view.includeTitle.pos.text = "Position: $pos"
            positionItemChoose = pos
        }
//
        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(context)
        view.recycleView.adapter=adapter
        view.recycleView.layoutManager=linearLayout

        // bắt sự kiện vuốt kéo thả để xóa
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,  ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                Collections.swap(list, from, to)
                adapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                list.removeAt(viewHolder.adapterPosition)
                adapter.notifyDataSetChanged()
            }

        }).attachToRecyclerView(view.recycleView)



        onClickCustom(view.includeTitle)
        onClickCustom(view.btnAddItem)
        myOnClick{
            when(it.id){
                R.id.includeTitle -> {
                    countCheck = 0
                    for(i in 0 until  adapter.list.size)
                        if(adapter.list[i].check)
                            countCheck++
                        Toast.makeText(requireContext(), countCheck.toString(), Toast.LENGTH_SHORT).show()
                }

                R.id.btnAddItem->{
                        addItemRecycleView()
                }

            }

        }

        return view
    }

    private fun addItemRecycleView(){
        list.add(DataItem("Quốc bảo ${list[list.size - 1].name.substring(9).toInt() + 1}", false))
        adapter.notifyItemInserted(list.size - 1)
    }

    // ghi đè vào hàm onClickListener và quăng view ra ngoài hàm
    private fun myOnClick(action: (View) -> Unit) {
        onclick = object : ListenerOnclick {
            override fun onClickListener(view: View) {
                super.onClickListener(view)
                action(view)
            }
        }

    }

    // bắt xự kiện, khai báo onClick cho view
    private fun onClickCustom(view:View){
        view.setOnTouchListener { v, event ->
            v.performClick()
            when(event.action){
                MotionEvent.ACTION_UP ->{
                    if(abs(event.rawX - posXDown) <10f && abs(event.rawY - posYDown) <10f){
                        onclick?.onClickListener(v)
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

}