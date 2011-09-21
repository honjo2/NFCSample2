package jp.co.mapion.android.nfcsample2;

import android.app.ListActivity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class TagTechViewActivity extends ListActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_list);

        onNewIntent(getIntent());
    }

    void ensureList(String[] techList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, techList);
        setListAdapter(adapter);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            ensureList(tag.getTechList());
        }
    }
}
