package com.superchat.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.app.voidz.R;
import com.app.voidz.interfaces.interfaceInstances;
import com.app.voidz.model.DeviceInfo;
import com.app.voidz.model.DeviceInfoOthers;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by MotoBeans on 9/29/2015.
 */
public class UtilDeviceInfo implements interfaceInstances {



    // Flag for GPS status
    public boolean isGPSEnabled = false;

    // Flag for network status
    public boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public static boolean appInstalledOrNot(Context context, String packagename) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * Device Info
     */
    public static String android_id;

    public static void getInternet() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static String getAndroidID(Context context) {
        try {
            android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }

        return android_id;
    }

    static class SIM_INFO{
        private String seviceprovider;
        private String mobileCountryCode;
        private String mobileNetworkCode;
        private String isoCountryCode;

        public String getSeviceprovider() {
            return seviceprovider;
        }

        public void setSeviceprovider(String seviceprovider) {
            this.seviceprovider = seviceprovider;
        }

        public String getMobileCountryCode() {
            return mobileCountryCode;
        }

        public void setMobileCountryCode(String mobileCountryCode) {
            this.mobileCountryCode = mobileCountryCode;
        }

        public String getMobileNetworkCode() {
            return mobileNetworkCode;
        }

        public void setMobileNetworkCode(String mobileNetworkCode) {
            this.mobileNetworkCode = mobileNetworkCode;
        }

        public String getIsoCountryCode() {
            return isoCountryCode;
        }

        public void setIsoCountryCode(String isoCountryCode) {
            this.isoCountryCode = isoCountryCode;
        }
    }
    public static String getIMEI(Context context){
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getSimCountryIso();
        return imei;
    }

    public static SIM_INFO getSIM_Info(Context context){
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String isoCountryCode = mngr.getNetworkCountryIso();
        int mobileNetworkCode = mngr.getNetworkType();
        String seviceprovider = mngr.getNetworkOperatorName();

        SIM_INFO objSIM = new SIM_INFO();
        objSIM.setSeviceprovider(seviceprovider);
        objSIM.setMobileNetworkCode(""+mobileNetworkCode);
        objSIM.setMobileCountryCode(isoCountryCode);
        objSIM.setIsoCountryCode(isoCountryCode);


        return objSIM;
    }

    String GetCountryZipCode(Context context) {

        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }

        return CountryZipCode;
    }

    public static String readKernelVersion() {
        try {
            Process p = Runtime.getRuntime().exec("uname -a");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is), 1024);
            String line = br.readLine();
            br.close();
            return line;
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }


    public static String getDeviceModelNumber() {
        String manufacturer = Build.VERSION.CODENAME;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "" + width + "X" + height + "";
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    // get System info.
    public static String OSNAME = System.getProperty("os.name");
    public static String OSVERSION = System.getProperty("os.version");
    public static String RELEASE = Build.VERSION.RELEASE;
    public static String DEVICE = Build.DEVICE;
    public static String MODEL = Build.MODEL;
    public static String PRODUCT = Build.PRODUCT;
    public static String BRAND = Build.BRAND;
    public static String DISPLAY = Build.DISPLAY;
    public static String CPU_ABI = Build.CPU_ABI;
    public static String CPU_ABI2 = Build.CPU_ABI2;
    public static String UNKNOWN = Build.UNKNOWN;
    public static String HARDWARE = Build.HARDWARE;
    public static String ID = Build.ID;
    public static String MANUFACTURER = Build.MANUFACTURER;
    public static String SERIAL = Build.SERIAL;
    public static String USER = Build.USER;
    public static String HOST = Build.HOST;

    public static String getDeviceInfo_JSON(Context context) {
        String JSON_DATA = "";
        try {
            DeviceInfo obj_device_info = getDeviceInfo_Object(context);
            JSON_DATA = new Gson().toJson(obj_device_info);
        } catch (Exception e) {

        }
        return JSON_DATA;
    }

    public static DeviceInfo getDeviceInfo_Object(Context context) {
        DeviceInfo obj_device_info = null;
        try {
            obj_device_info = new DeviceInfo();
            obj_device_info.setIMEI(getIMEI(context));
            obj_device_info.setANDROID_ID(getAndroidID(context));
            obj_device_info.setKERNEL_ID(readKernelVersion());
            obj_device_info.setDEVICE_MODEL_NUMBER(getDeviceModelNumber());
            obj_device_info.setOSNAME(OSVERSION);
            obj_device_info.setOSVERSION(OSVERSION);
            obj_device_info.setRELEASE(RELEASE);
            obj_device_info.setDEVICE(DEVICE);
            obj_device_info.setMODEL(MODEL);
            obj_device_info.setPRODUCT(PRODUCT);
            obj_device_info.setBRAND(BRAND);
            obj_device_info.setDISPLAY(DISPLAY);
            obj_device_info.setCPU_ABI(CPU_ABI);
            obj_device_info.setCPU_ABI2(CPU_ABI2);
            obj_device_info.setUNKNOWN(UNKNOWN);
            obj_device_info.setHARDWARE(HARDWARE);
            obj_device_info.setID(ID);
            obj_device_info.setMANUFACTURER(MANUFACTURER);
            obj_device_info.setSERIAL(SERIAL);
            obj_device_info.setUSER(USER);
            obj_device_info.setHOST(HOST);

            /**
             * Set More info Related to Device
             */
            {
                objLocation.setCurrentLocation(context);
                double latitude = objLocation.getLatitude();
                double longitude = objLocation.getLongitude();

                SIM_INFO objSIM = getSIM_Info(context);

                DeviceInfoOthers other = new DeviceInfoOthers();
                other.setNETWORK_TYPE(getNetworkClass(context));
                other.setLATITUDE(latitude);
                other.setLONGITUDE(longitude);
                other.setSCREEN_SIZE(getScreenResolution(context));
                if(objSIM != null){
                    other.setISO_COUNTRY_CODE(objSIM.getIsoCountryCode());
                    other.setMOBILE_COUNTRY_CODE(objSIM.getMobileCountryCode());
                    other.setMOBILE_NETWORK_CODE(objSIM.getMobileNetworkCode());
                    other.setSERVICE_PROVIDE(objSIM.getSeviceprovider());
                }
                obj_device_info.setOTHER(other);
            }

        } catch (Exception e) {

        }

        return obj_device_info;
    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; //not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    public boolean checkLoactionService(Context context) {
        boolean loactionCheck = false;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // Getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            loactionCheck = false;

        } else {
            loactionCheck = true;
        }
        return loactionCheck;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */
    public final static int KEY_GPS_SETTINGS = 111;
    public void showSettingsAlert(final Activity activity,
                                  final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.gps_enable));

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, KEY_GPS_SETTINGS);
                /*Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
				sendBroadcast(intent);*/
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
