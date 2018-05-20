package com.eways.etutor.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eways.etutor.R;
import com.eways.etutor.Utils.Handler.FragmentHandler;
import com.eways.etutor.Utils.Handler.GmailHandler;
import com.eways.etutor.Utils.SupportKey;
import com.google.android.gms.common.SignInButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    /** ----- VIEWS ------ */
    EditText edtPhone, edtPass;
    Button btnLogin;
    TextView  tvRules;
    View tvSignup;

    /** MODELS */
    FragmentHandler fragmentHandler;

    /** KEYS */
    public static final String KEY_PRE_LOGIN = "id_login";

    /** METHODS */
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        Declare(root);
        Handle();

        return root;
    }

    public static LoginFragment newInstance() {

        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    //Handle view
    public void Handle() {
        tvSignup.setOnClickListener(this);
    }

    //Declare views
    public void Declare(View root) {

        //Setup Fragment Handle
        fragmentHandler = new FragmentHandler(getActivity(), R.id.content_user);

        tvSignup = (View) root.findViewById(R.id.btn_dk);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        gmailHandler.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dk:
                fragmentHandler.changeFragment(new SignupFragment(), SupportKey.SIGN_UP_FRAGMENT_TAG,R.anim.slide_from_left, 0);
                break;
        }
    }
}
