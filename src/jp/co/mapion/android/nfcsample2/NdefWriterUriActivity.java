package jp.co.mapion.android.nfcsample2;

import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.preference.PreferenceManager;

public class NdefWriterUriActivity extends NdefWriterTextActivity {

	protected NdefMessage generateNdefMessage() {

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String uri = settings.getString(NFCSample2Activity.NFC_URI, "");

		NdefRecord record = NdefWriter.newUriRecord(Uri.parse(uri));
		NdefMessage message = new NdefMessage(new NdefRecord[] { record });

		return message;
	}
}
