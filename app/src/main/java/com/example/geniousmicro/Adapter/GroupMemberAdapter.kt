package com.example.geniousmicro.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.geniousmicro.Models.GlobalUtilityModels.GlobalDataModel
import com.example.geniousmicro.Models.UtilityModels.GroupEmiDataModel
import com.example.geniousmicro.Models.UtilityModels.GroupMemberModel
import com.example.geniousmicro.R

class GroupMemberAdapter(val items: List<GroupMemberModel>, val context: Context) :
    RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupMemberViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.group_member_layout, parent, false)
    )

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: GroupMemberViewHolder, position: Int) {
        val model = items.get(position);
        holder.serialno!!.text =
            "" + (if ((position + 1) < 10) "0" + (position + 1) else (position + 1))
        holder.membercode!!.text = model.GroupMemberCode
        holder.membername!!.text = model.GroupMemberName
        holder.emi!!.text = model.EMIAmount
        holder.membercode!!.setSelected(true)
        holder.membername!!.setSelected(true)

        holder.ed_emi!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called to notify you that the text has been changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    var lsamt = 0.0
                    if (!holder.ed_emi!!.text!!.toString()
                            .equals("") && (holder.ed_emi!!.text!!.toString()
                            .toDouble() % model.EMIAmount.toDouble()) == 0.0
                    ) {
                        GlobalDataModel.emidatalist.remove(model.GroupMemberCode)
                        if (!holder.ed_ls!!.text!!.toString().equals("")) {
                            lsamt = holder.ed_ls!!.text!!.toString().toDouble()
                        } else {
                            lsamt = 0.0
                        }
                        var groupEmiModel = GroupEmiDataModel(model.GroupMemberCode,
                            model.LoanCode,
                            model.AccountCode,
                            lsamt.toString(), holder.ed_emi!!.text!!.toString()
                        )
                        GlobalDataModel.emidatalist[model.GroupMemberCode] = groupEmiModel
                    } else {
                        holder.ed_emi!!.error = "Enter Proper Emi Amt."
                        GlobalDataModel.emidatalist.remove(model.GroupMemberCode)
                    }

                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        holder.ed_ls!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called to notify you that the text has been changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    if (model.AccountCode.equals("")) {
                        holder!!.ed_ls!!.setError("No Account Found")
                    } else {
                        var emiamt = 0.0
                        if (!holder.ed_ls!!.text!!.toString()
                                .equals("")
                        ) {
                            GlobalDataModel.emidatalist.remove(model.GroupMemberCode)
                            if (!holder.ed_emi!!.text!!.toString().equals("")) {
                                emiamt = holder.ed_emi!!.text!!.toString().toDouble()
                            } else {
                                emiamt = 0.0
                            }
                            var groupEmiModel = GroupEmiDataModel(model.GroupMemberCode,
                                model.LoanCode,
                                model.AccountCode,
                                holder.ed_ls!!.text!!.toString(), emiamt.toString()
                            )
                            GlobalDataModel.emidatalist[model.GroupMemberCode] = groupEmiModel
                        } else {
                            holder.ed_ls!!.error = "Enter Proper Emi Amt."
                            GlobalDataModel.emidatalist.remove(model.GroupMemberCode)
                        }
                    }

                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    class GroupMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serialno: TextView? = view.findViewById(R.id.serialno)
        var membercode: TextView? = view.findViewById(R.id.membercode)
        var membername: TextView? = view.findViewById(R.id.membername)
        var emi: TextView? = view.findViewById(R.id.emi)
        var ed_emi: TextView? = view.findViewById(R.id.ed_emi)
        var ed_ls: TextView? = view.findViewById(R.id.ed_ls)

    }
}