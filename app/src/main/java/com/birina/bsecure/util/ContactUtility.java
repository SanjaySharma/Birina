package com.example.bsecure.util;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.example.bsecure.backup.model.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// new ContactUtility(this).fetchAll();
public class ContactUtility {

    private final Context mContext;

    public ContactUtility(Context c) {
        this.mContext = c;
    }

    public ArrayList<Request.ContactBean> fetchAll() {

        ArrayList<Request.ContactBean> listContacts = new ArrayList<>();
        Cursor c;
        try{
        String[] projectionFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };

        CursorLoader cursorLoader = new CursorLoader(mContext,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields, // the columns to retrieve
                null, // the selection criteria (none)
                null, // the selection args (none)
                null // the sort order (default)
        );

         c = cursorLoader.loadInBackground();

        final Map<String, Request.ContactBean> contactsMap = new HashMap<>(c.getCount());

        if (c.moveToFirst()) {

            int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            do {
                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                Request.ContactBean contact = new Request.ContactBean(contactId, contactDisplayName);
                contactsMap.put(contactId, contact);
                listContacts.add(contact);
            } while (c.moveToNext());
        }

        c.close();

        matchContactNumbers(contactsMap);
        matchContactEmails(contactsMap);

        return listContacts;
        }catch (Exception e){
            Log.d(Constant.TAG_RESTORE, "Exception in fetchAll " + e);

        }finally {
            c = null;
        }

