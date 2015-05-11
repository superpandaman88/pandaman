package com.example.demo;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("panda_password",getValue(this,"content://com.panda.setting/biz_passport_url","b"));
		Log.d("panda_password",getValue(this,"content://com.panda.setting/biz_plugin_url","c"));
	}
	
	private String getValue(Context context,String url,String defaultVal){
		Cursor cursor = context.getContentResolver().query(Uri.parse(url), null, null, null, null);
		if(cursor!=null && cursor.getCount()>0){
			cursor.moveToFirst();
			String ret = cursor.getString(0);
			cursor.close();
			return ret;
		}
		return defaultVal;
	}
}
