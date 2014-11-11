package com.thibault01.k_net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TvActivity extends Fragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4663374640986617883L;
	View V;
	
	chanelTvAdaptater chanelTvAdaptater;
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		        // Inflate the layout for this fragment
		        V = inflater.inflate(R.layout.activity_tv, container, false);	 
		        return V;
		    }
	    @Override 
	    public void onActivityCreated(Bundle savedInstanceState) { 
	        super.onActivityCreated(savedInstanceState); 
	        ((MainActivity)getActivity()).requestAPI("https://nsi-routerv1.www.k-net.fr/m3u/$mac$/", "m3u");
	     // TODO Auto-generated method stub
	    }
	    
	    public void setListChanelTV(List<chanelTv> listChanel)
	    {
	        chanelTvAdaptater = new chanelTvAdaptater((MainActivity) getActivity(), listChanel);
	        
	        ListView listViewTv = (ListView)getActivity().findViewById(R.id.listViewTv);
	        listViewTv.setAdapter(chanelTvAdaptater);
	        
	        listViewTv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					
					chanelTv chanelTv = chanelTvAdaptater.getChanelTV(arg2);
					 try
					 {
					  Intent i = new Intent(Intent.ACTION_VIEW);
			        //i.setPackage("org.videolan.vlc.betav7neon");
			        i.setDataAndType(Uri.parse(chanelTv.Url), "video/h264");
			        startActivity(i);
					 }
					 catch(Exception e)
					 {
						 
					 }
				}
			});
	    }
	    
	    public List<chanelTv> getListChanelTV(String[] listchanel)
	    {
	    	List<chanelTv> chanelTvList = new ArrayList<chanelTv>();
	    	boolean radio = false;
	    	for(int i=1;i<listchanel.length-1;i+=2)
	    	{	 	
	    		chanelTv _chanelTv = new chanelTv();
	    		String aParser=listchanel[i].trim();
	    		Pattern p=Pattern.compile("#EXTINF:0,([0-9]*)[.] (.*)");
	    		Matcher m=p.matcher(aParser);
	    		while(m.find()){
	    			_chanelTv.Id = Integer.parseInt(m.group(1));
	    			_chanelTv.Name = m.group(2);
	    		}
	    		_chanelTv.Url = listchanel[i+1]; 
	    		_chanelTv.radio = radio;
	    		//Provisoire, pas propre
	    		if(_chanelTv.Id == 131)
	    		{
	    			radio=true;
	    		}
	    		chanelTvList.add(_chanelTv);
	    		
	    	}
	    	
	    	return chanelTvList;
	    	
	    }
	}
