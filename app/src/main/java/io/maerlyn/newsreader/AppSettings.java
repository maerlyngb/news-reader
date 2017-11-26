package io.maerlyn.newsreader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Application preference screen
 *
 * @author Maerlyn Broadbent
 */
public class AppSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // get parent activity
            Activity activity = this.getActivity();

            // get bundle data from parent activity
            Bundle bundle = activity.getIntent().getExtras();

            if (bundle != null) {
                // create preference screen
                PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(activity);

                // create preference category
                PreferenceCategory category = new PreferenceCategory(activity);

                // set preference category title
                category.setTitle("News Sections");

                // add category to preference screen
                screen.addPreference(category);

                // get current preferences
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);

                Collection<String> unsortedBundleKeys = bundle.keySet();
                List<String> sectionIds = asSortedList(unsortedBundleKeys);

                // loop through available news sections
                for (String sectionId : sectionIds) {

                    // current news section
                    Section section = (Section) bundle.get(sectionId);

                    // create preference checkbox for this section
                    CheckBoxPreference sectionPref = new CheckBoxPreference(activity);
                    sectionPref.setKey(sectionId);
                    sectionPref.setTitle(section.getWebTitle());

                    // load the current preference if we have one.
                    // defaults to true
                    Boolean showSection = sharedPrefs.getBoolean(section.getId(), true);

                    // set pref ui to the current pref val
                    sectionPref.setChecked(showSection);

                    // add this preference to the list
                    category.addPreference(sectionPref);

                    // listen for changes so we can update the UI
                    // this is only here because the project rubric wants
                    // the val below the pref.
                    sectionPref.setOnPreferenceChangeListener(this);

                    // convert the boolean value to a more friendly string
                    // this also allows the display to be translated into other languages
                    String preferenceString = showSection
                            ? activity.getString(R.string.show_section_msg)
                            : activity.getString(R.string.hide_section_msg);


                    onPreferenceChange(sectionPref, preferenceString);
                }

                setPreferenceScreen(screen);
            }
        }

        /**
         * Display the current preference value under it's name
         *
         * @param preference to update
         * @param value      that it was changed to
         * @return true
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

        public static
        <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
            List<T> list = new ArrayList<T>(c);
            java.util.Collections.sort(list);
            return list;
        }
    }
}
