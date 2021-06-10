package com.alexandrosbentevis.beer.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexandrosbentevis.beer.framework.SafeClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

/**
 * Extension function for showing a view.
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Extension function for hiding a view.
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Extension function for loading images from a url.
 */
fun ImageView.load(url: String, onFinished: () -> Unit = {}) {
    Picasso.get()
        .load(url)
        .into(this, object : Callback {
            override fun onSuccess() {
                onFinished()
            }
            override fun onError(e: Exception?) {
                onFinished()
            }
        })
}

/**
 * Extension function for text watcher as a flow.
 *
 * @return a flow of char sequence.
 */
fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySend(s)
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}

/**
 * Extension function for diff utils.
 */
fun <T> RecyclerView.Adapter<*>.autoNotify(oldList: List<T>, newList: List<T>, compare: (T, T) -> Boolean) {

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    })
    diff.dispatchUpdatesTo(this)
}

/**
 * Delegates the click listener.
 */
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}
