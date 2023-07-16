package com.example.composejava.Ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.composejava.R;
import com.example.composejava.Utils.Helper;
import com.example.composejava.databinding.ActivityCallBinding;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;

public class CallActivity extends AppCompatActivity {
    private ActivityCallBinding binding;
    private final String SERVER_URL = "https://meet.jit.si";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.joinBtn.setOnClickListener(v->{
            String meetingLink = binding.meetingIdEt.getText().toString();
            if(meetingLink.isEmpty()){
                Toast.makeText(CallActivity.this, "Please input the meeting link.", Toast.LENGTH_SHORT).show();
            }else{
                JitsiMeetConferenceOptions options = joinVideoSession(meetingLink);
                try{
                    JitsiMeetActivity.launch(CallActivity.this,options);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        binding.newMeetingBtn.setOnClickListener(v->{
            String meetingLink = Helper.randomCodeGenerator();

                Dialog customDialog  = new Dialog(CallActivity.this);
                customDialog.setContentView(R.layout.todo_custom_dialog_box);

                Button shareBtn = customDialog.findViewById(R.id.share_dialog_btn);
                Button joinBtn = customDialog.findViewById(R.id.join_dialog_btn);
                TextView dialog_tv = customDialog.findViewById(R.id.meetingLingDialog_tv);
                dialog_tv.setText(meetingLink);

                dialog_tv.setOnLongClickListener(v3->{

                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("copiedData",meetingLink);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(CallActivity.this,"Copied",Toast.LENGTH_SHORT)
                            .show();
                    return true;
                });

                String targetLink = "Meeting Link : "+meetingLink;
                shareBtn.setOnClickListener(v1->{
                    shareLinkUtil(targetLink);
                });

                joinBtn.setOnClickListener(v2->{
                    customDialog.dismiss();
                    //navigateToVideoActivity(meetingLink);

                    JitsiMeetConferenceOptions options = joinVideoSession(meetingLink);
                    try{
                        JitsiMeetActivity.launch(CallActivity.this,options);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });

                customDialog.show();
                Log.d("dialog", "onCreate: Here's the new meeting link "+targetLink);
        });

    }



    private JitsiMeetConferenceOptions joinVideoSession(String meetingLink) {
        JitsiMeetConferenceOptions options = null;
        JitsiMeetUserInfo userInformation = null;
        try{
            URL target_url = new URL(SERVER_URL);
            userInformation = new JitsiMeetUserInfo();
            userInformation.setDisplayName("Humans");
            userInformation.setEmail("dummy123@gmail.com");
             options = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(target_url)
                            .setRoom(meetingLink)
                            .setAudioMuted(true)
                            .setVideoMuted(true)
                            .setUserInfo(userInformation)
                            .setConfigOverride("requireDisplayName",true)
                            .build();

        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return options;

    }

/*
    private void navigateToVideoActivity(String meetingLink){
        Intent intent = new Intent(CallActivity.this,VideoActivity.class);
        intent.putExtra("link",meetingLink);
        intent.putExtra("SERVER_URL",SERVER_URL);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }*/
    private void shareLinkUtil(String meetingLink) {
        Intent shareLinkIntent = new Intent(Intent.ACTION_SEND);
        shareLinkIntent.setType("text/plain");
        shareLinkIntent.putExtra(Intent.EXTRA_TEXT,meetingLink);
        startActivity(Intent.createChooser(shareLinkIntent,"Shared-Link-Intent"));
    }
}