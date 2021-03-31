package com.baokiin.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_logic_code.*
import kotlinx.android.synthetic.main.fragment_logic_code.view.*
import kotlinx.android.synthetic.main.fragment_recycle_view.view.btnAddItem
import kotlinx.android.synthetic.main.fragment_recycle_view.view.includeTitle
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.title_layout.view.*
import kotlin.math.abs

class FragmentLogicCode : Fragment() {


    private var onclick:ListenerOnclick? = null
    private var posXDown = 0f
    private var posYDown = 0f
    private var idItem = 0 // biến sinh id cho các view sinh ra bằng code
    private var countCheckLogic = 0 // biến đếm các view dx chọn bằng logic code
    private var viewItemChoose:View? = null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_logic_code, container, false)
        countCheckLogic = 0 // biến đếm các view dx chọn bằng logic code
        viewItemChoose = null
        onClickCustom(view.includeTitle)
        onClickCustom(view.btnAddItem)
        onClickCustom(view.btnRemoveItem)
        myOnClick{
            when(it.id){
                R.id.includeTitle -> {
                        Toast.makeText(requireContext(),countCheckLogic.toString(), Toast.LENGTH_SHORT).show()
                }

                R.id.btnAddItem->{
                        addLogicCode()
                }

                R.id.btnRemoveItem->{
                        removeLogicCode()
                }


                in 0..idItem->{ // xử lí sự kiện các item mới tạo bằng logic code
                    val viewCheck  = it.checkLayout
                    view.includeTitle.name.text = it.txtName.text
                    view.includeTitle.pos.text = "Position: ${it.id}"
                    viewItemChoose = it
                    if(viewCheck?.visibility != View.VISIBLE){
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

        return view
    }


    // khi remove thì thứ tự các view sẽ tự sắp xếp lại.
    // nên muốn xóa vị trí nào chỉ cần đưa chỉ số. Ở đây em cho chọn view nào thì xóa view đó
    private fun removeLogicCode(){
        viewItemChoose?.let {
            Item.removeView(it)
        }
    }

    //inflater ép layout về view để add nó vào parent, đồng thời cấp id và bắt xự kiện onClick
    @SuppressLint("SetTextI18n")
    private fun addLogicCode(){
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_layout,Item,false)
        view?.let {
            it.id = idItem
            it.txtName.text = "Quốc Bảo $idItem"
            idItem++
            onClickCustom(it)
            Item.addView(it)

        }

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