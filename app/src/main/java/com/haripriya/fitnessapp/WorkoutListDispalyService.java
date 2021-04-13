package com.haripriya.fitnessapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

public class WorkoutListDispalyService {
    public static void handleActionUpdateWorkoutWidgets(Context context,
                                                       String WorkoutName, String listofWorkouts)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName name = new ComponentName(context, WorkoutWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(name);



        // Update all widgets
        WorkoutWidget.updateWorkoutWidgets(context, appWidgetManager,
                WorkoutName, listofWorkouts, appWidgetIds);
    }
}
