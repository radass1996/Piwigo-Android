/*
 * Piwigo for Android
 * Copyright (C) 2019-2019 Piwigo Team http://piwigo.org
 * Copyright (C) 2019-2019 Radko Varchola
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.piwigo.ui.settings;

import android.os.Bundle;

import org.piwigo.R;
import org.piwigo.io.repository.PreferencesRepository;

import javax.inject.Inject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.SeekBarPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import dagger.android.AndroidInjection;


public class SettingsActivity extends AppCompatActivity {

    @Inject
    PreferencesRepository preferences;

    private ListPreference mPreferenceThumbnailSize;
    private SeekBarPreference mPreferencePhotosPerRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);
        PreferenceFragmentCompat sf = new SettingsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, sf)
                .commitNow();

        mPreferenceThumbnailSize = sf.getPreferenceManager().findPreference(PreferencesRepository.KEY_PREF_DOWNLOAD_SIZE);
        mPreferencePhotosPerRow = sf.getPreferenceManager().findPreference(PreferencesRepository.KEY_PREF_PHOTOS_PER_ROW);

        String photosPerRowValue = getString(R.string.settings_photos_per_row_summary, preferences.getInt(PreferencesRepository.KEY_PREF_PHOTOS_PER_ROW));
        String thumbnailSizeValue = getString(R.string.settings_download_size_summary, preferences.getString(PreferencesRepository.KEY_PREF_DOWNLOAD_SIZE));

        mPreferenceThumbnailSize.setSummary(thumbnailSizeValue);
        mPreferencePhotosPerRow.setSummary(photosPerRowValue);

        mPreferencePhotosPerRow.setOnPreferenceChangeListener((preference, value) -> {
            mPreferencePhotosPerRow.setSummary(getString(R.string.settings_photos_per_row_summary, value));
            return true;
        });

        mPreferenceThumbnailSize.setOnPreferenceChangeListener((preference, value) -> {
            mPreferenceThumbnailSize.setSummary(getString(R.string.settings_download_size_summary, value.toString()));
            return true;
        });

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);
        }
    }
}
