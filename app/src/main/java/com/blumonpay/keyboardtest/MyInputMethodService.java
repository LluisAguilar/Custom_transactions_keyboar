package com.blumonpay.keyboardtest;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.blumonpay.keyboardtest.adapters.CardsAdapter;
import com.blumonpay.keyboardtest.listeners.OnClickCardListener;
import com.blumonpay.keyboardtest.model.CardInformation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Copyright 2019 (C) Blumon Pay S.A. de C.V.
 * Created on: 09/12/19
 * Author: Miguel Ángel Sánchez
 */

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener, OnClickCardListener, View.OnClickListener {
    private EditText amountEdt;
    private String current = null;
    private SharedPreferencesDB sharedPreferencesDB;
    private String selectedCardNum;
    private String selectedCardBank;

    private TextView confAmount;
    private TextView selectedCard;
    private Button sendBtn;
    private ImageView imgCheckPay;

    private TextView amountToCharge;
    private Button chargeBtn;
    private TextView linkTv;
    private ImageView imgCheckCharge;

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        sharedPreferencesDB.deleteView();
        setInputView(onCreateInputView());
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        sharedPreferencesDB.deleteView();
    }

    @Override
    public View onCreateInputView() {

        sharedPreferencesDB = new SharedPreferencesDB(this);

        if (sharedPreferencesDB.getViewValue()==0) {

            View linearView = getLayoutInflater().inflate(R.layout.keyboard_view, null);
            amountEdt = linearView.findViewById(R.id.numberTxt);
            setTextWatcher(amountEdt);
            KeyboardView keyboardView = linearView.findViewById(R.id.my_keyboard_view);
            Keyboard keyboard = new Keyboard(this, R.xml.number_pad);
            keyboardView.setKeyboard(keyboard);
            keyboardView.setPreviewEnabled(false);
            keyboardView.setOnKeyboardActionListener(this);
            return linearView;

        }else if (sharedPreferencesDB.getViewValue()==1){
            View linearView = getLayoutInflater().inflate(R.layout.choose_card_view, null);
            RecyclerView cardRecycler  = linearView.findViewById(R.id.cards_recycler);
            TextView amountTv = linearView.findViewById(R.id.amountToPay);
            ImageView back_arrow = linearView.findViewById(R.id.back);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            cardRecycler.setLayoutManager(linearLayoutManager);

            List<CardInformation> list = new ArrayList<>();
            list.add(new CardInformation("8888888888887578","Credit","Noe A Cano Martinez","06/2022","Banamex","1234","blue"));
            list.add(new CardInformation("9999999999994003","Credit","Noe A Cano Martinez","08/2024","American Express","1234","sky_blue"));

            amountTv.setText(sharedPreferencesDB.getAmountValue());
            back_arrow.setOnClickListener(this);

            CardsAdapter cardsAdapter = new CardsAdapter(this,list,this);
            cardRecycler.setAdapter(cardsAdapter);

            return linearView;
        }else if (sharedPreferencesDB.getViewValue()==2){
            View linearView = getLayoutInflater().inflate(R.layout.pay_confirmation_view, null);
            confAmount = linearView.findViewById(R.id.confAmountToPay);
            selectedCard = linearView.findViewById(R.id.selectedCardTv);
            sendBtn = linearView.findViewById(R.id.sendBtn);
            ImageView back_arrow = linearView.findViewById(R.id.back);
            imgCheckPay = linearView.findViewById(R.id.img_check);

            confAmount.setText("Monto a enviar: "+sharedPreferencesDB.getAmountValue());
            selectedCard.setText(selectedCardBank+": "+selectedCardNum);
            back_arrow.setOnClickListener(this);
            sendBtn.setOnClickListener(this);

            return linearView;
        }else if (sharedPreferencesDB.getViewValue()==3){
            View linearView = getLayoutInflater().inflate(R.layout.charge_confirmation_view, null);
            amountToCharge = linearView.findViewById(R.id.confAmountToCharge);
            chargeBtn = linearView.findViewById(R.id.chargeBtn);
            linkTv = linearView.findViewById(R.id.confLinkTv);
            ImageView back_arrow = linearView.findViewById(R.id.back);
            imgCheckCharge = linearView.findViewById(R.id.img_check);

            amountToCharge.setText("Monto a cobrar: "+ sharedPreferencesDB.getAmountValue());
            chargeBtn.setOnClickListener(this);
            back_arrow.setOnClickListener(this);

            return linearView;
        }

        return null;
    }

    private void setTextWatcher(final TextView amountEdt) {
        amountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int countCharsAmount;
                countCharsAmount = amountEdt.getText().toString().length();
                if (countCharsAmount > 13) {
                    amountDelete();

                }else {

                    if (!s.toString().equals(current)) {
                        amountEdt.removeTextChangedListener(this);
                        String cleanString = s.toString().replaceAll("[$,.]", "");
                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance(Locale.CANADA).format((parsed / 100));
                        current = formatted;
                        amountEdt.setText(formatted);
                        amountEdt.addTextChangedListener(this);
                    }

                    /*if (countCharsAmount < 8) {
                        amountEdt.setTextSize(25);
                    } else if (countCharsAmount == 8) {
                        amountEdt.setTextSize(20);
                    } else if (countCharsAmount < 11) {
                        amountEdt.setTextSize(18);
                    } else if (countCharsAmount == 11) {
                        amountEdt.setTextSize(15);
                    }*/
                }
            }
        });
    }

    private void amountDelete() {
        Editable editable = amountEdt.getText();
        int charCount = editable.length();
        if (charCount > 0) {
            editable.delete(charCount - 1, charCount);
        }
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {
    }

    @Override
    public void onKey(int primatyCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            if (primatyCode == Keyboard.KEYCODE_DELETE) {
                CharSequence selectedText = inputConnection.getSelectedText(0);

                if (TextUtils.isEmpty(selectedText)) {
                    //inputConnection.deleteSurroundingText(1, 0);
                    if (amountEdt.getText().toString().length()>0) {
                        amountEdt.setText(amountEdt.getText().toString().substring(0, amountEdt.getText().toString().length() - 1));
                    }else {
                        amountEdt.setText("$0.00");
                    }
                } else {
                    inputConnection.commitText("", 1);
                }
            } else {
                char code = (char) primatyCode;
                //inputConnection.commitText(String.valueOf(code), 1);
                if (code==65){
                    if (amountEdt.getText().toString().length()>0 && !amountEdt.getText().toString().isEmpty()) {
                        if (Double.parseDouble(amountEdt.getText().toString().replaceAll("[$,]", "")) > 0) {
                            //inputConnection.commitText("Cobro por: " + amountEdt.getText().toString() + " http://blumonpay.com/", 1);
                            //sharedPreferencesDB.deleteView();
                            sharedPreferencesDB.insertViewValue(3,amountEdt.getText().toString());
                            setInputView(onCreateInputView());
                        }
                    }
                }else if (code==66){
                    if (amountEdt.getText().toString().length()>0 && !amountEdt.getText().toString().isEmpty()){
                    if (Double.parseDouble(amountEdt.getText().toString().replaceAll("[$,]",""))>0) {
                        sharedPreferencesDB.insertViewValue(1,amountEdt.getText().toString());
                        setInputView(onCreateInputView());
                    }
                    }
                    }else if (code==10){

                    if (sharedPreferencesDB.getViewValue()==3){
                        inputConnection.commitText("Cobro por: " + sharedPreferencesDB.getAmountValue() + " https://affipay.com/"+stringGeneration(), 1);
                    }else if (sharedPreferencesDB.getViewValue()==2){
                        inputConnection.commitText("Envio por: " + sharedPreferencesDB.getAmountValue() + " Con tarjeta: " + selectedCardBank + " " + selectedCardNum + " https://affipay.com/"+stringGeneration(), 1);
                    }
                    }else {
                        amountEdt.setText(amountEdt.getText().toString() + code);
                    }

            }
        }
    }

    public String stringGeneration() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {
        Toast.makeText(this, "SwipeLeft", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swipeRight() {
        Toast.makeText(this, "SwipeRight", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void onCardClick(List<CardInformation> list, int position) {
        int size = list.get(position).getCardNumber().length();
        selectedCardNum = "****"+list.get(position).getCardNumber().substring(size-4);
        selectedCardBank = list.get(position).getCardBank();
        sharedPreferencesDB.insertViewValue(2);
        setInputView(onCreateInputView());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back:
                if (sharedPreferencesDB.getViewValue()==1 || sharedPreferencesDB.getViewValue()==0 || sharedPreferencesDB.getViewValue()==3) {
                    sharedPreferencesDB.deleteView();
                    setInputView(onCreateInputView());
                }else if (sharedPreferencesDB.getViewValue()==2){
                    sharedPreferencesDB.insertViewValue(1);
                    setInputView(onCreateInputView());
                }
                break;


            case R.id.sendBtn:
                int [] codes = new int[5];
                codes[0]=10;
                onKey(10,codes);
                confAmount.setTextSize(40);
                imgCheckPay.setVisibility(View.VISIBLE);
                confAmount.setText(sharedPreferencesDB.getAmountValue());
                selectedCard.setText("Enviado con exito");
                sendBtn.setVisibility(View.GONE);
                sharedPreferencesDB.deleteView();
                selectedCardNum="";
                selectedCardBank="";
                break;

                case R.id.chargeBtn:

                    int [] code = new int[5];
                    code[0]=10;
                    onKey(10,code);
                    amountToCharge.setTextSize(40);
                    imgCheckCharge.setVisibility(View.VISIBLE);
                    amountToCharge.setText(sharedPreferencesDB.getAmountValue());
                    sharedPreferencesDB.deleteView();
                    chargeBtn.setVisibility(View.GONE);
                    linkTv.setVisibility(View.VISIBLE);
                    linkTv.setText("Link generado con exito");
                    break;

        }
    }
}
