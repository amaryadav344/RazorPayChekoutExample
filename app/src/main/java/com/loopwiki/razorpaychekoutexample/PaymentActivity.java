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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    Stack<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Checkout.preload(getApplicationContext());
        fragments = new Stack<>();
        fragmentManager = getSupportFragmentManager();
        productsFragment = new ProductsFragment();
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
            fragmentManager.beginTransaction().replace(R.id.main_content, cartFragment).commit();
            fragments.add(productsFragment);
        });

    }


    @Override
    public void ProductAddedToCart(Product product) {
        cartCount++;
        textViewCartCount.setVisibility(View.VISIBLE);
        textViewCartCount.setText(String.valueOf(cartCount));
        Toast.makeText(this, "Product Added to Cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ProductRemovedFromCart(Product product) {
        cartCount--;
        textViewCartCount.setText(String.valueOf(cartCount));
        if (cartCount == 0) {
            textViewCartCount.setVisibility(View.GONE);
        }
        Toast.makeText(this, "Product Removed from Cart", Toast.LENGTH_SHORT).show();
    }


    private List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String[] ImageUrl = {"http://www.loopwiki.com/wp-content/uploads/2020/10/2e59b5b1-0397-4035-bdfd-5c4de10baaa41565334934936-Biba-Women-Red-Off-White-Self-Design-Kurta-with-Palazzos-D-1.jpg",
                "http://www.loopwiki.com/wp-content/uploads/2020/10/74076745-e286-46ff-9cd4-a7f4bab6a8941565943573250-Biba-Women-Navy-Blue-Embroidered-Straight-Kurta-658156594357-1.jpg",
                "http://www.loopwiki.com/wp-content/uploads/2020/10/11512457154814-HIGHLANDER-Men-Navy-White-Slim-Fit-Checked-Casual-Shirt-5541512457154505-1.jpg",
                "http://www.loopwiki.com/wp-content/uploads/2020/10/a9d4a7aa-af7e-4a37-a9d4-a94dc8c057e81566992341974-WROGN-Navy-Blue-Slim-Fit-Checked-Casual-Shirt-52415669923403-1.jpg",
                "http://www.loopwiki.com/wp-content/uploads/2020/10/0bad7752-d637-4be3-9ebc-6a601f562e191559812909206-1.jpg",
                "http://www.loopwiki.com/wp-content/uploads/2020/10/2bb95c43-4f0c-4792-bb52-d4ffbfdc6dc31567768077618-Biba-Women-Dresses-3161567768075818-1.jpg"

        };
        String[] Title = {"HRX by Hrithik", "Crew STREET", "Royal Enfield", "Kook N Keech", "ADIDAS", "UNDER ARMOUR"};
        int[] Price = {5000, 2000, 1500, 3000, 1256, 700};
        boolean[] IsNew = {true, false, false, true, true, false};
        for (int i = 0; i < ImageUrl.length; i++) {
            Product product = new Product();
            product.setName(Title[i]);
            product.setImageURL(ImageUrl[i]);
            product.setNew(IsNew[i]);
            product.setPrice(Price[i]);
            products.add(product);
        }
        return products;

    }

    @Override
    public void onBackPressed() {
        if (fragments.isEmpty()) {
            super.onBackPressed();
        } else {
            fragmentManager.beginTransaction().replace(R.id.main_content, fragments.pop()).commit();
        }

    }

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
            // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Amount * 100);

            JSONObject preFill = new JSONObject();
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
                textViewCartCount.setText("");
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

    @Override
    public void RemoveProduct(Product product) {
        int index = this.products.indexOf(product);
        Product ProductToRemove = this.products.get(index);
        ProductToRemove.setAddedToCart(false);
        ProductRemovedFromCart(product);
    }

    @Override
    public void ProceedToPay(int TotalPrice) {
        RazorPayCheckout(TotalPrice);
    }

    public void clearCart() {
        for (Product product : products) {
            if (product.isAddedToCart()) {
                product.setAddedToCart(false);
            }
        }
    }
}
