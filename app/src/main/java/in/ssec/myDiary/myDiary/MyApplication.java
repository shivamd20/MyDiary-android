package in.ssec.myDiary.myDiary;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import in.ssec.myDiary.myDiary.data.DiaryContract;
import in.ssec.myDiary.myDiary.data.DiaryDBHelper;

/**
 * Created by shivam on 30/9/17.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static class EncryptUtil{

        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        StrongPasswordEncryptor passwordEncryptor=new StrongPasswordEncryptor();

       public String encryptPassword(String str){

            return passwordEncryptor.encryptPassword(str);
        }

      public boolean checkPassword(String password,String orpassword)
        {
           return passwordEncryptor.checkPassword(password,orpassword);
        }

      public EncryptUtil(Context context)
       {
           DiaryDBHelper dbHelper;
           dbHelper=new DiaryDBHelper(context);
           SQLiteDatabase db = dbHelper.getWritableDatabase();
           Cursor cur= db.rawQuery("select "+DiaryContract.User.USER_NAME+" from "+ DiaryContract.User.TABLE_NAME,null);
           if(cur.moveToNext())
           {
               textEncryptor.setPassword(cur.getString(0)+"!@#$iloveindia%^&*fghf<>?");
           }

       }

        public  String encryptText(String str)
        {

            return  textEncryptor.encrypt(str);
        }

        public   String decryptText(String str)
        {
            return textEncryptor.decrypt(str);
        }

    }

}
