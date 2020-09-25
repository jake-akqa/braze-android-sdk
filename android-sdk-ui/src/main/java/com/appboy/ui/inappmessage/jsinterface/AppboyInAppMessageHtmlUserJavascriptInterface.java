package com.appboy.ui.inappmessage.jsinterface;

import android.content.Context;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.appboy.Appboy;
import com.appboy.AppboyUser;
import com.appboy.enums.Gender;
import com.appboy.enums.Month;
import com.appboy.enums.NotificationSubscriptionType;
import com.appboy.events.SimpleValueCallback;
import com.appboy.support.AppboyLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppboyInAppMessageHtmlUserJavascriptInterface {
  private static final String TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageHtmlUserJavascriptInterface.class);
  public static final String JS_BRIDGE_UNSUBSCRIBED = "unsubscribed";
  public static final String JS_BRIDGE_SUBSCRIBED = "subscribed";
  public static final String JS_BRIDGE_OPTED_IN = "opted_in";
  public static final String JS_BRIDGE_GENDER_MALE = Gender.MALE.forJsonPut();
  public static final String JS_BRIDGE_GENDER_FEMALE = Gender.FEMALE.forJsonPut();
  public static final String JS_BRIDGE_GENDER_OTHER = Gender.OTHER.forJsonPut();
  public static final String JS_BRIDGE_GENDER_UNKNOWN = Gender.UNKNOWN.forJsonPut();
  public static final String JS_BRIDGE_GENDER_NOT_APPLICABLE = Gender.NOT_APPLICABLE.forJsonPut();
  public static final String JS_BRIDGE_GENDER_PREFER_NOT_TO_SAY = Gender.PREFER_NOT_TO_SAY.forJsonPut();
  public static final String JS_BRIDGE_ATTRIBUTE_VALUE = "value";

  private final Context mContext;

  public AppboyInAppMessageHtmlUserJavascriptInterface(Context context) {
    mContext = context;
  }

  @JavascriptInterface
  public void setFirstName(final String firstName) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setFirstName(firstName);
      }
    });
  }

  @JavascriptInterface
  public void setLastName(final String lastName) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setLastName(lastName);
      }
    });
  }

  @JavascriptInterface
  public void setEmail(final String email) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setEmail(email);
      }
    });
  }

  @JavascriptInterface
  public void setGender(String genderString) {
    final Gender gender = parseGender(genderString);
    if (gender == null) {
      AppboyLogger.w(TAG, "Failed to parse gender in Braze HTML in-app message "
          + "javascript interface with gender: " + genderString);
    } else {
      Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
        @Override
        public void onSuccess(@NonNull AppboyUser currentUser) {
          currentUser.setGender(gender);
        }
      });
    }
  }

  @JavascriptInterface
  public void setDateOfBirth(final int year, int monthInt, final int day) {
    final Month month = monthFromInt(monthInt);
    if (month == null) {
      AppboyLogger.w(TAG, "Failed to parse month for value " + monthInt);
      return;
    }

    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setDateOfBirth(year, month, day);
      }
    });
  }

  @JavascriptInterface
  public void setCountry(final String country) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setCountry(country);
      }
    });
  }

  @JavascriptInterface
  public void setLanguage(final String language) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setLanguage(language);
      }
    });
  }

  @JavascriptInterface
  public void setHomeCity(final String homeCity) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setHomeCity(homeCity);
      }
    });
  }

  @JavascriptInterface
  public void setEmailNotificationSubscriptionType(String subscriptionType) {
    final NotificationSubscriptionType subscriptionTypeEnum = subscriptionTypeFromJavascriptString(subscriptionType);
    if (subscriptionTypeEnum == null) {
      AppboyLogger.w(TAG, "Failed to parse email subscription type in Braze HTML in-app message javascript interface with subscription " + subscriptionType);
      return;
    }

    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setEmailNotificationSubscriptionType(subscriptionTypeEnum);
      }
    });
  }

  @JavascriptInterface
  public void setPushNotificationSubscriptionType(String subscriptionType) {
    final NotificationSubscriptionType subscriptionTypeEnum = subscriptionTypeFromJavascriptString(subscriptionType);
    if (subscriptionTypeEnum == null) {
      AppboyLogger.w(TAG, "Failed to parse push subscription type in Braze HTML in-app message javascript interface with subscription: " + subscriptionType);
      return;
    }

    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setPushNotificationSubscriptionType(subscriptionTypeEnum);
      }
    });
  }

  @JavascriptInterface
  public void setPhoneNumber(final String phoneNumber) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setPhoneNumber(phoneNumber);
      }
    });
  }

  @JavascriptInterface
  public void setCustomUserAttributeJSON(final String key, final String jsonStringValue) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        setCustomAttribute(currentUser, key, jsonStringValue);
      }
    });
  }

  @JavascriptInterface
  public void setCustomUserAttributeArray(final String key, String jsonArrayString) {
    final String[] arrayValue = parseStringArrayFromJsonString(jsonArrayString);
    if (arrayValue == null) {
      AppboyLogger.w(TAG, "Failed to set custom attribute array for key " + key);
      return;
    }

    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setCustomAttributeArray(key, arrayValue);
      }
    });
  }

  @JavascriptInterface
  public void addToCustomAttributeArray(final String key, final String value) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.addToCustomAttributeArray(key, value);
      }
    });
  }

  @JavascriptInterface
  public void removeFromCustomAttributeArray(final String key, final String value) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.removeFromCustomAttributeArray(key, value);
      }
    });
  }

  @JavascriptInterface
  public void incrementCustomUserAttribute(final String attribute) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.incrementCustomUserAttribute(attribute);
      }
    });
  }

  @JavascriptInterface
  public void setCustomLocationAttribute(final String attribute, final double latitude, final double longitude) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.setLocationCustomAttribute(attribute, latitude, longitude);
      }
    });
  }

  @JavascriptInterface
  public void addAlias(final String alias, final String label) {
    Appboy.getInstance(mContext).getCurrentUser(new SimpleValueCallback<AppboyUser>() {
      @Override
      public void onSuccess(@NonNull AppboyUser currentUser) {
        currentUser.addAlias(alias, label);
      }
    });
  }

  @VisibleForTesting
  Month monthFromInt(int monthInt) {
    if (monthInt < 1 || monthInt > 12) {
      return null;
    }

    return Month.getMonth(monthInt - 1);
  }

  @VisibleForTesting
  NotificationSubscriptionType subscriptionTypeFromJavascriptString(String subscriptionType) {
    String subscriptionTypeLowerCase = subscriptionType.toLowerCase(Locale.US);
    switch (subscriptionTypeLowerCase) {
      case JS_BRIDGE_SUBSCRIBED:
        return NotificationSubscriptionType.SUBSCRIBED;
      case JS_BRIDGE_UNSUBSCRIBED:
        return NotificationSubscriptionType.UNSUBSCRIBED;
      case JS_BRIDGE_OPTED_IN:
        return NotificationSubscriptionType.OPTED_IN;
      default:
        return null;
    }
  }

  @VisibleForTesting
  void setCustomAttribute(AppboyUser user, String key, String jsonStringValue) {
    try {
      JSONObject jsonObject = new JSONObject(jsonStringValue);
      Object valueObject = jsonObject.get(JS_BRIDGE_ATTRIBUTE_VALUE);
      // JSONObject in Android never deals with float values, which
      // accounts for why that instanceof check is missing below
      if (valueObject instanceof String) {
        user.setCustomUserAttribute(key, (String) valueObject);
      } else if (valueObject instanceof Boolean) {
        user.setCustomUserAttribute(key, (Boolean) valueObject);
      } else if (valueObject instanceof Integer) {
        user.setCustomUserAttribute(key, (Integer) valueObject);
      } else if (valueObject instanceof Double) {
        user.setCustomUserAttribute(key, ((Double) valueObject));
      } else {
        AppboyLogger.w(TAG, "Failed to parse custom attribute type for key: " + key
            + " and json string value: " + jsonStringValue);
      }
    } catch (Exception e) {
      AppboyLogger.e(TAG, "Failed to parse custom attribute type for key: " + key
          + " and json string value: " + jsonStringValue, e);
    }
  }

  @VisibleForTesting
  String[] parseStringArrayFromJsonString(String jsonArrayString) {
    try {
      JSONArray parsedArray = new JSONArray(jsonArrayString);
      List<String> list = new ArrayList<>();
      for (int i = 0; i < parsedArray.length(); i++) {
        list.add(parsedArray.getString(i));
      }
      return list.toArray(new String[0]);
    } catch (Exception e) {
      AppboyLogger.e(TAG, "Failed to parse custom attribute array", e);
    }
    return null;
  }

  @VisibleForTesting
  Gender parseGender(String genderString) {
    if (genderString == null) {
      return null;
    }

    String genderStringLowerCase = genderString.toLowerCase(Locale.US);
    if (genderStringLowerCase.equals(JS_BRIDGE_GENDER_MALE)) {
      return Gender.MALE;
    } else if (genderStringLowerCase.equals(JS_BRIDGE_GENDER_FEMALE)) {
      return Gender.FEMALE;
    } else if (genderStringLowerCase.equals(JS_BRIDGE_GENDER_OTHER)) {
      return Gender.OTHER;
    } else if (genderStringLowerCase.equals(JS_BRIDGE_GENDER_UNKNOWN)) {
      return Gender.UNKNOWN;
    } else if (genderStringLowerCase.equals(JS_BRIDGE_GENDER_NOT_APPLICABLE)) {
      return Gender.NOT_APPLICABLE;
    } else if (genderStringLowerCase.equals(JS_BRIDGE_GENDER_PREFER_NOT_TO_SAY)) {
      return Gender.PREFER_NOT_TO_SAY;
    }

    return null;
  }
}
