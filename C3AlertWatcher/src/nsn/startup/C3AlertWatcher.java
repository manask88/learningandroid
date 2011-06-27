package nsn.startup;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;







import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class C3AlertWatcher extends ListActivity {
    /** Called when the activity is first created. */
	
	ItemsAdapter itemsAdapter;
	//private static final String serverAddress = "http://10.0.96.189:3000";
	private static final String serverAddress = "http://10.0.2.2:3000";
	private static int viewid = 1;
	static ArrayList<Alarm> alarms = new ArrayList<Alarm>();
	private static final int sleepTime = 5000;

	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        


        

		itemsAdapter = new ItemsAdapter(C3AlertWatcher.this, R.layout.list,
				alarms);
		setListAdapter(itemsAdapter);
        
		
		
/*
        alarms.add(new Alarm(1,1,"11","xx",1));
        alarms.add(new Alarm(1,1,"22","xx",1));
        alarms.add(new Alarm(1,1,"33","xx",1));


        alarms.add(new Alarm(1,1,"44","xx",1));

        alarms.add(new Alarm(1,1,"55","xx",1));

        alarms.add(new Alarm(1,1,"66","xx",1));
*/
        
        fetchAlarms();
    }








    private class ItemsAdapter extends BaseAdapter {
		ArrayList<Alarm> items;

		public ItemsAdapter(Context context, int textViewResourceId,
				ArrayList<Alarm> items) {
			// super(context, textViewResourceId, items);
			this.items = items;
		}

		// @Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list, null);
			}
			final TextView post = (TextView) v.findViewById(R.id.post);
			post.setText(items.get(position).getAlarm_id() + " "
					+ items.get(position).getAssetName() + "  "
					+ items.get(position).getAlarmName());


			if (items.get(position).isAck()) {

				post.setBackgroundColor(Color.BLACK);
				

			}


			return v;
		}

		public int getCount() {
			return items.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
	}

    
    
	public void fetchAlarms() {
		// final String url=T3A_URL+"?&id="+deviceID;

		new Thread(new Runnable() {
			// @Override
			public void run() {
				// alerts.add(""+new Date());

				// alarms = new ArrayList<Alarm>();
				// alerts.add(""+new Date());
				// handler.sendEmptyMessage(0);

				// Message msg = new Message();
				// msg.arg1=0;
				// handler.sendMessage(msg);

				while(true)	 {    
					try{

					// Create a new HttpClient and Post Header

					// handler.sendEmptyMessage(0);

					JSONObject jResponseView = new JSONObject(
							jsonRequest(serverAddress + "/views/" + viewid
									+ ".json"));

					JSONArray jArrayViewAssets = jResponseView.getJSONObject(
							"view").getJSONArray("assets");
					ArrayList<Integer> jArrayViewAssetsId = new ArrayList<Integer>();
					for (int i = 0; i < jArrayViewAssets.length(); i++) {

						jArrayViewAssetsId.add(jArrayViewAssets
								.getJSONObject(i).getInt("id"));

					}

					for (int k = 0; k < jArrayViewAssetsId.size(); k++) {

						JSONArray jResponse = new JSONArray(
								jsonRequest(serverAddress
										+ "/assets/all_sensor_data.json?asset_id="
										+ jArrayViewAssetsId.get(k) + "&at=now"));

						JSONObject jObject0 = jResponse.getJSONObject(0); // asset
																			// info
						JSONObject jObject1 = jResponse.getJSONObject(1); // sensor_data
																			// and
																			// sensor_alarms

						if (jObject1.has("sensor_alarms")) {
							JSONArray jArray_sensor_alarms = jObject1
									.getJSONArray("sensor_alarms"); // array of
																	// sensor_data

							JSONObject jObject_asset = jObject0
									.getJSONObject("asset"); // asset as an
																// object
							JSONArray jArray_sensors = jObject_asset
									.getJSONArray("sensors"); // asset:sensors

							for (int i = 0; i < jArray_sensor_alarms.length(); i++) {

								JSONObject sensor_alarm = jArray_sensor_alarms
										.getJSONObject(i).getJSONObject(
												"sensor_alarm"); // sensor_alarms:sensor_alarm

								int sensor_id = sensor_alarm
										.getInt("sensor_id"); // sensor_alarms:sensor_alarm:sensor_id
								String alarmName = "";
								for (int j = 0; j < jArray_sensors.length(); j++) {

									if (sensor_id == jArray_sensors
											.getJSONObject(j).getInt("id")) // sensor_alarms:sensor_alarm:sensor_id
																			// ==
																			// asset:sensors:id
									{
										alarmName = jArray_sensors
												.getJSONObject(j).getString(
														"name"); // asset:sensors:name

										// alerts.add(jObject_asset.getString("name")+
										// name+
										// sensor_alarm.getString("updated_at"));
										alarms.add(new Alarm(sensor_alarm
												.getInt("id"), 0,
												alarmName, jObject_asset
														.getString("name"),
												sensor_id));
									//itemsAdapter.notifyDataSetChanged();
										// alerts.add( sensor_alarm.getInt("id")
										// +
										// sensor_alarm.getString("updated_at"));
									}
									handler.sendEmptyMessage(0);

								}

							}
						}

					}

					// String jsonResponse = HttpHelper.request(response);
					// alerts.add(jsonResponse);

					// see
					// http://androidsnippets.com/executing-a-http-post-request-with-httpclient

					// String jsonResponse = HttpHelper.request(response);
					// alerts.add(jsonResponse);
					// Toast.makeText(AlertActivity.this, "-> " +
					// jsonResponse,10000000).show();
					/*	
				
*/
					} catch (Exception ex) {
					StringWriter sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					// handler.sendEmptyMessage(0);
					// log("Failed! "+ex.getMessage());
				}
					
				
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
					
					
					
				}

			
				
				
				
				
				
				
				
				

			}

		}

		).start();
	}

	
	
	public String jsonRequest(String path) {
		String jsonResponse = "";

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(path);
			HttpResponse response = client.execute(request);
			jsonResponse = HttpHelper.request(response);

		}

		catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));

			//handler.sendEmptyMessage(0);
			// log("Failed! "+ex.getMessage());
		}
		return jsonResponse;

	}

	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
	
			itemsAdapter.notifyDataSetChanged();
			// Toast.makeText(AlertActivity.this,
			// logData,Toast.LENGTH_LONG).show();
		};
	};
}