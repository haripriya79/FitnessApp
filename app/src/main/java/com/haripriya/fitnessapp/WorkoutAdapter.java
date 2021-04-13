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

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<Workouts> mWorkouts;
    LayoutInflater inflater;
    Context context;
    String mWorkoutName;



    public WorkoutAdapter(Context context, List<Workouts> mWorkouts,String workoutName) {
        this.mWorkouts = mWorkouts;
        this.inflater = LayoutInflater.from(context);
        mWorkoutName = workoutName;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = inflater.inflate(R.layout.workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder{
        TextView mTextview ;
        CardView mCardView;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextview = itemView.findViewById(R.id.tv_sub_workout_name);
            mCardView = itemView.findViewById(R.id.card_view_workout);
        }

        public void bind(final int position) {
            mTextview.setText(mWorkouts.get(position).getName());

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent  = new Intent(context ,WorkoutDetailActivity.class );
                    intent.putExtra("Details",mWorkouts.get(position));
                    intent.putExtra("workoutName",mWorkoutName);
                    context.startActivity(intent);

                }
            });
        }
    }
}
