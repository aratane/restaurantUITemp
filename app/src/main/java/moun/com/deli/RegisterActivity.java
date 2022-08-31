package moun.com.deli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.lang.ref.WeakReference;

import moun.com.deli.database.UserDAO;
import moun.com.deli.model.User;
import moun.com.deli.util.AppUtils;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mTitle;
    private EditText mInputUsername;
    private EditText mInputEmail;
    private EditText mInputAddress;
    private EditText mInputPhone;
    private EditText mInputPassword;
    private Button registerButton;
    private User user;
    private UserDAO userDAO;
    private UserRegisterTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.register));
        mTitle.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOLD));

        mInputUsername = (EditText) findViewById(R.id.name);
        mInputEmail = (EditText) findViewById(R.id.email);
        mInputAddress = (EditText) findViewById(R.id.address);
        mInputPhone = (EditText) findViewById(R.id.phone);
        mInputPassword = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.register_btn);
        userDAO = new UserDAO(this);

        registerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        boolean isEmptyUsername = isEmpty(mInputUsername);
        boolean isEmptyEmail = isEmpty(mInputEmail);
        boolean isEmptyAddress = isEmpty(mInputAddress);
        boolean isEmptyPhone = isEmpty(mInputPhone);
        boolean isEmptyPassword = isEmpty(mInputPassword);
        if (isEmptyUsername) {
            mInputUsername.setError("Please enter a username");
            mInputEmail.setError(null);
            mInputAddress.setError(null);
            mInputPhone.setError(null);
            mInputPassword.setError(null);
        } else if (isEmptyEmail) {
            mInputEmail.setError("Please enter an email address");
            mInputUsername.setError(null);
            mInputAddress.setError(null);
            mInputPhone.setError(null);
            mInputPassword.setError(null);
        } else if (isEmptyAddress) {
            mInputAddress.setError("Please enter your address");
            mInputUsername.setError(null);
            mInputEmail.setError(null);
            mInputPhone.setError(null);
            mInputPassword.setError(null);
        } else if (isEmptyPhone) {
            mInputPhone.setError("Please enter your phone number");
            mInputUsername.setError(null);
            mInputEmail.setError(null);
            mInputAddress.setError(null);
            mInputPassword.setError(null);
        } else if (isEmptyPassword) {
            mInputPassword.setError("Please enter a password");
            mInputUsername.setError(null);
            mInputEmail.setError(null);
            mInputAddress.setError(null);
            mInputPhone.setError(null);

        } else {
            String username = mInputUsername.getText().toString().trim();
            String email = mInputEmail.getText().toString().trim();
            String phone = mInputPhone.getText().toString().trim();
            String address = mInputAddress.getText().toString().trim();
            String password = mInputPassword.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mInputEmail.setError("Not valid");
                mInputUsername.setError(null);
                mInputAddress.setError(null);
                mInputPhone.setError(null);
                mInputPassword.setError(null);
            } else if (!isValidPassword(password)) {
                mInputPassword.setError("Must be at least 6 characters");
                mInputUsername.setError(null);
                mInputAddress.setError(null);
                mInputPhone.setError(null);
                mInputEmail.setError(null);
            } else if (username.length() < 3) {
                mInputUsername.setError("Username is too short");
                mInputAddress.setError(null);
                mInputPhone.setError(null);
                mInputEmail.setError(null);
                mInputPassword.setError(null);
            } else if (username.length() > 15) {
                mInputUsername.setError("Username is too long");
                mInputAddress.setError(null);
                mInputPhone.setError(null);
                mInputEmail.setError(null);
                mInputPassword.setError(null);
            } else if (userDAO.searchForUser(username) != null) {
                mInputUsername.setError("Choose a unique name");
                mInputAddress.setError(null);
                mInputPhone.setError(null);
                mInputEmail.setError(null);
                mInputPassword.setError(null);
            } else {
                mInputUsername.setError(null);
                mInputAddress.setError(null);
                mInputPhone.setError(null);
                mInputEmail.setError(null);
                mInputPassword.setError(null);
                userRegister(username, email, phone, address, password);

            }

        }

    }

    /**
     * This is not a complete login and registration built system.
     * simply we get the user data from the input form and save it in local database, in our case (sqlite).
     * but you need to interact with database server by inserting and fetching data using (GET/POST methods) requests,
     * and get the response back in JSON format.
     *
     * @param username
     * @param email
     * @param phone
     * @param address
     * @param password
     */
    public void userRegister(final String username, final String email, final String phone,
                             final String address, final String password) {

        user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);

        // use AsyncTask to save user data in database
        task = new UserRegisterTask(this);
        task.execute((Void) null);

    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;
        Context context;

        public UserRegisterTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
            this.context = context.getApplicationContext();
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            // Inserting row in user table
            long result = userDAO.saveUserToTable(user);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1) {
                    // successful registration
                    Intent intent = new Intent(context, LoginActivity.class);
                    // Closing all the Activities
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // Add new Flag to start new Activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Staring Login Activity
                    context.startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                    Log.d(LOG_TAG, user.toString());

                }


            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    // Method to check for empty data in the form
    private boolean isEmpty(EditText editText) {
        return editText.getText() == null
                || editText.getText().toString() == null
                || editText.getText().toString().isEmpty();

    }

    private boolean isValidPassword(String pass) {
        if (pass.length() >= 6) {
            return true;
        }
        return false;
    }


}
