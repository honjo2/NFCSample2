package jp.co.mapion.android.nfcsample2;

import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class NdefWriterTextActivity extends Activity {

	private NfcAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdapter = NfcAdapter.getDefaultAdapter(this);

		onNewIntent(getIntent());
	}

	@Override
	public void onResume() {
		super.onResume();
		enableForegroundDispatch();
	}

	@Override
	public void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}

	public void enableForegroundDispatch() {
		IntentFilter[] filters = makeFilter();
		String[][] techLists = makeTechLists();
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()), 0);
		mAdapter.enableForegroundDispatch(this, pendingIntent, filters,
				techLists);
	}

	private IntentFilter[] makeFilter() {
		IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		IntentFilter[] filters = new IntentFilter[] { tech, tech };
		return filters;
	}

	private String[][] makeTechLists() {
		String[] ndef = new String[] { Ndef.class.getName() };
		String[] ndefFormatable = new String[] { NdefFormatable.class.getName() };
		String[][] techLists = new String[][] { ndef, ndefFormatable };
		return techLists;
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			writeNdefMessage(tag);
		}
	}

	protected NdefMessage generateNdefMessage() {

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String text = settings.getString(NFCSample2Activity.NFC_TEXT, "");

		NdefRecord record = NdefWriter.newTextRecord(text);
		NdefMessage message = new NdefMessage(new NdefRecord[] { record });

		return message;
	}

	void writeNdefMessage(Tag tag) {

		NdefMessage message = generateNdefMessage();

		try {
			if (Arrays.asList(tag.getTechList()).contains(
					NdefFormatable.class.getName())) {

				NdefFormatable ndef = NdefFormatable.get(tag);
				try {
					if (!ndef.isConnected()) {
						ndef.connect();
					}
					ndef.format(message);
					showSuccessToast();
				} finally {
					ndef.close();
				}
			} else if (Arrays.asList(tag.getTechList()).contains(
					Ndef.class.getName())) {

				Ndef ndef = Ndef.get(tag);
				try {
					if (!ndef.isConnected()) {
						ndef.connect();
					}
					if (ndef.isWritable()) {
						ndef.writeNdefMessage(message);
						showSuccessToast();
					} else {
						showNotWritableToast();
					}
				} finally {
					ndef.close();
				}
			}
		} catch (FormatException e) {
			showFailureToast();
		} catch (IOException e) {
			showFailureToast();
		}
	}

	private void showSuccessToast() {
		Toast.makeText(this, "書込成功", Toast.LENGTH_SHORT).show();
	}

	void showFailureToast() {
		Toast.makeText(this, "書込失敗", Toast.LENGTH_SHORT).show();
	}

	void showNotWritableToast() {
		Toast.makeText(this, "書込不可", Toast.LENGTH_SHORT).show();
	}
}
