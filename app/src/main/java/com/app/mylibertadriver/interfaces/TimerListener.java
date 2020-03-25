package com.app.mylibertadriver.interfaces;

public interface TimerListener {

    void onTick(long millisUntilFinished);
    void onFinish();
    void onExpire();
}
