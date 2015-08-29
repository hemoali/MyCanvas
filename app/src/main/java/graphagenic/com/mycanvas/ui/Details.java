package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import graphagenic.com.inappbilling.util.IabHelper;
import graphagenic.com.inappbilling.util.IabResult;
import graphagenic.com.inappbilling.util.Purchase;
import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.server.UploadToServer;
import graphagenic.com.mycanvas.utils.Checks;
import graphagenic.com.mycanvas.utils.MyTypeFace;
import graphagenic.com.mycanvas.utils.Tools;

public class Details extends Activity {
    public static IabHelper mHelper;
    static String code;
    public TextView name, email, phone, address;
    public static String nameTXT, emailTXT, phoneTXT, addressTXT, width, height, type;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        width = getIntent().getStringExtra("width");
        height = getIntent().getStringExtra("height");
        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);

        activity = this;
        // Initialize billing
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyxiUXjfCt5Mi9Sc5DH/ONqe1fzSz//Re2uuBUySuP6lLbgNYbhOiy4RYV4QtYTZgiqL3l8OTDni3SPTIgCCVjvqtSGAK+m1ov2ixj2AkCw9sW7LKEvdu+Vq0gS9hqts17Wbg3Na5ncHgxpS38sjhzjJxB6+p78/VJECwgO1KOiSO614j/CMbgv4fcQeZA2eIxJ+YdAIsNaGwY409TPgtR5ZcZfRbr37jbUzT7xy1G08htAUVuqycJMgKsEhGCi23OHtBMcKrb4YNJvbQ3qvagwp72bICRX6LiiKgaIeIqM/5t4LshOb8iIBsGntwPD0hriEO6to5ZJ5/rEn2irdLeQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {
                                               Log.d("Billing", "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d("Billing", "In-app Billing is set up OK");
                                           }
                                       }
                                   });

        MyTypeFace.setTextViewTypeFace(((TextView) findViewById(R.id.top_bar_title)), "helbold.ttf", this, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                Tools.showLongMsg(activity, "Please try again later");
                enableView();
                return;
            } else if (purchase.getSku().equals(code)) {
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                new UploadToServer(activity, nameTXT, emailTXT, phoneTXT, addressTXT, width, height, type ).execute();
                // upload to server and check if everything goes well

                // Go to next page showing thnx msg
            }

        }
    };
    public static IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (mHelper == null) return;

        }
    };

    public void buy(View v) {
        phoneTXT = phone.getText().toString().trim();
        nameTXT = name.getText().toString().trim();
        addressTXT = address.getText().toString().trim();
        emailTXT = email.getText().toString().trim();
        if (nameTXT.equals("") || emailTXT.equals("") || addressTXT.equals("") || phoneTXT.equals("")) {
            Tools.showShortMsg(this, "Please fill all fields");
        } else {
            if (Checks.isEmailValid(emailTXT)) {
                if (Checks.isPhoneValid(phoneTXT)) {
                    disableView(v);

                    mHelper.launchPurchaseFlow(this, code, 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");

                } else {
                    Tools.showShortMsg(this, "Please use valid phone");
                }
            } else {
                Tools.showShortMsg(this, "Please use valid email");
            }
        }
    }

    public static void disableView(View v) {
        v.setClickable(false);
    }

    public static void enableView() {
        ((Button) activity.findViewById(R.id.buy_button)).setClickable(true);
    }

    public void goBack(View v) {
        onBackPressed();
    }

}
