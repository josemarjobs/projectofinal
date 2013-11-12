package com.example.projetofinal;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

public class BaseActivity extends Activity {

	static final int REQUEST_ENABLE_BT = 1;
	static final UUID UU_ID = new UUID(1, 4);

	static BluetoothSocket openSocket;
	static BluetoothDevice selectedDevice;

	void disconnectDevice() {
		if (openSocket != null && openSocket.isConnected()) {
			try {
				openSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	boolean hasOpenedSocket() {
		return openSocket.isConnected();
	}
	public void connect() {
		ConnectThread thread = new ConnectThread(selectedDevice);
		thread.run();
	}


	/**
	 * connection helper
	 */

	class ConnectThread extends Thread {

		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;

			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				mmDevice.fetchUuidsWithSdp();
				ParcelUuid[] uuids = mmDevice.getUuids();
				System.out.println(Arrays.toString(uuids));

				tmp = device.createRfcommSocketToServiceRecord(uuids[0]
						.getUuid());
			} catch (IOException e) {
			}
			openSocket = tmp;
		}

		public void run() {

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception

				openSocket.connect();

			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					openSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// Do work to manage the connection (in a separate thread)
			// manageConnectedSocket(mmSocket);
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				openSocket.close();
			} catch (IOException e) {
			}
		}
	}
//
//	class ConnectTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket> {
//
//		private Context context;
//		private AlertDialog dialog;
//
//		public ConnectTask(Context context) {
//			this.context = context;
//		}
//
//		@Override
//		protected void onPreExecute() {
//
//			AlertDialog.Builder builder = new Builder(context);
//			builder.setTitle("Connectar");
//			builder.setMessage("Conectando ao dispositivo");
//			builder.setCancelable(false);
//			builder.setNeutralButton("Cancelar", new OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					ConnectTask.this.cancel(true);
//					dialog.dismiss();
//				}
//			});
//			this.dialog = builder.create();
//			this.dialog.show();
//		}
//
//		@Override
//		protected BluetoothSocket doInBackground(BluetoothDevice... params) {
//			BluetoothSocket tmp = null;
//
//			BluetoothDevice device = params[0];
//			BluetoothDevice mmDevice = device;
//
//			// Get a BluetoothSocket to connect with the given BluetoothDevice
//			try {
//				// MY_UUID is the app's UUID string, also used by the server
//				// code
//				mmDevice.fetchUuidsWithSdp();
//				ParcelUuid[] uuids = mmDevice.getUuids();
//				System.out.println(Arrays.toString(uuids));
//
//				tmp = device.createRfcommSocketToServiceRecord(uuids[0]
//						.getUuid());
//			} catch (IOException e) {
//			}
//			BluetoothSocket mmSocket = tmp;
//
//			try {
//				// Connect the device through the socket. This will block
//				// until it succeeds or throws an exception
//				mmSocket.connect();
//			} catch (IOException connectException) {
//				// Unable to connect; close the socket and get out
//				try {
//					mmSocket.close();
//				} catch (IOException closeException) {
//				}
//				return null;
//			}
//
//			return mmSocket;
//		}
//
//		@Override
//		protected void onPostExecute(BluetoothSocket result) {
//			openSocket = result;
//			this.dialog.dismiss();
//			if (chamaListaDeComodos) {
//				Intent intent = new Intent(BaseActivity.this,
//						ListaDeComodos.class);
//				startActivity(intent);
//			}
//
//		}
//	}
}
