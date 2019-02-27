package com.example.asus.sinkeyboard;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class Skeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard shifted_keyboard;
    private Keyboard number_keyboard;
    private Keyboard symbol_keyboard;

    private boolean isCaps = false;



    @Override
    public View onCreateInputView() {
       kv =(KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
       keyboard = new Keyboard(this,R.xml.qwerty);
       shifted_keyboard = new Keyboard(this,R.xml.qwerty_shift);
       number_keyboard=new Keyboard(this,R.xml.numbers);
       symbol_keyboard =new Keyboard(this,R.xml.symbols);

       kv.setKeyboard(keyboard);
       kv.setOnKeyboardActionListener(this);
       return kv;

    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int i, int[] ints) {

        InputConnection ic=getCurrentInputConnection();
        playClick(i);
        switch (i)
        {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);
                break;
            case Keyboard.KEYCODE_SHIFT:

                isCaps=!isCaps;
                handleShift();
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_ALT:
                handleAlt();
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                handleModeChange();
                break;
            default:

                char code = (char)i;
                if (Character.isLetter(code) && isCaps)
                    code=Character.toUpperCase(code);
                Log.d("qq",String.valueOf(code));
                ic.commitText(String.valueOf(code),1);


        }
    }

    private void handleModeChange() {
        Keyboard currentKeyboard= kv.getKeyboard();
        if (currentKeyboard == keyboard || currentKeyboard == shifted_keyboard || currentKeyboard == symbol_keyboard){
            kv.setKeyboard(number_keyboard);
        }else{
            kv.setKeyboard(keyboard);
        }
    }

    private void handleAlt() {
        Keyboard currentKeyboard= kv.getKeyboard();
        if (currentKeyboard == symbol_keyboard){
            kv.setKeyboard(keyboard);
        }else if(currentKeyboard == number_keyboard){
            kv.setKeyboard(symbol_keyboard);
        }

    }

    private void handleShift(){
        if(isCaps){
            kv.setKeyboard(shifted_keyboard);

        }else{
            kv.setKeyboard(keyboard);


        }
    }

    private void playClick(int i) {

        AudioManager am= (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (i)
        {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);

        }

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
