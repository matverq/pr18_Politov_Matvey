package com.bignerdranch.android.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WorkoutFormFragment : Fragment(R.layout.fragment_workoutform) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        val etDate = view.findViewById<EditText>(R.id.etDate)
        val etType = view.findViewById<EditText>(R.id.etType)
        val etDuration = view.findViewById<EditText>(R.id.etDuration)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val btnSaveWorkout = view.findViewById<Button>(R.id.btnSaveWorkout)
        val btnViewHistory = view.findViewById<Button>(R.id.btnViewHistory)

        val sharedPrefs = requireActivity().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val userJson = sharedPrefs.getString("USER_DATA", null)
        if (userJson != null) {
            val user = gson.fromJson(userJson, User::class.java)
            tvWelcome.text = "Спортсмен: ${user.name} | ${user.age} лет | ${user.weight} кг"
        }

        btnSaveWorkout.setOnClickListener {
            val date = etDate.text.toString().trim()
            val type = etType.text.toString().trim()
            val durationStr = etDuration.text.toString().trim()
            val desc = etDescription.text.toString().trim()

            if (date.isEmpty() || type.isEmpty() || durationStr.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newWorkout = Workout(date, type, durationStr.toInt(), if (desc.isEmpty()) null else desc)

            val currentListJson = sharedPrefs.getString("WORKOUTS_LIST", null)
            val workoutList: MutableList<Workout> = if (currentListJson != null) {
                val typeToken = object : TypeToken<MutableList<Workout>>() {}.type
                gson.fromJson(currentListJson, typeToken)
            } else {
                mutableListOf()
            }

            workoutList.add(newWorkout)

            val updatedJson = gson.toJson(workoutList)
            sharedPrefs.edit().putString("WORKOUTS_LIST", updatedJson).apply()

            Toast.makeText(requireContext(), "Тренировка записана", Toast.LENGTH_SHORT).show()

            etDate.text.clear()
            etType.text.clear()
            etDuration.text.clear()
            etDescription.text.clear()
        }

        btnViewHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WorkoutListFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
