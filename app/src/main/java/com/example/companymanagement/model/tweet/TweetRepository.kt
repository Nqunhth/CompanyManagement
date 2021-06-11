package com.example.companymanagement.model.tweet

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class TweetRepository(var col: CollectionReference) {
    suspend fun addNewTweet(tweet: TweetModel): TweetModel? {
        return col.add(tweet).await().get().await().toObject(TweetModel::class.java)
    }

    private fun getTweetRef(tweet: TweetModel): DocumentReference {
        return col.document(tweet.postuid!!)
    }

    fun getCommentRepo(tweet: TweetModel): CommentRepository {
        return CommentRepository(getTweetRef(tweet));
    }

    suspend fun getTweet(count: Long = 10): MutableList<TweetModel>? {
        return col.orderBy("create_time", Query.Direction.DESCENDING).limit(count).get().await()
            .toObjects(TweetModel::class.java)
    }

    suspend fun getTweet(count: Long = 10, startafter: TweetModel): MutableList<TweetModel>? {
        return col.orderBy("create_time", Query.Direction.DESCENDING).limit(count)
            .startAfter(startafter.CreateTime)
            .get().await().toObjects(TweetModel::class.java)
    }
}