package com.fionera.adbtcp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fionera.adbtcp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rv_interface_info.*
import java.net.NetworkInterface

/**
 * InterfaceInfoListAdapter
 * Created by fionera on 03/01/2018 in AdbOverTCP.
 */
class InterfaceInfoListAdapter(val context: Context, val dataList: List<NetworkInterface>) : RecyclerView.Adapter<InterfaceInfoListAdapter.InterfaceInfoHolder>() {
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InterfaceInfoHolder, position: Int) {
        val networkInterface = dataList[position]
        holder.tv_interface_info_name.text = networkInterface.displayName
        val inetAddrs = networkInterface.inetAddresses
        var inetAddrsInfo = ""
        while (inetAddrs.hasMoreElements()) {
            val inetAddr = inetAddrs.nextElement()
            inetAddrsInfo += "[${inetAddr.hostAddress}]\n"
        }
        holder.tv_interface_info_ip_address.text = inetAddrsInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = InterfaceInfoHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_interface_info, parent, false))

    override fun getItemCount() = dataList.size


    class InterfaceInfoHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}