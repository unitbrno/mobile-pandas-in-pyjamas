package com.pip.phonexiaapi.service;

import com.pip.phonexiaapi.data.AttachDictateResult;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.phonexiaapi.data.StreamResult;
import com.pip.phonexiaapi.data.TechnologiesResult;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface PhonexiaService {


   @GET("/technologies")
   Single<TechnologiesResult> getTechnologiesAvailable(
           @Header("Authorization") String token
   );

   // <----- dictate begin ------->


   @POST("/stream/http")
   Single<StreamResult> startStream(
           @Query("frequency") int frequency,
           @Query("n_channels") Integer numberOfChannels,
           @Query("path") String pathToFile, // path to file where saved if not set no data is saved
           @Header("Authorization") String token

   );


   @POST("/technologies/dictate")
   Single<AttachDictateResult> attachDictate(
           @Query("stream") String streamId,
           @Query("model") Language language,
           @Header("Authorization") String token
   );

   @PUT("/stream/http")
   Single<Response> sendChunksOfData(
           @Query("stream") String streamId,
           @Body Object S16IErawData,
           @Header("Authorization") String token
   );

   @GET("/technologies/dictate")
   Single<SpeechRecognitionResult> getResults(
           @Query("task") String task,
           @Header("Authorization") String token
   );



   @DELETE("/stream/http")
   Single closeStream(
           @Query("stream") String streamId,
           @Header("Authorization") String token
   );

   // <----- dictate end ------->

   // <------ STT begin -------->

   @POST("/audiofile")
   Single uploadRecording(
           @Query("path") String path,
           @Header("Authorization") String token
   );

   @GET("/technology/stt")
   Single<Response> processRecording(
           @Query("path") String path,
           @Query("model") Language model,
           @Header("Authorization") String token
   );

   @GET("/pending/{operationId}")
   Single<Response> getPendingResult(
           @Path("operationId") String operationId,
           @Header("Authorization") String token
   );

   // <------- STT end --------->


}
