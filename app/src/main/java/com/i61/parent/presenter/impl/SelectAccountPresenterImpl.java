package com.i61.parent.presenter.impl;

import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.model.SelectAccountModel;
import com.i61.parent.model.impl.SelectAccountModelImpl;
import com.i61.parent.presenter.OnAccountStoreListener;
import com.i61.parent.presenter.SelectAccountPresenter;
import com.i61.parent.view.SelectAccountView;

/**
 * Created by linxiaodong on 2018/3/16.
 */

public class SelectAccountPresenterImpl implements SelectAccountPresenter, OnAccountStoreListener{
    private SelectAccountView selectAccountView;
    private SelectAccountModel selectAccountModel;
    int saveCount;

    public SelectAccountPresenterImpl(SelectAccountView selectAccountView) {
        this.selectAccountView = selectAccountView;
        selectAccountModel = new SelectAccountModelImpl();
        saveCount = 0;
    }

    @Override
    public void removeActiveAccount() {
        selectAccountModel.removeAccount(this);
    }

    @Override
    public void getRelatedInfo() {
        //由于获取userCenter信息必须在workCenter之前，所以获取workCenter信息的函数在获取userCenter的函数的调用
        selectAccountModel.getUserCenter(this);
        //selectAccountModel.getWorkCenter(this);
    }


    @Override
    public void onSave() {
        saveCount++;
        if(saveCount == 2){
            selectAccountView.intent();
        }
    }

    @Override
    public void onRemove() {
        selectAccountView.back();
    }

    @Override
    public void onSaveFailed() {
        selectAccountView.showSaveFailed();
    }


}
