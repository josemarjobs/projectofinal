package com.example.projetofinal;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class BluetoothHelper {

	private static final int REQUEST_ENABLE_BT = 1;
	
	
	private BluetoothAdapter adapter;
	private Context context;

	public BluetoothHelper(Context context) {
		this.context = context;
		this.adapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public void checkEnabled(){
		if (!this.adapter.isEnabled()) {
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			((Activity) this.context).startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
		}
	}
}
