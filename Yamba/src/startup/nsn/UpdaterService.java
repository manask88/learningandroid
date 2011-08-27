package startup.nsn;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class UpdaterService extends IntentService {

private static final String TAG = UpdaterService.class.getSimpleName();
	

public static final String NEW_STATUS_INTENT = "startup.nsn.yamba.NEW_STATUS";
public static final String NEW_STATUS_EXTRA_COUNT = "NEW_STATUS_EXTRA_COUNT";
public static final String RECEIVE_TIMELINE_NOTIFICATIONS = "startup.nsn.yamba.RECEIVE_TIMELINE_NOTIFICATIONS";
	




public UpdaterService() {
	super(TAG);
	// TODO Auto-generated constructor stub
	Log.d(TAG,"UpdaterSErvice constructed");
}





@Override
protected void onHandleIntent(Intent inIntent) {
	// TODO Auto-generated method stub
	
	
	Intent intent;
	Log.d(TAG,"onHandleIntent'ing");
	YambaApplication yamba = (YambaApplication) getApplication();
	int newUpdates = yamba.fetchStatusUpdates();
	if (newUpdates>0) {
		Log.d(TAG,"we have a new status");
		intent = new Intent (NEW_STATUS_INTENT);
		intent.putExtra(NEW_STATUS_EXTRA_COUNT, newUpdates);
		sendBroadcast(intent,RECEIVE_TIMELINE_NOTIFICATIONS);
	}
	
	
}





}
