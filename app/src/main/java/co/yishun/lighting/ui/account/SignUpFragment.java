package co.yishun.lighting.ui.account;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.model.Token;
import co.yishun.lighting.ui.LoginActivity;
import co.yishun.lighting.ui.common.BaseFragment;
import co.yishun.lighting.ui.view.CountDownResentView;
import retrofit2.Response;

/**
 * Created by carlos on 3/30/16.
 */
@EFragment(R.layout.fragment_sign_up)
public class SignUpFragment extends BaseFragment {
    public static final String TAG = "SignUpFragment";
    @FragmentArg
    String phone;
    @ViewById
    TextInputEditText phoneEditText;
    @ViewById
    TextInputEditText verifyCodeEditText;
    @ViewById
    Toolbar toolbar;
    @ViewById
    CountDownResentView resentView;
    @ViewById
    View nextBtn;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void autoSendSms() {
        if (!TextUtils.isEmpty(phone)) {
            phoneEditText.setText(phone);
            phoneEditText.setSelection(phoneEditText.length());
        }
        trySendSms();
    }

    @AfterViews
    void setViews() {
        toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(getActivity()));

        resentView.setOnClickListenerWhenEnd(view -> {
            phone = phoneEditText.getText().toString();
            trySendSms();
        });

    }

    @AfterTextChange(R.id.verifyCodeEditText)
    void onVerifyCodeEditTextChange(Editable text) {
        nextBtn.setEnabled(!TextUtils.isEmpty(text));
    }

    @Click
    @Background(id = CANCEL_WHEN_DESTROY)
    void nextBtnClicked() {
        String code = verifyCodeEditText.getText().toString().trim();
        final String phoneVerified = phone;

        safelyDoWithActivity((activity) -> {
            AccountActivity accountActivity = (AccountActivity) activity;

            Response<Token> response = APIFactory.getAccountAPI().validateSMS(phoneVerified, code, "register").execute();
            if (response.isSuccessful()) {
                accountActivity.showSnackMsg(R.string.fragment_sign_up_msg_verify_ok);
                Token token = response.body();
                accountActivity.goToPassword(phoneVerified, token);
            } else {
                accountActivity.showSnackMsg(R.string.fragment_sign_up_msg_verify_fail);
            }
        });
    }

    private void trySendSms() {
        if (LoginActivity.isPhoneValid(phone)) {
            resentView.countDown();
            sendSms(phone);
        }
    }

    @Background(id = CANCEL_WHEN_DESTROY)
    void sendSms(final String phone) {
        boolean fail = safelyDoWithActivity((activity) -> {
            Response response = APIFactory.getAccountAPI().register(phone).execute();
            if (response.isSuccessful()) {
                showSnackMsg(R.string.fragment_sign_up_msg_send_ok);
            } else if (response.code() == 500) {
                showSnackMsg(R.string.fragment_sign_up_error_registered);
                onFail();
            } else {
                showSnackMsg(R.string.fragment_sign_up_msg_send_fail);
                onFail();
            }
        });

        if (fail) {
            onFail();
        }
    }

    @UiThread
    void onFail() {
        resentView.reset();
    }


}
