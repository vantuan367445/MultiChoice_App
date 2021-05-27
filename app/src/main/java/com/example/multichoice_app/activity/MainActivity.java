package com.example.multichoice_app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.example.multichoice_app.R;
import com.example.multichoice_app.common.GlobalHelper;
import com.example.multichoice_app.common.PreferenceHelper;
import com.example.multichoice_app.utils.APIUtills;
import com.example.multichoice_app.utils.DataClient;
import com.example.multichoice_app.model.JSONStudentObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends BaseActivity {
    @BindView(R.id.relative_content)
    RelativeLayout relative_content;
    @BindView(R.id.relative_loading)
    RelativeLayout relative_loading;
    @BindView(R.id.iv_background)
    View iv_background;

    @BindView(R.id.card_login_google)
    CardView card_login_google;

    @BindDrawable(R.drawable.bg_button_white_3)
    Drawable bg_button_white_3;


    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceHelper = new PreferenceHelper(this, GlobalHelper.PREFERENCE_NAME_APP);
        if (preferenceHelper.getThemeValue() == 0)
            setTheme(R.style.ThemeLight);
        else setTheme(R.style.ThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDataPhone();

        if (preferenceHelper.getProfile() != null && !preferenceHelper.getProfile().isEmpty()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mAuth = FirebaseAuth.getInstance();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            card_login_google.setBackground(bg_button_white_3);


        }

    }

    private void getDataPhone() {
        if(preferenceHelper.getStatusBarHeight() == 0)
            preferenceHelper.setStatusBarHeight(GlobalHelper.getStatusBarHeight(MainActivity.this));
         if(preferenceHelper.getActionBarHeight() == 0)
            preferenceHelper.setActionBarHeight(GlobalHelper.getActionBarHeight(MainActivity.this));
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            relative_loading.setVisibility(View.VISIBLE);
            iv_background.setVisibility(View.VISIBLE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("CHECK_TOKEN","token2 = " + account.getIdToken());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("LOGIN_FIREBASE", "Google sign in failed", e);
                // ...
            }
        }
    }

    @OnClick({R.id.card_login_google})
    void action(View view) {
        if (view.getId() == R.id.card_login_google) {

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.amin_shake_1);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    signIn();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            card_login_google.startAnimation(animation);

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if (account == null)
            return;
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user != null){
                            Log.d("CHECK_TOKEN","token2 = " + new Gson().toJson(user));
                            updateUI(user);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LOGIN_LOG", "signInWithCredential:failure", task.getException());
                    }

                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String personName = user.getDisplayName();
            String personEmail = user.getEmail();
            Uri personPhoto = user.getPhotoUrl();

            sendInfoClient(personEmail, personName, personPhoto == null ? "" : personPhoto.toString());
        }
    }

    private void sendInfoClient(String personEmail, String personName, String personPhoto) {
        DataClient dataClient = APIUtills.getData();
        Call<String> call = dataClient.requesrStudent(personName, personEmail);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        relative_loading.setVisibility(View.GONE);
                        iv_background.setVisibility(View.GONE);

                        JSONStudentObject studentObject = new Gson().fromJson(response.body(), JSONStudentObject.class);
                        studentObject.setUrl_Photo(personPhoto);

                        String json = new Gson().toJson(studentObject);

                        preferenceHelper.setProfile(json);

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.d("LOGIN_LOG", "2 = " + e.toString());
                    }


                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("LOI", t.getMessage());
            }
        });
    }

}