package com.example.financas2;

import android.content.res.Configuration;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import java.util.Locale;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.definicoes);

        final ListPreference filtro = (ListPreference) findPreference("filtro_inicial");
        filtro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Integer valor = Integer.parseInt(String.valueOf(newValue).substring(1));
                filtro.setSummary(getResources().getText(R.string.filtro_inicial).toString() + ": " + nomeFiltro(valor));
                return true;
            }
        });
/*
        final ListPreference idioma = (ListPreference) findPreference("idioma_preferido");
        idioma.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Locale locale = new Locale(newValue.toString());
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                finish();
                startActivity(getIntent());
                return true;
            }
        });
*/
    }

    private String nomeFiltro(int valor) {
        switch (valor) {
            case 0: return getResources().getString(R.string.filtro_semana);
            case 1: return getResources().getString(R.string.filtro_mês);
            case 2:
            default: return getResources().getString(R.string.filtro_todos);
        }
    }
}
