package com.example.contsctsrv

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.example.contsctsrv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var contacts = contactList()

    private val contactsAdapter = ContactsAdapter(
        contacts,
        onClickContact = { position -> onClickContact(position) },
    )

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val name = data?.getStringExtra(NewContactActivity.NAME) ?: ""
                val lastName = data?.getStringExtra(NewContactActivity.LAST_NAME) ?: ""
                val phone = data?.getStringExtra(NewContactActivity.PHONE) ?: ""
                val id = data?.getIntExtra(NewContactActivity.ID, -1) ?: -1

                val newContact = Contact(id, name, lastName, phone)

                if (id > 0 && id <= contacts.size) {
                    val newContactList = contacts.toMutableList()
                    newContactList[id - 1] = newContact
                    updateList(newContactList)
                } else if (id > contacts.size) {
                    updateList(contacts.plus(newContact))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerview.adapter = contactsAdapter

        binding.btnAdd.setOnClickListener {
            NewContactActivity.setId(contacts.size + 1)
            openNewContactActivity()
        }

        binding.trashBtn.setOnClickListener {
            updateUi(isClickedTrash = true)
        }

        binding.cancelBtn.setOnClickListener {
            updateUi(isClickedTrash = false)
        }

        binding.deleteBtn.setOnClickListener {
            updateList(contactsAdapter.deletedContactList())
            updateUi(isClickedTrash = false)
        }

    }

    private fun openNewContactActivity() {
        val intent = Intent(this, NewContactActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun contactList(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        for (i in 1..100) {
            contacts.add(Contact(i, "Имя$i", "Фамилия$i", "8-800-$i$i"))
        }
        return contacts
    }

    private fun onClickContact(position: Int) {
        NewContactActivity.setId(contacts[position].id)
        openNewContactActivity()
    }

    private fun updateUi(isClickedTrash: Boolean) {
        contactsAdapter.setShowCheckbox(isClickedTrash)

        if (isClickedTrash) {
            binding.btnAdd.visibility = View.INVISIBLE
            binding.deleteBtn.visibility = View.VISIBLE
            binding.cancelBtn.visibility = View.VISIBLE
        } else {
            binding.btnAdd.visibility = View.VISIBLE
            binding.deleteBtn.visibility = View.INVISIBLE
            binding.cancelBtn.visibility = View.INVISIBLE
        }

    }

    private fun updateList(newList: List<Contact>) {
        val result = DiffUtil.calculateDiff(
            ContactsDiffUtilCallback(
                contacts,
                newList
            )
        )
        contactsAdapter.contacts = newList
        result.dispatchUpdatesTo(contactsAdapter)
        contacts = newList
    }

}
