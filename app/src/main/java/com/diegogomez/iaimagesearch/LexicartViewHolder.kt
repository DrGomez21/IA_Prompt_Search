package com.diegogomez.iaimagesearch

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diegogomez.iaimagesearch.databinding.ItemLexicartBinding
import com.squareup.picasso.Picasso

class LexicartViewHolder(view:View) : RecyclerView.ViewHolder(view){
    private val binding = ItemLexicartBinding.bind(view)

    fun bind(
        data:LexicartData,
        onClickListener: (LexicartData) -> Unit,
        onClickDownloader: (LexicartData) -> Unit
    ){
        Picasso.get().load(data.src).into(binding.ivLexicart)
        binding.ivLexicart.setOnClickListener { scaleImage() }
        binding.bExpandir.setOnClickListener { expandirCard() }
        binding.bCopy.setOnClickListener { onClickListener(data) }
        binding.bDownload.setOnClickListener { onClickDownloader(data) }
        binding.tvPrompt.text = data.prompt
        binding.tvDimension.text = "Dimensiones: ${data.width} X ${data.height}"
        binding.tvModel.text = "Modelo: ${data.model}"
    }

    private fun expandirCard() {
        if (binding.llDataContainer.visibility == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.cardViewMain, AutoTransition())
            binding.llDataContainer.visibility = View.VISIBLE
            binding.bExpandir.text = "Menos info"
        } else {
            TransitionManager.beginDelayedTransition(binding.cardViewMain, AutoTransition())
            binding.llDataContainer.visibility = View.GONE
            binding.bExpandir.text = "Ver info"
        }
    }

    private fun scaleImage() {
        if ( binding.ivLexicart.scaleType == ImageView.ScaleType.CENTER_CROP ) {
            binding.ivLexicart.scaleType = ImageView.ScaleType.CENTER
        } else {
            binding.ivLexicart.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

}