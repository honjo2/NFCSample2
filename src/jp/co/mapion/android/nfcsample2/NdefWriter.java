package jp.co.mapion.android.nfcsample2;

import java.nio.charset.Charset;
import java.util.Locale;

import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import com.google.common.primitives.Bytes;

public final class NdefWriter {

	private NdefWriter() {

	}

	public static NdefRecord newTextRecord(String text) {
		byte[] langBytes = Locale.JAPANESE.getLanguage().getBytes(
				Charset.forName("US-ASCII"));

		byte[] textBytes = text.getBytes(Charset.forName("UTF-8"));

		char status = (char) langBytes.length;

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
				new byte[0], data);
	}

	public static NdefRecord newUriRecord(Uri uri) {
		byte[] uriBytes = uri.toString().getBytes(Charset.forName("UTF-8"));
		byte[] payload = Bytes.concat(new byte[] { 0x00 }, uriBytes);
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI,
				new byte[0], payload);
	}

	public static NdefRecord newSmartPosterRecord(NdefRecord textRecord,
			NdefRecord uriRecord) {
		NdefMessage posterMessage = new NdefMessage(new NdefRecord[] {
				textRecord, uriRecord });
		NdefRecord poster = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_SMART_POSTER, new byte[0],
				posterMessage.toByteArray());
		return poster;
	}
}
