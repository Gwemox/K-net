package com.thibault01.k_net;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.HttpResponse;
import org.json.JSONArray;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	private BasicCookieStore cookieStore = new BasicCookieStore();

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mIdClient;
	private String mPassword;
	
	// UI references.
	private EditText mIdClientView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private int FragmentPosMainActivity = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//On change le logo dans la barre du haut et on ajoute la version
		setContentView(R.layout.activity_login);
		getActionBar().setLogo(R.drawable.logo_knet_white);
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			getActionBar().setTitle(" " + pInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 SharedPreferences sharedPref= getSharedPreferences("Login", 0);
		 mIdClient = sharedPref.getString("name", "");
		 mPassword = sharedPref.getString("password", "");
		 FragmentPosMainActivity = sharedPref.getInt("FragmentPosMainActivity", 2);
		 ((CheckBox)findViewById(R.id.checkBoxAutoConnect)).setChecked(sharedPref.getBoolean("autoConnect", false));
		// Set up the login form.
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);

		 
		mIdClientView = (EditText) findViewById(R.id.email);
		mIdClientView.setText(mIdClient);
	
		
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setText(mPassword);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							try {
								attemptLogin();
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
							try {
								attemptLogin();
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				});
		
		if(sharedPref.getBoolean("autoConnect", false))
		{
			try {
				attemptLogin();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() throws ClientProtocolException, IOException {
		//System.out.println("attemptLogin appelé");
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mIdClientView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mIdClient = mIdClientView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		
		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mIdClient)) {
			mIdClientView.setError(getString(R.string.error_field_required));
			focusView = mIdClientView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			 try {
		            DataLoader dl = new DataLoader();
		            
		            HttpResponse response = dl.secureLoadData("https://nsi-routerv1.www.k-net.fr/auth/" + mIdClient + "/" + mPassword + "/"); 
		           if(response.getStatusLine().getStatusCode() == 400)
		           {
		        	   //System.out.println("Erreur authentification");
		        	   return false;
		           }
		           else if(response.getStatusLine().getStatusCode() == 200)
		           {
		        	   //System.out.println("Authentification réussite");
		        	  	if(dl.getCookies().size() >0)
		        	  	{
		        	  		 
		        	  		cookieStore = (BasicCookieStore) dl.getCookieStore();
		        	  	   // Create object of SharedPreferences.
		        	  	     SharedPreferences sharedPref= getSharedPreferences("Login", 0);
		        	  	    //now get Editor
		        	  	     SharedPreferences.Editor editor= sharedPref.edit();
		        	  	    //put your value
		        	  	     editor.putString("name", mIdClient);
		        	  	     editor.putString("password", mPassword);
		        	  	     editor.putBoolean("autoConnect", ((CheckBox) findViewById(R.id.checkBoxAutoConnect)).isChecked());
		        	  	    	
		        	  	   //commits your edits
		        	  	     editor.commit();
							return true;
		        	  	}
		        	  	else
		        	  	{
		        	  		return false;
		        	  	}
		           }

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    	return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				String PHPSESSID = "";
	        	List<Cookie> cookies = cookieStore.getCookies();
	            for (int i = 0; i < cookies.size(); i++) {
	               // System.out.println("Local cookie: " + cookies.get(i).getName());
	                if(cookies.get(i).getName().equalsIgnoreCase("PHPSESSID"))
	                {
	                	//System.out.println("Local cookie: " + cookies.get(i).getValue());
	                	intent.putExtra("Name",cookies.get(i).getName());
	                	intent.putExtra("Value",cookies.get(i).getValue());
	                	intent.putExtra("FragmentPosMainActivity", FragmentPosMainActivity);
	                	intent.putExtra("initializeMainActivity", false);	
	                	PHPSESSID=cookies.get(i).getValue();
	                	break;
	                }
	            }	
	            
	            //Recp de l'adresse mac
	            DataLoader dl = new DataLoader();
		        
		        HttpResponse response2;
				try {
					 if (android.os.Build.VERSION.SDK_INT > 9) 
					{
						    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
						    StrictMode.setThreadPolicy(policy);
					}
					response2 = dl.secureLoadData("https://nsi-routerv1.www.k-net.fr/list_mac/", PHPSESSID);
			
			        InputStream is = response2.getEntity().getContent();
			        StringBuilder out = new StringBuilder();
			        BufferedReader br = new BufferedReader(new InputStreamReader(is));
			        for (String line = br.readLine(); line != null; line = br.readLine())
			            out.append(line);
			        br.close();

			        JSONArray json = new JSONArray(out.toString());
				 	
				 	intent.putExtra("mac",json.getString(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		       
		        
			    
				startActivity(intent);
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
