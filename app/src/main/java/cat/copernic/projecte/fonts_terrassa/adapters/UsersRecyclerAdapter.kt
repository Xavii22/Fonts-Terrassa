package cat.copernic.projecte.fonts_terrassa.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.projecte.fonts_terrassa.R
import cat.copernic.projecte.fonts_terrassa.databinding.ItemUserListBinding
import cat.copernic.projecte.fonts_terrassa.models.User
import com.google.firebase.firestore.FirebaseFirestore

class UsersRecyclerAdapter : RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>() {
    private var users: MutableList<User> = ArrayList()
    lateinit var context: Context
    private val db = FirebaseFirestore.getInstance()

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun UsersRecyclerAdapter(usersList: MutableList<User>, contxt: Context) {
        this.users = usersList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemUserListBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(users[position]) {
                db.collection("users")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (document.get("email").toString() == users[position].email) {
                                if (document.get("active").toString().toBoolean()) {
                                    binding.txtUser.text = this.email
                                } else {
                                    binding.txtUser.text = this.email
                                    binding.btnDelete.setImageResource(R.drawable.ic_person_add)
                                    binding.cardViewUser.setCardBackgroundColor(Color.LTGRAY)
                                }
                            }
                        }
                        //Listener Disable/Enable Button
                        val deleteBtn = holder.itemView.findViewById<ImageView>(R.id.btnDelete)
                        deleteBtn.setOnClickListener {
                            db.collection("users")
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        if (document.get("email").toString() == users[position].email) {
                                            if (document.get("active").toString().toBoolean()) {
                                                db.collection("users").document(users[position].email).delete()
                                                db.collection("users").document(users[position].email)
                                                    .set(
                                                        hashMapOf(
                                                            "email" to users[position].email,
                                                            "active" to false
                                                        )
                                                    )
                                                binding.txtUser.text = this.email
                                                binding.btnDelete.setImageResource(R.drawable.ic_person_add)
                                                binding.cardViewUser.setCardBackgroundColor(Color.LTGRAY)
                                            } else {
                                                db.collection("users").document(users[position].email).delete()
                                                db.collection("users").document(users[position].email)
                                                    .set(
                                                        hashMapOf(
                                                            "email" to users[position].email,
                                                            "active" to true
                                                        )
                                                    )
                                                binding.txtUser.text = this.email
                                                binding.btnDelete.setImageResource(R.drawable.ic_person_remove)
                                                binding.cardViewUser.setCardBackgroundColor(Color.WHITE)
                                            }
                                        }
                                    }
                                }
                        }
                    }
            }
        }
        val item = users[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return users.size
    }


    class ViewHolder(val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(users: User) {

        }
    }
}