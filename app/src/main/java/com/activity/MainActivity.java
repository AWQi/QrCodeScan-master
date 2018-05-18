package com.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;
import com.qrcodescan.R;
import com.utils.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    Button openQrCodeScan ;

    EditText text ;

    Button CreateQrCode ;

    ImageView QrCode ;

    TextView qrCodeText ;

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         openQrCodeScan = findViewById(R.id.openQrCodeScan);
         text = findViewById(R.id.text);
         CreateQrCode = findViewById(R.id.CreateQrCode);
         QrCode = findViewById(R.id.QrCode);
        qrCodeText = findViewById(R.id.qrCodeText);

        openQrCodeScan.setOnClickListener(this);
        CreateQrCode.setOnClickListener(this);
        ButterKnife.bind(this);
    }

//    @OnClick({R.id.openQrCodeScan, R.id.CreateQrCode})
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        switch (view.getId()) {
            case R.id.openQrCodeScan:
                //打开二维码扫描界面
//                if(CommonUtil.isCameraCanUse()){
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
//                }else{
//                    Toast.makeText(this,"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.CreateQrCode:
                try {
                    Log.d(TAG, "onClick: "+CreateQrCode);
                    //获取输入的文本信息
                    String str = text.getText().toString().trim();
                    Log.d(TAG, "文本信息: "+str);
                    if(str != null && !"".equals(str.trim())){
                        //根据输入的文本生成对应的二维码并且显示出来
                        Bitmap mBitmap = EncodingHandler.createQRCode(text.getText().toString(), 500);
                        Log.d(TAG, "bitmap: ");
                        if(mBitmap != null){
                            Log.d(TAG, "bitmap 不为空: ");
                            Toast.makeText(this,"二维码生成成功！",Toast.LENGTH_SHORT).show();
                            QrCode.setImageBitmap(mBitmap);
                        }
                    }else{
                        Toast.makeText(this,"文本信息不能为空！",Toast.LENGTH_SHORT).show();
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
            qrCodeText.setText(scanResult);
        }
    }
}
