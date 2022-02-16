package com.example.party;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.party.dao.UserDao;
import com.example.party.entity.User;

/**
 * function：连接注册页面
 */
public class register extends AppCompatActivity {
    private static final String TAG = "mysql-party-register";
    EditText userAccount = null;
    EditText userPassword = null;
    EditText userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userAccount = findViewById(R.id.userAccount);
        userPassword = findViewById(R.id.userPassword);
        userName = findViewById(R.id.userName);
    }


    public void register(View view){

        String userAccount1 = userAccount.getText().toString();
        String userPassword1 = userPassword.getText().toString();
        String userName1 = userName.getText().toString();


        User user = new User();

        user.setUserAccount(userAccount1);
        user.setUserPassword(userPassword1);
        user.setUserName(userName1);
        user.setUserType(1);
        user.setUserState(0);
        user.setUserDel(0);

        new Thread(){
            @Override
            public void run() {

                int msg = 0;

                UserDao userDao = new UserDao();

                User uu = userDao.findUser(user.getUserAccount());
                if(uu != null){
                    msg = 1;
                }
                else{
                    boolean flag = userDao.register(user);
                    if(flag){
                        msg = 2;
                    }
                }
                hand.sendEmptyMessage(msg);

            }
        }.start();


    }
    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_LONG).show();
            } else if(msg.what == 1) {
                Toast.makeText(getApplicationContext(),"该账号已经存在，请换一个账号",Toast.LENGTH_LONG).show();
            } else if(msg.what == 2) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                //将想要传递的数据用putExtra封装在intent中
                intent.putExtra("a","注册");
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        }
    };
}