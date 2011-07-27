package jp.co.mapion.android.nfcsample2;

import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.preference.PreferenceManager;

public class NdefWriterSmartPosterActivity extends NdefWriterTextActivity {

	protected NdefMessage generateNdefMessage() {

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String text = settings.getString(
				NFCSample2Activity.NFC_SMARTPOSTER_TEXT, "");
		String uri = settings.getString(NFCSample2Activity.NFC_SMARTPOSTER_URI,
				"");

		NdefRecord record = NdefWriter.newSmartPosterRecord(
				NdefWriter.newTextRecord(text),
				NdefWriter.newUriRecord(Uri.parse(uri)));
		NdefMessage message = new NdefMessage(new NdefRecord[] { record });

		return message;
	}
}
