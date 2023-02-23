package com.example.udhar_book

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.udhar_book.databinding.ActivityDetailedBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Detailed_Activity : AppCompatActivity() {
    private lateinit var transaction: Transaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        transaction=intent.getSerializableExtra("transaction") as Transaction

        binding.labelInput.setText(transaction.label)
        binding.amountInput.setText(transaction.amount.toString())
        binding.deInput.setText(transaction.description)

        binding.rootview.setOnClickListener {
            this.window.decorView.clearFocus()

            var imm=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }


        binding.labelInput.addTextChangedListener {
            binding.updatebtn.visibility= View.VISIBLE

            if (it!!.count()>0)
                binding.labelLayout.error=null
        }
        binding.amountInput.addTextChangedListener {
            binding.updatebtn.visibility= View.VISIBLE

            if (it!!.count()>0)
                binding.amountInput.error=null
        }
        binding.deInput.addTextChangedListener {
            binding.updatebtn.visibility= View.VISIBLE


        }

        binding.updatebtn.setOnClickListener {


            val label= binding.labelInput.text.toString()
            val description=binding.deInput.text.toString()
            val amount=binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty()) {
                binding.labelLayout.error="Please enter a valid label"
            } else  if (amount==null)
                binding.amountLayout.error="Please enter a valid amount"

            else{
                val transaction=Transaction( transaction.id,label,amount, description)
                update(transaction)


            }

        }

        binding.closebtn.setOnClickListener {
            finish()
        }
    }

    private fun update (transaction: Transaction){

        val db:AppDatabase= Room.databaseBuilder(
            this,AppDatabase::class.java,"transaction"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }


}