package com.example.myappjava.auth;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.result.step.AuthSignInStep;
import com.amplifyframework.core.Amplify;
import com.example.myappjava.R;
import com.example.myappjava.config.base.BaseFragment;
import com.example.myappjava.databinding.AuthBinding;

public class AuthFragment extends BaseFragment {
    private AuthBinding binding;

    public void signInWithCustomUI() {
        findNavController(requireView()).navigate(R.id.to_sign_in);
    }

    public void signInWithWebUI() {
        Amplify.Auth.signInWithWebUI(
                requireActivity(),
                result -> {
                    Log.i(TAG, result.toString());

                    if (result.isSignInComplete() && result.getNextStep().getSignInStep() == AuthSignInStep.DONE) {
                        binding.setIsLoggedIn(true);
                        binding.setUsername(Amplify.Auth.getCurrentUser().getUsername());
                    }
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    public void signOut() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    binding.setIsLoggedIn(false);
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    private void updateSignInState() {
        Amplify.Auth.fetchAuthSession(result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;

                    switch (cognitoAuthSession.getIdentityId().getType()) {
                        case SUCCESS:
                            Log.i(TAG, "IdentityId: " + cognitoAuthSession.getIdentityId().getValue());
                            binding.setUsername(Amplify.Auth.getCurrentUser().getUsername());
                            binding.setIsLoggedIn(true);
                            break;
                        case FAILURE:
                            binding.setIsLoggedIn(false);

                            Exception error = cognitoAuthSession.getIdentityId().getError();
                            Log.i(TAG, "IdentityId not present because: " + error);
                    }
                },
                error -> Log.e(TAG, error.toString()));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AuthBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateSignInState();
    }
}
