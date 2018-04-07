package com.pip.unitskoda;

import com.pip.phonexiaapi.data.Language;
import com.pip.unitskoda.memo.Memo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by filipsollar on 7.4.18.
 */

public class KeywordHandler {
    
    private State mState;
    private Language mLanguage;
    private Callback mCallback;

    private List<String> mTokens;

    public void start(Language language, Callback callback) {
        mState = State.READY;
        mLanguage = language;
    }

    public void process(List<String> words) {
        if (words.size() < 2) {
            return;
        }

        String lastWord = words.get(words.size()-1);
        String preLastWord = words.get(words.size()-2);

        switch (mState) {
            case READY:
                if (preLastWord.equals(getMapByLang().get(KeyWord.BEGIN_MEETING).get(0))
                        && lastWord.equals(getMapByLang().get(KeyWord.BEGIN_MEETING).get(1))) {

                    mState = State.MEETING;
                    mCallback.onBeginMeeting();
                }
                break;
            case MEETING:
                if (preLastWord.equals(getMapByLang().get(KeyWord.BEGIN_RECORDING).get(0))
                        && lastWord.equals(getMapByLang().get(KeyWord.BEGIN_RECORDING).get(1))) {
                    mState = State.RECORDING;
                    mCallback.onBeginRecording();
                    mTokens = new ArrayList<>();
                }
                if (preLastWord.equals(getMapByLang().get(KeyWord.END_MEETING).get(0))
                        && lastWord.equals(getMapByLang().get(KeyWord.END_MEETING).get(1))) {
                    mState = State.ENDED;
                    mCallback.onEndMeeting();
                }
                break;
            case RECORDING:
                if (preLastWord.equals(getMapByLang().get(KeyWord.END_RECORDING))
                        && lastWord.equals(getMapByLang().get(KeyWord.END_RECORDING).get(1))) {
                    mState = State.MEETING;
                    mCallback.onEndRecording(mTokens);
                    break;
                }
                mTokens.add(lastWord);
                break;
            case ENDED:
                break;


        }
    }

    private HashMap<KeyWord, List<String>> getMapByLang() {
        switch (mLanguage) {
            case CS_CZ:
                return czechKeyWords();
            case ENGLISH:
                return englishKeyWords();
        }
        return null;
    }



    private HashMap<KeyWord, List<String>> englishKeyWords() {
        HashMap<KeyWord, List<String>> hashMap = new HashMap<>();

        hashMap.put(KeyWord.BEGIN_MEETING, Arrays.asList("BEGIN", "MEETING"));
        hashMap.put(KeyWord.BEGIN_RECORDING, Arrays.asList("BEGIN", "RECORDING"));
        hashMap.put(KeyWord.END_RECORDING, Arrays.asList("END", "RECORDING"));
        hashMap.put(KeyWord.END_MEETING, Arrays.asList("END", "MEETING"));

        return hashMap;
    }

    private HashMap<KeyWord, List<String>> czechKeyWords() {
        HashMap<KeyWord, List<String>> hashMap = new HashMap<>();

        hashMap.put(KeyWord.BEGIN_MEETING, Arrays.asList("ZAČÁTEK", "PORADY"));
        hashMap.put(KeyWord.BEGIN_RECORDING, Arrays.asList("ZAČÁTEK", "ZÁPISU"));
        hashMap.put(KeyWord.END_RECORDING, Arrays.asList("KONEC", "ZÁPISU"));
        hashMap.put(KeyWord.END_MEETING, Arrays.asList("KONEC", "PORADY"));

        return hashMap;
    }


    public List<Memo> parse(List<String> text, Language language) {
        mLanguage = language;
        List<Memo> result = new ArrayList<>();

        boolean recording = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < text.size(); i++) {
            String lastWord = text.get(i-1);
            String preLastWord = text.get(i);

            if (recording && preLastWord.equals(getMapByLang().get(KeyWord.END_RECORDING))
                    && lastWord.equals(getMapByLang().get(KeyWord.END_RECORDING).get(1))) {
                recording = false;
                result.add(new Memo(null, sb.toString()));
                continue;
            }

            if (!recording && preLastWord.equals(getMapByLang().get(KeyWord.BEGIN_RECORDING).get(0))
                    && lastWord.equals(getMapByLang().get(KeyWord.BEGIN_RECORDING).get(1))) {
                recording = true;
                sb.setLength(0);
                sb = new StringBuilder();
                continue;
            }

            if (recording) {
                sb.append(lastWord);
            }

        }

        return result;
    }

    public enum State {
        READY,
        MEETING,
        RECORDING,
        ENDED
    }

    public enum KeyWord {
        BEGIN_MEETING,
        BEGIN_RECORDING,
        END_RECORDING,
        END_MEETING
    }

    public interface Callback {
        void onBeginMeeting();
        void onBeginRecording();
        void onEndRecording(List<String> tokens);
        void onEndMeeting();
    }


    public State getState() {
        return mState;
    }

}
