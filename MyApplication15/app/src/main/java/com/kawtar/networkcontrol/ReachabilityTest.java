package com.kawtar.networkcontrol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

//! ReachabilityTest class
/**
 * Class that handles the network availability and access.
 * It verifies that a network is available and that an address is reachable.
 * It launches an async task to establish a connection to a website.
 */
public class ReachabilityTest extends AsyncTask<Void, Void, Boolean> {
	/**
	 * Private variable. Details: Used to define the app's context.
	 */
	private Context		mContext;
	/**
	 * Private variable. Details: Used to define the host.
	 */
	private String		mHostname;
	/**
	 * Private variable. Details: used to define the port.
	 */
	private int			mServicePort;
	/**
	 * Private variable. Details: Used to define the interface.
	 */
	private Callback	mCallback;

	/**
	 * Define two interfaces: Connection successful and unsuccessful.
	 */
	public interface Callback {
		void onReachabilityTestPassed();

		void onReachabilityTestFailed();
	}

	/**
	 * Class constructor. It initializes both the app's context, host,port and interface.
	 //param Context context : The activity context.
	 //@param String hostname : Ip address.
	 //param int port : Connection's port=80 (Android).
	 //param  Callback callback : Interfaces (test failes or passed).
	 */
	public ReachabilityTest(Context context, String hostname, int port, Callback callback) {
		/**
		 * Initialize variables.
		 */
		mContext = context.getApplicationContext(); // Avoid leaking the Activity!
		mHostname = hostname;
		mServicePort = port;
		mCallback = callback;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	protected Boolean doInBackground(Void... args) {
		/**
		 * Verify that a network is available.
		 * 
		 * @see function isConnected.
		 */
		if (isConnected(mContext))
		{
			InetAddress address = isResolvable(mHostname);
			if (address != null)
			{
				/**
				 * Verify that an address is reachable.
				 * 
				 * @see function canConnect.
				 */
				if (canConnect(address, mServicePort))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		/*
		 * ! Call the appropriate interface according to the result of the connection's test.
		 */
		if (mCallback != null)
		{
			if (result)
			{
				/*
				 * !
				 * 
				 * @see function onReachabilityTestPassed.
				 */
				mCallback.onReachabilityTestPassed();
			}
			else
			{
				/*
				 * !
				 * 
				 * @see function onReachabilityTestFailed.
				 */
				mCallback.onReachabilityTestFailed();
			}
		}
	}

	/**
	 * Function that verifies if a network is available.
	 //param Context context : The activity context.
	 * @return boolean (state of connection).
	 */
	private boolean isConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		// Get network info
		if (networkInfo != null && networkInfo.isConnected())
		{
			return true;
		}
		return false;
	}
	/**
	 * Function that uses the DNS server to resolve the hostname.
	  //@param String hostname : Ip address.
	  @return InetAddress (inet address of the hostanme).
	 */
	private InetAddress isResolvable(String hostname) {
		try
		{
			return InetAddress.getByName(hostname);
		}
		catch (UnknownHostException e)
		{
			return null;
		}
	}
	/**
	 * Function that verifies that a connection can be established between the tablet and the website.
	 // @param InetAddress address : inet address of the website.
	 // @param int port : connection's port.
	 * @return
	 */
	private boolean canConnect(InetAddress address, int port) {
		Socket socket = new Socket();
		SocketAddress socketAddress = new InetSocketAddress(address, port);
		try
		{
			/*
			 * try to connect with a timeout.
			 */
			socket.connect(socketAddress, 2000);
		}
		catch (IOException e)
		{
			return false;
		}
		finally
		{
			if (socket.isConnected())
			{
				try
				{
					/*
					 * Close socket if the connection test is successful
					 */
					socket.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
