package com.example.myappjava.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {
    public final MutableLiveData<String> username = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> phone = new MutableLiveData<>();
    public final MutableLiveData<String> confirmationCode = new MutableLiveData<>();
    private final MutableLiveData<Auth> _auth = new MutableLiveData<>();
    final LiveData<Auth> auth = _auth;

    public Auth getAuth() {
        Auth auth = new Auth();
        auth.setUsername(username.getValue());
        auth.setEmail(email.getValue());
        auth.setPassword(password.getValue());
        auth.setPhone(phone.getValue());
        auth.setConfirmationCode(confirmationCode.getValue());
        return auth;
    }

    public void setAuth(Auth auth) {
        _auth.postValue(auth);
    }
}
