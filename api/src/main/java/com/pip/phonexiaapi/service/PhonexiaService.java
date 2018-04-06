package com.pip.phonexiaapi.service;

import com.pip.phonexiaapi.data.AttachDictateResult;
import com.pip.phonexiaapi.data.AudioFileInfoResult;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.ReqResult;
import com.pip.phonexiaapi.data.SpeakerStreamResult;
import com.pip.phonexiaapi.data.SpeakersResult;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.phonexiaapi.data.StreamResult;
import com.pip.phonexiaapi.data.TechnologiesResult;
import com.pip.phonexiaapi.request.SpeakerModels;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
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
   Single<ReqResult<TechnologiesResult>> getTechnologiesAvailable();

   // <----- dictate begin ------->


   @POST("/stream/http")
   Single<ReqResult<StreamResult>> startStream(
           @Query("frequency") int frequency,
           @Query("n_channels") Integer numberOfChannels,
           @Query("path") String pathToFile // path to file where saved if not set no data is saved

   );


   @POST("/technologies/dictate")
   Single<ReqResult<AttachDictateResult>> attachDictate(
           @Query("stream") String streamId,
           @Query("model") Language language
   );

   @PUT("/stream/http")
   Single<Response<ResponseBody>> sendChunksOfData(
           @Query("stream") String streamId,
           @Body short[] S16IErawData
   );

   @GET("/technologies/dictate")
   Single<ReqResult<SpeechRecognitionResult>> getResults(
           @Query("task") String task
   );


   @DELETE("/stream/http")
   Single closeStream(
           @Query("stream") String streamId
   );

   // <----- dictate end ------->

   // <------ STT begin -------->

   @POST("/audiofile")
   Single uploadRecording(
           @Query("path") String path
   );

   @GET("/technologies/stt")
   Single<ResponseBody> processRecording(
           @Query("path") String path,
           @Query("model") Language model
   );

   @GET("/pending/{operationId}")
   Single<Response> getPendingResult(
           @Path("operationId") String operationId
   );

   // <------- STT end --------->

    // <------ Speaker identification begin ------>

    @POST("/technologies/speakerid/speakermodels/{name}")
    Single<Response<ResponseBody>> createSpeaker(
            @Path("name") String name
    );

    @POST("/technologies/speakerid/speakermodels/{name}/audiofile")
    Single<ReqResult<AudioFileInfoResult>> attachAudioFileToSpeaker(
            @Query("path") String path,
            @Body RequestBody audioData
    );


    @POST("/technologies/speakerid/groups/{group_name}")
    Single<Response<ResponseBody>> createSpeakerGroup(
            @Path("group_name") String groupName
    );

    @POST("/technologies/speakerid/groups/{group_name}/speakermodel")
    Single<Response<ResponseBody>> addToSpeakerGroup(
            @Body SpeakerModels speakers
    );

    @PUT("/technologies/speakerid/speakermodels/{user_name}/prepare?model=S")
    Single<Response<ResponseBody>> prepareSpeakerModel(
            @Path("user_name") String speakerName
    );

    @PUT("/technologies/speakerid/speakermodels/{group_name}/prepare?model=S")
    Single<Response<ResponseBody>> prepareSpeakerGroup(
            @Path("group_name") String groupName
    );

    @POST("/technologies/speakerid/stream")
    Single<ReqResult<SpeakerStreamResult>> postChunksToStream(
            @Query("group") String groupName

    );

    @GET("/technologies/speakerid/stream")
    Single<ReqResult<SpeakersResult>> getSpeakersResults(
            @Query("task") String taskId
    );




    // <------- Speaker identification end --------->





}
