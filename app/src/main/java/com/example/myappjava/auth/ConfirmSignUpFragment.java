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

import com.amplifyframework.auth.options.AuthResendSignUpCodeOptions;
import com.amplifyframework.auth.result.step.AuthSignUpStep;
import com.amplifyframework.core.Amplify;
import com.example.myappjava.R;
import com.example.myappjava.config.base.BaseFragment;

public class ConfirmSignUpFragment extends BaseFragment {
    private AuthViewModel vm;
    private ConfirmSignUpBinding binding;

    private void sendConfirmationCode(Auth auth) {
        AuthResendSignUpCodeOptions options = AuthResendSignUpCodeOptions.defaults();
        Amplify.Auth.resendSignUpCode(auth.getUsername(), options,
                result -> {
                    Log.i(TAG, result.toString());

                    if (result.getNextStep().getSignUpStep() == AuthSignUpStep.CONFIRM_SIGN_UP_STEP) {
                        Log.d(TAG, "Confirmation code sent!");
                    }
                },
                error -> Log.i(TAG, error.toString())
        );
    }

    public void onConfirmSignUp() {
        Auth auth = vm.getAuth();

        Amplify.Auth.confirmSignUp(auth.getUsername(), auth.getConfirmationCode(),
                result -> {
                    String msg = result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete";
                    Log.i(TAG, msg);

                    if (isRemoving() || isDetached()) return;

                    if (result.isSignUpComplete()) {
                        requireActivity().runOnUiThread(() -> {
                            findNavController(this).navigate(R.id.to_sign_in);
                        });
                    }
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelStoreOwner store = findNavController(this).getViewModelStoreOwner(R.id.auth_graph);
        vm = new ViewModelProvider(store).get(AuthViewModel.class);

        vm.auth.observe(this, this::sendConfirmationCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ConfirmSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setFragment(this);
        binding.setVm(vm);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }
}
