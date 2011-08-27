package startup.nsn;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StatusProvider extends ContentProvider {
	final String TAG = StatusProvider.class.getSimpleName();
	StatusData statusData = new StatusData(null); 
	final String MULTIPLE_RECORDS_TYPE = "vnd.android.cursor.dir/vnd.startup.nsn.yamba.status";
	final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.startup.nsn.yamba.status";
	public static final Uri CONTENT_URI = Uri .parse("content://startup.nsn.yamba.statusprovider");
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		long id=this.getId(uri);
		SQLiteDatabase db=statusData.dbHelper.getWritableDatabase();
		try {
			if (id<0) {
				return db.delete(statusData.TABLE, selection, selectionArgs);
			} else {
				return db.delete(StatusData.TABLE, StatusData.C_ID+ "="+id, null);
			}
		} finally {
			db.close();
		}
	}



	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = statusData.dbHelper.getWritableDatabase(); //dbHelper was private in StatusData class
		try {
			long id= db.insertOrThrow(StatusData.TABLE, null,values);
			if (id==-1) {
				throw new RuntimeException(String.format("%s: Failed to insert [%s] to [%s] for unknown reasons.", TAG, values, uri));
			} else {
				return ContentUris.withAppendedId(uri, id);
			}
			
		}	
		 finally {
				db.close();
			}
				
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		long id = this.getId(uri);
		SQLiteDatabase db = statusData.dbHelper.getReadableDatabase();
		if (id<0) {
			return db.query(StatusData.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
		} else {
			return db.query(StatusData.TABLE, projection, StatusData.C_ID+"="+id,null,null,null,null);
		}
				
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		
		long id= this.getId(uri);
		SQLiteDatabase db = statusData.dbHelper.getWritableDatabase();
		try {
			if (id<0) {
				return db.update(StatusData.TABLE, values, selection, selectionArgs);
			} else {
				return db.update(StatusData.TABLE, values, StatusData.C_ID + "=" + id, null);
			}
				
			
		} finally {
			db.close();
		}
		
	}
	
	@Override
	public String getType(Uri uri) {
		return this.getId(uri ) <0 ? MULTIPLE_RECORDS_TYPE : SINGLE_RECORD_MIME_TYPE;
		
	}
	
	private long getId (Uri uri) {
		String lastPathSegment = uri.getLastPathSegment();
		if (lastPathSegment != null) {
			try {
				return Long.parseLong(lastPathSegment);
			} catch (NumberFormatException e) {
				
			}
		}
	
	return -1;
	
	}
}
