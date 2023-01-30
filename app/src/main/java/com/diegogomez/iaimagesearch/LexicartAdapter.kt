package com.diegogomez.iaimagesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LexicartAdapter(
    private val information:List<LexicartData>,
    private val onClickListener:(LexicartData) -> Unit,
    private val onClickDownloader:(LexicartData) -> Unit
):RecyclerView.Adapter<LexicartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LexicartViewHolder {
        val layoutInflater =LayoutInflater.from(parent.context)
        return LexicartViewHolder(layoutInflater.inflate(R.layout.item_lexicart, parent, false))
    }

    override fun onBindViewHolder(holder: LexicartViewHolder, position: Int) {
        val item = information[position]
        holder.bind(item, onClickListener, onClickDownloader)
    }

    override fun getItemCount(): Int {
        return information.size
    }

}