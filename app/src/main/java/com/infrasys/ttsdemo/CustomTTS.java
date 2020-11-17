package com.infrasys.ttsdemo;

import android.speech.tts.SynthesisCallback;
import android.speech.tts.SynthesisRequest;
import android.speech.tts.TextToSpeechService;
import android.util.Log;

/**
 * @author Loki_Zhou
 * @Date 2020/11/13
 **/
public class CustomTTS extends TextToSpeechService {
    String Tag = "tag";

    @Override
    protected int onIsLanguageAvailable(String lang, String country, String variant) {
        return 1;
    }

    @Override
    protected String[] onGetLanguage() {
        return new String[0];
    }

    @Override
    protected int onLoadLanguage(String lang, String country, String variant) {
        Log.e(Tag, "onLoadLanguage: " + lang + " " + country + " " + variant);
        return 0;
    }

    @Override
    protected void onStop() {
        Log.e(Tag, "stop");
    }

    @Override
    protected void onSynthesizeText(SynthesisRequest request, SynthesisCallback callback) {
        Log.e(Tag, "onSynthesizeText: " + request.toString());
    }
}
