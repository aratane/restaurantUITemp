package moun.com.deli.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import moun.com.deli.R;


public class AppUtils {

    public static final String FONT_BOOK = "bebas_beue_book.otf";
    public static final String FONT_BOLD = "bebas_beue_bold.otf";


    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    // A custom toast with customized shape including an image
    public static void CustomToast(Activity context, String text) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layouttoast = inflater.inflate(R.layout.custom_toast, (ViewGroup) context.findViewById(R.id.toast_custom));
        ((TextView) layouttoast.findViewById(R.id.text_toast)).setText(text);
        Toast mytoast = new Toast(context.getBaseContext());
        mytoast.setView(layouttoast);
        mytoast.setDuration(Toast.LENGTH_SHORT);
        mytoast.show();
    }

    public static Intent makeShareIntent(String text) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }


}
