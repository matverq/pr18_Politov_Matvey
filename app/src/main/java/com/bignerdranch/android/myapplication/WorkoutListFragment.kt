package com.bignerdranch.android.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WorkoutListFragment : Fragment(R.layout.fragment_workoutlist) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLogData = view.findViewById<TextView>(R.id.tvLogData)
        val btnBackToForm = view.findViewById<Button>(R.id.btnBackToForm)

        val sharedPrefs = requireActivity().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        val workoutsJson = sharedPrefs.getString("WORKOUTS_LIST", null)

        if (workoutsJson != null) {
            val gson = Gson()
            val typeToken = object : TypeToken<List<Workout>>() {}.type
            val list: List<Workout> = gson.fromJson(workoutsJson, typeToken)

            val sb = StringBuilder()
            list.forEachIndexed { index, workout ->
                sb.append("${index + 1}. [${workout.date}] — ${workout.type}\n")
                sb.append("   Время: ${workout.duration} мин.\n")
                sb.append("   Упражнения: ${workout.description ?: "Нет описания"}\n")
                sb.append("-------------------------------------\n\n")
            }
            tvLogData.text = sb.toString()
        } else {
            tvLogData.text = "У вас ещё нет сохраненных тренировок."
        }

        btnBackToForm.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}