package com.thibault01.k_net;


import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class InfosActivity extends Fragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2787693116565435900L;
	public View V;
	TableLayout containerTable;
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		        // Inflate the layout for this fragment
		        V = inflater.inflate(R.layout.activity_infos, container, false);	 
		      
		        //((MainActivity)getActivity()).setTagInfosActivity(getTag());
		
				//((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/get_config/841b5e52bae7/", "Infos");
				//System.out.print(((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/get_config/841b5e52bae7/"));
		        return V;
		    }
		
	    @Override 
	    public void onActivityCreated(Bundle savedInstanceState) { 
	        super.onActivityCreated(savedInstanceState); 
	        containerTable = (TableLayout) getActivity().findViewById(R.id.containerTable); 
			 containerTable.removeAllViews();
			 ((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/infos/$mac$/", "infos");
	    } 

	 @Override
	 public void onResume ()
	 {
		 super.onResume ();
		 //System.out.println("[InfosActivity] onResume ");
		 //((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/get_config/841b5e52bae7/", "get_config");
		
	 }
	 

	 public void setWanIpv4(String text){
	                
		 if(getActivity() != null)
		 {
	        TableRow row = new TableRow(getActivity());
	        
	        TextView itemName = new TextView(getActivity());
	        TextView itemValue = new TextView(getActivity());
	        itemValue.setSelected(true);
	        itemValue.setTextIsSelectable(true);
	        itemName.setTypeface(null, Typeface.BOLD);
	        itemName.setText("Adresse Ipv4 :");
	        itemValue.setText(text);
	        row.addView(itemName);
	        row.addView(itemValue);
	        containerTable.addView(row);
		 }
		 //((TextView)V.findViewById(R.id.print_wan_ipv4)).setText(text);
     }
	 public void setWanIpv6(String text){
		 if(getActivity() != null)
		 {
	        TableRow row = new TableRow(getActivity());
	        
	        TextView itemName = new TextView(getActivity());
	        TextView itemValue = new TextView(getActivity());
	        itemValue.setSelected(true);
	        itemValue.setTextIsSelectable(true);
	        itemName.setTypeface(null, Typeface.BOLD);
	        itemName.setText("Adresse Ipv6 :");
	        itemValue.setText(text);
	        row.addView(itemName);
	        row.addView(itemValue);
	        containerTable.addView(row);
		 //((TextView)V.findViewById(R.id.print_wan_ipv6)).setText(text);
		 //((TextView)V.findViewById(R.id.print_wan_ipv6)).
		 }
     }
	 public void setWirelessEquipment(JSONObject json) throws JSONException{
		 if(getActivity() != null)
		 {
		        TableRow row = new TableRow(getActivity());     
		        TableRow borderRow;     
		        TextView border;
		        TextView itemName = new TextView(getActivity());
		        TextView itemValue;
		        itemName.setTypeface(null, Typeface.BOLD);
		        itemName.setText("Equipements Wifi :");
		        row.addView(itemName);
		        containerTable.addView(row);
			JSONObject equips = json.getJSONObject("list_equipment");
			int i = 0;
			JSONObject equipement;
			while(true)
			{
				try
				{
					equipement = equips.getJSONObject(String.valueOf(i));
			        row = new TableRow(getActivity()); 
			      

			        itemName = new TextView(getActivity());
			        itemValue = new TextView(getActivity());
			        itemName.setSelected(true);
			        itemName.setTextIsSelectable(true);
			        itemValue.setSelected(true);
			        itemValue.setTextIsSelectable(true);
			        itemName.setTypeface(null, Typeface.BOLD_ITALIC);		     
			        itemName.setText(equipement.getString("name"));
			        itemValue.setText(equipement.getString("ip"));
			        row.addView(itemName);
			        row.addView(itemValue);
			        
			        //Border
			        borderRow = new TableRow(getActivity()); 
			        border = new TextView(getActivity());
			        border.setHeight(2);
			        border.setBackgroundColor(Color.BLACK);
			        borderRow.addView(border);
			        border = new TextView(getActivity());
			        border.setHeight(2);
			        border.setBackgroundColor(Color.BLACK);
			        borderRow.addView(border);
			        
			        containerTable.addView(borderRow);
			        containerTable.addView(row);
					i++;
				}
				catch(Exception e)
				{
					break;
				}
			}
				
	
		
		 }
     }
	 
	 
}
