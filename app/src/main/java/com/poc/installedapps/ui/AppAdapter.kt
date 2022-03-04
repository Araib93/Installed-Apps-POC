package com.poc.installedapps.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.poc.installedapps.R
import com.poc.installedapps.model.App
import kotlinx.android.synthetic.main.layout_app.view.*

class AppAdapter(private val actions: AppActions? = null) : RecyclerView.Adapter<AppViewHolder>() {
    private val apps: ArrayList<App> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_app, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        holder.bind(app, actions)
    }

    override fun getItemCount() = apps.size

    fun addApps(apps: List<App>) {
        this.apps.addAll(apps)
        notifyDataSetChanged()
    }

    fun clear() {
        apps.clear()
        notifyDataSetChanged()
    }
}

class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(app: App, actions: AppActions?) {
        itemView.tv_appName.text = app.name
        Glide.with(itemView).load(app.imageUrl).into(itemView.iv_appIcon)
        itemView.setOnClickListener {
            actions?.openApp(app.`package`)
        }
        itemView.setOnLongClickListener {
            actions?.openAppSettings(app.`package`) ?: false
        }
    }
}

interface AppActions {
    fun openApp(`package`: String)
    fun openAppSettings(`package`: String): Boolean
}