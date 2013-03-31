package com.anna.sent.soft.childbirthdate.widget;

import java.util.Calendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class MyPregnancyWidgetConfigure extends Activity {
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setResult(RESULT_CANCELED);
		setContentView(R.layout.my_pregnancy_widget_configure_layout);

		// First, get the App Widget ID from the Intent that launched the
		// Activity:
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

			// Perform App Widget configuration.
			init();
		}

		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			Toast.makeText(this, R.string.errorInvalidAppWidgetId,
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	public void click(View v) {
		Context context = MyPregnancyWidgetConfigure.this;

		// When the configuration is complete, get an instance of the
		// AppWidgetManager
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		// Update the App Widget with a RemoteViews layout
		RemoteViews views = MyPregnancyWidget.buildViews(context,
				Calendar.getInstance());
		appWidgetManager.updateAppWidget(mAppWidgetId, views);

		// Finally, create the return Intent, set it with the Activity
		// result, and finish the Activity:
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);

		saveCalculatingMethod();
		saveCountdown();

		finish();
	}

	private void init() {
		// TODO read extras by..., init radio
		// if there is no by...: other text and button show, radio hide
	}

	private void saveCalculatingMethod() {
		// TODO radio state save to settings
	}

	private void saveCountdown() {
		// TODO checkbox state save to settings
	}

	public static int loadCalculatingMethod() {
		// TODO read from settings
		return Shared.Saved.Widget.Calculate.UNKNOWN;
	}

	public static boolean loadCountdown() {
		// TODO read from settings
		return false;
	}
}
