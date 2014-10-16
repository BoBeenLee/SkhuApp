package com.skhu.bobinlee.skhuapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;

import com.skhu.bobinlee.skhuapp.core.SessionManager;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by BoBinLee on 2014-09-04.
 */
public class CommonUtils {
    public final static int INET4ADDRESS = 1;
    public final static int INET6ADDRESS = 2;

    public static String getLocalIpAddress(int type) {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        switch (type) {
                            case INET6ADDRESS:
                                if (inetAddress instanceof Inet6Address) {
                                    return inetAddress.getHostAddress().toString();
                                }
                                break;
                            case INET4ADDRESS:
                                if (inetAddress instanceof Inet4Address) {
                                    return inetAddress.getHostAddress().toString();
                                }
                                break;
                        }

                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static String getMACAddress(Context context) {
        String str = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        Log.d("android_id", "android_id : " + str);
        return str;
//        try {
//            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface intf : interfaces) {
//                if (interfaceName != null) {
//                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
//                }
//                byte[] mac = intf.getHardwareAddress();
//                if (mac==null) return "";
//                StringBuilder buf = new StringBuilder();
//                for (int idx=0; idx<mac.length; idx++)
//                    buf.append(String.format("%02X:", mac[idx]));
//                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
//                return buf.toString();
//            }
//        } catch (Exception ex) { } // for now eat exceptions
//        return "";
    }

    public static float dpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float pxToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
