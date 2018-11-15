package com.min.sample.contract;

import com.min.core.base.AbstractBasePresenter;
import com.min.core.base.BaseView;

/**
 * Created by minyangcheng on 2017/9/20.
 */

public class LoginContract {

    public interface View extends BaseView {
        void loginSuccess();

        void loginFail();
    }

    public static abstract class Presenter extends AbstractBasePresenter<LoginContract.View> {

        public abstract void login(String userName, String userPass);

    }

}
