package in.ssec.myDiary.myDiary;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.ssec.myDiary.R;
import in.ssec.myDiary.myDiary.data.DiaryContract;

/**
 * Created by shivam on 1/10/17.
 */

public class MyCursorAdapter extends CursorAdapter {

    public  static interface NoteClickListner{

        void onClick(String id);

    }

    private LayoutInflater cursorInflater;

    MyApplication.EncryptUtil encryptUtil;
    NoteClickListner noteClickListener;

    // Default constructor
    public MyCursorAdapter(Context context, Cursor cursor, boolean flags,NoteClickListner l) {
        super(context, cursor, flags);
        noteClickListener=l;
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        encryptUtil=new MyApplication.EncryptUtil(context);
    //   ...
    }

    public void bindView(View view, Context context, final Cursor cursor) {

        String headTxt=cursor.getString(cursor.getColumnIndex(DiaryContract.Notes.HEAD));
        String noteTxt=cursor.getString(cursor.getColumnIndex(DiaryContract.Notes.NOTE));
        final String idTxt=cursor.getString(cursor.getColumnIndex(DiaryContract.Notes._id));
        String date=cursor.getString(cursor.getColumnIndex(DiaryContract.Notes.DATE));


        TextView headTv = (TextView) view.findViewById(R.id.headText);
        TextView noteTv = (TextView) view.findViewById(R.id.noteText);
        TextView dateTv = (TextView) view.findViewById(R.id.dateText);
        ImageView imTv=(ImageView)view.findViewById(R.id.noteIcon);

        headTv.setText(encryptUtil.decryptText(headTxt));
        noteTv.setText(encryptUtil.decryptText(noteTxt));
        dateTv.setText(date);

       byte[] imageByte = encryptUtil.decrypttByte(cursor.getBlob(4));
//

        if (imageByte != null) {
            Bitmap bitmap = ImageUtil.getImage(imageByte);
            if (bitmap != null)
                imTv.setImageBitmap(bitmap);
        }

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteClickListener.onClick(cursor.getString(cursor.getColumnIndex(DiaryContract.Notes._id)));
            }
        };

        view.setOnClickListener(onClickListener);
        headTv.setOnClickListener(onClickListener);
        noteTv.setOnClickListener(onClickListener);
        dateTv.setOnClickListener(onClickListener);

      // ...
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.simple_row, parent, false);
    }
}
