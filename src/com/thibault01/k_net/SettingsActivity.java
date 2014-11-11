package com.thibault01.k_net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;

public class SettingsActivity extends Fragment implements OnCheckedChangeListener, OnItemSelectedListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6678839555620740837L;
	public View V;
	TableLayout containerTable;
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		        // Inflate the layout for this fragment
		        V = inflater.inflate(R.layout.activity_settings, container, false);	 
		        return V;
		    }
		
	    @Override 
	    public void onActivityCreated(Bundle savedInstanceState) { 
	        super.onActivityCreated(savedInstanceState); 
	        SharedPreferences sharedPref= getActivity().getSharedPreferences("Login", 0);
	        ((CheckBox)V.findViewById(R.id.checkBoxSettingsAutoConnect)).setChecked(sharedPref.getBoolean("autoConnect", false));
	        ((CheckBox)V.findViewById(R.id.checkBoxSettingsAutoConnect)).setOnCheckedChangeListener(this);
	        Spinner spinner = (Spinner)V.findViewById(R.id.spinnerActivityLoad);
	        List<String> list = new ArrayList<String>();
	        
	        if(sharedPref.getInt("FragmentPosMainActivity", 2)==0)
	        {
	        	list.add("Infos");
	 	        list.add("WoL");
	 	        list.add("Tv");
	 	        list.add("Outils");
	 	        list.add("Paramètres");
	        }
	        else  if(sharedPref.getInt("FragmentPosMainActivity", 2)==1)
	        {
	 	        list.add("WoL");
	        	list.add("Infos");
	 	        list.add("Tv");
	 	        list.add("Outils");
	 	        list.add("Paramètres");
	        }
	        else  if(sharedPref.getInt("FragmentPosMainActivity", 2)==2)
	        {
	 	        list.add("Tv");
	        	list.add("Infos");
	 	        list.add("WoL");
	 	        list.add("Outils");
	 	        list.add("Paramètres");
	        }
	        else  if(sharedPref.getInt("FragmentPosMainActivity", 2)==3)
	        {
	 	        list.add("Outils");
	        	list.add("Infos");
	 	        list.add("WoL");
	 	        list.add("Tv");
	 	        list.add("Paramètres");
	        }
	        else  if(sharedPref.getInt("FragmentPosMainActivity", 2)==4)
	        {
	        	list.add("Paramètres");
	        	list.add("Infos");
	 	        list.add("WoL");
	 	        list.add("Tv");
	 	        list.add("Outils");
	        }
	       
	                 
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
	                     (getActivity(), android.R.layout.simple_spinner_item,list);
	                      
	        dataAdapter.setDropDownViewResource
	                     (android.R.layout.simple_spinner_dropdown_item);
	                      
	        spinner.setAdapter(dataAdapter);
	        spinner.setOnItemSelectedListener(this);
	    }

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		      String selected = arg0.getItemAtPosition(arg2).toString();
		      int position = 2;
		      if(selected.equalsIgnoreCase("Infos"))
		      {
		    	  position=0;
		      }
		      else if(selected.equalsIgnoreCase("Wol"))
		      {
		    	  position=1;
		      }
		      else if(selected.equalsIgnoreCase("Tv"))
		      {
		    	  position=2;
		      }
		      else if(selected.equalsIgnoreCase("Outils"))
		      {
		    	  position=3;
		      }
		      else if(selected.equalsIgnoreCase("Paramètres"))
		      {
		    	  position=4;
		      }
	  	     SharedPreferences sharedPref= getActivity().getSharedPreferences("Login", 0);
	  	    //now get Editor
	  	     SharedPreferences.Editor editor=sharedPref.edit();
	  	    //put your value
	  	 
	  	   editor.putInt("FragmentPosMainActivity", position);
	  	   //commits your edits
	  	     editor.commit();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
	  	     SharedPreferences sharedPref= getActivity().getSharedPreferences("Login", 0);
	  	    //now get Editor
	  	     SharedPreferences.Editor editor=sharedPref.edit();
	  	    //put your value
	  	     editor.putBoolean("autoConnect", isChecked);	
	  	   //commits your edits
	  	     editor.commit();
			
		}


}
