package com.pip.unitskoda.memo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kserno.baseclasses.BaseRecyclerAdapter
import com.kserno.baseclasses.BaseViewHolder
import com.pip.unitskoda.R

class MemoAdapter : BaseRecyclerAdapter<Memo, MemoViewHolder>() {

    override fun createViewHolder(context: Context, parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_memo, parent, false);

        return MemoViewHolder(view)
    }
}

class MemoViewHolder(itemView: View) : BaseViewHolder<Memo>(itemView) {

    val tvName: TextView = itemView.findViewById(R.id.tvName)
    val tvContent: TextView = itemView.findViewById(R.id.tvContent)


    override fun update() {
        tvName.text = item.attendee.name
        tvContent.text = item.text
    }

}

