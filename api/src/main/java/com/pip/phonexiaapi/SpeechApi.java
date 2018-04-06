package com.pip.phonexiaapi;

import com.pip.phonexiaapi.data.AttachDictateResult;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.ReqResult;
import com.pip.phonexiaapi.data.SpeakersResult;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.phonexiaapi.data.StreamResult;
import com.pip.phonexiaapi.data.TechnologiesResult;
import com.pip.phonexiaapi.data.Technology;
import com.pip.phonexiaapi.service.PhonexiaService;

import java.io.IOException;
import java.nio.ByteBuffer;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;
import rx.Subscriber;

import rx.schedulers.Schedulers;

/**
 * Created by filipsollar on 6.4.18.
 */

public class SpeechApi implements ISpeechApi {

    public static final int SUCCESS_CODE = 200;
    public static final String SERVER_API = "http://77.240.177.148:8601";

    public static final String USERNAME = "team2";
    public static final String PASS = "hackathon";

    private Retrofit mRetrofit;

    private PhonexiaService mPhonexiaService;

    private String mStreamId;
    private String mTaskId;

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
        return new RecorderCallback() {

            @Override
            public void onRecording(short[] data) {
                sendData(mStreamId, mTaskId, data);
            }

            @Override
            public void finished() {

            }
        };
    }

    @Override
    public void startSpeakerIdentification(String groupName) {

        mPhonexiaService.prepareSpeakerGroup(groupName)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<ResponseBody>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mCallback.onError(e);
                            }

                            @Override
                            public void onNext(Response<ResponseBody> response) {
                                if (response.code() == SUCCESS_CODE) {
                                    startChecking();
                                }
                            }
                        }
                );
    }

    @Override
    public void createSpeakerModel(String userName) {
        mPhonexiaService.createSpeaker(userName)
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
                        if (response.code() == SUCCESS_CODE) {
                            // TODO add wav file
                        }
                    }
                });
    }

    private void startChecking() {
        // TODO !!!!!
    }

    private void uploadWavFileToSpeakerModel(String userName) {
        RequestBody body = RequestBody.create("", );

    }


}
