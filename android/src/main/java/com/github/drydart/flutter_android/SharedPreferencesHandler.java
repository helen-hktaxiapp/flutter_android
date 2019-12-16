/* This is free and unencumbered software released into the public domain. */

package com.github.drydart.flutter_android;

import android.content.Context;
import android.content.SharedPreferences;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.text.TextUtils;

/** SharedPreferencesHandler */
class SharedPreferencesHandler extends FlutterMethodCallHandler {
  static final String CHANNEL = "flutter_android/SharedPreferences";

  SharedPreferencesHandler(final Registrar registrar) {
    super(registrar);
  }

  @Override
  public void onMethodCall(final MethodCall call, final Result result) {
    assert(call != null);
    assert(result != null);

    final Context context = registrar.context();
    assert(context != null);

    assert(call.method != null);
    switch (call.method) {
      case "getAll": {
        final String name = getRequiredArgument(call, "name");
        final int mode = getRequiredArgument(call, "mode");
        final SharedPreferences prefs = context.getSharedPreferences(name, mode);
        
        Map<String, ?> allPrefs = prefs.getAll();
        Map<String, Object> filteredPrefs = new HashMap<>();
        for (String key : allPrefs.keySet()) {
          Object value = allPrefs.get(key);
          if (value instanceof Set) {
            ArrayList<String> listValue = new ArrayList<>((Set) value);
            value = TextUtils.join("``", listValue);
          }
          filteredPrefs.put(key, value);
        }
        
        result.success(filteredPrefs);
        break;
      }
      default: {
        result.notImplemented();
      }
    }
  }
}
