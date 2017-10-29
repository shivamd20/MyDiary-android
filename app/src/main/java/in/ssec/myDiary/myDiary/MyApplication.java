package in.ssec.myDiary.myDiary;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import org.jasypt.encryption.ByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import in.ssec.myDiary.myDiary.data.BackupService;
import in.ssec.myDiary.myDiary.data.DiaryContract;
import in.ssec.myDiary.myDiary.data.DiaryDBHelper;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.ProjectConfig;

/**
 * Created by shivam on 30/9/17.
 */

public class MyApplication extends Application{


   public   boolean lockvarified=false;


    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();

        ProjectConfig config = new ProjectConfig.Builder()
                .setProjectName("avatar99") // or it can be .setCustomBaseDomain("myCustomDomain.com")
                .build();

        Hasura.setProjectConfig(config)
                .enableLogs() // not included by default
                .initialise(this);

        BackupService.startCloudBackup(this);

    }

    public static class EncryptUtil{

        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        BasicPasswordEncryptor passwordEncryptor=new BasicPasswordEncryptor();
        StandardPBEByteEncryptor byteEncryptor=new StandardPBEByteEncryptor();

        public  byte[] encryptByte(byte  by[])
        {
            return byteEncryptor.encrypt(by);
        }

        public  byte[] decrypttByte(byte  by[])
        {
            return byteEncryptor.decrypt(by);
        }

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
               byteEncryptor.setPassword(cur.getString(0)+"!@#$iloveindia%^&*fghf<>?");
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
