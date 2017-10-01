package in.ssec.myDiary.myDiary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.ssec.myDiary.R;
import in.ssec.myDiary.myDiary.data.DiaryContract;
import in.ssec.myDiary.myDiary.data.DiaryDBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Handler;


public class WriteNew extends Activity {
    WriteNew wN=this;
    EditText headText;
    EditText noteText;
    Button   saveBtn;
    Button  deleteBtn;
    String date;
    SQLiteDatabase db;
    TextView dateText;
    String id;
    byte[] imageByte=null;
    ImageView imgView;
    Button imgCBtn;
    Button updateBtn;
    MyApplication.EncryptUtil encryptUtil;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       encryptUtil =new MyApplication.EncryptUtil(this);

        DiaryDBHelper dbHelper;
        dbHelper=new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor cur;


        //  if(Build.VERSION.SDK_INT<20)

            setContentView(R.layout.write_new);

       // else
       // setContentView(R.layout.write_new_lollipop);

        saveBtn= (Button)   findViewById(R.id.save_btn);
        noteText=(EditText) findViewById(R.id.noteText);
        headText=(EditText) findViewById(R.id.headText);
        deleteBtn= (Button)   findViewById(R.id.delete_note);
        dateText=(TextView)findViewById(R.id.note_date) ;
        imgView=(ImageView)findViewById(R.id.image_view) ;
        imgCBtn=(Button)findViewById(R.id.image_btn) ;
        updateBtn=(Button)findViewById(R.id.update_note) ;


        id=getIntent().getStringExtra("id");
        if(id!=null) {
           cur= db.rawQuery("select * from " + DiaryContract.Notes.TABLE_NAME + " where "
                            + DiaryContract.Notes._ID + "=?"
                    , new String[]{id});
            cur.moveToNext();
            Toast.makeText(this, id + "     2==" + cur.getString(2), Toast.LENGTH_SHORT).show();


            headText.setText(encryptUtil.decryptText(cur.getString(0)));
            noteText.setText(encryptUtil.decryptText(cur.getString(2)));
            date = getIntent().getStringExtra(cur.getString(1));
            dateText.setText(date);
            imageByte = encryptUtil.decrypttByte(cur.getBlob(4));
//

            if (imageByte != null) {
                Bitmap bitmap = ImageUtil.getImage(imageByte);
                if (bitmap != null)
                    imgView.setImageBitmap(bitmap);
            }
        }

        headText.addTextChangedListener(textWatcher);
        noteText.addTextChangedListener(textWatcher);



         cur= db.rawQuery("select * from "+ DiaryContract.User.TABLE_NAME,null);
        String userName="";
        while(cur.moveToNext())
        {
            userName =userName+cur.getString(0);
        }
        this.setTitle(userName+"'s Diary");
        Toast.makeText(this,userName,Toast.LENGTH_SHORT);
        cur.close();

        saveBtn.setOnClickListener(saveOnclickLis);

        deleteBtn.setOnClickListener(deleteOnclickLis);

        if(id==null)
        {
            deleteBtn.setVisibility(View.INVISIBLE);
            saveBtn.setText("save");
        }
        else
        {
            updateBtn.setVisibility(View.VISIBLE);
        }

    }

    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateNote(null);
        }
    };

    public void updateNote(View view)
    {

        delete(false);
        save(null);
    //    finish();
    }

    public void addImg(View view)
    {
        openImageChooser();
    }
 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_new_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on t:he "Save" menu option
            case R.id.action_save_note:
                  save(null);

                // Save pet to databas
                // Exit activity

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete_note:
               delete(true);
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

        View.OnClickListener saveOnclickLis= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            save(null);

        }
    };

    View.OnClickListener deleteOnclickLis= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            delete(true);

        }
    };
    void save(View view)
    {

        if(headText.length()<1)
        {
        if(noteText.length()<1)
        {
            Toast.makeText(this,"Write something",Toast.LENGTH_SHORT).show();
            return;
        }
        }

        DiaryDBHelper dbHelper;
        dbHelper=new DiaryDBHelper(wN);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();


//        if(date!="")
//        {
            values.put(DiaryContract.Notes.DATE,new SimpleDateFormat("dd/MM/yyyy hh:mm::ssSS").format (Calendar.getInstance().getTime()));
//        }
//        else {
//            values.put(DiaryContract.Notes.DATE, date.toString());
//        }

        String enhead=encryptUtil.encryptText(headText.getText().toString());
        String enNote=encryptUtil.encryptText(noteText.getText().toString());

        values.put(DiaryContract.Notes.HEAD,enhead);
        values.put(DiaryContract.Notes.NOTE,enNote);
        if(imageByte!=null)
        {
            byte[] encByte=encryptUtil.encryptByte(imageByte);
            values.put(DiaryContract.Notes.IMAGE,encByte);
        }


      id=""+  db.insert(DiaryContract.Notes.TABLE_NAME,null,values);
        Toast.makeText(wN,"Note Saved",Toast.LENGTH_LONG);
       // wN.finish();

    }

    void delete(boolean finish)
    {


        DiaryDBHelper dbHelper;
        dbHelper=new DiaryDBHelper(wN);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

          int n= db.delete(DiaryContract.Notes.TABLE_NAME," "+DiaryContract.Notes._id+"==?",new String[]{id});
            if(n>0)
            {
                Toast.makeText(this,"deleted",Toast.LENGTH_SHORT).show();
             //   finish();
            }
            else
            {
                Toast.makeText(this," please save the note first",Toast.LENGTH_SHORT).show();
            }

            if(finish)
            {
                finish();
            }

    }

    void loadImageFromDB()
    {

    }



    int  PICK_IMAGE=100;

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {

try {
    Uri uri = data.getData();
    if (uri != null) {
        InputStream iStream = getContentResolver().openInputStream(uri);
        imageByte = ImageUtil.getBytes(iStream);

        String img=Base64.encodeToString(imageByte,Base64.DEFAULT);

        Toast.makeText(this,"image  found",Toast.LENGTH_SHORT).show();

        imgView.setImageURI(uri);
    }

}
catch (IOException fe)
{
    Toast.makeText(this,"image not found",Toast.LENGTH_SHORT).show();
}

            }
        }
    }

}
