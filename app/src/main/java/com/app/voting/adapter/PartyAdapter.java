package com.app.voting.adapter;

import static com.app.voting.utils.Const.currentUser;
import static com.app.voting.utils.Const.votes;
import static com.app.voting.utils.FireBase.PARTY_REF;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.voting.MainActivity;
import com.app.voting.R;
import com.app.voting.model.PartiesModel;
import com.app.voting.model.UserModel;
import com.app.voting.utils.Const;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.ViewHolder> {

    Context context;
    ArrayList<PartiesModel> data;
    boolean isVoted = false;

    public PartyAdapter(Context c, ArrayList<PartiesModel> list) {
        this.context = c;
        this.data = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_party, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PartiesModel model = data.get(position);

        Picasso.get().load(model.getLogo()).into(holder.imageView);
        holder.Name.setText(model.getPartyName());

        holder.vote_Status.setOnClickListener(v -> {
            if (!isVoted) {
                PARTY_REF.child(model.getId()).child(votes).child(currentUser.getUid()).setValue(true).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Voted successfully.", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        Toast.makeText(context, "Something went wrong. Try again! \n"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Already voted", Toast.LENGTH_SHORT).show();
            }
        });

        PARTY_REF.child(model.getId()).child(votes).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(currentUser.getUid()).exists()) {
                    holder.vote_Status.setText("Voted");
                    holder.vote_Status.setTextColor(Color.GREEN);
                    isVoted = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView Name, vote_Status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.logo);
            Name = itemView.findViewById(R.id.name);
            vote_Status = itemView.findViewById(R.id.vote_Status);

        }
    }
}
