package com.baokiin.demo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.title_layout.view.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var onclick:ListenerOnclick? = null
    private var posXDown = 0f
    private var posYDown = 0f
    private var isRecycleView = true  //kiểm tra đang dùng cách recycleVew hay logic code(true:recycleView)
    private var idItem = 0 // biến sinh id cho các view sinh ra bằng code
    private var countCheckLogic = 0 // biến đếm các view dx chọn bằng logic code
    private var positionItemChoose:Int? = null
    private var viewItemChoose:View? = null
    private lateinit var adapter: Adapter
    private val list = mutableListOf<DataItem>()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for(i in 0..5)
            list.add(DataItem("Quốc Bảo $i",false))

        adapter = Adapter(list){pos->
                includeTitle.name.text = list[pos].name
                includeTitle.pos.text = "Position: $pos"
                positionItemChoose = pos
        }

        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycleView.adapter=adapter
        recycleView.layoutManager=linearLayout
        setSupportActionBar(toolbar)

        onClick(includeTitle)
        onClick(addItem)
        onClick(removeItem)
        myOnClick{
            when(it.id){
                R.id.includeTitle -> {
                    if(isRecycleView)
                        Toast.makeText(this, adapter.countCheck.toString(), Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this,countCheckLogic.toString(),Toast.LENGTH_SHORT).show()
                }

                R.id.addItem->{
                    if(isRecycleView) {
                       addItemRecycleView()
                    }
                    else{
                      addLogicCode()
                    }
                }

                R.id.removeItem->{
                    if(isRecycleView) {
                        removeItemRecycleView()
                    }
                    else{
                        removeLogicCode()
                    }
                }

                in 0..idItem->{
                    val viewCheck = it.checkLayout
                    includeTitle.name.text = it.txtName.text
                    includeTitle.pos.text = "Position: ${it.id}"
                    viewItemChoose = it
                    if(viewCheck.visibility != View.VISIBLE){
                        viewCheck.visibility = View.VISIBLE
                        countCheckLogic++
                    }
                    else{
                        viewCheck.visibility = View.GONE
                        countCheckLogic--

                    }
                }
            }

        }

    }
    private fun addItemRecycleView(){
         list.add(DataItem("Quốc bảo ${list[list.size - 1].name.substring(9).toInt() + 1}", false))
                adapter.notifyItemInserted(list.size - 1)
    }
    private fun removeItemRecycleView(){
        positionItemChoose?.let {
            list.removeAt(it)
            adapter.notifyItemRemoved(it)
        }
    }
    private fun removeLogicCode(){
        viewItemChoose?.let {
            Item.removeView(it)
        }
    }
    private fun addLogicCode(){
            val view = LayoutInflater.from(applicationContext).inflate(R.layout.item_layout,Item,false)
            view?.let {
                it.id = idItem
                it.txtName.text = "Quốc Bảo $idItem"
                idItem++
                onClick(it)
                Item.addView(it)
            }

    }
    private fun myOnClick(action: (View) -> Unit) {
            onclick = object : ListenerOnclick {
                override fun onClickListener(view: View) {
                        super.onClickListener(view)
                        action(view)
                }
            }

    }
    private fun onClick(view:View){
            view.setOnTouchListener { v, event ->
                v.performClick()
                when(event.action){
                    MotionEvent.ACTION_UP ->{
                        if(abs(event.rawX - posXDown)<10f && abs(event.rawY - posYDown)<10f){
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.chuyenmh->{
                positionItemChoose = null
                viewItemChoose = null
                if(isRecycleView) {
                    recycleView.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE
                    isRecycleView = false
                }
                else{
                    recycleView.visibility = View.VISIBLE
                    scrollView.visibility = View.GONE
                    isRecycleView = true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
interface ListenerOnclick {
    fun onItemClick(view: View, position: Int) {

    }
    fun onClickListener(view:View){}
}