package com.octostudio.octosugar.activity;

import android.os.Bundle;
import android.content.Context;
import android.util.Log;

import com.octostudio.octosugar.R;

public class SugarActivity extends OctoActivity {
	Context m_Context;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_Context=getBaseContext();
    }
}
