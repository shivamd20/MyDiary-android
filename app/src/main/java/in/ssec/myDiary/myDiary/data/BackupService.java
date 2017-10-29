package in.ssec.myDiary.myDiary.data;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;

import in.ssec.myDiary.R;
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

    boolean uploadTask()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.diary)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!").setColor(Color.RED);

// Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        mBuilder.setProgress(0, 0, true);


        Hasura.getClient().useFileStoreService().uploadFile(new File(new DiaryDBHelper(this).getReadableDatabase().getPath()), "image/*", new FileUploadResponseListener() {
            @Override
            public void onUploadComplete(FileUploadResponse fileUploadResponse) {
                Log.i(BackupService.class.getName(),"Done");
            }

            @Override
            public void onUploadFailed(HasuraException e) {

                Log.i(BackupService.class.getName(),e.getMessage());
            }
        });

        return  false;
    }


}
