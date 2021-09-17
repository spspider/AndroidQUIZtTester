package com.example.ttester_paukov.ExternalDB_worked;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity_externalDB extends ListActivity {

	private Cursor employees;
	private MyDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new MyDatabase(this);
		employees = db.getEmployees(); // you would not typically call this on the main thread

		ListAdapter adapter = new SimpleCursorAdapter(this, 
				android.R.layout.simple_list_item_1, 
				employees, 
				new String[] {"FullName"}, 
				new int[] {android.R.id.text1});

		getListView().setAdapter(adapter);

        setTitle("Test - " + db.getUpgradeVersion());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		employees.close();
		db.close();
	}

}