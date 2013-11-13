package com.example.projetofinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private BluetoothSocket mmSocket;

	private ListView bluetoothList;
	LayoutInflater inflater;

	BluetoothAdapter bluetoothAdapter;
	BluetoothDevicesArrayAdapter devicesArrayAdapter;
	List<BluetoothDevice> devices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inflater = getLayoutInflater();

		bluetoothList = (ListView) findViewById(R.id.list_bluetooth);

		bluetoothList.setOnItemClickListener(new ListClickHandler());

		/**
		 * verifica se o bluetooth estah ativado, se nao, tenta ativar, se nao
		 * for ativado para a aplicacao
		 */
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		checkEnabled();
	}

	public void checkEnabled() {
		if (!this.bluetoothAdapter.isEnabled()) {
			Intent enableBluetooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
		} else {
			setUp();
		}
	}

	private List<BluetoothDevice> getBondedDevices() {
		Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter
				.getBondedDevices();
		List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>(
				bondedDevices);

		return devices;
	}

	BroadcastReceiver receiver = null;

	public void setUp() {
		bluetoothAdapter.startDiscovery();

		devices = getBondedDevices();

		devicesArrayAdapter = new BluetoothDevicesArrayAdapter(this, devices);
		bluetoothList.setAdapter(devicesArrayAdapter);

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					devices.add(device);
					devicesArrayAdapter = new BluetoothDevicesArrayAdapter(
							getApplicationContext(), devices);
					bluetoothList.setAdapter(devicesArrayAdapter);
				}
			}
		};

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(receiver, filter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			Toast.makeText(this,
					"Bluetooth ativado, procurando dispositivos...",
					Toast.LENGTH_LONG).show();
			setUp();
		}
		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Bluetooth nao ativado, parando aplicacao",
					Toast.LENGTH_SHORT).show();
			this.finish();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_search_devices) {
			checkEnabled();
		}

		return true;
	}

	/**
	 * 
	 * @author josemarmagalhaes Click handler for the list of bluetooth devices
	 */
	private class ListClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long arg3) {
			/**
			 * depois de encontrar o dispositivo para se conectar para de
			 * procurar
			 */
			bluetoothAdapter.cancelDiscovery();
			if (receiver != null && receiver.isOrderedBroadcast()) {
				unregisterReceiver(receiver);
			}

			selectedDevice = devices.get(position);

			/**
			 * Inicia a conexao com o dispositivo
			 */
			Toast.makeText(getApplicationContext(),
					"Conectando ao dispotisitivo: " + selectedDevice.getName(),
					Toast.LENGTH_SHORT).show();

			connect();
			if (!hasOpenedSocket()) {
				System.out.println("Falhou a conexao. Tentando novamente");
				connect();
			}
			Toast.makeText(getApplicationContext(), "Conectado com sucesso",
					Toast.LENGTH_SHORT).show();

			Intent i = new Intent(getApplicationContext(), ListaDeComodos.class);
			startActivity(i);

			// ConnectThread connectThread = new ConnectThread(selectedDevice);
			// connectThread.run();

		}
	}

	private class BluetoothDevicesArrayAdapter extends
			ArrayAdapter<BluetoothDevice> {

		private List<BluetoothDevice> devices;

		public BluetoothDevicesArrayAdapter(Context context,
				List<BluetoothDevice> devices) {
			super(context, R.id.textViewBluetoothName, devices);
			this.devices = devices;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = inflater.inflate(R.layout.list_row_bluetooth, parent,
					false);
			TextView tvNome = (TextView) row
					.findViewById(R.id.textViewBluetoothName);
			tvNome.setText(devices.get(position).getName());
			TextView tvAddress = (TextView) row
					.findViewById(R.id.textViewBluetoothAddress);
			tvAddress.setText(devices.get(position).getAddress());
			return row;
		}

	}

}
