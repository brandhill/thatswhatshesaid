package com.venkatesh.ThatsWhatSheSaid;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.venkatesh.ThatsWhatSheSaid.R;

public class MainActivity extends Activity {

	private String[] catchPhrases = {"fuck this", "action", "spot", "function", "grease", "" +
			"knob", "juice", "fold", "lick", "rear", "frolic", "junk", "enter", "pole", 
			"lips",	"hole", "dick", "pork", "hang", "beat", "squeeze", "sack", "seep",
			"come", "coming", "under-carriage", "gross", "canal", "leak", "prick", "stilt",
			"sin hole", "slit", "balls", "swallow", "taste", "tongue", "dangle", "jam", 
			"slam", "dip", "slurp", "stroke", "drip", "pant", "pants", "snatch", 
			"grope", "huge", "ram", "soak", "bottom", "wet", "moist", "tight", "gobble",
			"felt", "ripe", "fluid", "hard", "put it in", "long", "rigid", "erect", 
			"small mouth", "nuts", "big", "blow", "do it", "stiff", "melon", "wood", 
			"banana", "pull it out", "small", "slip out", "slip off", "penetrate", 
			"penetrating", "sixty nine", "69", "pulled it out", "go faster", "swallow",
			"jumps up", "in the mouth", "hole", "too small", "too big", "put it on", 
			"get it out", "came"};
	protected static final int RESULT_SPEECH = 1;

	private ImageButton btnSpeak;
	private TextView txtText;
	private Intent intent = new Intent(
			RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	private AudioManager mAudioManager ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtText = (TextView) findViewById(R.id.txtText);
		mAudioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		
		listen();

	}

	public void listen() {
		mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
		try {
			startActivityForResult(intent, RESULT_SPEECH);
			txtText.setText("");
		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Oops! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				String sample = text.get(0);

				for(String s: catchPhrases) {
					if(sample.contains(s)) {
						txtText.setText("That's what she said!");
						mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
						try {
							playRecording();
							Thread.currentThread();
							Thread.sleep(2000);
							listen();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}	else {
						//listen();
					}
				}

			}
			break;
		}

		}
		listen();	
	}

	private void playRecording() throws IllegalArgumentException, IllegalStateException, IOException {
		AssetFileDescriptor afd = getAssets().openFd("twss.mp3");
		MediaPlayer player = new MediaPlayer();
		player.setDataSource(afd.getFileDescriptor());
		player.prepare();
		player.start();
	}
}
