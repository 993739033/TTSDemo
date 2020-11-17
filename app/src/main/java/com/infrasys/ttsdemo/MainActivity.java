package com.infrasys.ttsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infrasys.ttsdemo.yzs.TTSConfig;
import com.infrasys.ttsdemo.yzs.TTSUtil;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String mFrontendModel = TTSUtil.ttsPath + TTSUtil.frontFileName;
    private final String mBackendModel = TTSUtil.ttsPath + TTSUtil.backFileName;

    private SpeechSynthesizer mTTSPlayer;
    EditText et_speech;
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TTSUtil.initTTSFiles(MainActivity.this, new TTSUtil.CallBack() {
            @Override
            public void call() {
                initTts();
            }
        });
        final TextToSpeech mSpeech = new TextToSpeech(MainActivity.this, new TTSListener());
        mSpeech.setLanguage(Locale.CHINA);
        mSpeech.setSpeechRate(1.0f);
        mSpeech.setPitch(1.0f);
        et_speech = findViewById(R.id.et_speech);
        btn_start = findViewById(R.id.btn_start);
//        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
//                mSpeech.speak("A1",
//                        TextToSpeech.QUEUE_ADD, null, null);
//            }
//        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSPlay();
            }
        });
    }

    private class TTSListener implements TextToSpeech.OnInitListener {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
            } else {
            }
        }
    }

    /**
     * 初始化本地离线TTS
     */
    private void initTts() {
        // 初始化语音合成对象
        mTTSPlayer = new SpeechSynthesizer(this, TTSConfig.appKey, TTSConfig.secret);
        // 设置本地合成
        mTTSPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL);
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, 50);//设置语速
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_PITCH, 50);//设置音高
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, 50);//设置音量
        File _FrontendModelFile = new File(mFrontendModel);
        if (!_FrontendModelFile.exists()) {
            showToast("文件：" + mFrontendModel + "不存在，请将assets下相关文件拷贝到SD卡指定目录！");
        }
        File _BackendModelFile = new File(mBackendModel);
        if (!_BackendModelFile.exists()) {
            showToast("文件：" + mBackendModel + "不存在，请将assets下相关文件拷贝到SD卡指定目录！");
        }
        // 设置前端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, mFrontendModel);
        // 设置后端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, mBackendModel);
        // 设置回调监听
        mTTSPlayer.setTTSListener(new SpeechSynthesizerListener() {

            @Override
            public void onEvent(int type) {
                switch (type) {
                    case SpeechConstants.TTS_EVENT_INIT:
                        // 初始化成功回调
                        log_i("onInitFinish");
                        btn_start.setEnabled(true);
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
                        // 开始合成回调
                        log_i("beginSynthesizer");
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
                        // 合成结束回调
                        log_i("endSynthesizer");
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
                        // 开始缓存回调
                        log_i("beginBuffer");
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_READY:
                        // 缓存完毕回调
                        log_i("bufferReady");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_START:
                        // 开始播放回调
                        log_i("onPlayBegin");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_END:
                        // 播放完成回调
                        log_i("onPlayEnd");
                        break;
                    case SpeechConstants.TTS_EVENT_PAUSE:
                        // 暂停回调
                        log_i("pause");
                        break;
                    case SpeechConstants.TTS_EVENT_RESUME:
                        // 恢复回调
                        log_i("resume");
                        break;
                    case SpeechConstants.TTS_EVENT_STOP:
                        // 停止回调
                        log_i("stop");
                        break;
                    case SpeechConstants.TTS_EVENT_RELEASE:
                        // 释放资源回调
                        log_i("release");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(int type, String errorMSG) {
                // 语音合成错误回调
                log_i("onError");
                showToast(errorMSG);
            }
        });
        // 初始化合成引擎
        mTTSPlayer.init("");
    }

    private void TTSPlay() {
        mTTSPlayer.playText(et_speech.getText().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        // 主动停止识别
        if (mTTSPlayer != null) {
            mTTSPlayer.stop();
        }
    }


    @Override
    protected void onDestroy() {
        // 主动释放离线引擎
        if (mTTSPlayer != null) {
            mTTSPlayer.release(SpeechConstants.TTS_RELEASE_ENGINE, null);
        }
        super.onDestroy();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    String tag = "speech";
    private void log_i(String text) {
        Log.i(tag, text);
    }
}
