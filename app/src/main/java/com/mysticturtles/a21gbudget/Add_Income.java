package com.mysticturtles.a21gbudget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Add_Income extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        final Spinner income_types_spinner = findViewById(R.id.income_type);
        final Spinner users_spinner = findViewById(R.id.income_user);

        OkHttpClient client = new OkHttpClient();

        String url = "https://budgetapi.mysticturtles.com/api/Types?GetIncomeTypes=true";

        Request typesRequest = new Request.Builder()
                .url(url)
                .build();
        client.newCall(typesRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final ArrayList<Types> types = new ArrayList<>();
                    types.add(new Types(0, "Please Select a Type"));
                    try {
                        JSONArray incomeTypesJSONArray = new JSONArray(response.body().string());
                        for (int i = 0; i < incomeTypesJSONArray.length(); i++) {
                            JSONObject incomeTypesJSON1 = incomeTypesJSONArray.getJSONObject(i);
                            String TypeName = incomeTypesJSON1.getString("typeName");
                            int TypeID = incomeTypesJSON1.getInt("typeId");
                            types.add(new Types(TypeID, TypeName));
                        }
                        Log.e("Test", types.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                income_types_spinner.setAdapter(new ArrayAdapter<Types>(Add_Income.this, android.R.layout.simple_spinner_dropdown_item, types));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                response.body().close();
            }
        });

        String userURL = "https://budgetapi.mysticturtles.com/api/Transactions?getUsers=true";
        Request userRequest = new Request.Builder()
                .url(userURL)
                .build();
        client.newCall(userRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final ArrayList<Users> users = new ArrayList<>();
                    users.add(new Users( 0, "Please Select a User"));
                    try {
                        JSONArray usersJSONArray = new JSONArray(response.body().string());
                        Log.e("Test", usersJSONArray.toString());
                        for (int i = 0; i < usersJSONArray.length(); i++) {
                            JSONObject usersJSON1 = usersJSONArray.getJSONObject(i);
                            String name = usersJSON1.getString("name");
                            int userID = usersJSON1.getInt("userID");
                            users.add(new Users(userID, name));
                        }
                        Log.e("Test", users.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                users_spinner.setAdapter(new ArrayAdapter<Users>( Add_Income.this, android.R.layout.simple_spinner_dropdown_item, users));
                            }
                        });
                    } catch (JSONException e) {
                    e.printStackTrace();
                    }
                }
                response.body().close();
            }
        });
    }

    public void submit_income(View view) {

        EditText amount;
        EditText source;
        Spinner user;
        Spinner type;
        EditText date;
        final TextView status;



        amount= findViewById(R.id.income_amount);
        source = findViewById(R.id.income_source);
        date = findViewById(R.id.income_date);
        user = findViewById(R.id.income_user);
        type = findViewById(R.id.income_type);
        status = findViewById(R.id.income_status);

        String AmountVar = amount.getText().toString();
        String SourceVar = source.getText().toString();
        String DateVar = date.getText().toString();
        Types typeSpinner = (Types) type.getSelectedItem();
        Integer TypeVar = typeSpinner.getId();
        Users userSpinner = (Users) user.getSelectedItem();
        Integer UserVar = userSpinner.getUserID();
        Log.e("Amount Variable",AmountVar);
        Log.e("Source Variable", SourceVar);
        Log.e("Date Variable", DateVar);
        Log.e("Type Variable", TypeVar.toString());
        Log.e("User Variable", UserVar.toString());
        OkHttpClient client = new OkHttpClient();

        String url = "https://budgetapi.mysticturtles.com/api/Transactions?addTransaction=true";

        RequestBody postData = new FormBody.Builder()
                .add("spender", UserVar.toString())
                .add("source", SourceVar)
                .add("amount", AmountVar)
                .add("category", TypeVar.toString())
                .add("date", DateVar)
                .add("description", "AndroidAppAdd")
                .add("reoccur", "0")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(postData)
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

                    Add_Income.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            status.setText(myResponse);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                        finish();
                        Intent start_main = new Intent(Add_Income.this, MainActivity.class);
                        startActivity(start_main);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Error", response.toString());
                }
            }
        });
    }
}