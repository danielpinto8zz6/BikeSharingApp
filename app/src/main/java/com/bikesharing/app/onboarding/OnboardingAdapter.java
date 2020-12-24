package com.bikesharing.app.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikesharing.app.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{

    private List<OnboardingItem> myOnboardingItems;
    private Context myContext;

    public OnboardingAdapter(@NonNull Context myContext, @NonNull List<OnboardingItem> myOnboardingItems) {
        this.myOnboardingItems = myOnboardingItems;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onbording, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(this.myOnboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return this.myOnboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {

        private TextView myTextTitle;
        private TextView myTextDescription;
        private ImageView myImage;

        OnboardingViewHolder(@NonNull View myItemView) {

            super(myItemView);
            this.myTextTitle = myItemView.findViewById(R.id.textTitle);
            this.myTextDescription = myItemView.findViewById(R.id.textDescription);
            this.myImage= myItemView.findViewById(R.id.action_image);
        }

        void setOnboardingData(OnboardingItem onboardingItem) {

            this.myTextTitle.setText(onboardingItem.getTitle());
            this.myTextDescription.setText(onboardingItem.getDescription());

            this.myImage.setImageResource(onboardingItem.getImage());
            Glide.with(myContext).load(onboardingItem.getImage()).into(myImage);
        }
    }
}
