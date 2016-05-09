package com.example.ramesh.wikisearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;


public class GlobalAccess extends Application{

	public boolean haveNetworkConnection(Activity activity) 
	{
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

		if(cm!=null)
		{
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();

			if(netInfo!=null)
			{
				for (NetworkInfo ni : netInfo) {
					if (ni.getTypeName().equalsIgnoreCase("WIFI"))
						if (ni.isConnected())
							haveConnectedWifi = true;
					if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
						if (ni.isConnected())
							haveConnectedMobile = true;
				}
			}
		}

		return haveConnectedWifi || haveConnectedMobile;
	}

	public void Networkalertmessage(Activity activity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("No Network Connection");
		builder.setMessage("Cannot connect to Internet. Please check your connection settings and try again")
		.setCancelable(false)
		.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int id) {

				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showAlert(final Context context, String title,
			String text, int buttonNumber, String positiveText, String NegativeText) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>"+title+"</font>"));

		// set dialog message
		alertDialogBuilder
		.setMessage(text)
		.setCancelable(false)
		.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		if (buttonNumber == 2)
			alertDialogBuilder.setNegativeButton(NegativeText,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
					//((Activity) context).finish();
				}
			});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();	
	}

}
