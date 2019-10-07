package trieuphu.donglv.com.hoingu.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import trieuphu.donglv.com.hoingu.model.Question;

public class DatabaseManager {
    private static final String DATABASE_PATH = Environment.getDataDirectory().getAbsolutePath() + "/data/com.dong.luong.hoingu/";
    private static final String DATABSE_NAME = "hoi_ngu.sqlite";
    private static final String DATABSE_NAME1 = "cac_thanh.sqlite";
    private static final String TABLE_NAME_QUESTION = "question";
    private static final String SQL_GET_QUESTION = "SELECT * FROM " + TABLE_NAME_QUESTION;
    private static final String ID = "_id";
    private static final String QUESTION = "cauhoi";
    private static final String ANSWER_A = "a";
    private static final String ANSWER_B = "b";
    private static final String ANSWER_C = "c";
    private static final String ANSWER_D = "d";
    private static final String ANSWER_TRUE = "dapan";
    private static final String NICK = "nick";
    private static final String GIAI_THICH = "giai_thich";
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public DatabaseManager(Context context) {

        this.context = context;
        copyDatabases();

    }

    private void copyDatabases() {
        new File(DATABASE_PATH).mkdir();
        try {
            File file = new File(DATABASE_PATH + DATABSE_NAME);
            if (file.exists()) return;
            InputStream input = context.getAssets().open(DATABSE_NAME);

            file.createNewFile();
            FileOutputStream output = new FileOutputStream(file);
            int len;
            byte buff[] = new byte[1024];
            while ((len = input.read(buff)) > 0) {
                output.write(buff, 0, len);
            }
            output.close();
            input.close();

            Log.i("a", "Copy Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDB() {
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            sqLiteDatabase = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABSE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    private void closeDB() {
        if (sqLiteDatabase != null || sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    public Question getOneQuestion() {
        openDB();
        //Uu tien lay theo dk sau

        Cursor c = sqLiteDatabase.rawQuery(SQL_GET_QUESTION + " ORDER BY Random() LIMIT 1", null);

        if (c == null) {
            return null;
        }
        Question question1 = null;
        //Lay thu tu cot dua vao ten cot
        //Lay du lieu dua vao thu tu cot
        int indexQuestionId = c.getColumnIndex(ID);
        int indexQuestion = c.getColumnIndex(QUESTION);
        int indexAnswerA = c.getColumnIndex(ANSWER_A);
        int indexAnswerB = c.getColumnIndex(ANSWER_B);
        int indexAnswerC = c.getColumnIndex(ANSWER_C);
        int indexAnswerD = c.getColumnIndex(ANSWER_D);
        int indexAnswerTrue = c.getColumnIndex(ANSWER_TRUE);
        int indexNick = c.getColumnIndex(NICK);
        int indexGiaiThich = c.getColumnIndex(GIAI_THICH);

        String questionId, answerA, answerB, answerC, answerD, answerTrue, answerNick,
                question, answerGiaiThich;

        c.moveToFirst();
        //Khi con tro chua tro toi hang cuoi cung
        while (c.isAfterLast() == false) {
            //Lay du lieu dua vao thu tu cot

            questionId = c.getString(indexQuestionId);
            question = c.getString(indexQuestion);
            answerA = c.getString(indexAnswerA);
            answerB = c.getString(indexAnswerB);
            answerC = c.getString(indexAnswerC);
            answerD = c.getString(indexAnswerD);
            answerTrue = c.getString(indexAnswerTrue);
            answerNick = c.getString(indexNick);
            answerGiaiThich = c.getString(indexGiaiThich);

            question1 = new Question(question, answerA, answerB, answerC, answerD, answerTrue, answerNick,
                    questionId, answerGiaiThich);


            //Dua con tro den vi tri tiep theo
            c.moveToNext();
        }
        //Dong con tro lai
        c.close();
        //Dong csdl
        closeDB();
        return question1;
    }


    public ArrayList<Question> getListQuestion() {
        openDB();
        //Uu tien lay theo dk sau

        Cursor c = sqLiteDatabase.rawQuery(SQL_GET_QUESTION, null);

        if (c == null) {
            return null;
        }
        //Lay thu tu cot dua vao ten cot
        //Lay du lieu dua vao thu tu cot
        int indexQuestionId = c.getColumnIndex(ID);
        int indexQuestion = c.getColumnIndex(QUESTION);
        int indexAnswerA = c.getColumnIndex(ANSWER_A);
        int indexAnswerB = c.getColumnIndex(ANSWER_B);
        int indexAnswerC = c.getColumnIndex(ANSWER_C);
        int indexAnswerD = c.getColumnIndex(ANSWER_D);
        int indexAnswerTrue = c.getColumnIndex(ANSWER_TRUE);
        int indexNick = c.getColumnIndex(NICK);
        int indexGiaiThich = c.getColumnIndex(GIAI_THICH);

        String questionId, answerA, answerB, answerC, answerD, answerTrue, answerNick,
                question, answerGiaiThich;

        c.moveToFirst();
        //Khi con tro chua tro toi hang cuoi cung
        ArrayList<Question> listQuestion = new ArrayList<>();
        while (c.isAfterLast() == false) {
            //Lay du lieu dua vao thu tu cot

            questionId = c.getString(indexQuestionId);
            question = c.getString(indexQuestion);
            answerA = c.getString(indexAnswerA);
            answerB = c.getString(indexAnswerB);
            answerC = c.getString(indexAnswerC);
            answerD = c.getString(indexAnswerD);
            answerTrue = c.getString(indexAnswerTrue);
            answerNick = c.getString(indexNick);
            answerGiaiThich = c.getString(indexGiaiThich);

            listQuestion.add(new Question(question, answerA, answerB, answerC, answerD, answerTrue, answerNick,
                    questionId, answerGiaiThich));

            //Dua con tro den vi tri tiep theo
            c.moveToNext();
        }
        //Dong con tro lai
        c.close();
        //Dong csdl
        closeDB();

        //Tra ve list Question
        return listQuestion;
    }
}

