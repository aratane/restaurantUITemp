package moun.com.deli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;

import moun.com.deli.database.UserDAO;
import moun.com.deli.fragment.ResetPasswordDialogFragment;
import moun.com.deli.util.AppUtils;
import moun.com.deli.util.SessionManager;


public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mTitle;
    private EditText mInputUsername;
    private EditText mInputPassword;
    private UserDAO userDAO;
    private SessionManager session;
    LoginActivity loginActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.login));
        mTitle.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOLD));
        mInputUsername = (EditText) findViewById(R.id.username);
        mInputPassword = (EditText) findViewById(R.id.password);
        userDAO = new UserDAO(this);
        // Session manager
        session = new SessionManager(getApplicationContext());


    }

    // Login button Click Event
    public void LoginClick(View view) {
        boolean isEmptyUsername = isEmpty(mInputUsername);
        boolean isEmptyPassword = isEmpty(mInputPassword);
        // Check for empty data in the form
        if (isEmptyUsername) {
            mInputUsername.setError("Enter your username");
            mInputPassword.setError(null);
        } else if (isEmptyPassword) {
            mInputPassword.setError("Enter your password");
            mInputUsername.setError(null);
        } else {
            mInputUsername.setError(null);
            mInputPassword.setError(null);
            final String username = mInputUsername.getText().toString().trim();
            final String password = mInputPassword.getText().toString().trim();
            if (userDAO.searchForUser(username) == null) {
                dialogMessage("Couldn't Sign In!", "Please check your username and password and try again.");
            } else {
                // user successfully logged in
                // Create login session
                session.setLogin(true);
                // Launch main activity
                Intent intent = new Intent(this, MainActivity.class);
                // Closing all the Activities
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Main Activity
                startActivity(intent);
                finish();


            }
        }

    }

    // Link to reset password dialog fragment
    public void ResetPassword(View view) {
        ResetPasswordDialogFragment.show(loginActivity);

    }

    // Link to Register Screen
    public void RegisteronClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    // Method to check for empty data in the form
    private boolean isEmpty(EditText editText) {
        return editText.getText() == null
                || editText.getText().toString() == null
                || editText.getText().toString().isEmpty();

    }

    // Custom dialog fragment using SimpleDialogFragment library
    private void dialogMessage(String title, String message) {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButtonText(getString(R.string.ok))
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
