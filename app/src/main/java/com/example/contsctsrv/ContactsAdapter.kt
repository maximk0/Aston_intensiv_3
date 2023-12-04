package com.example.contsctsrv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contsctsrv.databinding.ItemContactBinding

class ContactsAdapter(
    var contacts: List<Contact>,
    private val onClickEditBtn: (position: Int) -> Unit,
    private val onClickCheckbox: (position: Int) -> Unit
) : RecyclerView.Adapter<ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]

        with(holder.binding) {

            id.text = holder.itemView.context.getString(R.string.id, contact.id)
            firstName.text = contact.firstName
            lastName.text = contact.lastName
            number.text = contact.number

            btnEdit.setOnClickListener {
                onClickEditBtn(position)
            }

            checkbox.setOnClickListener {
                if (checkbox.isChecked)
                    onClickCheckbox(position)
            }
        }
    }

}

class ContactsViewHolder(
    val binding: ItemContactBinding
) : RecyclerView.ViewHolder(binding.root)

class ContactsDiffUtilCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}
