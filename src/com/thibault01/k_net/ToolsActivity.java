package com.thibault01.k_net;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

public class ToolsActivity extends Fragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8657148666712663994L;
	public View V;
	TableLayout containerTable;
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		        V = inflater.inflate(R.layout.activity_tools, container, false);	 
		        return V;
		    }
	 
	    @Override 
	    public void onActivityCreated(Bundle savedInstanceState) { 
	        super.onActivityCreated(savedInstanceState); 
			V.findViewById(R.id.buttonDownloadM3u).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							DownloadM3u();
						}
					});
			V.findViewById(R.id.buttonReboot).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/reboot/$mac$/", "reboot");
						}
					});
			V.findViewById(R.id.buttonReset).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							new AlertDialog.Builder(getActivity())
					           .setMessage("Êtes-vous sûr de vouloir réinitialiser votre routeur ? [Supprime entièrement vos paramètres]")
					           .setCancelable(false)
					           .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
					               public void onClick(DialogInterface dialog, int id) {
					            	   ((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/reset/$mac$/", "reset");
					            	   Toast.makeText(getActivity(), "Réinitialisation ...", Toast.LENGTH_LONG).show(); 
					               }
					           })
					           .setNegativeButton("Annuler", null)
					           .show();
						}
					});
	    } 
	    
	
	    private void DownloadM3u()
	    {
	    	((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/m3u/$mac$/", "m3u_download");   	  
	    }

		@SuppressLint("SdCardPath")
		public void saveM3U(StringBuilder out) throws IOException, JSONException    
	    {
	 
	    	  File file = null;
	    	  //FileOutputStream outputStream;
	    	  String filename = "Tv K-net.m3u";
	    	  File fileExt = new File("/mnt/extSdCard/Download/");
	    	  File fileInt = new File("/mnt/sdcard/Download/");
	    	  if (fileExt.exists() && fileExt.isDirectory()) {
	    	      // We can read and write the media 
	    		    file = new File("/mnt/extSdCard/Download/",filename );
	    	  } else if (fileInt.exists() && fileInt.isDirectory()) {
	    	      // Something else is wrong. It may be one of many other states, but all we need
	    	      //  to know is we can neither read nor write
	    		  file = new File("/mnt/sdcard/Download/",filename );
	    	  } 
	    	     	  
	    	  if(file !=null)
	    	  {
	    		  //outputStream = new FileOutputStream(file);
		          JSONObject json = new JSONObject(out.toString());
		          FileWriter fw = new FileWriter(file.getAbsolutePath(), false);
		          
		          //String[] outJson = json.getString("data").split("\n");
		          //for (int i =0; i < outJson.length; i++)
		         // {
		        //	  fw.write(outJson[i]);
		        //	 
		          //}
		          fw.write(json.getString("data"));
		          fw.close();
		         // outputStream.write(json.getString("data").getBytes());
	    	    //outputStream.close();
	    	    Toast.makeText(getActivity(), "Fichier enregistré : " + file.getAbsolutePath(), Toast.LENGTH_LONG).show(); 
	    	    DownloadManager dm = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
				dm.addCompletedDownload("Tv k-net.M3u" , "Fichier de chaines TV, afin de regarder la TV via le réseau K-net.", true,"audio/x-mpegurl, audio/mpeg-url, application/x-winamp-playlist, audio/scpls, audio/x-scpls", file.getAbsolutePath(), file.length(), true);
	    	  }
	    	  else
	    	  {
	    		  Toast.makeText(getActivity(), "Impossible d'enregistrer le fichier !", Toast.LENGTH_LONG).show(); 
	    	  }
		
	    }
}
