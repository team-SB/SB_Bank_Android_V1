package com.example.bank.Account.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bank.ApiProvider;
import com.example.bank.Auth.activity.SecPasswordActiviity;
import com.example.bank.R;
import com.example.bank.ServerAPI;
import com.example.bank.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepositActivity extends AppCompatActivity {

    private static final String TAG = "DepositActivity";

    private ImageButton deposit_ib_back;

    private TextView tv_deposit;
    private TextView tv_depositCancel;

    private EditText deposit_et_money;

    private ImageView deposit_iv_deposit;

    public static int depositMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        deposit_et_money = (EditText) findViewById(R.id.deposit_et_money);

        deposit_ib_back = (ImageButton) findViewById(R.id.deposit_ib_back);
        deposit_ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_deposit = (TextView) findViewById(R.id.tv_deposit);
        tv_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositMoney = Integer.parseInt(deposit_et_money.getText().toString());
                if(!(depositMoney == 0)) {
                    secCertified();
                }
            }
        });

        tv_depositCancel = (TextView) findViewById(R.id.tv_depositCancel);
        tv_depositCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deposit_iv_deposit = (ImageView) findViewById(R.id.deposit_iv_deposit);
        deposit_et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_deposit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                deposit_iv_deposit.setImageResource(R.drawable.pinkbox);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void secCertified() {
        startActivity(new Intent(DepositActivity.this, SecPasswordActiviity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(SecPasswordActiviity.secSuccess) {
            ServerAPI serverAPI = ApiProvider.getInstance().create(ServerAPI.class);

            String bearerUserToken = "Bearer " + UserData.temp_token;

            Call<Void> call = serverAPI.chargeAccount(bearerUserToken, depositMoney);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int result = response.code();

                    if(result == 200) { // 성공
                        UserData.temp_token = UserData.user_token;
                        SecPasswordActiviity.secSuccess = false;

                        Toast.makeText(DepositActivity.this, depositMoney + "원 입금", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (result == 403) {
                        Toast.makeText(DepositActivity.this, "2차 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });

        }
    }
}