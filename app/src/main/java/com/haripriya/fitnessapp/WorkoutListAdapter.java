package com.haripriya.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutListViewHolder> {
    private List<String> mWorkoutList;
    private LayoutInflater inflater;
    Context context;
    int[] imageIds = {R.drawable.full_body_workout,R.drawable.arm_workout,R.drawable.leg_workout,R.drawable.abs_workout};
    public WorkoutListAdapter(Context context , List<String> WorkoutList) {
        this.inflater = LayoutInflater.from(context);
        mWorkoutList = WorkoutList;
    }

    @NonNull
    @Override
    public WorkoutListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = inflater.inflate(R.layout.workout_list, parent, false);
        return new WorkoutListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutListViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        if(mWorkoutList==null||mWorkoutList.size()== 0){
            return -1;

        }
        return mWorkoutList.size();
    }

    public class WorkoutListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        CardView mCardView;
        public WorkoutListViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_workout_name);
            mCardView = itemView.findViewById(R.id.card_view_workout_list);
        }

        public void bind(final int position) {
            mTextView.setText(mWorkoutList.get(position));
            mCardView.setBackgroundResource(imageIds[position]);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , WorkoutActivity.class);
                    intent.putExtra("workoutName" , mWorkoutList.get(position));
                    context.startActivity(intent);

                }
            });
        }
    }
}
