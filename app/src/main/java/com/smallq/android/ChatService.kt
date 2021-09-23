package com.smallq.android

import com.smallq.android.adapter.ContactsPageListAdapter
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ChatService {
    @Multipart
    @POST("apis/register")
    fun requestRegister(
        @Part fileData: MultipartBody.Part,
        @Query("name") name:String,
        @Query("password")password:String
    ): Observable<ServerResult<ContactsPageListAdapter.ContactInfo>>
}