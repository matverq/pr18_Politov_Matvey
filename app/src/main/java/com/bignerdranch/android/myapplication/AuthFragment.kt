package com.bignerdranch.android.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class AuthFragment : Fragment(R.layout.fragment_auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etAge = view.findViewById<EditText>(R.id.etAge)
        val etWeight = view.findViewById<EditText>(R.id.etWeight)
        val btnSaveUser = view.findViewById<Button>(R.id.btnSaveUser)

        btnSaveUser.setOnClickListener {
            val name = etName.text.toString().trim()
            val ageStr = etAge.text.toString().trim()
            val weightStr = etWeight.text.toString().trim()

            if (name.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните профиль полностью", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(name, ageStr.toInt(), weightStr.toDouble())

            val userJson = Gson().toJson(user)

            val sharedPrefs = requireActivity().getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putString("USER_DATA", userJson).apply()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WorkoutFormFragment())
                .commit()
        }
    }
}