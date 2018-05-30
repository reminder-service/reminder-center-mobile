package de.bahmut.android.app.reminder.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import de.bahmut.android.app.reminder.R;
import de.bahmut.android.app.reminder.api.ReminderClient;
import de.bahmut.android.app.reminder.api.ReminderException;
import de.bahmut.android.app.reminder.helper.SettingsHelper;

public class MainActivity extends AppCompatActivity {

    private EditText textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textMessage = (EditText) findViewById(R.id.message);
        final FloatingActionButton sendFab = (FloatingActionButton) findViewById(R.id.send);
        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendMessage(final View view) {
        if (textMessage.getText().toString().isEmpty()) {
            Snackbar.make(view, "Please enter a message", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        final Uri apiLocation = SettingsHelper.getApiLocation(this);
        final String apiUsername = SettingsHelper.getApiUsername(this);
        final String apiPassword = SettingsHelper.getApiPassword(this);

        final ReminderClient reminderClient = new ReminderClient(apiLocation, apiUsername, apiPassword);

        String message = "Successfully send";
        try {
            reminderClient.sendMessage(textMessage.getText().toString());
        } catch (final ReminderException e) {
            message = e.getMessage();
        }

        textMessage.setText("");
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
