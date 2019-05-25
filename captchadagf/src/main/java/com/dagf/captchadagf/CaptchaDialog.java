package com.dagf.captchadagf;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

public class CaptchaDialog extends AlertDialog {

    private SharedPreferences preferences;
    private CaptchaListener captchaListener;

    public static final String key_s = "DASDASDASDA";

    public interface CaptchaListener{
        void onAcceptCorrect();
        void onCancel();
    }


    protected CaptchaDialog(@NonNull Activity context) {
        super(context);
        preferences = context.getPreferences(Context.MODE_PRIVATE);
    }


    public CaptchaDialog(Activity c, CaptchaListener listener){
        super(c);
        preferences = c.getPreferences(Context.MODE_PRIVATE);
        this.captchaListener = listener;
    }


    public boolean shouldPresent(){
        return preferences.getInt(key_s, 0) == 0;
    }


    /** VIEWS ================================================== **/

    private EditText writer;
    private View[] butns;
    private ImageView img_captcha;


    private LottieAnimationView anims;

    @Override
    public void dismiss() {
        super.dismiss();

        if(!accept){
            captchaListener.onCancel();
        }
    }

    private boolean accept = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
butns = new View[4];


setContentView(R.layout.catpcha_dialog);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(getContext(), R.color.transparent));
        //getWindow().setGravity(Gravity.CENTER);


        img_captcha = findViewById(R.id.imgc);

        writer = findViewById(R.id.input_text);

        butns[0] = findViewById(R.id.view_normal);

       // butns[1] = findViewById(R.id.loadingview);

butns[2] = findViewById(R.id.cancelbtn);

butns[3] = findViewById(R.id.acceptbtn);

butns[2].setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        captchaListener.onCancel();
        dismiss();
    }
});


anims = findViewById(R.id.loadinganim);


SetupLoading(4);

getData();
    }


    /** =========================== GET DATA FROM SERVER =================================== **/
    private String valuereal;
    private void getData() {
        String urr = "http://wineberryhalley.com/secure/api/captcha/?getCaptcha";


        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, urr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject ob = new JSONObject(response);

                    String urlimg = ob.getString("link");

                    valuereal = ob.getString("value");

                    Picasso.get().load(Uri.parse(urlimg)).fit().into(img_captcha);

                    SetupOkButton();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
           captchaListener.onCancel();
           dismiss();
            }
        });

        queue.add(request);

    }


    /** ======================== SETUPSS =========================**/
    private void SetupOkButton() {
        butns[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writer.getText().toString().toLowerCase().equals(valuereal.toLowerCase())){
                    captchaListener.onAcceptCorrect();
                    preferences.edit().putInt(key_s, 1).commit();
                    accept = true;
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "Incorrect captcha", Toast.LENGTH_SHORT).show();
                    /*butns[1].setVisibility(View.VISIBLE);
                    butns[0].setVisibility(View.GONE);
                    SetupLoading(2);
                    getData();*/
                    captchaListener.onCancel();
                }
            }
        });
    }


    private void SetupLoading(final long i) {

    new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
            anims.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                 SetupBack(i / 2);

anims.setVisibility(View.GONE);
//butns[1].setVisibility(View.GONE);
butns[0].setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            anims.setRepeatCount(1);
        }
    }, i * 1000);




    }

    private void SetupBack(long is){

                final  LottieAnimationView vv = findViewById(R.id.animv);

                vv.setVisibility(View.VISIBLE);


                vv.setSpeed(0.5f);
         //   vv.setRepeatCount(1);
                vv.playAnimation();
            }

}
