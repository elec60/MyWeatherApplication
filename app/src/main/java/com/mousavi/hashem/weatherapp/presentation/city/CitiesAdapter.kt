package com.mousavi.hashem.weatherapp.presentation.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mousavi.hashem.weatherapp.databinding.ItemCityBinding

class CitiesAdapter(
    private val items: List<String>,
    private val onCLickCity: (String) -> Unit,
) : RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class CityViewHolder(private val itemBinding: ItemCityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION){
                    onCLickCity(items[pos])
                }
            }
        }

        fun bind(name: String) {
            itemBinding.tvCityName.text = name
        }
    }
}