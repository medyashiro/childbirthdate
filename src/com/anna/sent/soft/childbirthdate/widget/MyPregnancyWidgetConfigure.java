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
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.DataImpl;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.ThemeUtils;

public abstract class MyPregnancyWidgetConfigure extends Activity implements
		OnClickListener {
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private TextView textView;
	private RadioButton radio[] = new RadioButton[Shared.Calculation.METHODS_COUNT];
	private CheckBox checkBoxCountdown, checkBoxShowCalculatingMethod;
	private Button button;
	private boolean doCalculation = false;

	@Override
	protected void onCreate(Bundle arg0) {
		ThemeUtils.onDialogStyleActivityCreateSetTheme(this);
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
			return;
		}

		// Perform App Widget configuration.
		init();
	}

	protected abstract Class<?> getWidgetProviderClass();

	public void addWidget() {
		boolean addWidget = false;
		for (int i = 0; !addWidget && i < radio.length; ++i) {
			addWidget = addWidget || radio[i].isChecked();
		}

		if (addWidget) {
			// When the configuration is complete, get an instance of the
			// AppWidgetManager

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(this);

			// First
			saveWidgetParams();

			// Second. Update the App Widget with a RemoteViews layout
			RemoteViews views = getBuilder().buildViews(this, mAppWidgetId);
			appWidgetManager.updateAppWidget(mAppWidgetId, views);

			MyPregnancyWidget.installAlarms(this, getWidgetProviderClass());

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
		DataImpl data = new DataImpl(this);
		data.update();
		doCalculation = data.thereIsAtLeastOneSelectedMethod();
		textView = (TextView) findViewById(R.id.widgetConfigureTextView);
		textView.setText(doCalculation ? getString(R.string.widgetSpecifyTheMethodOfCalculation)
				: getString(R.string.widgetStartTheApplicationToSpecifyNecessaryData));
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		String[] methodNames = getResources().getStringArray(
				R.array.methodNames);
		boolean check = false, byMethod[] = data.byMethod();
		for (int i = 0; i < radio.length; ++i) {
			radio[i] = new RadioButton(this);
			radio[i].setVisibility(byMethod[i] ? View.VISIBLE : View.GONE);
			radio[i].setText(methodNames[i]);
			radioGroup.addView(radio[i]);
			if (!check) {
				radio[i].setChecked(true);
				check = true;
			}
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
		int calculatingMethod = Shared.Calculation.UNKNOWN;
		for (int i = 0; i < radio.length; ++i) {
			if (radio[i].isChecked()) {
				calculatingMethod = i + 1;
				break;
			}
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
