package dam.android.u4t6contacts;

import android.content.ContentResolver;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.jar.Manifest;

public class U4T6ContactsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    ListView listViewContacts;
    TextView resultado;

    private static final int REQUEST_CONTACTS = 1;
    private static String[] PERMISSIONS_CONTACT = {android.Manifest.permission.READ_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u4_t6_contacts);
        setUI();
    }

    private void setUI() {
        listViewContacts = (ListView) findViewById(R.id.listViewContacts);
        resultado=(TextView)findViewById(R.id.resultado);
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    resultado.setText(listViewContacts.getItemAtPosition(position).toString());
            };

        });



        setListData();
    }

    private void setListData() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        } else {
            setListAdapter();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setListAdapter();

            } else {
                Toast.makeText(this, getString(R.string.contacts_read_right_required), Toast.LENGTH_LONG).show();

            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setListAdapter() {
        listViewContacts.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getContacts()));

    }


    private ArrayList<String> getContacts() {

        ArrayList<String> contactsList = new ArrayList<String>();

        ContentResolver contentResolver = getContentResolver();

        String[] projection = new String[]{
                ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE};

        String selectionFilter = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL ";

        Cursor contactsCursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selectionFilter,
                null,
                ContactsContract.Data.DISPLAY_NAME + " ASC");
        if (contactsCursor != null) {
            int nameIndex = contactsCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
            int numberIndex = contactsCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

            int id = contactsCursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
            int tipo = contactsCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE);

            while (contactsCursor.moveToNext()) {

                String name = contactsCursor.getString(nameIndex);
                String number = contactsCursor.getString(numberIndex);

                String idd = contactsCursor.getString(id);
                String tip = contactsCursor.getString(tipo);

                switch (tip) {
                    case "1":
                        tip = "HOME";
                        break;
                    case "2":
                        tip = "MOBILE";
                        break;
                    case "3":
                        tip = "WORK";
                        break;
                    default:
                        tip = "OTHER";
                        break;
                }

                contactsList.add(idd + " - " + name + " : " + number + "\n" + tip);

            }
            contactsCursor.close();
        }

        return contactsList;
    }


}
//




