package com.baokiin.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.app_bar.view.*
import kotlinx.android.synthetic.main.navigation_view.view.*

class MainActivity : AppCompatActivity() {
    private  var listFragment:MutableList<Fragment> = mutableListOf(FragmentRecycleView(),FragmentLogicCode())
    private  var listTitleNavi:MutableList<String> = mutableListOf("RecycleView","LogicCode")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // onClick các item sẽ replace lại các fragment
        val adapter = AdapterNavi(listTitleNavi){
            replaceFragment(listFragment[it])
            textView.text = listTitleNavi[it]
            drawerLayout.closeDrawers()
        }

        val linearLayout: RecyclerView.LayoutManager = LinearLayoutManager(this)
        nav_view.naviRecycle.adapter=adapter
        nav_view.naviRecycle.layoutManager=linearLayout

        // tabLayout
        includeActionBar.tablayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    replaceFragment(listFragment[it.position])
                    textView.text = listTitleNavi[it.position]
                }
            }

        })


        // up button
        btnBack.setOnClickListener {
            if(supportFragmentManager.backStackEntryCount <= 0){
                drawerLayout.openDrawer(nav_view)
            }
            else
                onBackPressed()
        }

    }

    // mỗi khi repace thì em cho addBackStack để test up button, vì em không muốn nó add
    fun replaceFragment(fragment:Fragment){

            btnBack.setBackgroundResource(R.drawable.back)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.viewpager,fragment)
                .addToBackStack(fragment::class.java.simpleName)
                .commit()

    }

    // set lại icon khi stack == 0
    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount == 0)
            btnBack.setBackgroundResource(R.drawable.menu)

    }

}
interface ListenerOnclick {
    fun onItemClick(view: View, position: Int) {

    }
    fun onClickListener(view:View){}
}