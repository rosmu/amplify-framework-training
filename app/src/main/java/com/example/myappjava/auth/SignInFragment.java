package com.example.myappjava.auth;


import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.core.Amplify;
import com.example.myappjava.R;
import com.example.myappjava.config.base.BaseFragment;

public class SignInFragment extends BaseFragment {
    private AuthViewModel vm;
    private SignInBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setFragment(this);
        binding.setVm(vm);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    public void onSignUp() {
        findNavController(this).navigate(R.id.to_sign_up);
    }

    public void onSignIn() {
        Auth auth = vm.getAuth();
        Amplify.Auth.signIn(auth.getUsername(), auth.getPassword(),
                result -> {
                    Log.i(TAG, result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

                    requireActivity().runOnUiThread(() -> {
                        if (result.isSignInComplete()) {
                            findNavController(this).navigate(R.id.to_auth);
                        }
                    });
                },
                error -> {
                    Log.e(TAG, error.toString());

                    // user not confirmed; requesting for confirmation code
                    if (error instanceof AuthException.UserNotConfirmedException) {
                        findNavController(this).navigate(R.id.to_confirm_sign_up);
                    }
                }
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewModelStoreOwner store = findNavController(this).getViewModelStoreOwner(R.id.auth_graph);
        vm = new ViewModelProvider(store).get(AuthViewModel.class);
    }
}