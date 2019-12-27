package com.swavlambibharat.myenglishvocab;
/*  ======================================================================================================================================================*/
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*  ======================================================================================================================================================*/
public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    ClipboardManager mClipboard;
    ListView listview;
    Button clear, add;
    ArrayAdapter<SpannableString> arrayAdapter;
    ArrayList<SpannableString> arrayList;
    validf valid;
    handlef handler;
    SpannableString ss;
    StyleSpan bold_italic=new StyleSpan(Typeface.BOLD_ITALIC);
    static boolean bHasClipChangedListener = false;
    String word,url="https://www.dictionary.com/browse/";
/*---------------------------------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();
        valid = new validf();
        handler=new handlef();
        myDB = new DatabaseHelper(this);
        listview = findViewById(R.id.ttext);
        clear = findViewById(R.id.button2);
        add = findViewById(R.id.button3);


        Cursor res = myDB.getAlldata();
        while (res.moveToNext()){
        ss=new SpannableString(res.getString(0));
        ss.setSpan(bold_italic,0,res.getString(0).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        arrayList.add(ss);
        }
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listview.setAdapter(arrayAdapter);
        mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        registerPrimaryClipChanged();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                word=listview.getItemAtPosition(i).toString().trim();
                url=url+word;
                handler.progressDialog = new ProgressDialog(MainActivity.this);
                handler.progressDialog.setMax(100);
                handler.progressDialog.setMessage("Loading....");
                handler.progressDialog.setTitle(word);
                handler.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                handler.progressDialog.show();
                handler.progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (handler.progressDialog.getProgress() <= handler.progressDialog
                                    .getMax()) {
                                Thread.sleep(30);
                                handler.handle.sendMessage(handler.handle.obtainMessage());
                                if (handler.progressDialog.getProgress() == handler.progressDialog
                                        .getMax()) {
                                    handler.progressDialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                new doit().execute();
            }


        });


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor res = myDB.RWAlldata();
                res.moveToPosition(i);
                String x=res.getString(1);
                String deleted_data=res.getString(0);
                int key=Integer.parseInt(x);
                myDB.deleteSelectedData(key);
                Cursor rest = myDB.getAlldata();
                arrayList.clear();
                while (rest.moveToNext()) {
                    ss=new SpannableString(rest.getString(0));
                    ss.setSpan(bold_italic,0,rest.getString(0).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    arrayList.add(ss);
                    arrayAdapter.notifyDataSetChanged();
                }
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Deleted "+deleted_data, Toast.LENGTH_SHORT).show();

                return true;
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB.deleteAlldata();
                Cursor deleted = myDB.getAlldata();
                arrayList.clear();
                while (deleted.moveToNext()) {
                    ss=new SpannableString(deleted.getString(0));
                    ss.setSpan(bold_italic,0,deleted.getString(0).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    arrayList.add(ss);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add A Word");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor rest = myDB.getAlldata();
                        boolean exist = false, isInserted = false;
                        String text = input.getText().toString().trim().toUpperCase();
                        if (valid.validate(text)) {
                            while (rest.moveToNext()) {
                                if (rest.getString(0).equals(text))
                                    exist = true;
                                if (exist)
                                    break;
                        }
                            if (!exist)
                                isInserted = myDB.insertData(text);
                            if (isInserted) {
                                arrayList.clear();
                                arrayAdapter.notifyDataSetChanged();
                                rest = myDB.getAlldata();
                                while (rest.moveToNext()) {
                                    ss=new SpannableString(rest.getString(0));
                                    ss.setSpan(bold_italic,0,rest.getString(0).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    arrayList.add(ss);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterPrimaryClipChanged();
    }
    ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            updateClipData();
        }
    };
    /*---------------------------------------------------------------------------------------------------------------------------------------------*/
