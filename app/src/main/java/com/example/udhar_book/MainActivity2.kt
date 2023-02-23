package com.example.udhar_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.udhar_book.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity2 : AppCompatActivity() {


    private lateinit var transactions: Transaction
        private lateinit var transaction: List<Transaction>
        lateinit var TransactionAdapter: TransactionAdapter
        lateinit var deletedtransaction:Transaction
        lateinit var oldtransaction:Transaction
        lateinit var linearLayoutManager: LinearLayoutManager
        lateinit var db: AppDatabase



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            var binding = ActivityMain2Binding.inflate(layoutInflater)
            setContentView(binding.root)

            transaction = arrayListOf()


            TransactionAdapter = TransactionAdapter(transaction)
            linearLayoutManager = LinearLayoutManager(this)





            binding.addbtn.setOnClickListener {

                val intent = Intent(this, Add_Transaction::class.java)
                startActivity(intent)


            }

            db = Room.databaseBuilder(
                this,
                AppDatabase::class.java, "transaction"
            ).build()


            binding.recyclerview.apply {

                adapter = TransactionAdapter
                layoutManager = linearLayoutManager
            }



            var itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    deletedtransaction(transaction[viewHolder.adapterPosition])

                }


            }
            val swipeHelper=ItemTouchHelper(itemTouchHelper)
            swipeHelper.attachToRecyclerView(binding.recyclerview)


        }
    companion object{
            private lateinit var oldTransaction: List<Transaction>
            private lateinit var transaction: List<Transaction>
            lateinit var TransactionAdapter: TransactionAdapter
            lateinit var linearLayoutManager: LinearLayoutManager
            lateinit var db: AppDatabase
            lateinit var binding:MainActivity2

        }
    override fun onResume() {
            super.onResume()
            fetchAll()
        }


        fun fetchAll() {

            GlobalScope.launch {

                transaction = db.transactionDao().getAll()

                runOnUiThread {

                    upadateDashboard()
                    TransactionAdapter.setData(transaction)

                }
            }
        }

    fun upadateDashboard(){

        var totalAmount = transaction.map { it.amount }.sum()
        var budgetAmount = transaction.filter { it.amount > 0 }.map { it.amount }.sum()
        var expenceAmount = totalAmount - budgetAmount

        TransactionAdapter.setData(transaction)

        var balance=findViewById<TextView>( R.id.balance)
        "$%.2f".format(totalAmount)
        var budjet1=findViewById<TextView>( R.id.budjet1)
        "$%.2f".format(budgetAmount)
        var budjet2=findViewById<TextView>( R.id.budjet2)
        "$%.2f".format(expenceAmount)

    }

    fun deletedtransaction(transaction: Transaction) {


        deletedtransaction= transactions
        oldTransaction= this.transaction

        GlobalScope.launch {
            db.transactionDao().delete(transactions)

            this@MainActivity2.transaction = this@MainActivity2.transaction.filter { it.id !=transactions.id }


            runOnUiThread(){

                upadateDashboard()
                TransactionAdapter.setData(listOf(transaction))
                showSnackbar()
            }
        }

    }

    fun showSnackbar(){

        val coordinator=findViewById<View>(R.id.coordinator)
        val snackbar=Snackbar.make(coordinator,"Transaction Deleted!",Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){

            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this,R.color.red))
            .setTextColor(ContextCompat.getColor(this,R.color.white))
            .show()
    }

    fun undoDelete(){

        GlobalScope.launch {
            db.transactionDao().insertAll(deletedtransaction)

            transaction= oldTransaction

            runOnUiThread(){
                upadateDashboard()
                TransactionAdapter.setData((transaction))

            }
        }
    }


    }























