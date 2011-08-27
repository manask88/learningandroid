package startup.nsn;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity{
	static final String SEND_TIMELINE_NOTIFICATIONS = "startup.nsn." +
			".SEND_TIMELINE_NOTIFICATIONS";
	TimelineReceiver receiver;
	DbHelper dbHelper;
	SQLiteDatabase db;
	Cursor cursor;
	ListView listTimeline;
	SimpleCursorAdapter adapter;
	static final String[] FROM = {DbHelper.C_CREATED_AT, DbHelper.C_USER,DbHelper.C_TEXT};
	static final int[] TO = {R.id.textCreatedAt, R.id.textUser, R.id.textText};
	  SharedPreferences prefs;

	
	@Override
	protected void onCreate(Bundle savedInstanteState) {
		super.onCreate(savedInstanteState);
		setContentView(R.layout.timeline_basic);
		
		
		
		dbHelper = new DbHelper(this);
		db=dbHelper.getReadableDatabase();
		
		 if ( yamba.getPrefs().getString("username", null) ==null) {
			startActivity(new Intent(this,PrefsActivity.class));
			Toast.makeText(this, "Debe llenar preferencias", Toast.LENGTH_LONG).show();

		}
		 
		 
			listTimeline = (ListView) findViewById(R.id.listTimeline);
			startService(new Intent(this,UpdaterService.class));
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		yamba.getStatusData().close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		this.setupList();
		IntentFilter filter;
		receiver=new TimelineReceiver();
		filter=new IntentFilter("startup.nsn.yamba.NEW_STATUS");
		registerReceiver(receiver,filter,SEND_TIMELINE_NOTIFICATIONS,null);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}
	private void setupList() {
		cursor = yamba.getStatusData().getStatusUpdates();
		startManagingCursor(cursor);
		
		
	adapter = new SimpleCursorAdapter(this,R.layout.row,cursor,FROM,TO);
	adapter.setViewBinder(VIEW_BINDER);
	listTimeline.setAdapter(adapter);
		Toast.makeText(this, "Iniciado TimeLine", Toast.LENGTH_LONG).show();
	}
	static final ViewBinder VIEW_BINDER = new ViewBinder() {
		public boolean setViewValue(View view,Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.textCreatedAt)
				return false;
			
			long timestamp = cursor.getLong(columnIndex);
			CharSequence relTime = DateUtils.getRelativeTimeSpanString(view.getContext(), timestamp);
			((TextView) view).setText(relTime);
			
			return true;
		}
	};
	class TimelineReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			cursor.requery();
			adapter.notifyDataSetChanged();
			Log.d("TimelineReceiver","onReceived");
		}
	}
}
