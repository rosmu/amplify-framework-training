package com.example.myappjava.auth;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.auth.result.step.AuthSignUpStep;
import com.amplifyframework.core.Amplify;
import com.example.myappjava.R;
import com.example.myappjava.config.base.BaseFragment;

import java.util.ArrayList;

public class SignUpFragment extends BaseFragment {
    private AuthViewModel vm;
    private SignUpBinding binding;

    public void onSignUp() {
        Auth auth = vm.getAuth();

        ArrayList<AuthUserAttribute> attributes = new ArrayList<>();
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), auth.getEmail()));
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), auth.getPhone()));

        Amplify.Auth.signUp(auth.getUsername(), auth.getPassword(),
                AuthSignUpOptions.builder().userAttributes(attributes).build(),
                result -> {
                    Log.i(TAG, result.toString());

                    if (isRemoving()) return;

                    requireActivity().runOnUiThread(() -> {
                        AuthSignUpStep step = result.getNextStep().getSignUpStep();
                        if (result.isSignUpComplete()) {
                            if (step == AuthSignUpStep.CONFIRM_SIGN_UP_STEP) {
                                findNavController(this).navigate(R.id.to_confirm_sign_up);
                            } else if (step == AuthSignUpStep.DONE) {
                                findNavController(this).navigate(R.id.nav_auth);
                            } else {
                                throw new RuntimeException(String.format("Unexpected signup step %s; Not implemented", step));
                            }
                        } else {
                            Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                },
                error -> {
                    Log.e(TAG, error.toString());

                    if (isRemoving()) return;

                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show());
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setFragment(this);
        binding.setVm(vm);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelStoreOwner store = findNavController(this).getViewModelStoreOwner(R.id.auth_graph);
        vm = new ViewModelProvider(store).get(AuthViewModel.class);
    }
}
