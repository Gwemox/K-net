package com.thibault01.k_net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements
ListView.OnItemClickListener {

	//Stockage des variables récupéré via API
	private String PHPSESSID = "";
	public List<Equipement> equips_co_static; //
	public String mac = "";

	  private InfosActivity mInfosActivity = null;
	  private WolActivity mWOLActivity = null;
	  private TvActivity mTvActivity = null;
	  private ToolsActivity mToolsActivity = null;
	  private SettingsActivity mSettingsActivity = null;
	  
	  private CharSequence menuTitle = "Menu";
	 private CharSequence activityTitle = "";

	  private DrawerLayout menuLayout; //Layout Principal
	  private ListView menuElementsList; //Menu
	  private ActionBarDrawerToggle menuToggle; //Gère l'ouverture et la fermeture du menu
	  
	  
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		//On change le logo dans la barre du haut et on ajoute la version
		getActionBar().setLogo(R.drawable.logo_knet_white);
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			activityTitle =  " " + pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//On fait ce qu'on a à faire pour ajouter le menu (de gauche)
		menuElementsList = (ListView) findViewById(R.id.menu_elements);
		menuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
		
		menuLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

	
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, R.layout.element_menu);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapter.add("Infos");
		adapter.add("Wol");
		adapter.add("Tv");
		adapter.add("Outils");
		adapter.add("Paramètres");
		
		menuElementsList.setAdapter(adapter);
		menuElementsList.setOnItemClickListener(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	
		menuToggle = new ActionBarDrawerToggle(this, menuLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
		menuLayout.setDrawerListener(menuToggle);

		//Fin du menu de gauche
		
		//Récupères les variables essentielles
		Bundle var = this.getIntent().getExtras();
		PHPSESSID= var.getString("Value");
		mac = var.getString("mac");

		//Si MainActivity a déjà était initialisé on recupère les anciennes Acitivty
		//Sinon on en fait des nouvelles
		if(var.getBoolean("initializeMainActivity"))
		{
			mInfosActivity = (InfosActivity) this.getIntent().getSerializableExtra("mInfosActivity");
			mWOLActivity = (WolActivity) this.getIntent().getSerializableExtra("mWOLActivity");
			mToolsActivity = (ToolsActivity) this.getIntent().getSerializableExtra("mToolsActivity");
			mTvActivity = (TvActivity) this.getIntent().getSerializableExtra("mTvActivity");
			mSettingsActivity= (SettingsActivity) this.getIntent().getSerializableExtra("mSettingsActivity");
		}
		else
		{
			mInfosActivity = new InfosActivity();
			mWOLActivity = new WolActivity();
			mToolsActivity = new ToolsActivity();
			mSettingsActivity= new SettingsActivity();
			mTvActivity = new TvActivity();
		}
		
		this.changeFragment(var.getInt("FragmentPosMainActivity"));
			
			
	}
	

	
	public void onBackPressed() {
				getActionBar().setTitle(activityTitle);
				invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(activityTitle);
				invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(menuTitle);
				invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
	
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Hide menu element when menu is opened
		boolean drawerOpen = menuLayout.isDrawerOpen(menuElementsList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	public void setTitle(CharSequence title) {
		activityTitle = title;
		getActionBar().setTitle(activityTitle);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		menuToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		menuToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (menuToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_settings:
			changeFragment(4);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	public void changeFragment(int position)
	{
		FragmentManager fm = getSupportFragmentManager();	
		FragmentTransaction ft = fm.beginTransaction();
		switch(position)
		{
			case 0:
				ft.replace(R.id.listFragment, mInfosActivity);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				 break;
			case 1:
				ft.replace(R.id.listFragment, mWOLActivity);	
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				 break;
			case 2:
				ft.replace(R.id.listFragment, mTvActivity);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				 break;
			case 3:
				ft.replace(R.id.listFragment, mToolsActivity);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				 break;
			case 4:
				ft.replace(R.id.listFragment, mSettingsActivity);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
				 break;	
		}
		ft.commit();
		this.getIntent().putExtra("initializeMainActivity",true);
		this.getIntent().putExtra("mInfosActivity",mInfosActivity);
		this.getIntent().putExtra("mWOLActivity",mWOLActivity);
		this.getIntent().putExtra("mTvActivity",mTvActivity);
		this.getIntent().putExtra("mToolsActivity",mToolsActivity);
		this.getIntent().putExtra("mSettingsActivity",mSettingsActivity);
		this.getIntent().putExtra("FragmentPosMainActivity",position);
		// update selected item and title, then close the drawer
		menuElementsList.setItemChecked(position, true);
		setTitle(activityTitle);
		menuLayout.closeDrawer(menuElementsList);
	}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			changeFragment(position);
		}

	//On appelle cette fonction pour faire une requête via API
		//Avec l'url (possible de mettre $mac$ et sera remplacée par l'adresse Mac du routeur
		//PHPSESSID est automatiquement ajouté
	//action permet de faire ou appeler une fonction après avoir fait la requête (dans un nouveau thread) par exemple modifier une View
	public void requestAPI(String url, String action)
	{
		showProgress(true);
		Runnable r = new requestAPI(url,action);
		new Thread(r).start();
	}
	
	
	ProgressDialog myProgressDialog;
	/**
	 * Shows the progress UI and hides the login form.
	 */
	private int countProgress = 0;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if(show){
			countProgress+=1;
			if(myProgressDialog != null)
			{
				myProgressDialog.cancel();
			}
			myProgressDialog = new ProgressDialog(this);
			myProgressDialog.setTitle("Chargement ...");
			myProgressDialog.setMessage("Récupération des informations ...");
			myProgressDialog.show();
		}
		else if(countProgress>1 && show==false)
		{
			countProgress-=1;
		}
		else if(show==false && countProgress==1)
		{
			countProgress=0;
			if(myProgressDialog != null)
			{
				try
				{
					myProgressDialog.cancel();
				}
				catch (Exception e)
				{
					
				}
			}
			myProgressDialog= null;
		}

	}
	

	
	public class requestAPI implements Runnable {

		String _myUrl;
		String _action;
		public requestAPI(String url, String action) {
		       // store parameter for later user
			_myUrl =url;
			_action=action;

		   }

		   public void run() {
		        if (android.os.Build.VERSION.SDK_INT > 9) 
				{
				    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				    StrictMode.setThreadPolicy(policy);
				}

				    try {
				        DataLoader dl = new DataLoader();
				        _myUrl=_myUrl.replace("$mac$", mac);
				        HttpResponse response2 = dl.secureLoadData(_myUrl, PHPSESSID); 
				        
				      
				        InputStream is = response2.getEntity().getContent();
				        StringBuilder out = new StringBuilder();
				        BufferedReader br = new BufferedReader(new InputStreamReader(is));
				        for (String line = br.readLine(); line != null; line = br.readLine())
				            out.append(line);
				        br.close();
				        System.out.println(out.toString());
						SetData(out, _action); //Ici on fait l'action
						
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
				    runOnUiThread(new Runnable() {  
	                    @Override
	                    public void run() {
	                        // TODO Auto-generated method stub
	                    	showProgress(false);
	                    }
	                });
				
		    }
		   }
	
	void SetData(StringBuilder _out, String _action) {
	    class OneShotTask implements Runnable {
	    	StringBuilder out;
	    	String action="";
	        OneShotTask(StringBuilder _out, String _action) { out = _out; action =_action;}
	        public void run() {     	     	
	        	try {
			        if(action.equalsIgnoreCase("get_config"))
			        {		     
			        	if(!out.toString().contains("<!DOCTYPE"))
				    	{
				        	JSONObject json = new JSONObject(out.toString());
			            	JSONObject data = json.getJSONObject("data");
			            	JSONArray dhcpd_static = data.getJSONArray("dhcpd_static");
			            	equips_co_static = new ArrayList<Equipement>();
			            	for (int i=0;i< dhcpd_static.length(); i++)
			            	{
			            		//Ici on ajoute nos équipements statiques dans une List pour faire le WOL par exemple
			            		equips_co_static.add(new Equipement(dhcpd_static.getJSONObject(i).getString("desc"),dhcpd_static.getJSONObject(i).getString("mac_address")));	            		
			            	}
			            	mWOLActivity.setSpinner();
				    	}
				    	else
				    	{
				    		  //System.out.println("Le serveur n'a pas répondu correctement");
				    	}
			        }
			        else if(action.equalsIgnoreCase("infos"))
			        {	
			        	if(!out.toString().contains("<!DOCTYPE"))
				    	{
				        	JSONObject json = new JSONObject(out.toString());
				        	JSONObject data;					
							data = json.getJSONObject("data");
							mInfosActivity.setWanIpv4( data.getString("ip_wan"));
							mInfosActivity.setWanIpv6( data.optString("ipv6_wan", "Ipv6 désactivée"));
							mInfosActivity.setWirelessEquipment(data.getJSONObject("wireless"));
					    }
				    	else
				    	{
				    		  //System.out.println("Le serveur n'a pas répondu correctement");
				    	}
			        }  
			        else if(action.equalsIgnoreCase("m3u_download"))
			        {		     		            	
						mToolsActivity.saveM3U(out);
			        }  
			        else if(action.equalsIgnoreCase("m3u"))
			        {		     		            	
			        	JSONObject json = new JSONObject(out.toString());
			        	String[] data = json.getString("data").split("\n");
			        	mTvActivity.setListChanelTV(mTvActivity.getListChanelTV(data));
			        }  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	    runOnUiThread(new OneShotTask(_out, _action));
	}

}


