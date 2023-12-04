package com.example.contsctsrv

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contsctsrv.databinding.ActivityNewContactBinding

class NewContactActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNewContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            val name = binding.editName.text.toString()
            val lastName = binding.editLastName.text.toString()
            val phone = binding.editPhone.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra(NAME, name)
            resultIntent.putExtra(LAST_NAME, lastName)
            resultIntent.putExtra(PHONE, phone)
            resultIntent.putExtra(ID, id)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    companion object {
        const val REQUEST_CODE = 1
        const val NAME = "name"
        const val LAST_NAME = "lastName"
        const val PHONE = "phone"
        const val ID = "id"
        private var id = 0

        fun setId(value: Int) {
            id = value
        }
    }

}
