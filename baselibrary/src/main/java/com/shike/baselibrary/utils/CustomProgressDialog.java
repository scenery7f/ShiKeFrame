package com.shike.baselibrary.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.shike.baselibrary.R;

public class CustomProgressDialog extends ProgressDialog {

	private Context mContext;

	public CustomProgressDialog(Context context) {
		super(context, R.style.Dialog_Fullscreen);
		this.mContext = context;
		setCanceledOnTouchOutside(false);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		setCanceledOnTouchOutside(false);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_layout);
 	}

}
