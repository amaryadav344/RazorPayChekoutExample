package com.loopwiki.razorpaychekoutexample;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter {
    private List<Product> products;
    ProductAdapterCallBack productAdapterCallBack;

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setProductAdapterCallBack(ProductAdapterCallBack productAdapterCallBack) {
        this.productAdapterCallBack = productAdapterCallBack;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        ProductHolder productHolder = new ProductHolder(row);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product currentProduct = products.get(position);
        ProductHolder productHolder = (ProductHolder) holder;
        productHolder.textViewProductName.setText(currentProduct.getName());
        productHolder.textViewProductPrice.setText("₹" + currentProduct.getPrice());
        Picasso.get().load(currentProduct.getImageURL()).into(productHolder.imageViewProduct);
        if (currentProduct.isAddedToCart()) {
            productHolder.imageButtonAddToCart.setColorFilter(ContextCompat.getColor(productHolder.itemView.getContext(), android.R.color.holo_purple), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            productHolder.imageButtonAddToCart.setColorFilter(ContextCompat.getColor(productHolder.itemView.getContext(), android.R.color.tertiary_text_light), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewProduct)
        ImageView imageViewProduct;
        @BindView(R.id.textViewProductName)
        TextView textViewProductName;
        @BindView(R.id.textViewProductPrice)
        TextView textViewProductPrice;
        @BindView(R.id.imageButtonAddToCart)
        ImageButton imageButtonAddToCart;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageButtonAddToCart.setOnClickListener(v -> {
                Product product = products.get(getAdapterPosition());
                if (product.isAddedToCart()) {
                    productAdapterCallBack.ProductRemovedFromCart(product);
                    imageButtonAddToCart.setColorFilter(ContextCompat.getColor(v.getContext(), android.R.color.tertiary_text_light), PorterDuff.Mode.SRC_IN);
                    product.setAddedToCart(false);
                } else {
                    productAdapterCallBack.ProductAddedToCart(product);
                    imageButtonAddToCart.setColorFilter(ContextCompat.getColor(v.getContext(), android.R.color.holo_purple), PorterDuff.Mode.SRC_IN);
                    product.setAddedToCart(true);
                }

            });
        }
    }

    public interface ProductAdapterCallBack {
        void ProductAddedToCart(Product product);

        void ProductRemovedFromCart(Product product);
    }
}
