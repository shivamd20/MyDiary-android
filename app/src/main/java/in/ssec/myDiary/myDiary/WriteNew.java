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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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


public class WriteNew extends AppCompatActivity {
    private static final String TAG = WriteNew.class.getName();
    WriteNew wN=this;
    EditText noteText;
    String date;
    SQLiteDatabase db;
    TextView dateText;
    String id;
    byte[] imageByte=null;
    ImageView imgView;
    MyApplication.EncryptUtil encryptUtil;
    //private String mHeading="";
    private String mNote="";


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

        noteText=(EditText) findViewById(R.id.noteText);
        dateText=(TextView)findViewById(R.id.note_date) ;
        imgView=(ImageView)findViewById(R.id.image_view) ;
//        updateBtn=(Button)findViewById(R.id.update_note) ;


        id=getIntent().getStringExtra("id");
        if(id!=null) {
           cur= db.rawQuery("select * from " + DiaryContract.Notes.TABLE_NAME + " where "
                            + DiaryContract.Notes._ID + "=?"
                    , new String[]{id});
            cur.moveToNext();
            Toast.makeText(this, id + "     2==" + cur.getString(2), Toast.LENGTH_SHORT).show();


             String heading=encryptUtil.decryptText(cur.getString(0)).toString();



                //  headText.setText();

            setTitle(heading);

            mNote=heading+"\n"+encryptUtil.decryptText(cur.getString(2));

            date = cur.getString(1);
            dateText.setText("Last Modified: "+date);
            imageByte = encryptUtil.decrypttByte(cur.getBlob(4));


            SpannableStringBuilder str = new SpannableStringBuilder(mNote);

            int l=str.toString().indexOf('\n');


            if(l==-1)
            {
                l=str.length()-1;
            }

        //    mHeading=str.subSequence(0,l).toString();


            if(mNote.length()>0) {
                str.setSpan(new RelativeSizeSpan(1.4f), 0, l, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                noteText.setText(str);
            }else

            noteText.setText(mNote);

//

            if (imageByte != null) {
                Bitmap bitmap = ImageUtil.getImage(imageByte);
                if (bitmap != null)
                    imgView.setImageBitmap(bitmap);
            }
        }

//        headText.addTextChangedListener(textWatcher);
        noteText.addTextChangedListener(textWatcher);



//        else
//        {
//            updateBtn.setVisibility(View.VISIBLE);
//        }

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




            int l=s.toString().indexOf('\n');


            if(l==-1)
            {
                l=s.length()-1;
            }
            if(s.length()>0)
                setTitle(s.toString().substring(0,l));



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
// @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.write_new_menu, menu);
//        return true;
//    }


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.write_new_menu, menu);
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

            case R.id.action_attach_image:
                addImg(null);
                return true;

            case R.id.action_lock_diary:
                Intent intent = new Intent(wN, LoginActivity.class);
                startActivity(intent);
                return true;


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

        if(noteText.length()<1)
        if(imageByte==null)
        {
            Toast.makeText(this,"Write something",Toast.LENGTH_SHORT).show();
            return;
        }


        Log.e(TAG,"under save");

        DiaryDBHelper dbHelper;
        dbHelper=new DiaryDBHelper(wN);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();


//        if(date!="")
//        {

        String date=new SimpleDateFormat("dd/MM/yyyy hh:mm::ssSS").format (Calendar.getInstance().getTime());

            values.put(DiaryContract.Notes.DATE,date);
//        }
//        else {
//            values.put(DiaryContract.Notes.DATE, date.toString());
//        }

        String rawNote=noteText.getText().toString();
        int l=rawNote.indexOf('\n');

        String head,note;

        if(l==-1)
        {
           head=rawNote;
            note="";
        }
        else{

           head= rawNote.substring(0,l);
         note=   rawNote.substring(l,rawNote.length());

        }

        String enhead=encryptUtil.encryptText(head);
        String enNote=encryptUtil.encryptText(note);

        values.put(DiaryContract.Notes.HEAD,enhead);
        values.put(DiaryContract.Notes.NOTE,enNote);
        if(imageByte!=null)
        {
            byte[] encByte=encryptUtil.encryptByte(imageByte);
            values.put(DiaryContract.Notes.IMAGE,encByte);
        }


      id=""+  db.insert(DiaryContract.Notes.TABLE_NAME,null,values);


        dateText.setText("last modified: "+date);



       // wN.finish();

    }

    void delete(boolean finish)
    {


        DiaryDBHelper dbHelper;
        dbHelper=new DiaryDBHelper(wN);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

          int n= db.delete(DiaryContract.Notes.TABLE_NAME," "+DiaryContract.Notes._id+"==?",new String[]{id});
//            if(n>0)
//            {
//             //   finish();
//            }
//            else
//            {
//                Toast.makeText(this," please save the note first",Toast.LENGTH_SHORT).show();
//            }

            if(finish)
            {
                finish();
                Log.e(TAG,"finish called");
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

        updateNote(null);
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
