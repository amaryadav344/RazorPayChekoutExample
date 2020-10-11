package com.loopwiki.razorpaychekoutexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter {
    private List<Product> products;
    CartCallbacks cartCallbacks;

    public static final int ITEM_TYPE_PRODUCT = 1;
    public static final int ITEM_TYPE_EMPTY = 0;


    public void setCartCallbacks(CartCallbacks cartCallbacks) {
        this.cartCallbacks = cartCallbacks;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == ITEM_TYPE_PRODUCT) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            holder = new CartItemHolder(row);
        } else {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_item, parent, false);
            holder = new EmptyCartHolder(row);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CartItemHolder) {
            Product currentProduct = products.get(position);
            CartItemHolder productHolder = (CartItemHolder) holder;
            productHolder.textViewProductName.setText(currentProduct.getName());
            Picasso.get().load(currentProduct.getImageURL()).into(productHolder.imageViewProduct);
            productHolder.textViewProductPrice.setText("₹" + currentProduct.getPrice());
        }
    }

    @Override
    public int getItemCount() {
        return products.size() == 0 ? 1 : products.size();
    }

    @Override
    public int getItemViewType(int position) {
        return products.size() == 0 ? ITEM_TYPE_EMPTY : ITEM_TYPE_PRODUCT;
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewProduct)
        ImageView imageViewProduct;
        @BindView(R.id.textViewProductName)
        TextView textViewProductName;
        @BindView(R.id.textViewProductPrice)
        TextView textViewProductPrice;
        @BindView(R.id.ImageButtonDelete)
        ImageButton ImageButtonDelete;

        public CartItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ImageButtonDelete.setOnClickListener(v -> {
                Product ProductToRemove = products.get(getAdapterPosition());
                int index = products.indexOf(ProductToRemove);
                products.remove(index);
                notifyItemRemoved(index);
                cartCallbacks.RemoveProduct(ProductToRemove);

            });
        }
    }

    public class EmptyCartHolder extends RecyclerView.ViewHolder {
        public EmptyCartHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface CartCallbacks {
        public void RemoveProduct(Product product);
    }
}
