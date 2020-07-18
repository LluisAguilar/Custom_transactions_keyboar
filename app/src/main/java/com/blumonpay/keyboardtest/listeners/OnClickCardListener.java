package com.blumonpay.keyboardtest.listeners;

import com.blumonpay.keyboardtest.model.CardInformation;

import java.util.List;

public interface OnClickCardListener {

    void onCardClick(List<CardInformation> list, int position);
}
