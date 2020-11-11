package com.dev.snsh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dev.snsh.model.PetItem
import com.squareup.picasso.Picasso

class PetAdapter
    (
    private var list: List<PetItem>,
    private var rowClickListener: MyRecyclerViewRowClickListener2?
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(
                inflater.inflate(R.layout.viewholder_pet, parent, false),
                rowClickListener
            )
    }

    override fun onBindViewHolder(
        holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as MyViewHolder).bindData(list[position], position)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    internal class MyViewHolder(itemView: View, rowClickListener: MyRecyclerViewRowClickListener2?) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        private var tvTitle = itemView.findViewById<View>(R.id.tvTitle) as TextView
        private var tvDescription = itemView.findViewById<View>(R.id.tvDescription) as TextView
        private var ivImageView = itemView.findViewById<View>(R.id.ivImageView) as ImageView

        init {
            itemView.setSafeOnClickListener {
                rowClickListener?.onRowClicked(adapterPosition)
            }
        }

        fun bindData(dataModel: PetItem, position: Int) {
            tvTitle.text = dataModel.name
            tvDescription.text = dataModel.description

            if (!dataModel.image.isNullOrEmpty())
                Picasso.get().load(dataModel.image)
                    .into(ivImageView)

        }
    }
}