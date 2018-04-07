package com.pip.unitskoda.calendar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kserno.baseclasses.BaseRecyclerAdapter
import com.kserno.baseclasses.BaseViewHolder
import com.pip.unitskoda.R

class ParticipantAdapter : BaseRecyclerAdapter<Attendee, ParticipantViewHolder>() {

    override fun createViewHolder(context: Context, parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_participant, parent, false);

        return ParticipantViewHolder(view)
    }

    fun setUserModels(userModels: List<String>) {
        data.forEach { it.isInModel = userModels.contains(it.email) }
        notifyDataSetChanged()
    }
}

class ParticipantViewHolder(itemView: View) : BaseViewHolder<Attendee>(itemView) {

    val tvName:TextView = itemView.findViewById(R.id.tvName)
    val tvEmail:TextView = itemView.findViewById(R.id.tvEmail)


    override fun update() {
        // Color participant by usermodel
        if (item.isInModel) {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.primaryLightColor))
        } else {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.material_grey_50))
        }

        tvName.text = item.name
        tvEmail.text = item.email
    }



}

