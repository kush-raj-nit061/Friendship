package com.test.friendship.MessageNotif;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA0puR0so:APA91bHPs4yfIMXKRPFU0DhQkZ7s6rpXBy3YQYq2Btv0rSQSSOeTMDBYJSKyuQQXTLhAdJFSHvE_L9_nzgzUv5Fkj8-4nCNS1fga9pigiBe2X1wJpgwZaioNfe-kXKlI-88AVz8bat7e"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);


}
