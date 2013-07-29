package com.anna.sent.soft.utils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.MainActivity;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class ChildActivity extends StateSaverActivity {
	@Override
	public void setViews(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new ActionBarHelper().setupActionBar();
		}
	}

	private class ActionBarHelper {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void setupActionBar() {
			ActionBar actionBar = getActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Bundle savedState = getIntent().getBundleExtra(
					Shared.Result.EXTRA_MAIN_ACTIVITY_STATE);
			if (savedState == null) {
				// child activity is started from Widget
				createParentStack();
			} else {
				saveAdditionalData(savedState);

				// child activity is started from MainActivity
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtras(savedState);
				NavUtils.navigateUpTo(this, intent);
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void saveAdditionalData(Bundle state) {
	}

	@Override
	public void onBackPressed() {
		// Imitate Home-Up button click
		Bundle savedState = getIntent().getBundleExtra(
				Shared.Result.EXTRA_MAIN_ACTIVITY_STATE);
		if (savedState == null) {
			// child activity is started from Widget
			createParentStack();
		}

		super.onBackPressed();
	}

	private void createParentStack() {
		TaskStackBuilder tsb = TaskStackBuilder.create(this).addParentStack(
				this);
		tsb.startActivities();
	}
}
