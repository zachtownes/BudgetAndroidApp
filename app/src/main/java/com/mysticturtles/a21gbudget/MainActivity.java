package com.mysticturtles.a21gbudget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView current_balance_box;
    private TextView titleBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current_balance_box = findViewById(R.id.current_balance);
        titleBar = findViewById(R.id.titleBar);
        OkHttpClient client = new OkHttpClient();
        int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
        String url = "https://budgetapi.mysticturtles.com/api/transactions?getBalance=true&year=" + CurrentYear;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String stringBalance = myResponse.substring(0, myResponse.length() - 2);
                            current_balance_box.setText(stringBalance);
                            if(stringBalance.contains("-")) {
                                titleBar.setBackgroundResource(R.color.purchase);
                                current_balance_box.setTextColor(getResources().getColor(R.color.purchase));
                            } else {
                                titleBar.setBackgroundResource(R.color.income);
                                current_balance_box.setTextColor(getResources().getColor(R.color.income));
                            }
                        }
                    });

                }
            }
        });
    }

    public void addIncome(View view){
        Intent start_income = new Intent(this, Add_Income.class);
        startActivity(start_income);
    }
    public void addExpense(View view){
        Intent start_expense = new Intent(this, Add_Expense.class);
        startActivity(start_expense);
    }
    public void addPurchase(View view){
        Intent start_purchase = new Intent(this, Add_Purchase.class);
        startActivity(start_purchase);
    }
}

