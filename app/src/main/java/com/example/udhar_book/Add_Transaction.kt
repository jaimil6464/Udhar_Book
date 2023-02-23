package com.example.udhar_book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.udhar_book.databinding.ActivityAddTransactionBinding
import com.example.udhar_book.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Add_Transaction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding= ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.labelInput.addTextChangedListener {

            if (it!!.count()>0)
                binding.labelLayout.error=null
        }

        binding.addTraBtn.setOnClickListener {

            val label= binding.labelInput.text.toString()
            val description=binding.deInput.text.toString()
            val amount=binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                binding.labelLayout.error="Please enter a valid label"

          else  if (amount==null)
                binding.amountLayout.error="Please enter a valid amount"

            else{
                val transaction=Transaction(0,label,amount, description)
                insert(transaction)


            }

        }

        binding.closebtn.setOnClickListener {
            finish()
        }
    }

    private fun insert(transaction: Transaction){

        val db:AppDatabase=Room.databaseBuilder(
            this,AppDatabase::class.java,"transaction"
        ).build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}