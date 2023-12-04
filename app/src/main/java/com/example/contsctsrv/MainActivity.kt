package com.example.contsctsrv

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.example.contsctsrv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var contacts = contactList()

    private val contactsAdapter = ContactsAdapter(
        contacts,
        onClickEditBtn = { position -> onClickEditBtn(position) },
        onClickCheckbox = { position -> onClickCheckbox(position) }
    )

    private var deletedContacts = mutableListOf<Contact>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerview.adapter = contactsAdapter

        binding.btnAdd.setOnClickListener {
            NewContactActivity.setId(contacts.size + 1)
            openNewContactActivity()
        }

        binding.btnDelete.setOnClickListener {
            updateList(contacts.minus(deletedContacts))
            deletedContacts.clear()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
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

    private fun openNewContactActivity() {
        val intent = Intent(this, NewContactActivity::class.java)
        startActivityForResult(intent, NewContactActivity.REQUEST_CODE)
    }

    private fun contactList(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        for (i in 1..100) {
            contacts.add(Contact(i, "Имя$i", "Фамилия$i", "8-800-$i$i"))
        }
        return contacts
    }

    private fun onClickEditBtn(position: Int) {
        NewContactActivity.setId(contacts[position].id)
        openNewContactActivity()
    }

    private fun onClickCheckbox(position: Int) {
        deletedContacts.add(contacts[position])
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