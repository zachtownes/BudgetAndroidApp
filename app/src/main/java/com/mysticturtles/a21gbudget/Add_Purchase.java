package com.mysticturtles.a21gbudget;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Add_Purchase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);

        final Spinner Purchase_types_spinner = findViewById(R.id.purchase_type);
        final Spinner users_spinner = findViewById(R.id.purchase_user);

        OkHttpClient client = new OkHttpClient();

        String url = "https://budgetapi.mysticturtles.com/api/Types?GetPurchaseTypes=true";

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
                    final ArrayList<Types> types = new ArrayList<>();
                    types.add(new Types(0, "Please Select a Type"));
                    try {
                        JSONArray purchaseTypesJSONArray = new JSONArray(response.body().string());
                        for (int i = 0; i < purchaseTypesJSONArray.length(); i++) {
                            JSONObject purchaseTypesJSON1 = purchaseTypesJSONArray.getJSONObject(i);
                            String TypeName = purchaseTypesJSON1.getString("typeName");
                            int TypeID = purchaseTypesJSON1.getInt("typeId");
                            types.add(new Types(TypeID, TypeName));
                        }
                        Log.e("Test", types.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Purchase_types_spinner.setAdapter(new ArrayAdapter<Types>(Add_Purchase.this, android.R.layout.simple_spinner_dropdown_item, types));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
                                users_spinner.setAdapter(new ArrayAdapter<Users>( Add_Purchase.this, android.R.layout.simple_spinner_dropdown_item, users));
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

    public void submit_purchase(View view) {

        EditText amount;
        EditText source;
        Spinner user;
        Spinner type;
        EditText description;
        EditText date;
        final TextView status;

        amount= findViewById(R.id.purchase_amount);
        source = findViewById(R.id.purchase_source);
        date = findViewById(R.id.purchase_date);
        user = findViewById(R.id.purchase_user);
        type = findViewById(R.id.purchase_type);
        description = findViewById(R.id.purchase_description);
        status = findViewById(R.id.purchase_status);

        String AmountVar = amount.getText().toString();
        String SourceVar = source.getText().toString();
        String DateVar = date.getText().toString();
        String DescriptionVar = description.getText().toString();
        Types typeSpinner = (Types) type.getSelectedItem();
        Integer TypeVar = typeSpinner.getId();
        Users userSpinner = (Users) user.getSelectedItem();
        Integer UserVar = userSpinner.getUserID();
        Log.e("Amount Variable",AmountVar);
        Log.e("Source Variable", SourceVar);
        Log.e("Date Variable", DateVar);
        Log.e("Type Variable", TypeVar.toString());
        Log.e("User Variable", UserVar.toString());
        Log.e("Description Variable", DescriptionVar);
        OkHttpClient client = new OkHttpClient();

        String url = "https://budgetapi.mysticturtles.com/api/Transactions?addTransaction=true";

        RequestBody postData = new FormBody.Builder()
                .add("spender", UserVar.toString())
                .add("source", SourceVar)
                .add("amount", AmountVar)
                .add("category", TypeVar.toString())
                .add("date", DateVar)
                .add("description", DescriptionVar)
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

                    Add_Purchase.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            status.setText(myResponse);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                        finish();
                        Intent start_main = new Intent(Add_Purchase.this, MainActivity.class);
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