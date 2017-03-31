package com.fkjslee.schoolv3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fkjslee.schoolv3.R;
import com.fkjslee.schoolv3.function.TimeCount;


/**
 * 1. 点击登录 成功则进入主界面, 失败3次有罚时
 * 2. 点击退出, 退出应用
 * 3. 点击忘记密码, 跳到忘记密码界面
 * 4. 点击绑定手机, 跳到绑定手机界面
 * */

public class LogActivity extends AppCompatActivity implements View.OnClickListener{

    private int testTime = 0;
    private long mExitTime = -2005;

    private Button btnLog;
    private Button btnQuit;
    private Button btnFgtPwd;
    private Button btnBind;
    private Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log);
        testTime = 0;

        GouldMapLocation gouldMapLocation = new GouldMapLocation(getApplicationContext());
        gouldMapLocation.startLocation();
        initView();
    }

    /**
     * 点击事件
     * 1. 点击登录
     * 2. 点击退出
     * 3. 点击忘记密码
     * 4. 点击绑定手机
     * */
    public void onClick(View v) {
        Class<? extends android.app.Activity> activityClass = null;
        if(R.id.btn_fgt_pwd == v.getId()) activityClass = FgtPwdActivity.class;
        if(R.id.btn_bind == v.getId()) activityClass = BindPhoneActivity.class;
        if(R.id.btn_set == v.getId()) activityClass = SettingActivity.class;
        if(null != activityClass) {
            startActivity(new Intent(this.getApplicationContext(), activityClass));
            return;
        }
        if(R.id.btn_log == v.getId()) clickBtnLog();
        if(R.id.btn_rtn == v.getId()) clickBtnQuit();
    }

    /**
     * 功能: 点击"登录"后的具体实现逻辑
     * 返回值: 无
     * 参数: 无
     * 正确则进入主界面, 错误给出提醒, 3次以上罚时, 罚时等于2^(x-3)秒
     * */
    private void clickBtnLog() {
        EditText stu_ID = (EditText) findViewById(R.id.et_stu_ID);
        EditText pwd = (EditText) findViewById(R.id.et_pwd);
        if (checkPWD(stu_ID.getText().toString(), pwd.getText().toString()))
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        else {
            Toast.makeText(getApplicationContext(), "密码错误",
                    Toast.LENGTH_SHORT).show();
            testTime++;
            if (testTime >= 3) {
                btn_log_delay((int) Math.pow(2, (testTime - 3)));
            }
        }
    }

    /**
     * 功能: 点击"退出"后的具体实现,
     * 返回值: 无
     * 参数: 无
     * 调用android的api
     * */
    private void clickBtnQuit() {
        finish();
    }

    /**
     * 功能: 按钮延时
     * 返回值: 无
     * 参数: 需要延迟的时间
     * 错误输入密码后罚时登录, 使用继承与CountDownTimer的TimeCount类实现及时功能,
     * 重写了每次显示过程和及时完毕的过程
     * */
    private void btn_log_delay(int waitTime) {
        TimeCount timer = new TimeCount(waitTime * 1000, 1000, (Button) findViewById(R.id.btn_log),
                "验证失败, 请等待", "秒后重新验证");
        timer.start();
    }

    /**
     * to do
     * 功能: 检查密码是否正确
     * 返回值: boolean, 密码正确
     * 参数, String, 学生的学号, String, 学生的密码
     * */
    private boolean checkPWD(String stu_ID, String pwd) {
        return stu_ID.length() == 0 && pwd.length() == 0;
    }

    /**
     * 两次点击返回退出程序
     * */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                clickBtnQuit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        btnLog = (Button)findViewById(R.id.btn_log);
        btnQuit = (Button)findViewById(R.id.btn_quit);
        btnFgtPwd = (Button)findViewById(R.id.btn_fgt_pwd);
        btnBind = (Button)findViewById(R.id.btn_bind);
        btnSet = (Button)findViewById(R.id.btn_set);

        btnLog.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        btnSet.setOnClickListener(this);
        btnFgtPwd.setOnClickListener(this);
        btnBind.setOnClickListener(this);
    }

}