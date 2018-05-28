package com.esiea.instafood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    GoogleSignInClient signInClient;
    TextView email;
    SignInButton signInButton;
    //private FirebaseAuth firebaseAuth;
    Button signOutButton;
    MenuItem menuItem;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.serveur_id_web))
                //.requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, signInOptions);
        //firebaseAuth = FirebaseAuth.getInstance();

        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signOutButton = (Button)findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.geo:
                return true;
            case R.id.sign_out:
                //Toast.makeText(MainActivity.this, " Item du Menu sélectionné", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, SecondActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseUser signInAccount = firebaseAuth.getCurrentUser();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(signInAccount);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(googleSignInAccountTask);
        }

    }

    private  void updateUI(GoogleSignInAccount account){//FirebaseUser account){
        email = (TextView)findViewById(R.id.email);
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);

        if (account != null){
            signInButton.setVisibility(View.GONE);
            //signOutButton.setVisibility(View.VISIBLE);
            //startActivity(new Intent(MainActivity.this, RestaurantActivity.class));
            email.append("\n " + account.getEmail());
        }
        else {
            signInButton.setVisibility(View.VISIBLE);
            //signOutButton.setVisibility(View.GONE);
        }
    }

    private void signIn(){
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        //firebaseAuth.signOut();
        signInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUI(account);//firebaseAuthGoogle(account);
        } catch (ApiException e) {
            Log.e("SignIn", "signInResult: failed code=" + e.getStatusCode());
            Log.e("TAG", "Task complete? :" + task.isComplete()
                    + " Task successful? :"+ task.isSuccessful());
            updateUI(null);
        }
    }

    /*private void firebaseAuthGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.e("SignIn", "Authentification failed");
                            updateUI(null);
                        }
                    }
                });
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }
}
