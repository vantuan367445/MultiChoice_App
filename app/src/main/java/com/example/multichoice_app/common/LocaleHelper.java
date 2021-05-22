package com.example.multichoice_app.common;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class LocaleHelper {

    public static Context onAttach(Context context) {
        if (context == null)
            return null;
        return setLocale(context);
    }


    private static Context setLocale(Context context) {
        String language = new PreferenceHelper(context, GlobalHelper.PREFERENCE_NAME_APP).getLanguageDevice();
        Locale locale;
        if (language.contains("-")) {
            String[] langLocal = language.split("-");
            locale = new Locale(langLocal[0], langLocal[1]);
        } else {
            switch (language) {
                case "vi":
                    locale = new Locale(language, "VN");
                    break;
                case "en":
                    locale = new Locale(language, "EN");
                    break;
                default:
                    locale = new Locale(language);
                    break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return updateResources(context, locale);
        return updateResourcesLegacy(context, locale);
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale){
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);

        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, Locale locale){
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }


}
