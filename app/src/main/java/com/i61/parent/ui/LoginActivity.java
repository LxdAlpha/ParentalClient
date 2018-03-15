package com.i61.parent.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.i61.parent.R;
import com.i61.parent.model.entities.User;
import com.i61.parent.presenter.LoginPresenter;
import com.i61.parent.presenter.impl.LoginPresenterImpl;
import com.i61.parent.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

	private ProgressBar progressBar;
	private EditText userName;
	private EditText password;
	private LoginPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		progressBar = findViewById(R.id.progress);
		userName = findViewById(R.id.userName);
		password = findViewById(R.id.password);
		findViewById(R.id.button).setOnClickListener(this);
		presenter = new LoginPresenterImpl(this);
	}

	@Override
	public void onClick(View view) {
		User user = new User();
		user.setPassword(password.getText().toString());
		user.setUserName(userName.getText().toString());
		presenter.validateCredentials(user);
	}

	@Override
	public void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void setUserNameError() {
		userName.setError("用户名错误");
	}

	@Override
	public void setPasswordError() {
		password.setError("密码错误");
	}

	@Override
	public void showSuccess() {
		progressBar.setVisibility(View.GONE);
		Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showError(String errorMsg) {
		progressBar.setVisibility(View.GONE);
		Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		presenter.onDestroy();
		super.onDestroy();
	}
}