void updateClipData() {
        ClipData clip = mClipboard.getPrimaryClip();
        if (clip!=null) {
            ClipData.Item item = clip.getItemAt(0);
            String text=item.coerceToText(this).toString().trim().toUpperCase();
            if(item.getText()!=null) {
                if (valid.validate(text)) {
                    boolean isInserted = myDB.insertData(text);
                    Cursor rest = myDB.getAlldata();
                    if (isInserted) {
                        arrayList.clear();
                        arrayAdapter.notifyDataSetChanged();
                        while (rest.moveToNext()) {
                            ss=new SpannableString(rest.getString(0));
                            ss.setSpan(bold_italic,0,rest.getString(0).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            arrayList.add(ss);
                            arrayAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(MainActivity.this, "Added a New Word To Your Vocabulary", Toast.LENGTH_SHORT).show();
                    }
                }
                startPrimaryClipChangedListenerDelayThread();
            }
        }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------*/
    void startPrimaryClipChangedListenerDelayThread() {
        mClipboard.removePrimaryClipChangedListener(mPrimaryChangeListener);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClipboard.addPrimaryClipChangedListener(mPrimaryChangeListener);
            }
        }, 500);
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------*/
    private void registerPrimaryClipChanged() {
        if (!bHasClipChangedListener) {
            mClipboard.addPrimaryClipChangedListener(mPrimaryChangeListener);
            bHasClipChangedListener = true;
        }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------*/
    private void unregisterPrimaryClipChanged() {
        if (bHasClipChangedListener) {
            mClipboard.removePrimaryClipChangedListener(mPrimaryChangeListener);
            bHasClipChangedListener = false;
        }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------*/
    class doit extends AsyncTask<Void,Void,Void>{
    String form="",data="";
        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }
        private boolean isInternetAvailable() {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com");
                return !ipAddr.equals("");

            } catch (Exception e) {
                return false;
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
        try{
            if(isInternetAvailable()) {
                Document document = Jsoup.connect(url).timeout(5000).get();
                Elements links = document.getElementsByClass("css-1gxch3 e1hk9ate2");
                Elements Tlinks = document.getElementsByClass("css-1o58fj8 e1hk9ate4");
                form = links.first().text() + "\n--------------------------------------------------------\n";
                for (int valuev = 1; valuev <= Tlinks.first().getElementsByAttribute("value").size(); valuev++) {
                    form = form + valuev + " -> " + (Tlinks.first().getElementsByAttributeValueMatching("value", Integer.toString(valuev)).text()) + "\n--------------------------------------------------------\n";
                }
            }
            else if(!isNetworkConnected())
                form="\n\n\n\n\n\n\n\n\n\n\n\n\nMobile Data is turned off and your Device is not connected to any Wi-fi.".toUpperCase();
            else
                form="Internet not available";

/*            int k=1;
            for(int n = 0; n < links.size(); n++) {
                if(Tlinks.get(n).getElementsByAttributeValueMatching("value", Integer.toString(k)).text().isEmpty()){}
                else{
                    if (form=="")
                        form=form+(links.get(n).text())+"\n";
                    else
                        form=form+"\n\n"+(links.get(n).text())+"\n";
                }
                int s=1;
                while (s <= Tlinks.get(n).getElementsByAttribute("value").size()) {
                    if(Tlinks.get(n).getElementsByAttributeValueMatching("value", Integer.toString(k)).text().isEmpty()){

                    }
                    else
                    form = form + k + "-> " + (Tlinks.get(n).getElementsByAttributeValueMatching("value", Integer.toString(k)).text()) + "\n--------------------------------------------------------\n";
                    s++;
                    k++;
                }
            }*/

            data=form+"\n\n";
        }
        catch(Exception e)
            {e.printStackTrace();}
            return null;
        }
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

            Intent intent = new Intent ( MainActivity.this, OnItemClick.class );

            if(word!=null && word.length()>0){
                intent.putExtra("Word",word);
            }
            else {intent.putExtra("Meaning","\n\n\n\n\n\n\n\n\n\n\n\n\n Please check the word again, it might have been misspelled");
            }

            if(data!=null && data.length()>0){
                intent.putExtra("Meaning",data);
            }
            else intent.putExtra("Meaning","\n\n\n\n\n\n\n\n\n\n\n\n\nPlease check the word again, it might have been misspelled");

            url="https://www.dictionary.com/browse/";

            startActivity(intent);

            handler.progressDialog.cancel();

        };
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------*/
}
/*  ======================================================================================================================================================*/
class validf {
    public boolean validate(String text) {

        boolean v = false;
        String regex = "[a-zA-Z|-]+$";
        //Matcher matcher = Pattern.compile(regex).matcher(text);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches())
        {
            v=matcher.matches();
            if(text.charAt(0)=='-'||text.charAt(text.length()-1)=='-')
                return false;
            else{
                char[] chars = text.toCharArray();
                int x=0;
                for (char ch : chars) {

                    if (ch == '-') {
                        x++;
                        if(x>1){
                            return false;
                        }
                    }
                }
            }
            return v;
        }
        else return false;
    }

}
/*  ======================================================================================================================================================*/
class handlef{

    static ProgressDialog progressDialog;
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.incrementProgressBy(1);
        }
    };

}
/*  ======================================================================================================================================================*/
