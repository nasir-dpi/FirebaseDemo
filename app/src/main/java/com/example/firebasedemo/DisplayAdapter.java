package com.example.firebasedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.DisplayViewHolder> {
    private Context context;
    private List<User> userList;

    public DisplayAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public DisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list, null);
        return new DisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayViewHolder holder, int position) {

        final User user= userList.get(position);
        holder.nameAddress.setText(user.getName());
        holder.emailAddress.setText(user.getEmail());
        holder.birthAddress.setText(user.getBirthDate());
        //holder.imageAddress.setImageURI(user.getImageUrl(Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView)));
        //holder.imageAddress.setImageDrawable(context.getResources());
        Picasso.get().load(user.getImageUrl()).into(holder.imageAddress);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class DisplayViewHolder extends RecyclerView.ViewHolder {
        TextView nameAddress,emailAddress,birthAddress;
        ImageView imageAddress;

        public DisplayViewHolder(@NonNull View item) {
            super(item);
            nameAddress=item.findViewById(R.id.nameId);
            emailAddress=item.findViewById(R.id.emailId);
            birthAddress=item.findViewById(R.id.birthDateId);
            imageAddress=item.findViewById(R.id.imageId);
        }
    }
}


