package com.google.tapcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {


    private val _count = MutableLiveData<Int>()

    fun count(): LiveData<Int> {
        return _count
    }

    fun getCount(): Int? {
        return _count.value
    }

    fun setValue(savedCount: Int) {
        _count.value = savedCount
    }

    fun addCount() {
        _count.value = _count.value?.inc()
    }

    fun removeCount() {
        _count.value = _count.value?.dec()
    }

    fun reset() {
        _count.value = 0
    }


}