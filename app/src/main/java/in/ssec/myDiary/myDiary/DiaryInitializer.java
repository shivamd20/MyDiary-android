package in.ssec.myDiary.myDiary;

        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import in.ssec.myDiary.R;
        import in.ssec.myDiary.myDiary.data.DiaryContract;
        import in.ssec.myDiary.myDiary.data.DiaryDBHelper;


@SuppressWarnings("serial")

public class DiaryInitializer extends AppCompatActivity{

    MyApplication.EncryptUtil encryptUtil;

    EditText name;
    EditText pass;
    EditText cnfpass;
    Button create ;
    DiaryInitializer dI=this;
    DiaryDBHelper dbHelper;
    MenuItem deleteMenuItem;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       encryptUtil=new MyApplication.EncryptUtil(this);

        dbHelper=new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();


        Cursor cur=db.rawQuery("select * from user",null );
        if(!(cur.getCount()==0)) {
            finish();
            Intent intent = new Intent(dI, MainActivity.class);
            startActivity(intent);
        }
             cur.close();
        setContentView(R.layout.diary_sign_up);
        name=(EditText) findViewById(R.id.name);
        pass=(EditText) findViewById(R.id.pass);
        cnfpass=(EditText) findViewById(R.id.cnfpass);
        create =(Button) findViewById(R.id.create);
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




               if( create()) {

                   Intent intent = new Intent(dI, MainActivity.class);
                   EditText editText = (EditText) findViewById(R.id.name);
                   String message = editText.getText().toString();
                   intent.putExtra("hello", message);
                   startActivity(intent);
               }

            }
        });
    }
    @Override
    protected void onStart() {


        dbHelper=new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cur=db.rawQuery("select * from user",null );
        if(!(cur.getCount()==0)) {
            finish();
            Intent intent = new Intent(dI, MainActivity.class);
            startActivity(intent);
        }
cur.close();
        super.onStart();

    }





    void addUser(String name,String password)
    {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

       password= encryptUtil.encryptPassword(password);

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.

        Cursor cur=db.rawQuery("select * from user",null );
        if(cur.getCount()==0) {
            ContentValues values = new ContentValues();
            values.put(DiaryContract.User.USER_NAME, name);
            values.put(DiaryContract.User.USER_PASSWORD, password);

            long newRowId = db.insert(DiaryContract.User.TABLE_NAME, null, values);
            if (newRowId == -1) {
            }
        }
        else
        {
            Toast.makeText(this, "Welcome",Toast.LENGTH_SHORT).show();
        }
    }


    boolean create()
    {
        if(name.getText().length()>3) {
            if (pass.getText().toString().length() >= 4) {
                if (pass.getText().toString().equals(cnfpass.getText().toString())) {

                    addUser(name.getText().toString(), pass.getText().toString());
                    return true;
                    //   name.setText("created");
                    // new MyDiaryHomeScreen().lock();

                }
                else
                {
                    Toast.makeText(this, "password Didn't matched",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "password to short",Toast.LENGTH_SHORT).show();
            }
        }

        else
        {
            Toast.makeText(this, "name to short",Toast.LENGTH_SHORT).show();
            return false;
          //  JOptionPane.showMessageDialog(null, "please check your password");
        }

        Toast.makeText(this,"long press on any of the 'S' ",Toast.LENGTH_LONG).show();
        return false;
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_initilizer, menu);
        deleteMenuItem=(MenuItem)  findViewById(R.id.deleteUser);
        deleteMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                db.delete(DiaryContract.User.TABLE_NAME,null,null);
                Toast.makeText(dI,"",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }
    */
}
