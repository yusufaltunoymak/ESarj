package com.altunoymak.esarj.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.altunoymak.esarj.R

class CustomAlertDialogBuilder {
    companion object {
        var alertPositiveOnClickListener: () -> Unit = {}

        fun alertPositiveOnClickListener(block: () -> Unit) {
            alertPositiveOnClickListener = block
        }

        fun createDialog(context: Context, title: String, message: String, positiveButtonText: String, positiveButtonClickListener: (() -> Unit)? = null) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog, null)
            val titleTextView = dialogView.findViewById<TextView>(R.id.titleTextView)
            val messageTextView = dialogView.findViewById<TextView>(R.id.messageTextView)
            val positiveButton = dialogView.findViewById<Button>(R.id.positiveButton)

            titleTextView.text = title
            messageTextView.text = message
            positiveButton.text = positiveButtonText

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            positiveButton.setOnClickListener {
                positiveButtonClickListener?.invoke()
                alertDialog.dismiss()
            }

            alertDialog.show()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}