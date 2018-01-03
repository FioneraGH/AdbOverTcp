package com.fionera.adbtcp

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.fionera.adbtcp.adapter.InterfaceInfoListAdapter
import com.fionera.adbtcp.util.CommandExecutor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.NetworkInterface

class MainActivity : BaseActivity() {

    private val interfaceInfoList = arrayListOf<NetworkInterface>()

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_tcp_ip_list.layoutManager = LinearLayoutManager(mContext)
        rv_tcp_ip_list.adapter = InterfaceInfoListAdapter(mContext, interfaceInfoList)

        Log.e("AOT", "Start scanning")

        btn_refresh_prop.setOnClickListener {
            refreshDeviceProp()
        }

        btn_set_port_directly.setOnClickListener {
            val port = et_tcp_port_to_use.text.toString()
            if (port == "" || port.length < 4) {
                Toast.makeText(mContext, "Port Illegal: " + port, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            CommandExecutor.execCommandsAsSU(arrayOf(getString(R.string.commands_to_set_tcp_port_of_prop, port)))
            CommandExecutor.execCommandsAsSU(resources.getStringArray(R.array.commands_to_enable_tcp_forward))
            tv_using_ip_for_forward.text = tv_current_prop_wifi_address.text
        }

        refreshDeviceProp()
    }

    private fun refreshDeviceProp() {
        launch(UI) {
            val getWifiAddressDefer = async {
                CommandExecutor.execCommand(getString(R.string.commands_to_get_ip_on_wlan0))
            }

            val getTcpPortDefer = async {
                CommandExecutor.execCommand(getString(R.string.commands_to_get_tcp_port_of_prop))
            }

            val interfaceInfoListDefer = async {
                val interfaceInfoListTemp = arrayListOf<NetworkInterface>()

                Log.e("AOT", "All interface count: "
                        + NetworkInterface.getNetworkInterfaces().toList().apply { interfaceInfoListTemp.addAll(this) }.size)

                interfaceInfoListTemp
            }

            tv_current_prop_wifi_address.text = getString(R.string.current_prop_tcp_port_1_s, getWifiAddressDefer.await())
            tv_current_prop_tcp_port.text = getString(R.string.current_prop_tcp_port_1_s, getTcpPortDefer.await())

            interfaceInfoList.clear()
            interfaceInfoList.addAll(interfaceInfoListDefer.await())
            rv_tcp_ip_list.adapter.notifyDataSetChanged()
        }
    }

}

