package com.carsecurity.carsecurity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The configuration screen for the {@link MyWidgetActivity MyWidgetActivity} AppWidget.
 */
public class MyWidgetActivityConfigureActivity extends AppCompatActivity {
    DatabaseReference Xreference;
    TextView txEmail;
    TextView txName, latitude;
    ImageView imageView_logo;
    String Image;
    private String str_Name;


    private static final String PREFS_NAME = "com.carsecurity.carsecurity.MyWidgetActivity";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.my_widget_activity_configure);
        findViewById(R.id.add_Widget).setOnClickListener(mOnClickListener);


        txName = findViewById(R.id.txName);
        latitude = findViewById(R.id.latitude);
        txEmail = findViewById(R.id.txEmail);
        imageView_logo = findViewById(R.id.imageView_logo);
        Xreference = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Xreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Image = String.valueOf(dataSnapshot.child("imgs").getValue());
                str_Name = String.valueOf(dataSnapshot.child("Name").getValue());
                String str_Email = String.valueOf(dataSnapshot.child("EmailAddress").getValue());
                txEmail.setText(str_Email);
                txName.setText(str_Name);
                if (!Image.equals("") || !Image.isEmpty()) {
                    Picasso.with(MyWidgetActivityConfigureActivity.this).load(Image).into(imageView_logo);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        new MyAsyncTask().execute();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MyWidgetActivityConfigureActivity.this;

            // When the button is clicked, store the string locally

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MyWidgetActivity.updateAppWidget(context, appWidgetManager,
                    mAppWidgetId, "titlePrefix");

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            finish();
        }
    };

    public MyWidgetActivityConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = null;
            StringBuffer stringBuffer = null;
            InputStream inputStream = null;
            String result = null;
            try {
                URL url = new URL("https://api.thingspeak.com/channels/510399/feeds.json?api_key=E7XHVIY0U4XNI65T&results=2/");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                int n = 0;
                stringBuffer = new StringBuffer();
                while ((n = inputStream.read()) != -1) {
                    stringBuffer.append((char) n);
                }
                result = stringBuffer.toString();

            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            latitude.setText(s);

        }
    }
}

