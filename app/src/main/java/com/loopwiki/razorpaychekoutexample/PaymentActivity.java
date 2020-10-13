package com.loopwiki.razorpaychekoutexample;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener, ProductsFragment.ProductInteractionListener, CartFragment.CartInteractionListener {
    public static final String TAG = PaymentActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    ProductsFragment productsFragment;
    int cartCount = 0;
    @BindView(R.id.textViewCartCount)
    TextView textViewCartCount;
    @BindView(R.id.imageViewCart)
    ImageView imageViewCart;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Checkout.preload(getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        productsFragment = ProductsFragment.newInstance();
        products = getProducts();
        productsFragment.products = products;
        fragmentManager.beginTransaction().replace(R.id.main_content, productsFragment).commit();
        imageViewCart.setOnClickListener(v -> {
            CartFragment cartFragment = new CartFragment();
            List<Product> productList = new ArrayList<>();
            for (Product product : products) {
                if (product.isAddedToCart()) {
                    productList.add(product);
                }
            }
            cartFragment.products = productList;
            fragmentManager.beginTransaction().replace(R.id.main_content, cartFragment).addToBackStack(ProductsFragment.TAG).commit();
        });

    }

    // Callback from Products fragment when product is added
    @Override
    public void ProductAddedToCart(Product product) {
        cartCount++;
        textViewCartCount.setVisibility(View.VISIBLE);
        textViewCartCount.setText(String.valueOf(cartCount));
        Toast.makeText(this, getString(R.string.product_added), Toast.LENGTH_SHORT).show();
    }

    // Callback from Products fragment when product is removed
    @Override
    public void ProductRemovedFromCart(Product product) {
        cartCount--;
        textViewCartCount.setText(String.valueOf(cartCount));
        if (cartCount == 0) {
            textViewCartCount.setVisibility(View.GONE);
        }
        Toast.makeText(this, getString(R.string.product_removed), Toast.LENGTH_SHORT).show();
    }

    // method to create dummy product
    private List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        int[] ImageUrl = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six};
        String[] Title = {"HRX by Hrithik", "Crew STREET", "Royal Enfield", "Kook N Keech", "ADIDAS", "UNDER ARMOUR"};
        int[] Price = {5000, 2000, 1500, 3000, 1256, 700};
        boolean[] IsNew = {true, false, false, true, true, false};
        for (int i = 0; i < ImageUrl.length; i++) {
            Product product = new Product();
            product.setName(Title[i]);
            product.setImageResourceId(ImageUrl[i]);
            product.setNew(IsNew[i]);
            product.setPrice(Price[i]);
            products.add(product);
        }
        return products;

    }

    // Back button press method
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            fragmentManager.popBackStackImmediate();
        }

    }

    // this method calls our payment gateway
    public void RazorPayCheckout(int Amount) {
         /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");// You can omit the image option to fetch the image from dashboard
            // set image of you brand
            // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Amount * 100);

            JSONObject preFill = new JSONObject();
            // Preset email and phone
            // preFill.put("email", "test@razorpay.com");
            // preFill.put("contact", "123456789");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Dialog dialog = Helper.getSuccessDialog(this);
            TextView textViewGoHome = dialog.findViewById(R.id.textViewGoHome);
            textViewGoHome.setOnClickListener(v -> {
                dialog.dismiss();
                clearCart();
                cartCount = 0;
                textViewCartCount.setVisibility(View.GONE);
                fragmentManager.beginTransaction().replace(R.id.main_content, productsFragment).commit();
            });
            dialog.show();
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    // Method called when product is removed from cart
    @Override
    public void RemoveProduct(Product product) {
        int index = this.products.indexOf(product);
        Product ProductToRemove = this.products.get(index);
        ProductToRemove.setAddedToCart(false);
        ProductRemovedFromCart(product);
    }

    // Method is called when we click on Pay button
    @Override
    public void ProceedToPay(int TotalPrice) {
        RazorPayCheckout(TotalPrice);
    }

    // method to clear cart
    public void clearCart() {
        for (Product product : products) {
            if (product.isAddedToCart()) {
                product.setAddedToCart(false);
            }
        }
    }
}