        return listContacts;
    }

    public void matchContactNumbers(Map<String, Request.ContactBean> contactsMap) {
        // Get numbers
        Cursor phone;
        try{
        final String[] numberProjection = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

         phone = new CursorLoader(mContext,
                Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(Phone.CONTACT_ID);

            while (!phone.isAfterLast()) {
                final String number = phone.getString(contactNumberColumnIndex);
                final String contactId = phone.getString(contactIdColumnIndex);
                if(number.equals(number)){
                    int count=phone.getCount();
                }
                Request.ContactBean contact = contactsMap.get(contactId);
                if (contact == null) {
                    Log.i("tag",""+contactId);
                    continue;
                }
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = Phone.getTypeLabel
                        (mContext.getResources(), type, customLabel);
                contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }
        }

        phone.close();

    }catch (Exception e){
        Log.d(Constant.TAG_RESTORE, "Exception in matchContactNumbers " + e);

    }finally {
        phone = null;
        }
    }

    public void matchContactEmails(Map<String, Request.ContactBean> contactsMap) {
        // Get email
        Cursor email;
        try {
            final String[] emailProjection = new String[]{
                    Email.DATA,
                    Email.TYPE,
                    Email.CONTACT_ID,
            };

             email = new CursorLoader(mContext,
                    Email.CONTENT_URI,
                    emailProjection,
                    null,
                    null,
                    null).loadInBackground();

            if (email.moveToFirst()) {
                final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
                final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);
                final int contactIdColumnsIndex = email.getColumnIndex(Email.CONTACT_ID);

                while (!email.isAfterLast()) {
                    final String address = email.getString(contactEmailColumnIndex);
                    final String contactId = email.getString(contactIdColumnsIndex);
                    final int type = email.getInt(contactTypeColumnIndex);
                    String customLabel = "Custom";
                    Request.ContactBean contact = contactsMap.get(contactId);
                    if (contact == null) {
                        continue;
                    }
                    CharSequence emailType = Email.getTypeLabel(mContext.getResources(), type, customLabel);
                    contact.addEmail(address, emailType.toString());
                    email.moveToNext();
                }
            }

            email.close();
        }catch (Exception e){
            Log.d(Constant.TAG_RESTORE, "Exception in matchContactEmails " + e);

        }finally {
            email = null;
        }
    }





    public void testBatchInsertion1(List<Request.ContactBean> contacts) throws RemoteException, OperationApplicationException {
        long startTime = System.currentTimeMillis();
        Log.d("BatchInsertionTest", "Starting batch insertion on: " + new Date(startTime));


        final int MAX_OPERATIONS_FOR_INSERTION = 100; //100
        int size = contacts.size();

            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            for (int i = 0; i < size; i++) {

                generateSampleProviderOperation2(ops, contacts.get(i));
                if (ops.size() >= MAX_OPERATIONS_FOR_INSERTION) {
                    mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    ops.clear();
                }
            }
            if (ops.size() > 0)
                mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d("BatchInsertionTest", "End of batch insertion, elapsed: " + new Date(System.currentTimeMillis()));

    }


    private void generateSampleProviderOperation2(ArrayList<ContentProviderOperation> ops,
                                                  Request.ContactBean contact){
        int backReference = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.AGGREGATION_MODE, ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
                .build()
        );
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                   // .withYieldAllowed(true)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backReference)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getName())
                    .build());

            if (contact.getNumbers() != null && contact.getNumbers().size() > 0) {
                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        //.withYieldAllowed(true)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backReference)
                        .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                        .withValue(Phone.NUMBER, contact.getNumbers().get(0).getNumber())
                        .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                        .build());
                if (contact.getNumbers().size() > 1) {
                    // Adding insert operation to operations list
                    // to  insert Home Phone Number in the table ContactsContract.Data
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            //.withYieldAllowed(true)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backReference)
                            .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                            .withValue(Phone.NUMBER, contact.getNumbers().get(1).getNumber())
                            .withValue(Phone.TYPE, Phone.TYPE_HOME)
                            .build());
                }

            }
            if (contact.getEmails() != null && contact.getEmails().size() > 0) {
                // Adding insert operation to operations list
                // to insert Work Email in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                       // .withYieldAllowed(true)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backReference)
                        .withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                        .withValue(Email.ADDRESS, contact.getEmails().get(0).getAddress())
                        .withValue(Email.TYPE, Email.TYPE_WORK)
                        .build());
            }
            if (contact.getEmails().size() > 1) {
                // Adding insert operation to operations list
                // to insert Home Email in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                       // .withYieldAllowed(true)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, backReference)
                        .withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                        .withValue(Email.ADDRESS, contact.getEmails().get(1).getAddress())
                        .withValue(Email.TYPE, Email.TYPE_HOME)
                        .build());
            }

            /*Log.d(Constant.TAG_RESTORE, "Contact  Name: " + contact.getName() + ", Number:" + contact.getNumbers()
                    + " Email: " + contact.getEmails());*/
    }



    public void deleteContact(){

        Log.d(Constant.TAG_RESTORE, "Start of deleteContact: " + new Date(System.currentTimeMillis()));

        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
            contentResolver.delete(uri, null, null);
        }

        Log.d(Constant.TAG_RESTORE, "End of deleteContact: " + new Date(System.currentTimeMillis()));

    }

    public void deleteSms(){

        Uri inboxUri = Uri.parse("content://sms/inbox");
        int count = 0;
        Cursor c = mContext.getContentResolver().query(inboxUri , null, null, null, null);
        while (c.moveToNext()) {
            try {
                // Delete the SMS
                String pid = c.getString(0); // Get id;
                String uri = "content://sms/" + pid;
                count = mContext.getContentResolver().delete(Uri.parse(uri),
                        null, null);
            } catch (Exception e) {
            }
        }
        c.close();

        Uri sentUri = Uri.parse("content://sms/sent");
        int sentCount = 0;
        Cursor sentc = mContext.getContentResolver().query(sentUri , null, null, null, null);
        while (sentc.moveToNext()) {
            try {
                // Delete the SMS
                String pid = sentc.getString(0); // Get id;
                String uri = "content://sms/" + pid;
                sentCount = mContext.getContentResolver().delete(Uri.parse(uri),
                        null, null);
            } catch (Exception e) {
            }
        }
        sentc.close();


        Log.d(Constant.TAG_RESTORE, "Total deleted sent sms: " +sentCount+" inbox "+count);

    }

