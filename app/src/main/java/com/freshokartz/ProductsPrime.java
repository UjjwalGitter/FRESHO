package com.freshokartz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.freshokartz.Product.OrderInfo;
import com.freshokartz.Product.ProductList;
import com.freshokartz.Product.Result;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//Class for products individual description

public class ProductsPrime extends AppCompatActivity {
    TextView product_name, test;
    Button check, addtoCart, buynow;
    EditText incre;
    boolean invalidQuantity=false;
    boolean checkOfUnknown;
    TextView checks;
    Spinner spinner, pickup;
    int k;
    String value = "";
    double finalValue;

    ImageView plus, minus, cross;

    ExpandableRelativeLayout expa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String id = intent.getStringExtra("skId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_prime);
        //pickup = findViewById(R.id.pickup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        String[] grams = new String[]{
                "Kg",
                "g"
        };

//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.spinner_item,grams
//        );
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//        pickup.setAdapter(spinnerArrayAdapter);
//
//        pickup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (pickup.getSelectedItem()=="g"){
//                    value="gram";
//                }if (pickup.getSelectedItem()=="Kg"){
//                    value="kilogram";
//                }
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addtoCart = findViewById(R.id.addtocat);
        buynow = findViewById(R.id.buynow);
        cross = findViewById(R.id.cross);
        checks = findViewById(R.id.checks);
        incre = findViewById(R.id.incre);
//        String gr = incre.getText().toString();
//        if (gr.matches("")) {
//            Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        incre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (Double.parseDouble(incre.getText().toString())>10){
//                    incre.setError("Quantity can be atmost 10");
//                }
//            }
//        });
        //incre.addTextChangedListener(new Textc);

        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        expa = findViewById(R.id.expa);
        check = findViewById(R.id.check);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = incre.getText().toString();
                try {
                    finalValue = Double.parseDouble(value);
                    double gra = (int) finalValue;
                    if (!checkOfUnknown) {
                        finalValue = gra;
                    }
                    finalValue = finalValue + 0.250;
                    incre.setText(String.valueOf(finalValue));
                    checkOfUnknown = true;
                } catch (Exception e) {
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = incre.getText().toString();
                try {
                    finalValue = Double.parseDouble(value);
                    double gra = (int) finalValue;
                    if (!checkOfUnknown) {
                        finalValue = gra;
                        finalValue = finalValue + 0.250;
                    }
                    finalValue = finalValue - 0.250;
                    if (finalValue > 0) {
                        incre.setText(String.valueOf(finalValue));
                    }
                    checkOfUnknown = true;
                } catch (Exception e) {
                }
            }
        });
        getProductsDescription(id);

    }

    private void getProductsDescription(String id) {

        Call<ProductList> result = CatApi.getService().getProductsDescription(id);
        result.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                ProductList productList = response.body();
                final List<Result> main = productList.getResults();
                TextView prod;
                final TextView price, descript;
                ImageView pic;
                String pil;
                Button cart;
                prod = findViewById(R.id.product_name);
                pic = findViewById(R.id.product_image);
                price = findViewById(R.id.price);
                descript = findViewById(R.id.description);

                //cart = findViewById(R.id.cart);
//                finalValue = Integer.valueOf(incre.getText().toString());
//                k = Integer.valueOf(incre.getText().toString());
                Log.i("proname", main.get(0).getProductName());


                incre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        expa.collapse();
                        cross.setImageResource(R.drawable.ic_action_rigth);
                        try {
                            double gtr = Double.parseDouble(s.toString());
                            if (gtr > Double.valueOf(main.get(0).getMaximumSaleQuantity()) / 1000) {
                                s.replace(0, s.length(), "10");
                            }
                        } catch (NumberFormatException e) {
                        }

                        try {
                            double krt = Double.parseDouble(s.toString());
                            String numberD = String.valueOf(krt);
                            numberD = numberD.substring(numberD.indexOf("."));
                            System.out.println(numberD);

                            Log.i("numbers", numberD);
                            if (!numberD.equals(".5") && !numberD.equals(".25") && !numberD.equals(".75") && !numberD.equals(".0") ) {
                                Log.i("nbeers", numberD);
                                incre.setError("Write quantity like 0.25,1, 1.5, 1.75 ");
                                checkOfUnknown = false;
                                invalidQuantity=true;
                            }
                            else {
                                invalidQuantity=false;
                                incre.setError(null);
                            }
                        } catch (Exception e) {
                        }

                    }

                });

                addtoCart.setOnClickListener(new View.OnClickListener() {
                    int trt;

                    @Override
                    public void onClick(View v) {

                        if (addtoCart.getText().equals("Add to Cart")) {
                            addtoCart.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
                            addtoCart.setText("Added to Cart");
                        }
                        else if(addtoCart.getText().equals("Added to Cart")){
                            addtoCart.setBackgroundColor(Color.parseColor("#30978E"));
                            addtoCart.setText("Add to Cart");

                        }
                    }
                });


                Log.i("pronam", main.get(0).getDescription());
                String name = main.get(0).getProductName();
                prod.setText(name);
                price.setText("Price: Rs." + main.get(0).getPrice().toString());
                check.setOnClickListener(new View.OnClickListener() {
                    double trt;

                    @Override
                    public void onClick(View v) {
                        String gr = incre.getText().toString();
                        if (gr.matches("")) {
                            expa.toggle();
                            checks.setText("Specify Quantity");
                            cross.setImageResource(R.drawable.ic_action_name);
                            return;
                        }

                        trt = Double.parseDouble(incre.getText().toString());
                        String sValue = (String) String.format("%.2f", trt);
                        trt = Double.parseDouble(sValue);
                        if (!expa.isExpanded()) {
                            if (invalidQuantity==false) {
                                expa.expand();
                                checks.setText("Rs. " + String.valueOf(trt * main.get(0).getPrice()));
                                cross.setImageResource(R.drawable.ic_action_name);
                            }
                            else if (invalidQuantity){
                                expa.expand();
                                checks.setText("Invalid Quantity");
                                cross.setImageResource(R.drawable.ic_action_name);
                            }

                            } else if (expa.isExpanded()) {
                            expa.collapse();
                            cross.setImageResource(R.drawable.ic_action_rigth);
                        }
                    }
                });

//                updateUI();
//                price.setText(main.get(0).getPrice().toString());
//                descript.setText(main.get(0).getDescription());
                String url = "http://13.127.236.125/" + (String) main.get(0).getProductImage();
                Glide.with(getApplicationContext())
                        .load(url)
                        //.apply(new RequestOptions().override(140, 140))
                        .into(pic);

                buynow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gintent = new Intent(getApplicationContext(), Buynow_order.class);
                        gintent.putExtra("skId",main.get(0).getSku());
                        getApplicationContext().startActivity(gintent);                    }
                });
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {

            }
        });

        // @@@@@@   On buynow button clicked   @@@@@@





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}
