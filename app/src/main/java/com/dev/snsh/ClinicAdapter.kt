package com.dev.snsh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.dev.snsh.model.ConfigItem
import com.dev.snsh.model.ConfigModel
import com.squareup.picasso.Picasso

class ClinicAdapter
    (
    private var list: List<ConfigItem>,
    private var rowClickListener: MyRecyclerViewRowClickListener2?,
    private var configModel: ConfigModel
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(
                inflater.inflate(R.layout.viewholder_clinic, parent, false),
                rowClickListener
            )
    }

    override fun onBindViewHolder(
        holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as MyViewHolder).bindData(list[position], position, configModel)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /* VIEW HOLDERS */

    internal class MyViewHolder(itemView: View, rowClickListener: MyRecyclerViewRowClickListener2?) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        private var tvTitle = itemView.findViewById<View>(R.id.tvTitle) as TextView
        private var tvDescription = itemView.findViewById<View>(R.id.tvDescription) as TextView
        private var tvStar = itemView.findViewById<View>(R.id.tvStar) as TextView
        private var tvOpeningHour = itemView.findViewById<View>(R.id.tvOpeningHour) as TextView
        private var ibCall = itemView.findViewById<View>(R.id.ibCall) as ImageButton
        private var ibChat = itemView.findViewById<View>(R.id.ibChat) as ImageButton
        private var ibMore = itemView.findViewById<View>(R.id.ibMore) as ImageButton
        private var ivImageView = itemView.findViewById<View>(R.id.ivImageView) as ImageView

        init {
            itemView.setSafeOnClickListener {
                rowClickListener?.onViewClicked(it,adapterPosition)
            }
            ibCall.setSafeOnClickListener {
                rowClickListener?.onViewClicked(it,adapterPosition)
            }
            ibChat.setSafeOnClickListener {
                rowClickListener?.onViewClicked(it,adapterPosition)
            }
            ibMore.setSafeOnClickListener {
                rowClickListener?.onViewClicked(it,adapterPosition)
            }
        }

        fun bindData(dataModel: ConfigItem, position: Int ,configModel: ConfigModel) {

            tvTitle.text = dataModel.name
            tvDescription.text = dataModel.description
            tvStar.text = dataModel.rating.toFloat().toString()
            tvOpeningHour.text = "${dataModel.openHourFrom} - ${dataModel.openHourTo}"

            if(MainActivity.checkIsOpeningHour(configModel,position)){
                ibCall.visibility = View.VISIBLE
                ibChat.visibility = View.VISIBLE
            }else{
                ibCall.visibility = View.GONE
                ibChat.visibility = View.GONE
            }

            if (!dataModel.image.isNullOrEmpty())
                Picasso.get().load(dataModel.image)
                    .into(ivImageView)

        }
    }
}