package com.blumonpay.keyboardtest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blumonpay.keyboardtest.R;
import com.blumonpay.keyboardtest.listeners.OnClickCardListener;
import com.blumonpay.keyboardtest.model.CardInformation;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> implements View.OnClickListener {
    Context context;
    List<CardInformation> list;
    OnClickCardListener onClickCardListener;

    public CardsAdapter(Context context, List<CardInformation> myList, OnClickCardListener onClickCardListener){
        this.context=context;
        this.list=myList;
        this.onClickCardListener = onClickCardListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.choose_card_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        int size = list.get(position).getCardNumber().length();

        viewHolder.cardNumber.setText("**** **** **** "+list.get(position).getCardNumber().substring(size-4));
        viewHolder.cardType.setText(list.get(position).getCardType());
        viewHolder.cardBank.setText(list.get(position).getCardBank());
        viewHolder.cardOwner.setText(list.get(position).getCardOwner());
        viewHolder.cardExpiration.setText(list.get(position).getCardExpiration());

        if (list.get(position).getCardColor().equals("sky_blue")){
            viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.sky_blue));
        }else if (list.get(position).getCardColor().equals("blue")){
            viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
        }else if (list.get(position).getCardColor().equals("Red")){
            viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        }
        viewHolder.cardView.setOnClickListener(this);
        viewHolder.cardView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

        onClickCardListener.onCardClick(list,Integer.parseInt(v.getTag().toString()));

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView cardNumber;
        TextView cardType;
        TextView cardBank;
        TextView cardOwner;
        TextView cardExpiration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.my_item_cardView);
            cardNumber = itemView.findViewById(R.id.cardNumberTxt);
            cardType = itemView.findViewById(R.id.cardTypeTxt);
            cardBank = itemView.findViewById(R.id.cardBankTxt);
            cardOwner = itemView.findViewById(R.id.cardOwnerTxt);
            cardExpiration = itemView.findViewById(R.id.cardExpirationTxt);
        }
    }
}
