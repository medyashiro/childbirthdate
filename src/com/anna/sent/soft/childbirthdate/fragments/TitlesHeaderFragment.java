package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.ResultActivity;
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.childbirthdate.shared.DataClient;

public class TitlesHeaderFragment extends Fragment implements DataClient {
	public TitlesHeaderFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.titles_header, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button buttonCalculate = (Button) getActivity().findViewById(
				R.id.buttonCalculate);
		buttonCalculate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mData != null && mData.thereIsAtLeastOneSelectedMethod()) {
					Intent intent = new Intent(getActivity(),
							ResultActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(
							getActivity(),
							getString(R.string.errorNotSelectedCalculationMethod),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private Data mData = null;

	@Override
	public void setData(Data data) {
		mData = data;
	}
}