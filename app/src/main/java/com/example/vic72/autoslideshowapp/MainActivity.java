package com.example.vic72.autoslideshowapp;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    Cursor cursor;

    Timer timer;
   Handler mHandler = new Handler();

    Button mStartButton;
    Button mPauseButton;
    Button mResetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (Button) findViewById(R.id.button);
        mPauseButton = (Button) findViewById(R.id.button2);
        mResetButton = (Button) findViewById(R.id.button3);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cursor != null) {
                    if (cursor.getCount() != 0) {
                        if (cursor.moveToNext()) {
                            // indexからIDを取得し、そのIDから画像のURIを取得する
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);


                        } else {
                            cursor.moveToFirst();
                            // indexからIDを取得し、そのIDから画像のURIを取得する
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);
                        }
                    }
                }
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cursor != null) {
                    if (cursor.getCount() != 0) {

                        if (cursor.moveToPrevious()) {
                            // indexからIDを取得し、そのIDから画像のURIを取得する
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);
                        } else {
                            cursor.moveToLast();
                            // indexからIDを取得し、そのIDから画像のURIを取得する
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);
                        }
                    }

                }
            }

        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (timer != null) {
                    mResetButton.setText("再生");
                    timer.cancel();
                    mStartButton.setEnabled(true);
                    mPauseButton.setEnabled(true);
                    timer = null;
                } else {
                    mResetButton.setText("停止");

                    timer = new Timer();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    if (cursor != null) {
                                        if (cursor.getCount() != 0) {
                                            if (cursor.moveToNext()) {
                                                // indexからIDを取得し、そのIDから画像のURIを取得する
                                                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                                Long id = cursor.getLong(fieldIndex);
                                                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                                                imageVIew.setImageURI(imageUri);


                                            } else {
                                                cursor.moveToFirst();
                                                // indexからIDを取得し、そのIDから画像のURIを取得する
                                                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                                Long id = cursor.getLong(fieldIndex);
                                                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                                                imageVIew.setImageURI(imageUri);
                                            }

                                            mStartButton.setEnabled(false);
                                            mPauseButton.setEnabled(false);


                                        }
                                    }
                                }
                            });


                        }
                    }, 2000, 2000);


                }

            }
        });


        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                } else {

                    Toast.makeText(this, "権限の許可をお願いします", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }









    private void getContentsInfo() {



            // 画像の情報を取得する
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目(null = 全項目)
                    null, // フィルタ条件(null = フィルタなし)
                    null, // フィルタ用パラメータ
                    null // ソート (null ソートなし)
            );


               if (cursor.moveToFirst()) {

                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);


                    ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                    imageVIew.setImageURI(imageUri);
                }
            }
            }




















