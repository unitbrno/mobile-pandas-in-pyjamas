package com.pip.phonexiaapi.service;

import com.pip.phonexiaapi.data.AttachDictateResult;
import com.pip.phonexiaapi.data.AudioFileInfoResult;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.ReqResult;
import com.pip.phonexiaapi.data.SpeakerModelsResponse;
import com.pip.phonexiaapi.data.SpeakerStreamResult;
import com.pip.phonexiaapi.data.SpeakersResult;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.phonexiaapi.data.StreamResult;
import com.pip.phonexiaapi.data.TechnologiesResult;
import com.pip.phonexiaapi.request.SpeakerModels;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
   Call<ReqResult<TechnologiesResult>> getTechnologiesAvailable();

   // <----- dictate begin ------->


   @POST("/stream/http")
   Call<ReqResult<StreamResult>> startStream(
           @Query("frequency") int frequency,
           @Query("n_channels") Integer numberOfChannels,
           @Query("path") String pathToFile // path to file where saved if not set no data is saved
   );


   @POST("/technologies/dictate")
   Call<ReqResult<AttachDictateResult>> attachDictate(
           @Query("stream") String streamId,
           @Query("model") Language language
   );

   @PUT("/stream/http")
   Call<Void> sendChunksOfData(
           @Query("stream") String streamId,
           @Body RequestBody S16IErawData
   );

   @GET("/technologies/dictate")
   Call<ReqResult<SpeechRecognitionResult>> getResults(
           @Query("task") String task
   );


   @DELETE("/stream/http")
   Call<Void> closeStream(
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
    Call<Void> createSpeaker(
            @Path("name") String name
    );

    @POST("/technologies/speakerid/speakermodels/{name}/audiofile")
    Call<ReqResult<AudioFileInfoResult>> attachAudioFileToSpeaker(
            @Path("name") String userName,
            @Query("path") String path,
            @Body RequestBody audioData
    );


    @PUT("/technologies/speakerid/groups/{group_name}")
    Call<Void> createSpeakerGroup(
            @Path("group_name") String groupName
    );

    @POST("/technologies/speakerid/groups/{group_name}/speakermodel")
    Call<Void> addToSpeakerGroup(
            @Path("group_name") String groupName,
            @Body SpeakerModels speakers
    );

    @PUT("/technologies/speakerid/speakermodels/{user_name}/prepare?model=XL3")
    Call<Void> prepareSpeakerModel(
            @Path("user_name") String speakerName
    );

    @PUT("/technologies/speakerid/groups/{group_name}/prepare?model=XL3")
    Call<Void> prepareSpeakerGroup(
            @Path("group_name") String groupName
    );

    @POST("/technologies/speakerid/stream?model=XL3")
    Call<ReqResult<SpeakerStreamResult>> addSidToStream(
            @Query("group") String groupName,
            @Query("stream") String streamId

    );

    @GET("/technologies/speakerid/stream")
    Call<ReqResult<SpeakersResult>> getSpeakersResults(
            @Query("task") String taskId
    );


    // <------- Speaker identification end --------->


    @GET("/technologies/speakerid/speakermodels")
    Call<ReqResult<SpeakerModelsResponse>> getSpeakerModels();



}
