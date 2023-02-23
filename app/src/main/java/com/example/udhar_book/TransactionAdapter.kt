package com.example.udhar_book

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private var transaction: List<Transaction>):RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    class TransactionHolder(view:View):RecyclerView.ViewHolder(view){

        var label:TextView=view.findViewById(R.id.label)
        var amount:TextView=view.findViewById(R.id.amount)
        var balance:TextView=view.findViewById(R.id.balance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent,false)
        return TransactionHolder(view)
    }

    override fun getItemCount(): Int {
        return transaction.size
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {


        var transaction=transaction[position]
        var context=holder.amount.context

        if (transaction.amount >=0){
            holder.amount.text="+ %$.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.teal_700))

        }

        else{

            holder.amount.text="-%$.2f".format(Math.abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.red))



        }
        holder.label.text= transaction.label
        holder.itemView.setOnClickListener {

            var intent=Intent(context,Detailed_Activity::class.java)
            intent.putExtra("transaction",transaction)
            context.startActivity(intent)
        }
    }

    fun setData(transaction: List<Transaction>) {

        this.transaction=transaction
        notifyDataSetChanged()


    }
}