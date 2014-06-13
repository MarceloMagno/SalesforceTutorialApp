package com.darkbot.tutorial.BeMyApp_Tutorial;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.app.SalesforceSDKManager.KeyInterface;
import com.salesforce.androidsdk.security.Encryptor;

/**
 * Created by Fabled on 6/11/2014.
 */
public class KeyImpl implements KeyInterface {
    @Override
    public String getKey(String name) {
        return Encryptor.hash(name + "12s9adpahk;n12-97sdainkasd=012", name + "12kl0dsakj4-cxh1qewkjasdol8");
    }

}
