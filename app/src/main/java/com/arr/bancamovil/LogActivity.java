package com.arr.bancamovil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.arr.bancamovil.databinding.ActivityLogBinding;
import com.arr.bugsend.BugSend;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogActivity extends AppCompatActivity {

    private BugSend bug;
    private static final String MIME_TYPE = "application/octet-stream";

    private ActivityLogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bug = new BugSend(this);
        String error = bug.readError();
        saveTasas(error);

        binding.sendReport.setOnClickListener(
                view -> {
                    shareLogFile(error);
                    bug.deleteStackTrace();
                });
    }

    private void shareLogFile(String log) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"alessrodriguez98@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "BM/REPORT");
            intent.putExtra(Intent.EXTRA_TEXT, log);
            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:"));

            startActivity(Intent.createChooser(intent, "Compartir archivo de registro"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getDirectory() {
        File file = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return new File(file, "log.txt");
    }

    public void saveTasas(String result) {
        try {
            String string = result.toString();
            FileOutputStream outputStream = new FileOutputStream(getDirectory());
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bug.deleteStackTrace();
    }

    private void showMessqge(String mensqje) {
        Toast.makeText(this, mensqje, Toast.LENGTH_SHORT).show();
    }
}
