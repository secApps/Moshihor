// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.jarman.touchstone.moshihor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

// Referenced classes of package com.appsynopsis.jarman.dse:
//            MainActivity

public class Splash extends ActionBarActivity
{

    private final int SPLASH_DISPLAY_LENGTH = 1500;

    public Splash()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        (new Handler()).postDelayed(new Runnable() {



            public void run()
            {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            
            {

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
