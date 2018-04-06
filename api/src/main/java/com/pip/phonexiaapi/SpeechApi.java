package com.pip.phonexiaapi;

import android.util.Base64;

import com.pip.phonexiaapi.data.AttachDictateResult;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.phonexiaapi.data.StreamResult;
import com.pip.phonexiaapi.data.TechnologiesResult;
import com.pip.phonexiaapi.data.Technology;
import com.pip.phonexiaapi.service.PhonexiaService;

import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Emitter;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by filipsollar on 6.4.18.
 */

public class SpeechApi implements ISpeechApi {

    public static final int SUCCESS_CODE = 200;
    public static final String SERVER_API = "77.240.177.148:8601";

    public static final String USERNAME = "team2";
    public static final String PASS = "hackathon";

    private Retrofit mRetrofit;

    private PhonexiaService mPhonexiaService;

    private String mStreamId;
    private String mTaskId;

    private RealTimeCallback<SpeechRecognitionResult> mCallback;

    public SpeechApi() {
        OkHttpClient client = new OkHttpClient.Builder()
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
    public RecorderCallback realTimeProcessing(int frequency, Language language, final RealTimeCallback<SpeechRecognitionResult> realTimeCallback) {
        mCallback = realTimeCallback;

        checkTechnologiesDictate(frequency, language);

        return new RecorderCallback() {
            @Override
            public void onRecording(Short[] data) {
                sendData(mStreamId, mTaskId, data);
            }

            @Override
            public void finished() {

            }
        };
    }

    private void checkTechnologiesDictate(final int frequency, final Language language) {
        mPhonexiaService.getTechnologiesAvailable(generateToken())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TechnologiesResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(TechnologiesResult technologiesResult) {
                        if (checkTechnologiesResult(technologiesResult, "DICTATE")) {
                            startStream(frequency, language);
                        }
                    }
                });
    }

    private void startStream(int frequency, final Language language) {
        mPhonexiaService.startStream(frequency, null, null, generateToken())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<StreamResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(StreamResult streamResult) {
                        mStreamId = streamResult.getStream();
                        attachDictate(language);
                    }
                });
    }


    private void attachDictate(Language language) {
        mPhonexiaService.attachDictate(mStreamId, language, generateToken())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AttachDictateResult>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   mCallback.onError(e);
                               }

                               @Override
                               public void onNext(AttachDictateResult attachDictateResult) {
                                   mTaskId = attachDictateResult.getStreamTaskInfo().getId();
                               }
                           }
                );

    }


    private void sendData(String streamId, final String taskId, Short[] data) {
        if (streamId == null || taskId == null) {
            return;
        }

        mPhonexiaService.sendChunksOfData(streamId, data, generateToken())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onError(e);
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.code() == SUCCESS_CODE) { // SUCCESS
                            sendResultBack(taskId);
                        }
                    }
                });


    }

    private void sendResultBack(String taskId) {
        mPhonexiaService.getResults(taskId, generateToken())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<SpeechRecognitionResult>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mCallback.onError(e);
                            }

                            @Override
                            public void onNext(SpeechRecognitionResult speechRecognitionResult) {
                                mCallback.onResult(speechRecognitionResult);
                            }
                        }
                );
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

    private String generateToken() {
        String text = USERNAME + "." + PASS;
        byte data[] = text.getBytes(Charset.forName("UTF-8"));

        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return "Basic " + base64;
    }





}
