package com.octostudio.octosugar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.octostudio.octosugar.*;
import com.octostudio.octosugar.activity.*;

public class OctoSugarActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(SugarContext.isLogin == false || SugarContext.sessionId <= 0)
        {


        }
        
    }
}