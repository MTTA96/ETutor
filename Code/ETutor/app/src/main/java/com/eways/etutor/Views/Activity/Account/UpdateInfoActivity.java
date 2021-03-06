package com.eways.etutor.Views.Activity.Account;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eways.etutor.Adapter.ImageChooseAdapter;
import com.eways.etutor.Interfaces.DataCallback.User.UserCallBack;
import com.eways.etutor.Model.Account.User;
import com.eways.etutor.Model.ImageSelect;
import com.eways.etutor.Presenter.Authentication.UserPresenter;
import com.eways.etutor.R;
import com.eways.etutor.Utils.DialogPlusHandler;
import com.eways.etutor.Utils.FileUtils;
import com.eways.etutor.Utils.Handler.FragmentHandler;
import com.eways.etutor.Utils.Handler.ImageHandler;
import com.eways.etutor.Utils.SharedPreferences.SharedPrefSupportKeys;
import com.eways.etutor.Utils.SharedPreferences.SharedPrefUtils;
import com.eways.etutor.Utils.SupportKey;
import com.eways.etutor.Utils.params.GlobalParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateInfoActivity extends AppCompatActivity implements View.OnClickListener, UserCallBack {

    /* VIEWS */
    ImageView ivAvarta, ivBack;
    EditText etLastName, etFirstName, etSkype, etCmnd, etBirthDay, etEmail, etPhone, etAddress, etCerti, etJob;
    TextView tvBack;

    /** ----- PROPS ----- */

    private UserPresenter userPresenter;
    private FragmentHandler fragmentHandler;
    private ImageHandler imageHandler;
    private DialogPlusHandler dialogPlusHandler;
    private ImageChooseAdapter imageChooseAdapter;
    private User user;
    private ArrayList<ImageSelect> imageSelects;
    private Uri imgUri;

    /** ---- LIFECYCLE----- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        declare_views();
        handle_views();
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(this, SharedPrefSupportKeys.SHARED_PREF_FILE_NAME);
        String id = sharedPrefUtils.getString(SharedPrefSupportKeys.UID);
        userPresenter = new UserPresenter(this);
        userPresenter.getUserInfo(id, this);
    }

    /** ----- CONFIG ----- */

    public void declare_views(){
        ivAvarta = findViewById(R.id.avarta);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etSkype = findViewById(R.id.skype_id);
        etCmnd = findViewById(R.id.cmnd);
        etBirthDay = findViewById(R.id.date_picker);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        etJob = findViewById(R.id.et_job);
        ivBack = findViewById(R.id.iv_back);
    }

    public void handle_views() {
        imageHandler = new ImageHandler(this);
        SetUpDialog();
        ivAvarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlusHandler.ShowDiglogPlus();
            }
        });

        etBirthDay.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    public void SetUpDialog() {
        imageSelects = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(FileUtils.loadJSONFromAsset(this, "image_choose"));
            for (int i = 0; i < jsonArray.length(); i++){
                imageSelects.add(GlobalParams.getInstance().getGSon().fromJson(jsonArray.get(i).toString(), ImageSelect.class));
            }
            imageChooseAdapter = new ImageChooseAdapter(this, R.layout.item_image_select, imageSelects);
            dialogPlusHandler = new DialogPlusHandler(this, imageChooseAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etBirthDay.setText(sdf.format(myCalendar.getTime()));
    }

    /** ----- SUPPORTED FUNC ----- */

    private void loadData() {

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etAddress.setText(user.getAddress());
        etBirthDay.setText(user.getBirthday());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etSkype.setText(user.getSkype());
        etJob.setText(user.getCareer());
        //etCmnd.setText(user.getVerification());

    }

    /** ----- ACTION ----- */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case DialogPlusHandler.REQUEST_CODE_CAMERA:
                imgUri = data.getData();
                imageHandler.loadImageRound(String.valueOf(imgUri), ivAvarta);

                break;

            case DialogPlusHandler.REQUEST_CODE_GALLERY:

                imgUri = data.getData();
                imageHandler.loadImageRound(String.valueOf(imgUri), ivAvarta);

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == DialogPlusHandler.REQUEST_CODE_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, DialogPlusHandler.REQUEST_CODE_CAMERA);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_picker:

                new DatePickerDialog(UpdateInfoActivity.this, mDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.iv_back:

                this.finish();
                break;
        }
    }
    @Override
    public void userCallBack(int errorCode, User user) {

        if(errorCode == SupportKey.FAILED_CODE) {
            Toast.makeText(this, "Không thể lấy thông tin!", Toast.LENGTH_LONG).show();
            return;
        }

        this.user = user;
        loadData();

    }
}
