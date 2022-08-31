package moun.com.deli.fragment;

import android.support.v4.app.FragmentActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;

import moun.com.deli.R;
import moun.com.deli.util.AppUtils;

/**
 * Custom Dialog Fragment using SimpleDialogFragment class
 * that prompts the user to add his email to reset his password.
 */
public class ResetPasswordDialogFragment extends SimpleDialogFragment {

    public static String TAG = "forgot_password";
    private EditText mInputEmail;
    private TextView textMsg;
    private TextView headerText;

    public static void show(FragmentActivity activity) {
        new ResetPasswordDialogFragment().show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        builder.setTitle("Reset Password");
        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.reset_pass_layout, null));

        builder.setPositiveButton("Reset Password", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (IPositiveButtonDialogListener listener : getPositiveButtonDialogListeners()) {
                    listener.onPositiveButtonClicked(mRequestCode);
                }
                mInputEmail = (EditText) getView().findViewById(R.id.your_email);
                textMsg = (TextView) getView().findViewById(R.id.text_msg);
                boolean isEmptyEmail = isEmpty(mInputEmail);
                if (isEmptyEmail) {
                    textMsg.setText("Please add your email address");
                } else {
                    String email = mInputEmail.getText().toString().trim();
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        textMsg.setText("Not valid");
                    } else {
                        AppUtils.CustomToast(getActivity(), "Please check your email address");
                        dismiss();
                    }
                }

            }
        });

        return builder;
    }

    // Method to check for empty data in the form
    private boolean isEmpty(EditText editText) {
        return editText.getText() == null
                || editText.getText().toString() == null
                || editText.getText().toString().isEmpty();

    }


}
