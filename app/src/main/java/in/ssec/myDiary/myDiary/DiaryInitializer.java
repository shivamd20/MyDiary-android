package in.ssec.myDiary.myDiary;

        import android.app.AlertDialog;
        import android.content.ContentValues;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.text.InputType;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import in.ssec.myDiary.R;
        import in.ssec.myDiary.myDiary.data.DiaryContract;
        import in.ssec.myDiary.myDiary.data.DiaryDBHelper;
        import io.hasura.sdk.Hasura;
        import io.hasura.sdk.HasuraClient;
        import io.hasura.sdk.HasuraUser;
        import io.hasura.sdk.exception.HasuraException;
        import io.hasura.sdk.responseListener.SignUpResponseListener;


@SuppressWarnings("serial")

public class DiaryInitializer extends AppCompatActivity{

    MyApplication.EncryptUtil encryptUtil;

    EditText name;
    EditText pass;
    Button create ;
    DiaryInitializer dI=this;
    DiaryDBHelper dbHelper;
    HasuraClient hasuraClient;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        hasuraClient= Hasura.getClient();

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
        create =(Button) findViewById(R.id.create);
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryInitializer.this);
                builder.setTitle("Confirm your password");

// Set up the input
                final EditText input = new EditText(DiaryInitializer.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //   m_Text = input.getText().toString();

                        if(pass.getText().toString().equals(input.getText().toString()))
                        {
                        if( create()) {

                            finish();
                            Intent intent = new Intent(dI, MainActivity.class);
//                            EditText editText = (EditText) findViewById(R.id.name);
//                            String message = editText.getText().toString();
//                            intent.putExtra("hello", message);
                            startActivity(intent);
                        }}else {
                            Toast.makeText(dI,"Wrong Password",Toast.LENGTH_SHORT).show();
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
    protected void onStart() {


        dbHelper=new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cur=db.rawQuery("select * from user",null );
        if(!(cur.getCount()==0)) {
            finish();

            ((MyApplication)getApplication()).lockvarified=true;

            Intent intent = new Intent(dI, MainActivity.class);
            startActivity(intent);
        }
cur.close();
        super.onStart();

    }


    void signUpToHasura(final String username, final String password)
    {
        HasuraUser user = hasuraClient.getUser();

        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SignUpResponseListener() {
            @Override
            public void onSuccessAwaitingVerification(HasuraUser user) {
                //The user is registered on Hasura, but either his mobile or email needs to be verified.
            }

            @Override
            public void onSuccess(HasuraUser user) {
                //Now Hasura.getClient().getCurrentUser() will have this user
                addUser(username,password);
            }

            @Override
            public void onFailure(HasuraException e) {
                Toast.makeText(DiaryInitializer.this, "password to short",Toast.LENGTH_SHORT).show();
            }
        });

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
            if (pass.getText().toString().length() >= 5) {
                 {

                    signUpToHasura(name.getText().toString(), pass.getText().toString());
                    return true;
                    //   name.setText("created");
                    // new MyDiaryHomeScreen().lock();

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
