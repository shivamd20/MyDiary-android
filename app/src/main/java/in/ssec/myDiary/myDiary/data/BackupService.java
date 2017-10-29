package in.ssec.myDiary.myDiary.data;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;

import in.ssec.myDiary.R;
import in.ssec.myDiary.myDiary.MainActivity;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.model.response.FileUploadResponse;
import io.hasura.sdk.responseListener.FileUploadResponseListener;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BackupService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPLOAD = "in.ssec.myDiary.myDiary.data.action.UPLOAD";
    private static final int NOTIFICATION_ID = 50;
    NotificationManager mNotifyMgr;


    public BackupService() {
        super("BackupService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */


    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startCloudBackup(Context context) {
        Intent intent = new Intent(context, BackupService.class);
        intent.setAction(ACTION_UPLOAD);
       // intent.putExtra(EXTRA_PARAM1, param1);
     //   intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD.equals(action)) {

                uploadTask();
            }
        }
    }

    private void makeNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Notification Title")
                .setContentText("Sample Notification Content")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                ;
        Notification n;

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;


         mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(NOTIFICATION_ID, n);
    }


    boolean uploadTask()
    {
        makeNotification(this);

        Hasura.getClient().useFileStoreService().uploadFile(new File(new DiaryDBHelper(this).getReadableDatabase().getPath()), "image/*", new FileUploadResponseListener() {
            @Override
            public void onUploadComplete(FileUploadResponse fileUploadResponse) {
                Log.i(BackupService.class.getName(),"Done");
                mNotifyMgr.cancel(NOTIFICATION_ID);
            }

            @Override
            public void onUploadFailed(HasuraException e) {

                Log.i(BackupService.class.getName(),e.getMessage());
                mNotifyMgr.cancel(NOTIFICATION_ID);
            }
        });

        return  false;
    }


}
