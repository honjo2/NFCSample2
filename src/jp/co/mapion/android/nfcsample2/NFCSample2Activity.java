package jp.co.mapion.android.nfcsample2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class NFCSample2Activity extends Activity {

	public static final String NFC_TEXT = "nfc_text";
	public static final String NFC_URI = "nfc_uri";
	public static final String NFC_SMARTPOSTER_TEXT = "nfc_smartposter_text";
	public static final String NFC_SMARTPOSTER_URI = "nfc_smartposter_uri";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
	}

	public void text(View view) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = settings.edit();
		final String text = ((EditText) findViewById(R.id.text_for_text))
				.getText().toString();
		editor.putString(NFC_TEXT, text);
		editor.commit();

		startActivity(new Intent(this, NdefWriterTextActivity.class));
	}

	public void uri(View view) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = settings.edit();
		final String text = ((EditText) findViewById(R.id.uri_for_uri))
				.getText().toString();
		editor.putString(NFC_URI, text);
		editor.commit();

		startActivity(new Intent(this, NdefWriterUriActivity.class));
	}

	public void smartposter(View view) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = settings.edit();
		final String text = ((EditText) findViewById(R.id.text_for_smartposter))
				.getText().toString();
		final String uri = ((EditText) findViewById(R.id.uri_for_smartposter))
				.getText().toString();
		editor.putString(NFC_SMARTPOSTER_TEXT, text);
		editor.putString(NFC_SMARTPOSTER_URI, uri);
		editor.commit();

		startActivity(new Intent(this, NdefWriterSmartPosterActivity.class));
	}

	public void read(View view) {
		startActivity(new Intent(this, NdefReaderActivity.class));
	}
}