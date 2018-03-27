package com.i61.parent.ui;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i61.parent.R;
import com.i61.parent.model.entities.User;
import com.i61.parent.presenter.LoginPresenter;
import com.i61.parent.presenter.impl.LoginPresenterImpl;
import com.i61.parent.util.CheckUtil;
import com.i61.parent.util.PxUtil;
import com.i61.parent.util.ToastUtil;
import com.i61.parent.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener{

    private ProgressBar progressBar;
    private EditText userName;
    private EditText password;
    private LoginPresenter presenter;
    private ImageButton backIcon;
    private Button backWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progress);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        backIcon = findViewById(R.id.action_bar_backIcon);
        backWord = findViewById(R.id.action_bar_backword);
        backIcon.setVisibility(View.GONE);
        backWord.setVisibility(View.GONE);
        backIcon.setVisibility(View.GONE);
        findViewById(R.id.button).setOnClickListener(this);
        presenter = new LoginPresenterImpl(this);
    }

    @Override
    public void onClick(View view){
        String name = userName.getText().toString();
        String pass = password.getText().toString();
        if(name.equals("") || pass.equals("")){
            ToastUtil.showErrorToast(this, "手机号码和密码都不能为空");
        }else if(!CheckUtil.isPasswordLegal(pass) || !CheckUtil.isPhoneLegal(name)){
            ToastUtil.showErrorToast(this, "手机号码或密码格式不正确");
        }else if(CheckUtil.isNetworkConnect(this) == false){
            ToastUtil.showErrorToast(this, "网络异常，请检查网络");
        }
        User user = new User();
        user.setPassword(pass);
        user.setUserName(name);
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
        userName.setError("请填写账号");
    }

    @Override
    public void setPasswordError() {
        password.setError("请填写密码");
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
