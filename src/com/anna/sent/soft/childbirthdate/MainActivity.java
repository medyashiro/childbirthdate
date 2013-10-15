package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.actions.EmailDataActionActivity;
import com.anna.sent.soft.childbirthdate.actions.MarketChildbirthDateActionActivity;
import com.anna.sent.soft.childbirthdate.actions.MarketWomanCycActionActivity;
import com.anna.sent.soft.childbirthdate.base.StateSaverActivity;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;
import com.anna.sent.soft.utils.ThemeUtils;

public class MainActivity extends StateSaverActivity {
	@Override
	public void setViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void beforeOnSaveInstanceState() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment details = fm.findFragmentById(R.id.details);
		if (details != null) {
			fm.beginTransaction().remove(details).commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// update all widgets
		MyPregnancyWidget.updateAllWidgets(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		switch (ThemeUtils.getThemeId(this)) {
		case ThemeUtils.LIGHT_THEME:
			menu.findItem(R.id.lighttheme).setChecked(true);
			break;
		case ThemeUtils.DARK_THEME:
			menu.findItem(R.id.darktheme).setChecked(true);
			break;
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.lighttheme:
			ThemeUtils.changeToTheme(this, ThemeUtils.LIGHT_THEME);
			return true;
		case R.id.darktheme:
			ThemeUtils.changeToTheme(this, ThemeUtils.DARK_THEME);
			return true;
		case R.id.help:
			Intent help = new Intent(this, HelpActivity.class);
			startActivity(help);
			return true;
		case R.id.rate:
			Intent rate = new Intent(this,
					MarketChildbirthDateActionActivity.class);
			startActivity(rate);
			return true;
		case R.id.womancyc:
			Intent womancyc = new Intent(this,
					MarketWomanCycActionActivity.class);
			startActivity(womancyc);
			return true;
		case R.id.sendData:
			Intent sendData = new Intent(this, EmailDataActionActivity.class);
			startActivity(sendData);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
