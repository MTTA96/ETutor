package com.eways.etutor.Views.Activity;

import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

        import com.eways.etutor.R;
        import com.eways.etutor.Utils.Handler.FragmentHandler;
        import com.eways.etutor.Utils.SupportKey;
import com.eways.etutor.Views.Fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {

    /*---- FRAGMENT HANDLE -----*/
    FragmentHandler fragmentHandler;

    /*------ VIEWS ------*/
    View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Declare();

        Handle();

    }

    //Declare
    public void Declare() {
        content = findViewById(R.id.content_user);
    }

    //Handle
    public void Handle() {
        fragmentHandler = new FragmentHandler(this, R.id.content_user);

        fragmentHandler.changeFragment(new LoginFragment(), SupportKey.LOGIN_FRAGMENT_TAG, R.anim.slide_from_left, 0);
    }
}