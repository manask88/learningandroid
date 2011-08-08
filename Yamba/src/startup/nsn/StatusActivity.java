package startup.nsn;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher, OnSharedPreferenceChangeListener {
	 private static final String TAG = "StatusActivity";
	  EditText editText;
	  Button updateButton;
	  Twitter twitter;
	  TextView textCount; // 
	  SharedPreferences prefs;


	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.status);

	    
	    
	 // Setup preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this); // 
        prefs.registerOnSharedPreferenceChangeListener(this);   // 
	    
	    // Find views
	    editText = (EditText) findViewById(R.id.editText); // 
	    updateButton = (Button) findViewById(R.id.buttonUpdate);

	    updateButton.setOnClickListener(this); // 

	    
	    textCount = (TextView) findViewById(R.id.textCount); // 
	    textCount.setText(Integer.toString(140)); // 
	    textCount.setTextColor(Color.GREEN); // 
	    editText.addTextChangedListener(this); // 
	    
	    
	    //twitter = new Twitter("student", "password"); // 
	    //twitter.setAPIRootUrl("http://yamba.marakana.com/api");
	  }

	  // Asynchronously posts to twitter
	  class PostToTwitter extends AsyncTask<String, Integer, String> { // 
	    // Called to initiate the background activity
	    @Override
	    protected String doInBackground(String... statuses) { // 
	      try {
	    	 
	    	    YambaApplication yamba = ((YambaApplication) getApplication()); // 

	    	    Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]); // 

	    	  
	    	  return status.text;
	      } catch (TwitterException e) {
	
	        Log.e(TAG, "Failed to connect to twitter service", e);
	        e.printStackTrace();
	        return "Failed to post";
	      }
	    }

	    // Called when there's a status to be updated
	    @Override
	    protected void onProgressUpdate(Integer... values) { // 
	      super.onProgressUpdate(values);
	      // Not used in this case
	    }

	    // Called once the background activity has completed
	    @Override
	    protected void onPostExecute(String result) { // 
	      Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
	    }
	  }

	  
	  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
	        // invalidate twitter object
	        twitter = null;
	}
	  
	  // Called when button is clicked
	 
	public void onClick(View v) {
	
    
	    // Update twitter status
        try {
                getTwitter().setStatus(editText.getText().toString());
                Log.d(TAG, "onClicked");
        } catch (TwitterException e) {
                Log.d(TAG, "Twitter setStatus failed: " + e);
        }
	  }

	  
	  private Twitter getTwitter() {
	        if (twitter == null) {  // 
	                String username, password, apiRoot;
	                username = prefs.getString("username", "");     // 
	                password = prefs.getString("password", "");
	    apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");

	                // Connect to twitter.com
	                twitter = new Twitter(username, password);      // 
	                twitter.setAPIRootUrl(apiRoot); // 
	        }
	        return twitter;
	}
	  
	  



	


	// TextWatcher methods
	  public void afterTextChanged(Editable statusText) { // 
	    int count = 140 - statusText.length(); // 
	    textCount.setText(Integer.toString(count));
	    textCount.setTextColor(Color.GREEN); // 
	    if (count < 10)
	      textCount.setTextColor(Color.YELLOW);
	    if (count < 0)
	      textCount.setTextColor(Color.RED);
	  }

	  public void beforeTextChanged(CharSequence s, int start, int count, int after) { // 
	  }

	  public void onTextChanged(CharSequence s, int start, int before, int count) { // 
	  }
	  
	  
	}