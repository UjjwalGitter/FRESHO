package com.freshokartz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityProfile extends AppCompatActivity {

    TextView name, email, mobile_number, area, outstanding_balance, organization, gender, dateOfBirth, gstin;
    private ImageButton backhome;

    SessionManagement session;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ProfileApi.DJANGO_SITE)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ProfileApi profileApi = retrofit.create(ProfileApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        String token = user.get(SessionManagement.KEY_TOKEN);

        getProfileDetails(token);


        backhome = findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(ActivityProfile.this, ActivityMain.class);
                startActivity(i);
            }        });
    }

    private void getProfileDetails(String token) {

        String Token_request = "Token " + token;

        Call<BpResponseBody> call = profileApi.getDetail(Token_request);

        call.enqueue(new Callback<BpResponseBody>() {
            @Override
            public void onResponse(Call<BpResponseBody> call, Response<BpResponseBody> response) {
                BpResponseBody finalResponseBody = response.body();

                name = findViewById(R.id.name);
                email = findViewById(R.id.email);
                mobile_number = findViewById(R.id.mobile_number);
                organization = findViewById(R.id.organization);
                gender = findViewById(R.id.gender);
                area =findViewById(R.id.area);
                dateOfBirth = findViewById(R.id.dateOfBirth);
                gstin = findViewById(R.id.gstin);
                outstanding_balance = findViewById(R.id.outstanding_balance);

                name.setText(finalResponseBody.getFirstName());
                email.setText(finalResponseBody.getEmail());
                mobile_number.setText(finalResponseBody.getMobileNumber());
                organization.setText(finalResponseBody.getOrganizationName());
                area.setText(finalResponseBody.getArea().getArea());
                gender.setText(finalResponseBody.getGender());
                outstanding_balance.setText(finalResponseBody.getOutstanding_balance());
                dateOfBirth.setText(finalResponseBody.getDate_of_birth());
                gstin.setText(finalResponseBody.getGSTIN());

                Log.i("tuy", "hghyjv");
                Toast.makeText(ActivityProfile.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BpResponseBody> call, Throwable t) {
                Toast.makeText(ActivityProfile.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
