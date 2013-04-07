package com.anna.sent.soft.childbirthdate.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.Utils;

public abstract class MyPregnancyWidgetConfigure extends Activity implements
		OnClickListener {
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private TextView textView;
	private RadioButton radioByLMP, radiobyByOvulation, radioByUltrasound;
	private CheckBox checkBoxCountdown, checkBoxShowCalculatingMethod;
	private Button button;
	private boolean doCalculation = false;

	@Override
	protected void onCreate(Bundle arg0) {
		Utils.onDialogStyleActivityCreateSetTheme(this);
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
		}

		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			Toast.makeText(this, R.string.errorInvalidAppWidgetId,
					Toast.LENGTH_SHORT).show();
			finish();
		}

		// Perform App Widget configuration.
		init();
	}

	public void addWidget() {
		if (radioByLMP.isChecked() || radiobyByOvulation.isChecked()
				|| radioByUltrasound.isChecked()) {
			// When the configuration is complete, get an instance of the
			// AppWidgetManager

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(this);

			// First
			saveWidgetParams();

			// Second. Update the App Widget with a RemoteViews layout
			RemoteViews views = getBuilder().buildViews(this, mAppWidgetId);
			appWidgetManager.updateAppWidget(mAppWidgetId, views);

			MyPregnancyWidget.installAlarms(this);

			// Finally, create the return Intent, set it with the Activity
			// result, and finish the Activity:
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);

			setResult(RESULT_OK, resultValue);

			finish();
		} else {
			Toast.makeText(
					this,
					getString(R.string.widgetToastSpecifyTheMethodOfCalculation),
					Toast.LENGTH_SHORT).show();
		}
	}

	protected abstract Builder getBuilder();

	private void startTheApplication() {
		Intent intent = new Intent(this,
				com.anna.sent.soft.childbirthdate.MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.widgetConfigureButton) {
			if (doCalculation) {
				addWidget();
			} else {
				startTheApplication();
			}
		}
	}

	protected abstract boolean hasCountdown();

	protected abstract boolean hasShowCalculatingMethod();

	private void init() {
		SharedPreferences settings = Shared.getSettings(this);
		boolean byLMP = settings
				.getBoolean(
						Shared.Saved.Calculation.EXTRA_BY_LAST_MENSTRUATION_DATE,
						false);
		boolean byOvulation = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_OVULATION_DATE, false);
		boolean byUltrasound = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND, false);
		doCalculation = byLMP || byOvulation || byUltrasound;
		textView = (TextView) findViewById(R.id.widgetConfigureTextView);
		textView.setText(doCalculation ? getString(R.string.widgetSpecifyTheMethodOfCalculation)
				: getString(R.string.widgetStartTheApplicationToSpecifyNecessaryData));
		radioByLMP = (RadioButton) findViewById(R.id.radioByLMP);
		radioByLMP.setVisibility(byLMP ? View.VISIBLE : View.GONE);
		radiobyByOvulation = (RadioButton) findViewById(R.id.radioByOvulation);
		radiobyByOvulation
				.setVisibility(byOvulation ? View.VISIBLE : View.GONE);
		radioByUltrasound = (RadioButton) findViewById(R.id.radioByUltrasound);
		radioByUltrasound
				.setVisibility(byUltrasound ? View.VISIBLE : View.GONE);

		if (radioByLMP.getVisibility() == View.VISIBLE) {
			radioByLMP.setChecked(true);
		} else if (radiobyByOvulation.getVisibility() == View.VISIBLE) {
			radiobyByOvulation.setChecked(true);
		} else if (radioByUltrasound.getVisibility() == View.VISIBLE) {
			radioByUltrasound.setChecked(true);
		}

		checkBoxCountdown = (CheckBox) findViewById(R.id.checkBoxCountdown);
		checkBoxCountdown
				.setVisibility(hasCountdown() && doCalculation ? View.VISIBLE
						: View.GONE);

		checkBoxShowCalculatingMethod = (CheckBox) findViewById(R.id.checkBoxShowCalculatingMethod);
		checkBoxShowCalculatingMethod.setVisibility(hasShowCalculatingMethod()
				&& doCalculation ? View.VISIBLE : View.GONE);

		button = (Button) findViewById(R.id.widgetConfigureButton);
		button.setText(doCalculation ? getString(R.string.widgetAddWidget)
				: getString(R.string.widgetStartTheApplication));
		button.setOnClickListener(this);
	}

	private void saveWidgetParams() {
		SharedPreferences settings = Shared.getSettings(this);
		Editor editor = settings.edit();
		int calculatingMethod = Shared.Saved.Widget.Calculate.UNKNOWN;
		if (radioByLMP.isChecked()) {
			calculatingMethod = Shared.Saved.Widget.Calculate.BY_LMP;
		} else if (radiobyByOvulation.isChecked()) {
			calculatingMethod = Shared.Saved.Widget.Calculate.BY_OVULATION_DAY;
		} else if (radioByUltrasound.isChecked()) {
			calculatingMethod = Shared.Saved.Widget.Calculate.BY_ULTRASOUND;
		}

		editor.putInt(Shared.Saved.Widget.EXTRA_CALCULATING_METHOD
				+ mAppWidgetId, calculatingMethod);

		if (hasCountdown()) {
			editor.putBoolean(Shared.Saved.Widget.EXTRA_COUNTDOWN
					+ mAppWidgetId, checkBoxCountdown.isChecked());
		}

		if (hasShowCalculatingMethod()) {
			editor.putBoolean(Shared.Saved.Widget.EXTRA_SHOW_CALCULATING_METHOD
					+ mAppWidgetId, checkBoxShowCalculatingMethod.isChecked());
		}

		editor.commit();
	}
}
