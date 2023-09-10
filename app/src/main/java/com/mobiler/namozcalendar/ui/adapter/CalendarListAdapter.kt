package com.mobiler.namozcalendar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobiler.namozcalendar.database.prayer.PrayerEntity
import com.mobiler.namozcalendar.databinding.ItemCalendarBinding

class CalendarListAdapter() : ListAdapter<PrayerEntity, CalendarListAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<PrayerEntity>() {
        override fun areItemsTheSame(oldItem: PrayerEntity, newItem: PrayerEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PrayerEntity, newItem: PrayerEntity): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(private val itemCalendarBinding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(itemCalendarBinding.root) {
        fun onBind(prayerEntity: PrayerEntity) {
            itemCalendarBinding.apply {
                tvTime.text = "${prayerEntity.data1} - ${prayerEntity.data2}"
                tv1.text = prayerEntity.bomdod
                tv2.text = prayerEntity.peshin
                tv3.text = prayerEntity.asr
                tv4.text = prayerEntity.shom
                tv5.text = prayerEntity.xufton
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }
}