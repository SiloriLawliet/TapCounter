package com.google.tapcounter

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.tapcounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var model: MainActivityViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: ActivityMainBinding
    private var isVolumeKeyEnabled = true;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

// Setting onClickListener on Views
        binding.layout.setOnClickListener(this)
        binding.btnRemoveCount.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        binding.btnLock.setOnClickListener(this)

// Setting up ViewModel
        model = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        model.count().observe(this, Observer {
            binding.textView.text = it.toString()
        })

// Getting Stored data from SharedPreference
        sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

// Sending the Stored sharedPreference to viewModel
        model.setValue(sharedPref.getInt("tapCount", 0))


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.layout -> {
                model.addCount()
            }
            R.id.btn_removeCount -> {
                model.removeCount()
            }
            R.id.btn_lock -> {
                if (binding.btnLock.text == "Lock") {
                    binding.btnLock.text = getString(R.string.unlock)
                    binding.btnRemoveCount.isEnabled = false
                    binding.layout.isEnabled = false
                    binding.btnReset.isEnabled = false
                    isVolumeKeyEnabled = false

                } else {
                    binding.btnLock.text = getString(R.string.lock)
                    binding.btnRemoveCount.isEnabled = true
                    binding.layout.isEnabled = true
                    binding.btnReset.isEnabled = true
                    isVolumeKeyEnabled = true

                }

            }
            R.id.btn_reset -> {
                model.reset()
            }
        }
    }

    //For volume button press
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (!isVolumeKeyEnabled) {
            return super.onKeyDown(keyCode, event)
        }
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> model.addCount()
            KeyEvent.KEYCODE_VOLUME_UP -> model.addCount()
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        editor.apply {
            model.getCount()?.let { putInt("tapCount", it) }
            apply()
        }
    }


}





