package in.ssec.myDiary.myDiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import in.ssec.myDiary.R;
import in.ssec.myDiary.myDiary.data.DiaryContract;
import in.ssec.myDiary.myDiary.data.DiaryDBHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyCursorAdapter.NoteClickListner {

    SQLiteDatabase db;
    MainActivity mA=this;
    int closeOpenfabs=0;
    FloatingActionButton write_new_fab;
    FloatingActionButton lock_fab;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu);


        return true;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);



        DiaryDBHelper dbHelper;
        dbHelper=new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor cur= db.rawQuery("select * from "+DiaryContract.User.TABLE_NAME,null);
        String userName="";
        while(cur.moveToNext())
        {
             userName =userName+cur.getString(0);
        }
        this.setTitle(userName+"'s Diary");
        Toast.makeText(this,userName,Toast.LENGTH_SHORT);
        cur.close();



       write_new_fab = (FloatingActionButton) findViewById(R.id.write_new_fab);
        write_new_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mA, WriteNew.class);
                startActivity(intent);

            }
        });
       lock_fab = (FloatingActionButton) findViewById(R.id.lock_diary_fab);
        lock_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mA, LoginActivity.class);
                startActivity(intent);

            }
        });
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume()
    {

        super.onResume();


        if(!((MyApplication)getApplication()).lockvarified) {
            Intent lockIntent = new Intent(this, LoginActivity.class);
            startActivity(lockIntent);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
      Cursor  cur= db.rawQuery("select * from "+DiaryContract.Notes.TABLE_NAME +" order by "+DiaryContract.Notes.DATE+" desc",null);

        ListView listView=(ListView)findViewById(R.id.all_Notes_layout);

        String[] from={DiaryContract.Notes.HEAD,DiaryContract.Notes.NOTE,DiaryContract.Notes.DATE,DiaryContract.Notes._id};


        MyCursorAdapter adapter1=new MyCursorAdapter( mA,
        cur,true,this);

        listView.setAdapter(adapter1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(mA, WriteNew.class);
                DiaryDBHelper dbHelper;


                dbHelper=new DiaryDBHelper(mA);
                db = dbHelper.getWritableDatabase();
                Cursor cur= db.rawQuery("select * from "+DiaryContract.Notes.TABLE_NAME+" where "
                                +DiaryContract.Notes._ID+"=?"
                        ,new String[]{id+""});

                cur.moveToNext();
                Toast.makeText(mA,id+""+"     2=="+cur.getString(2),Toast.LENGTH_SHORT).show();


                intent.putExtra("Heading",cur.getString(0));
                intent.putExtra("Note",cur.getString(2));
                intent.putExtra("date",cur.getString(1));
                intent.putExtra("id",cur.getString(3));
                intent.putExtra("image",cur.getBlob(4));

                startActivity(intent);

            }
        });
    }

//     void listClick(View v)
//    {
//        CoordinatorLayout linearLayout =(CoordinatorLayout) v;
//        TextView _idV=(TextView) linearLayout.getChildAt(3);
//        Intent intent = new Intent(mA, WriteNew.class);
//
//
//        DiaryDBHelper dbHelper;
//        dbHelper=new DiaryDBHelper(this);
//        db = dbHelper.getWritableDatabase();
//
//
////        Cursor cur= db.rawQuery("select * from "+DiaryContract.Notes.TABLE_NAME+" where "
////                +DiaryContract.Notes._ID+"=?"
////                ,new String[]{_idV.getText().toString()});
////
////        cur.moveToNext();
////        Toast.makeText(this,_idV.getText()+"     2=="+cur.getString(2),Toast.LENGTH_SHORT).show();
//
//
////        intent.putExtra("Heading",cur.getString(0));
////        intent.putExtra("Note",cur.getString(2));
////        intent.putExtra("date",cur.getString(1));
//        intent.putExtra("id",_idV.getText().toString());
////            intent.putExtra("image", cur.getBlob(4));
//
//        Toast.makeText(this,((TextView) linearLayout.getChildAt(3)).getText(),Toast.LENGTH_SHORT).show();
//
//        startActivity(intent);
//
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.write_New) {
        write_new_fab.performClick();

        } else if (id == R.id.nav_delete_All) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            db.delete(DiaryContract.User.TABLE_NAME, null, null);
                            db.delete(DiaryContract.Notes.TABLE_NAME, null, null);
                            Toast.makeText(mA, "user Deleted", Toast.LENGTH_SHORT);


                            Intent intent = new Intent(mA, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            return;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you really want to delete your Diary?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
//        } else if (id == R.id.nav_manage) {
//
////        } else if (id == R.id.nav_share) {
////
////        } else if (id == R.id.nav_send) {
////
////        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
           drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onClick(String id) {

        DiaryDBHelper dbHelper;
        dbHelper=new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();

        Intent intent = new Intent(mA, WriteNew.class);


//        Cursor cur= db.rawQuery("select * from "+DiaryContract.Notes.TABLE_NAME+" where "
//                +DiaryContract.Notes._ID+"=?"
//                ,new String[]{_idV.getText().toString()});
//
//        cur.moveToNext();
//        Toast.makeText(this,_idV.getText()+"     2=="+cur.getString(2),Toast.LENGTH_SHORT).show();


//        intent.putExtra("Heading",cur.getString(0));
//        intent.putExtra("Note",cur.getString(2));
//        intent.putExtra("date",cur.getString(1));
        intent.putExtra("id",id);
//            intent.putExtra("image", cur.getBlob(4));

        startActivity(intent);
    }
}
