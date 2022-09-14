package com.example.myappjava.config.binding;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

public class ViewBindingAdapter {
    @BindingAdapter("android:visibleIfIt")
    public static void visibleIfIt(View view, @Nullable Boolean isVisible) {
        view.setVisibility(isVisible != null && isVisible ? View.VISIBLE : View.GONE);
    }
}
