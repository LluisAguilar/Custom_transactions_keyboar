package com.blumonpay.keyboardtest.model;

public class CardInformation {

    String cardNumber;
    String cardType;
    String cardOwner;
    String cardExpiration;
    String cardBank;
    String cardNip;
    String cardColor;

    public CardInformation(String cardNumber, String cardType, String cardOwner, String cardExpiration, String cardBank, String cardNip, String cardColor) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardOwner = cardOwner;
        this.cardExpiration = cardExpiration;
        this.cardBank = cardBank;
        this.cardNip = cardNip;
        this.cardColor = cardColor;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public String getCardExpiration() {
        return cardExpiration;
    }

    public void setCardExpiration(String cardExpiration) {
        this.cardExpiration = cardExpiration;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public void setCardNip(String cardNip){
        this.cardNip = cardNip;
    }

    public String getCardNip(){
        return cardNip;
    }

    public String getCardColor(){
        return cardColor;
    }

    public void setCardColor(String cardColor){
        this.cardColor=cardColor;
    }
}