/*    fun saveSms(smsBeanList: List<Request.SmsBean>?){
        try {

            Log.d(Constant.TAG_RESTORE, "Enter in saveSms of " + TAG)

            smsBeanList?.forEach {


                if( ( null != it.address && it.address.isNotEmpty()) &&
                        ( null != it.msg && it.msg.isNotEmpty()) &&
                        ( null != it.time && it.time.isNotEmpty() ) ){


                    Log.d(Constant.TAG_RESTORE, "address: " + it.address + " body: " + it.msg + "\n read: " + it.readState
                            + "time: " + it.time)


                val values = ContentValues()
                values.put("address", it.address)
                values.put("body", it.msg)
                values.put("read", it.readState) //"0" for have not read sms and "1" for have read sms
                values.put("date", it.time)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    var uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Telephony.Sms.Sent.CONTENT_URI
                    } else {
                        applicationContext.contentResolver.insert(Uri.parse("content://sms/" + it.folderName), values)
                    }
                    if (it.folderName == "inbox") {
                        uri = Telephony.Sms.Inbox.CONTENT_URI
                    }
                    applicationContext.contentResolver.insert(uri, values)
                } else {
                    applicationContext.contentResolver.insert(Uri.parse("content://sms/" + it.folderName), values)
                }
            }
            }

            Log.d(Constant.TAG_RESTORE, "Exit from saveSms of " + TAG)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }*/


  /*  fun  saveSms(smsBeans: List<Request.SmsBean>?):Boolean {
        try {
            Log.d(Constant.TAG_RESTORE, "Enter in saveSms of " + TAG)

            val contentValues = arrayOfNulls<ContentValues>(smsBeans?.size!!)
            var uri: Uri? = null

            for (i in smsBeans?.indices!!) {

                if( ( null != smsBeans[i].address && smsBeans[i].address.isNotEmpty()) &&
                        ( null != smsBeans[i].msg && smsBeans[i].msg.isNotEmpty()) &&
                        ( null != smsBeans[i].time && smsBeans[i].time.isNotEmpty() ) ){

                Log.d(Constant.TAG_RESTORE, "SMS  address: " + smsBeans[i].address + " body: " + smsBeans[i].msg + "\n read: " + smsBeans[i].readState
                        + "time: " + smsBeans[i].time)

                val values = ContentValues()
                values.put("address", smsBeans[i].address)
                values.put("body", smsBeans[i].msg)
                values.put("read", smsBeans[i].readState) //"0" for have not read sms and "1" for have read sms
                values.put("date", smsBeans[i].time)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uri = Telephony.Sms.Sent.CONTENT_URI
                    if (smsBeans[i].folderName == "inbox") {
                        uri = Telephony.Sms.Inbox.CONTENT_URI
                    }
                } else {
                    uri = Uri.parse("content://sms/" + smsBeans[i].folderName)
                }

                contentValues[i] = values
            }
       }


            applicationContext.contentResolver.bulkInsert(uri!!, contentValues)

            Log.d(Constant.TAG_RESTORE, "Exit from saveSms of " + TAG)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
         return true;
    }
*/




/*
    public void insertContacts(List<Request.ContactBean> contacts){


        for ( Request.ContactBean contact : contacts) {


            ArrayList<ContentProviderOperation> ops =
                    new ArrayList<ContentProviderOperation>();

            int rawContactID = ops.size();

            // Adding insert operation to operations list
            // to insert a new raw contact in the table ContactsContract.RawContacts
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withYieldAllowed(true)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            // Adding insert operation to operations list
            // to insert display name in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withYieldAllowed(true)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .build());

            if (contact.getNumbers() != null && contact.getNumbers().size() > 0) {
                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withYieldAllowed(true)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                        .withValue(Phone.NUMBER, contact.getNumbers().get(0).getNumber())
                        .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                        .build());
                if (contact.getNumbers().size() > 1) {
                    // Adding insert operation to operations list
                    // to  insert Home Phone Number in the table ContactsContract.Data
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withYieldAllowed(true)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                            .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                            .withValue(Phone.NUMBER, contact.getNumbers().get(1).getNumber())
                            .withValue(Phone.TYPE, Phone.TYPE_HOME)
                            .build());
                }

            }
            if (contact.getEmails() != null && contact.getEmails().size() > 0) {
                // Adding insert operation to operations list
                // to insert Work Email in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withYieldAllowed(true)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                        .withValue(Email.ADDRESS, contact.getEmails().get(0).getAddress())
                        .withValue(Email.TYPE, Email.TYPE_WORK)
                        .build());
            }
            if (contact.getEmails().size() > 1) {
                // Adding insert operation to operations list
                // to insert Home Email in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withYieldAllowed(true)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                        .withValue(Email.ADDRESS, contact.getEmails().get(1).getAddress())
                        .withValue(Email.TYPE, Email.TYPE_HOME)
                        .build());
            }

            Log.d(Constant.TAG_RESTORE, "Contact  Name: " + contact.getName() + ", Number:" + contact.getNumbers()
                    + " Email: " + contact.getEmails());

            try {
                // Executing all the insert operations as a single database transaction
                mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }
    }*/


}
