package com.birina.bsecure.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.birina.bsecure.R;


/**
 * Created by 1262147 on 9/7/2016.
 */
public class BirinaProgressDialog extends ProgressDialog {
    public BirinaProgressDialog(Context context) {
        super(context);
    }

  RelativeLayout mProgressLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
       this.setContentView(R.layout.progress_bar_layout);
        mProgressLayout  = (RelativeLayout)findViewById(R.id.id_rlprogress_bar);
    }


    @Override
    public void show() {
        super.show();
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mProgressLayout.setVisibility(View.GONE);
    }
}
