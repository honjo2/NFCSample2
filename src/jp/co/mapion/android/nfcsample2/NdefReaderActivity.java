package jp.co.mapion.android.nfcsample2;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

public class NdefReaderActivity extends Activity {

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
			readNdefMessage(intent);
		}
	}

	private void readNdefMessage(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage[] msgs = null;
		if (rawMsgs != null) {
			msgs = new NdefMessage[rawMsgs.length];
			for (int i = 0; i < rawMsgs.length; i++) {
				msgs[i] = (NdefMessage) rawMsgs[i];
			}
			for (NdefMessage msg : msgs) {
				List<ParsedNdefRecord> prec = NdefMessageParser.parse(msg);
				toast(prec.toString());
			}
		}
	}

	private void toast(String value) {
		Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
	}
}
