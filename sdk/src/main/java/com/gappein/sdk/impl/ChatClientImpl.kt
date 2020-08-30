package com.gappein.sdk.impl

import android.webkit.URLUtil
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.listener.InitConnectionListener
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.gappein.sdk.util.db.FirebaseDbManager
import com.gappein.sdk.util.storage.FirebaseStorageManager
import java.util.*

class ChatClientImpl(
    private val storageManager: FirebaseStorageManager,
    private val dbManager: FirebaseDbManager
) : ChatClient {

    private var currentUser = User()

    override fun setUser(user: User, token: String, listener: InitConnectionListener?) {
        currentUser = (user)
        dbManager.createUser(user, {
            listener?.onSuccess(InitConnectionListener.ConnectionData(it, token))
        }, {
            listener?.onError(it.localizedMessage ?: "User already present")
        })

    }

    override fun getUser() = currentUser

    override fun sendMessage(message: String, onSuccess: () -> Unit, onError: () -> Unit) {

        val _message = Message(
            timeStamp = Calendar.getInstance().time,
            message = message,
            isUrl = URLUtil.isValidUrl(message),
            receiver = getUser().token,
            sender = getUser().token
        )

        dbManager.sendMessage(_message, {
        }, {
        })
    }

}