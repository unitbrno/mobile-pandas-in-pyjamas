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
}

class ParticipantViewHolder(itemView: View) : BaseViewHolder<Attendee>(itemView) {

    val tvName:TextView = itemView.findViewById(R.id.tvName)

    override fun update() {
        tvName.text = item.name
    }

}

