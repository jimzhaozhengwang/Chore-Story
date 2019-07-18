package com.chorestory.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCodeActivity extends ChoreStoryActivity {

    TextView addAPersonTextView;
    ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_qr_code);

        addAPersonTextView = findViewById(R.id.add_a_person_text_view);
        qrCodeImageView = findViewById(R.id.qr_code_image_view);

        String addAPerson = getIntent().getStringExtra(getString(R.string.qr_code));
        if (addAPerson != null && !addAPerson.isEmpty()) {
            addAPerson = "Scan the QR Code to add a " + addAPerson;
            addAPersonTextView.setText(addAPerson);
        }

        String qrContent = "1234-5678-91011"; // TODO: change this

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent,
                    BarcodeFormat.QR_CODE,
                    300,
                    300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
