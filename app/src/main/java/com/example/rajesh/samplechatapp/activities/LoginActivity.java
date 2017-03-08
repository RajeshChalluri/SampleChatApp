package com.example.rajesh.samplechatapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajesh.sample.core.ui.activity.CoreBaseActivity;
import com.example.rajesh.sample.core.ui.dialog.ProgressDialogFragment;
import com.example.rajesh.sample.core.utils.ErrorUtils;
import com.example.rajesh.samplechatapp.R;
import com.example.rajesh.samplechatapp.adapters.UsersAdapter;
import com.example.rajesh.samplechatapp.utils.Constants;
import com.example.rajesh.samplechatapp.utils.SharedPreferencesUtil;
import com.example.rajesh.samplechatapp.utils.chat.ChatHelper;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends CoreBaseActivity implements View.OnClickListener {

    private ListView userListView;
    private Dialog dialogLogin = null;
    private Dialog dialogSignUp = null;
    private TextView signUpTV;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUpTV = (TextView) findViewById(R.id.signUpTV);
        userListView = (ListView) findViewById(R.id.list_login_users);

        TextView listHeader = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.include_list_hint_header, userListView, false);
        listHeader.setText(R.string.login_select_user_for_login);

        userListView.addHeaderView(listHeader, null, false);
        userListView.setOnItemClickListener(new OnUserLoginItemClickListener());
        signUpTV.setOnClickListener(this);

        buildUsersList();
    }

    @Override
    public void onClick(View v) {
        signUpDialog();
    }

    public Dialog signUpDialog() {
        try {
            dialogSignUp = new Dialog(this);
            dialogSignUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogSignUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSignUp.setCancelable(false);
            dialogSignUp.setContentView(R.layout.signup_dialog_layout);
            final EditText userName = (EditText) dialogSignUp.findViewById(R.id.userName);
            final EditText userEmail = (EditText) dialogSignUp.findViewById(R.id.userEmail);
            final EditText userLogin = (EditText) dialogSignUp.findViewById(R.id.userLogin);
            final EditText userPassword = (EditText) dialogSignUp.findViewById(R.id.userPassword);
            final EditText userConfirmPassword = (EditText) dialogSignUp.findViewById(R.id.userConfirmPassword);
            final EditText userMobile = (EditText) dialogSignUp.findViewById(R.id.userMobile);
            Button submitButton = (Button) dialogSignUp.findViewById(R.id.signUpSubmit);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = userName.getText().toString().trim();
                    String email = userEmail.getText().toString().trim();
                    String login = userLogin.getText().toString().trim();
                    String password = userPassword.getText().toString().trim();
                    String confirmPassword = userConfirmPassword.getText().toString();
                    String mobile = userMobile.getText().toString().trim();
                    if (!email.equals("") && !login.equals("") && !password.equals("")) {
                        if (password.equals(confirmPassword)) {
                            final QBUser user = new QBUser(login, password);
                            user.setEmail(email);
                            user.setFullName(name);
                            user.setPhone(mobile);
                            ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_signup);
                            QBUsers.signUp(user, new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(QBUser user, Bundle args) {
                                    buildUsersList();
                                    dialogSignUp.cancel();
                                    ProgressDialogFragment.hide(getSupportFragmentManager());
                                    Toast.makeText(getApplicationContext(), "SignUp successfull", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(QBResponseException errors) {
                                    dialogSignUp.cancel();
                                    Toast.makeText(getApplicationContext(), "something went wrong, please try later...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "password & confirm password must be same", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (email.equals(""))
                            userEmail.setText("");
                        if (login.equals(""))
                            userLogin.setText("");
                        if (password.equals(""))
                            userPassword.setText("");
                        if (confirmPassword.equals(""))
                            userConfirmPassword.setText("");
                        Toast.makeText(getApplicationContext(), "* fields are mandatory", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ImageView dialog_cancel = (ImageView) dialogSignUp.findViewById(R.id.dialog_cancel);
            final Dialog finalDialog = dialogSignUp;
            dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalDialog.cancel();
                }
            });
            dialogSignUp.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialogSignUp;
    }

    private void buildUsersList() {
        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
        pagedRequestBuilder.setPage(1);
        pagedRequestBuilder.setPerPage(50);

        QBUsers.getUsers(pagedRequestBuilder, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                UsersAdapter adapter = new UsersAdapter(LoginActivity.this, users);
                userListView.setAdapter(adapter);
            }

            @Override
            public void onError(QBResponseException errors) {
                ErrorUtils.showSnackbar(userListView, R.string.login_cant_obtain_users, errors,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                buildUsersList();
                            }
                        });
            }
        });
    }

    private void login(final QBUser user) {
        ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                dialogLogin.cancel();
                SharedPreferencesUtil.saveQbUser(user);
                DialogsActivity.start(LoginActivity.this);
                finish();
                ProgressDialogFragment.hide(getSupportFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                dialogLogin.cancel();
                ProgressDialogFragment.hide(getSupportFragmentManager());
//                ErrorUtils.showSnackbar(userListView, R.string.login_chat_login_error, e,
//                        R.string.dlg_retry, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                login(user);
//                            }
//                        });
                Toast.makeText(getApplicationContext(), "login failed, please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class OnUserLoginItemClickListener implements AdapterView.OnItemClickListener {
        public static final int LIST_HEADER_POSITION = 0;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == LIST_HEADER_POSITION) {
                return;
            }
            final QBUser user = (QBUser) parent.getItemAtPosition(position);
            dialogLogin = loginDialog(user);
        }

    }

    public Dialog loginDialog(final QBUser user) {
        try {
            dialogLogin = new Dialog(this);
            dialogLogin.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogLogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogLogin.setCancelable(false);
            dialogLogin.setContentView(R.layout.login_dialog_layout);
            TextView userId = (TextView) dialogLogin.findViewById(R.id.userLoginId);
            userId.setText(user.getEmail());
            final EditText userPassword = (EditText) dialogLogin.findViewById(R.id.userLoginPassword);
            ImageView dialog_cancel = (ImageView) dialogLogin.findViewById(R.id.dialog_cancel);
            final Button loginSubmit = (Button) dialogLogin.findViewById(R.id.loginSubmit);

            loginSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password = userPassword.getText().toString().trim();
                    if (!password.equals("")) {
                        user.setPassword(password);
                        login(user);
                    }
                }
            });
            final Dialog finalDialog = dialogLogin;
            dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalDialog.cancel();
                }
            });
            dialogLogin.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialogLogin;
    }
}
