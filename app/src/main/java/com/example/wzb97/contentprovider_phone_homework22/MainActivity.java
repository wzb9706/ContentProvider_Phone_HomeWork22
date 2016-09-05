package com.example.wzb97.contentprovider_phone_homework22;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    ContentResolver resolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=(Button)findViewById(R.id.button);
        resolver=this.getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_DENIED){
                Log.i("", "onCreate: readerrror");
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
            }
            if(checkSelfPermission(Manifest.permission.WRITE_CONTACTS)== PackageManager.PERMISSION_DENIED){
                Log.i("", "onCreate: writeerror");
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_CONTACTS},2);
            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                Log.v("Read"," "+cursor.getColumnCount());
                while(cursor.moveToNext()){

                    String msg;
                    //id
                    String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    msg="id:"+id;

                    //name
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    msg=msg+" name:"+name;

                    //phone
                    Cursor phoneNumbers=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"="+id,null,null);
                    while(phoneNumbers.moveToNext()){
                        String phoneNumber=phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        msg=msg+" phone:"+phoneNumber;
                    }

                    //email
                    Cursor emails=resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID +"="+id,null,null);
                    while(emails.moveToNext()){
                        String email=emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        msg=msg+" email:"+email;
                    }

                    Log.v("ReadContact",msg);



                }

            }
        });
    }
}
