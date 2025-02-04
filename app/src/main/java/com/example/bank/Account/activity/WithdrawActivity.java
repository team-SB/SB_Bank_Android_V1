package com.example.bank.Account.activity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bank.ApiProvider;
import com.example.bank.Auth.activity.SecPasswordActiviity;
import com.example.bank.R;
import com.example.bank.ServerAPI;
import com.example.bank.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends AppCompatActivity {

    private ImageButton withDraw_ib_back;

    private TextView tv_createApoorovalNum;
    private TextView withdraw_tv_cancel;

    private EditText withdraw_et_money;

    public static int withdrawMoney;

    private ImageView iv_createApoorovalNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        withdraw_et_money = (EditText) findViewById(R.id.withdraw_et_money);

        withDraw_ib_back = (ImageButton) findViewById(R.id.withDraw_ib_back);
        withDraw_ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_createApoorovalNum = (TextView) findViewById(R.id.tv_createApoorovalNum);
        tv_createApoorovalNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawMoney = Integer.parseInt(withdraw_et_money.getText().toString()) * -1;
                if(!(withdrawMoney == 0 || Integer.toString(withdrawMoney).equals(""))) {
                    startActivity(new Intent(WithdrawActivity.this, SecPasswordActiviity.class));
                } else {
                    Toast.makeText(WithdrawActivity.this, "금액을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        withdraw_tv_cancel = (TextView) findViewById(R.id.withdraw_tv_cancel);
        withdraw_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        iv_createApoorovalNum = (ImageView) findViewById(R.id.iv_createApoorovalNum);
        withdraw_et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_createApoorovalNum.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                iv_createApoorovalNum.setImageResource(R.drawable.pinkbox);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(SecPasswordActiviity.secSuccess) {
            ServerAPI serverAPI = ApiProvider.getInstance().create(ServerAPI.class);

            String bearerUserToken = "Bearer " + UserData.temp_token;

            Call<Void> call = serverAPI.chargeAccount(bearerUserToken, withdrawMoney);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int result = response.code();

                    if(result == 200) { // 성공
                        UserData.temp_token = UserData.user_token;
                        SecPasswordActiviity.secSuccess = false;

                        finish();
                        startActivity(new Intent(WithdrawActivity.this, ApprovalNumberActivity.class));
                    } else if (result == 403) {
                        Toast.makeText(WithdrawActivity.this, "2차 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    } else if (result == 404) {
                        Toast.makeText(WithdrawActivity.this, "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });

        }
    }
}