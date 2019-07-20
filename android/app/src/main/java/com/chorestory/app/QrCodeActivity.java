package com.chorestory.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AccountResponse;
import com.chorestory.templates.SingleResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeActivity extends ChoreStoryActivity {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

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

        String token = tokenHandler.getToken(getApplicationContext());
        if (tokenHandler.isParentToken(token)) {
            Call<SingleResponse<String>> childTokenQuery = retrofitInterface.get_child_clan_token(token);
            childTokenQuery.enqueue(new Callback<SingleResponse<String>>() {
                @Override
                public void onResponse(Call<SingleResponse<String>> call, Response<SingleResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                        String qrContent = response.body().getData();

                        createQRCode(qrContent);
                    } else {
                        Toaster.showToast(getApplicationContext(), "Internal error occurred.");
                    }
                }

                @Override
                public void onFailure(Call<SingleResponse<String>> call, Throwable t) {
                    Toaster.showToast(getApplicationContext(), "Internal error occurred.");
                }
            });
        }
    }

    private void createQRCode(String qrContent) {
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
            Log.e(this.getString(R.string.debug), e.toString());
        }
    }
}
