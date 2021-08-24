package com.example.bank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionDetailsActivity extends AppCompatActivity {

    String transactionSelect;
    TextView tv_transactionDetails;

    ImageButton transactionDetails_ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        transactionDetails_ib_back = (ImageButton) findViewById(R.id.transactionDetails_ib_back);
        transactionDetails_ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_transactionDetails = (TextView) findViewById(R.id.tv_transactionDetails);
        tv_transactionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listItems = new ArrayList<>();
                listItems.add("전체");
                listItems.add("출금");
                listItems.add("입금");
                CharSequence[] items = listItems.toArray(new String[listItems.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetailsActivity.this);
                builder.setTitle("거래 내역 선택");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = items[which].toString();
                        if(Objects.equals(selectedText, "전체")) {
                            detailAll();
                        } else if(Objects.equals(selectedText, "입금")) {
                            detailDeposit();
                        } else if(Objects.equals(selectedText, "출금")) {
                            detailWithDraw();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void detailAll() {

    }
    private void detailDeposit() {

    }
    private void detailWithDraw() {

    }
}