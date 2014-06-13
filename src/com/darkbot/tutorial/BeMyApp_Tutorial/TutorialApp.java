package com.darkbot.tutorial.BeMyApp_Tutorial;

import android.app.Application;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

/**
 * Created by Fabled on 6/11/2014.
 */
public class TutorialApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SalesforceSDKManager.initNative(getApplicationContext(), new KeyImpl(), TutorialActivity.class);

		/*
		 * Un-comment the line below to enable push notifications in this app.
		 * Replace 'pnInterface' with your implementation of 'PushNotificationInterface'.
		 * Add your Google package ID in 'bootonfig.xml', as the value
		 * for the key 'androidPushNotificationClientId'.
		 */
        // SalesforceSDKManager.getInstance().setPushNotificationReceiver(pnInterface);
    }
}
