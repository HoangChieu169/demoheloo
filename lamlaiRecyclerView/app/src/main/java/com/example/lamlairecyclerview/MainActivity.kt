package com.example.lamlairecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.view.menu.MenuView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell.view.*
import android.widget.Adapter as Adapter1

interface AdapterDelegate {
    fun showDetailActivity(selectedIndex : Int ?=null)
}


class MainActivity : AppCompatActivity() {
    var list = MutableList(100) { it }
    var selectedIndex: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(list ,this)
    }

    fun showDetailActivity(selectedIndex: Int?){
        this.selectedIndex=selectedIndex
     val intent = Intent(this, DetailActivity::class.java)
     if (selectedIndex != null) {
         intent.putExtra("putDataToDetails", list[selectedIndex!!])
     }
     startActivityForResult(intent, 0)
 }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (requestCode != 0 || resultCode != 0) return
        var number = data.getIntExtra("Putback", Int.MIN_VALUE)
        if (number == Int.MIN_VALUE) return
        if (selectedIndex!=null){
            list[selectedIndex!!]=number
            recyclerView.adapter?.notifyItemInserted(selectedIndex!!)
        }else{
        recyclerView.adapter?.notifyItemInserted(list.size - 1)
        recyclerView.scrollToPosition(list.size - 1)}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        showDetailActivity(selectedIndex)
        return true
    }


    class Adapter(var list: MutableList<Int>, var delegate: MainActivity) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        enum class ViewType(val value: Int){
            ITEM_VIEW(0),
            PROGRESS_VIEW(1)
        }
        val pagingSize=20
        val maxNumberOfItem=100
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType==ViewType.ITEM_VIEW.value){
                val itemView=LayoutInflater.from(parent.context).inflate(R.layout.cell,parent,false)
                val viewHolder = MyViewHolder(itemView)
                itemView.setOnLongClickListener {
                    val removedIndex = viewHolder.adapterPosition
                    list.removeAt(removedIndex)
                    notifyItemRemoved(removedIndex)
                    true
                }
                return MyViewHolder(itemView)
            }else{
                val itemView=LayoutInflater.from(parent.context).inflate(R.layout.proges_cell,parent,false)
                val viewHolder = MyViewHolder(itemView)
                itemView.setOnLongClickListener {
                    val removedIndex = viewHolder.adapterPosition
                    list.removeAt(removedIndex)
                    notifyItemRemoved(removedIndex)
                    true
                }
                return ProgessViewHolder(itemView)
            }



        }

        override fun getItemCount(): Int = if (list.size==maxNumberOfItem)maxNumberOfItem else list.size+1


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MyViewHolder) {
                if(position<list.size){
                holder.itemView.editText.text = list[position].toString()
            }else{
                    loadMore()
                }
            }
        }
        fun loadMore(){
            if (list.size >= maxNumberOfItem){return}
           Handler().postDelayed({
               if (list.size+pagingSize>=maxNumberOfItem){
               list.addAll(list.size, MutableList(maxNumberOfItem-list.size){it+1+list.last()})
               notifyDataSetChanged()}else{
                   list.addAll(list.size,MutableList(pagingSize){it+1+list.last()})
                   notifyDataSetChanged()
               }
           },100)
        }

        override fun getItemViewType(position: Int): Int {
            if (position==maxNumberOfItem-1){
                return ViewType.ITEM_VIEW.value
            }
            return  if (position==list.size)ViewType.PROGRESS_VIEW.value else ViewType.ITEM_VIEW.value
        }

    }
}
class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView )
class ProgessViewHolder(itemView: View): RecyclerView.ViewHolder(itemView )







