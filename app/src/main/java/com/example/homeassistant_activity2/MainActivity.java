package com.example.homeassistant_activity2;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

public class MainActivity extends AppCompatActivity{


    // MARK: Debug info
    private final String TAG="HOME_ASSISTANT";
    // MARK: Particle Account Info
    private final String USERNAME = "gurjit.babrah@gmail.com";
    private final String PASSWORD = "honeykaur123";
    // MARK: Particle device-specific info
    private final String DEVICE_ID = "317002f000f47363333343437";


    SpeechRecognizer SpeechRecognizer;
    private ParticleDevice mDevice;
    ImageView btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btnTwo);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceFromCloud();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightsOn();
            }
        });

        ParticleCloudSDK.init(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }


    public void getDeviceFromCloud(){
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Got device from cloud");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {

            }

            @Override
            public Object callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(USERNAME,PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);
                List<String> parameters = new ArrayList<>();
                parameters.add("SOS");
                try {
                    mDevice.callFunction("lightsOn",parameters);
                } catch (ParticleCloudException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParticleDevice.FunctionDoesNotExistException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
    }

    public void lightsOn(){
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Successfully got device from Cloud");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {

            }

            @Override
            public Object callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(USERNAME, PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);
                List<String> parameters = new ArrayList<>();
                parameters.add("on");
                try {

                    mDevice.callFunction("lightsOn",parameters);
                } catch (ParticleCloudException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParticleDevice.FunctionDoesNotExistException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });

    }

}

