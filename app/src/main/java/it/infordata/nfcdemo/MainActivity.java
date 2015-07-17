package it.infordata.nfcdemo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends Activity

{
    private static final String TAG = "MainActivity" ;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilter;
    private String[][] techListsArray;






    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);



        //Variabili per l'nfc
        this.nfcAdapter = NfcAdapter.getDefaultAdapter (this);
        this.pendingIntent = PendingIntent.getActivity (this, 0, new Intent(this, getClass ()).addFlags (Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        this.intentFilter = new IntentFilter[]{new IntentFilter (NfcAdapter.ACTION_TAG_DISCOVERED)};
        this.techListsArray = new String[][] { new String[] { MifareClassic.class.getName() } };
    }

    @Override
    protected void onNewIntent (Intent intent) {
        //Leggo il tag
        Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        MifareClassic tesseraMifare = MifareClassic.get(tag);
        try {

            tesseraMifare.connect();
            byte[] keyb = {(byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00
            };

		 				/*
		 				 * Chiave A utilizzata per le tessere Mifare
		 				 */
            byte[] keya = {(byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00,
                    (byte) 0x00};


            tesseraMifare.authenticateSectorWithKeyA(0, keya);

            // ottengo un bytearray contenente i dati del blocco 2
            byte[] codice = tesseraMifare.readBlock(2);

            tesseraMifare.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


        @Override
            protected void onResume ()
            {
                super.onResume ();
                this.nfcAdapter.enableForegroundDispatch (this, pendingIntent, intentFilter, techListsArray);
            }

            @Override
            protected void onPause ()
            {
                super.onPause ();
                this.nfcAdapter.disableForegroundDispatch (this);
            }

        }
