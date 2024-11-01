package com.example.kotlin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by zpf on 2023/1/28.
 */
class KotlinViewModel(private val kotlinRepository: KotlinRepository) : ViewModel() {

    fun login(){
        viewModelScope.launch {
            Log.i(TAG, "login: start")
            withContext(Dispatchers.IO){
                kotlinRepository.loginRequest()
            }
            Log.i(TAG, "login: end")
        }
    }

    companion object{
        private const val TAG = "KotlinViewModel"
    }
}