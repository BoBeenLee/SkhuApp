package com.skhu.bobinlee.skhuapp.testcase;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.skhu.bobinlee.skhuapp.util.CommonUtils;

import junit.framework.TestCase;

/**
 * Created by BoBinLee on 2014-09-09.
 */
public class AddressTest extends TestCase {

    public void test(){
        Log.d("test", "test : " + CommonUtils.getLocalIpAddress(CommonUtils.INET4ADDRESS));
    }

    public void test1(){

    }
}
