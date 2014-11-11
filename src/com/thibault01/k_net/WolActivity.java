package com.thibault01.k_net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class WolActivity extends Fragment implements OnItemSelectedListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	View V;
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		        // Inflate the layout for this fragment
		        V = inflater.inflate(R.layout.activity_wol, container, false);	 
		        return V;
		    }
	    @Override 
	    public void onActivityCreated(Bundle savedInstanceState) { 
	        super.onActivityCreated(savedInstanceState); 
			V.findViewById(R.id.buttonStartWol).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
								String macAdrr = ((EditText)V.findViewById(R.id.editTextMacWol)).getText().toString().replace(":", "");
								//System.out.println(macAdrr);
								((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/wol/$mac$/" + macAdrr +"/", "wol");
						}
					});
			((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/get_config/$mac$/", "get_config");

	    } 
	    
	 @Override
	 public void onStart()
	 {
		 super.onStart();
	 }
	 
	 
	 
	 public void setSpinner(){
		 MainActivity _MainActivity = ((MainActivity)getActivity());
	       if(_MainActivity != null)
	       {
	    	   if(_MainActivity.equips_co_static != null)
	    	   {
		    	   Spinner spinner = (Spinner)V.findViewById(R.id.mac_spinner);
			        
			        List<String> list = new ArrayList<String>();
			        for(int i=0; i <_MainActivity.equips_co_static.size(); i++)
			        {
			        	list.add(_MainActivity.equips_co_static.get(i).Name +" - " + _MainActivity.equips_co_static.get(i).mac);
			        	
			        }
			                    
			        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
			                     (getActivity(), android.R.layout.simple_spinner_item,list);
			                      
			        dataAdapter.setDropDownViewResource
			                     (android.R.layout.simple_spinner_dropdown_item);
			                      
			        spinner.setAdapter(dataAdapter);
			        spinner.setOnItemSelectedListener(this);
		       }
		       else
		       {
		    	   //System.out.println("[setSpinner] _MainActivity is null");
		       }
	       }
	       else
	       {
	    	   //System.out.println("[setSpinner] _MainActivity is null");
	       }
		 //
     }
	 
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		String[] item = ((String) parent.getItemAtPosition(pos)).split(" ");
		
		EditText mac_edittext = (EditText)V.findViewById(R.id.editTextMacWol);
		mac_edittext.setText(item[item.length-1]);
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	 
	 
}
