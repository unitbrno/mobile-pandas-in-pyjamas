package com.pip.phonexiaapi;

import android.util.Log;

import com.pip.phonexiaapi.data.AttachDictateResult;
import com.pip.phonexiaapi.data.AudioFileInfoResult;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.ReqResult;
import com.pip.phonexiaapi.data.SpeakerModelsResponse;
import com.pip.phonexiaapi.data.SpeakersResult;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.phonexiaapi.data.StreamResult;
import com.pip.phonexiaapi.data.TechnologiesResult;
import com.pip.phonexiaapi.data.Technology;
import com.pip.phonexiaapi.request.SpeakerModels;
import com.pip.phonexiaapi.service.PhonexiaService;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by filipsollar on 6.4.18.
 */

public class SpeechApi implements ISpeechApi {

    public static final int SUCCESS_CODE = 200;
    public static final String RECORD = "/record.wav";
    public static final String SERVER_API = "http://77.240.177.148:8601";

    public static final String USERNAME = "team2";
    public static final String PASS = "hackathon";

    private Retrofit mRetrofit;

    private PhonexiaService mPhonexiaService;

    private String mStreamId;
    private String mTaskId;

    private RecorderCallback mRecorderCallback = new RecorderCallback() {

        @Override
        public void onRecording(short[] data) {
            sendData(mStreamId, mTaskId, data);
        }

        @Override
        public void finished() {

        }
    };

    private RealTimeCallback<SpeechRecognitionResult> mCallback;

    public SpeechApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        request = request.newBuilder()
                                .addHeader("Authorization",  Credentials.basic(USERNAME, PASS))
                                .build();


                        return chain.proceed(request);
                    }
                })
                .addInterceptor(getInterceptor())
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .baseUrl(SERVER_API)
                .build();


        mPhonexiaService = retrofit.create(PhonexiaService.class);
    }

    @Override
    public void realTimeProcessing(int frequency, Language language, final RealTimeCallback<SpeechRecognitionResult> realTimeCallback) {
        mCallback = realTimeCallback;

        checkTechnologiesDictate(frequency, language);
        //startStream(frequency, language);


    }

    private void checkTechnologiesDictate(final int frequency, final Language language) {
        mPhonexiaService.getTechnologiesAvailable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReqResult<TechnologiesResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(ReqResult<TechnologiesResult> technologiesResult) {
                        if (checkTechnologiesResult(technologiesResult.getResult(), "Dictate")) {
                            startStream(frequency, language);
                        }
                    }
                });
    }

    private void startStream(int frequency, final Language language) {
        mPhonexiaService.startStream(frequency, null, null)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReqResult<StreamResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(ReqResult<StreamResult> streamResult) {
                        mStreamId = streamResult.getResult().getStream();
                        attachDictate(language);
                    }
                });
    }


    private void attachDictate(Language language) {
        mPhonexiaService.attachDictate(mStreamId, language)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReqResult<AttachDictateResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(ReqResult<AttachDictateResult> attachDictateResult) {
                        mTaskId = attachDictateResult.getResult().getStreamTaskInfo().getId();
                        mCallback.onStarted();
                    }
                });

    }


    private void sendData(String streamId, final String taskId, short[] data) {
        if (streamId == null || taskId == null) {
            return;
        }



        mPhonexiaService.sendChunksOfData(streamId, data)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response.code() == SUCCESS_CODE) { // SUCCESS
                            sendResultBack(taskId);
                        }
                    }
                });




    }

    private void sendResultBack(String taskId) {
        mPhonexiaService.getResults(taskId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReqResult<SpeechRecognitionResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(ReqResult<SpeechRecognitionResult> speechRecognitionResult) {
                        mCallback.onResult(speechRecognitionResult.getResult());
                    }
                });


        mPhonexiaService.getSpeakersResults(taskId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReqResult<SpeakersResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(ReqResult<SpeakersResult> speakersResult) {
                        mCallback.onSpeakerResult(speakersResult.getResult().getResults().get(0));
                    }
                });
    }


    @Override
    public Single speechToText() {
        return null;
    }

    private boolean checkTechnologiesResult(TechnologiesResult result, String techName) {
        for (Technology technology:result.getTechnologies()) {
            if (technology.getName().equals(techName)) {
                return true;
            }
        }

        return false;
    }

    private Interceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }


    public RecorderCallback getCallback() {
        return mRecorderCallback;
    }


    @Override
    public void createSpeakerModel(final String userName, final File wavFile, final ApiCallback<AudioFileInfoResult> callback) {
        mPhonexiaService.createSpeaker(userName).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                uploadWavFileToSpeakerModel(userName, wavFile, callback);

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);

            }

        });
    }

    public void getUserModels(final ApiCallback<List<String>> callback) {
        mPhonexiaService.getSpeakerModels().enqueue(new Callback<ReqResult<SpeakerModelsResponse>>() {
            @Override
            public void onResponse(Call<ReqResult<SpeakerModelsResponse>> call, Response<ReqResult<SpeakerModelsResponse>> response) {
                callback.onSuccess(response.body().getResult().getModels());
            }

            @Override
            public void onFailure(Call<ReqResult<SpeakerModelsResponse>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    @Override
    public void stopProcessing() {
        mCallback = null;
    }

    @Override
    public void createAndPrepareGroup(final List<String> userModels, final String groupName, final ApiCallback<Boolean> callback) {
        mPhonexiaService.createSpeakerGroup(groupName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void>response) {
                addUsersToGroup(userModels, groupName, callback);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    private void addUsersToGroup(List<String> userModels, final String groupName, final ApiCallback<Boolean> callback) {
        SpeakerModels models = new SpeakerModels();

        models.setSpeakerNames(userModels);

        mPhonexiaService.addToSpeakerGroup(groupName, models).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                prepareGroup(groupName, callback);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }


        });
    }

    private void prepareGroup(String groupName, final ApiCallback<Boolean> callback) {
        mPhonexiaService.prepareSpeakerGroup(groupName).
                enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 202) {
                            //startChecking(callback); TODO
                            String location = response.headers().get("Location");
                            location = location.substring(9, location.length());
                            System.out.println(location);
                        } else {
                            callback.onSuccess(true);
                        }
                    }


                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onFailure(t);
                    }
                });
    }

    private void startChecking(ApiCallback<Boolean> callback) {
        // TODO !!!!!
    }

    private void uploadWavFileToSpeakerModel(String userName, File wavFile, final ApiCallback<AudioFileInfoResult> callback) {
        RequestBody body = RequestBody.create(MediaType.parse("audio/wav"), wavFile );

        mPhonexiaService.attachAudioFileToSpeaker(userName, RECORD,body)
                .enqueue(new Callback<ReqResult<AudioFileInfoResult>>() {
                    @Override
                    public void onResponse(Call<ReqResult<AudioFileInfoResult>> call, Response<ReqResult<AudioFileInfoResult>> response) {
                        callback.onSuccess(response.body().getResult());
                    }

                    @Override
                    public void onFailure(Call<ReqResult<AudioFileInfoResult>> call, Throwable t) {
                        t.printStackTrace();
                        callback.onFailure(t);
                    }
                });
    }


}
