package com.appliedrec.plugin;

import android.content.Intent;

import com.appliedrec.mrtdreader.BACSpec;
import com.appliedrec.mrtdreader.MRTDScanActivity;
import com.appliedrec.mrtdreader.MRTDScanResult;
import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PassportReaderPlugin extends CordovaPlugin {

    public static final int REQUEST_CODE_SCAN_PASSPORT = 0;

    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("scanPassport".equals(action)) {
            try {
                if (this.callbackContext != null) {
                    throw new Exception("Another passport scan is already in progress");
                }
                for (int i = 0; i < args.length(); i++) {
                    if (!args.isNull(i)) {
                        JSONObject jsonObject = args.optJSONObject(i);
                        if (jsonObject != null && jsonObject.has("bacSpec")) {
                            String bacSpecString = jsonObject.getString("bacSpec");
                            Gson gson = new Gson();
                            BACSpec bacSpec = gson.fromJson(bacSpecString, BACSpec.class);
                            Intent intent = new Intent(cordova.getActivity(), MRTDScanActivity.class);
                            intent.putExtra(MRTDScanActivity.EXTRA_BAC_SPEC, bacSpec);
                            this.callbackContext = callbackContext;
                            cordova.startActivityForResult(this, intent, REQUEST_CODE_SCAN_PASSPORT);
                            return true;
                        }
                    }
                }
                throw new Exception("Missing or invalid bacSpec parameter");
            } catch (Exception e) {
                callbackContext.error(e.getLocalizedMessage());
            }
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_SCAN_PASSPORT) {
            final CallbackContext context = this.callbackContext;
            this.callbackContext = null;
            if (resultCode == RESULT_OK) {
                if (intent != null) {
                    if (intent.hasExtra(MRTDScanActivity.EXTRA_MRTD_SCAN_RESULT)) {
                        MRTDScanResult scanResult = intent.getParcelableExtra(MRTDScanActivity.EXTRA_MRTD_SCAN_RESULT);
                        cordova.getThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                final String jsonResult = gson.toJson(scanResult);
                                cordova.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        context.sendPluginResult(new PluginResult(PluginResult.Status.OK, jsonResult));
                                    }
                                });
                            }
                        });
                    } else if (intent.hasExtra(MRTDScanActivity.EXTRA_MRTD_SCAN_ERROR)) {
                        context.error(intent.getStringExtra(MRTDScanActivity.EXTRA_MRTD_SCAN_ERROR));
                    } else {
                        context.error("Received unexpected result from activity");
                    }
                } else {
                    context.error("Received empty data from activity");
                }
            } else if (resultCode == RESULT_CANCELED) {
                context.sendPluginResult(new PluginResult(PluginResult.Status.NO_RESULT));
            } else {
                context.error("Received unexpected result code from activity");
            }
        }
    }
}