package com.arr.bancamovil.utils.backups;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import com.arr.bancamovil.R;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

public class ExportFile {

    private Context mContext;
    private String iv = "Yref7CzZHJHcCgJs";
    private String mCode;

    public ExportFile(Context context) {
        this.mContext = context;
    }

    /* Exportar archivo encriptado */
    public void exportFileEncrypt(Uri uri, String password) {
        try {
            OutputStream output = mContext.getContentResolver().openOutputStream(uri);
            if (output != null) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                SecretKeySpec spec =
                        new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "AES");
                cipher.init(
                        Cipher.ENCRYPT_MODE,
                        spec,
                        new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

                output.toString();
                
                inputStream(cipher, output);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /* exportar sin encriptar */
    public void exportFile(Uri uri) {
        try {
            OutputStream output = mContext.getContentResolver().openOutputStream(uri);
            if (output != null) {
                // file
                inputStream(output);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private FileInputStream inputStream(OutputStream output) {
        try {
            FileInputStream inputStream = new FileInputStream(directory());
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            output.close();
            return inputStream;
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    private FileInputStream inputStream(Cipher cipher, OutputStream output) {
        try {
            FileInputStream inputStream = new FileInputStream(directory());
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] outputt = cipher.update(buffer, 0, bytesRead);
                if (outputt != null) {
                    output.write(outputt);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                output.write(outputBytes);
            }
            inputStream.close();
            output.close();

            return inputStream;
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    // file dir
    private File directory() {
        File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return new File(file, "data.json");
    }

    /* generar una clave */
    public String generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomKey = new byte[16];
        secureRandom.nextBytes(randomKey);
        String claveAleatoria = bytesToHex(randomKey);
        return claveAleatoria;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public void importFileWithEncryptionCheck(Uri uri) {
        try {
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            if (input != null) {
                boolean isEncrypted = isEncrypted(input);
                input.close();
                if (isEncrypted) {
                    promptUserForPassword(uri);
                } else {
                    importFileUnencrypted(uri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void promptUserForPassword(Uri uri) {
        AlertDialog.Builder dialog = new MaterialAlertDialogBuilder(mContext);
        dialog.setTitle("Ingrese la contraseña");

        // Inflar el diseño del diálogo
        View view =
                LayoutInflater.from(mContext).inflate(R.layout.layout_export_alert_dialog, null);
        TextInputEditText editTextPassword = view.findViewById(R.id.edit_text);
        dialog.setView(view);

        dialog.setPositiveButton(
                "Aceptar",
                (dialogInterface, i) -> {
                    String password = editTextPassword.getText().toString().trim();
                    if (!password.isEmpty()) {
                        desencryptFile(uri, password);
                    } else {
                        showToast("¡Ingrese su token!");
                    }
                });

        dialog.show();
    }

    private void importFileUnencrypted(Uri uri) {
        try {
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            if (input != null) {
                byte[] fileData = input.readAllBytes();
                FileOutputStream fileOutput = new FileOutputStream(directory());
                fileOutput.write(fileData);
                input.close();
                fileOutput.close();
                showToast("Datos cargados con éxito");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desencryptFile(Uri uri, String key) {
        try {
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            if (input != null) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                SecretKeySpec keySpec =
                        new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
                cipher.init(
                        Cipher.DECRYPT_MODE,
                        keySpec,
                        new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

                // file
                byte[] encryptedData = input.readAllBytes();
                byte[] descrypted = cipher.doFinal(encryptedData);
                FileOutputStream fileOutput = new FileOutputStream(directory());
                fileOutput.write(descrypted);
                input.close();
                fileOutput.close();
                showToast("Datos cargados con éxito");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEncrypted(InputStream input) {
        try {
            byte[] buffer = new byte[4096];
            int byteReads = input.read(buffer);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec("dummy".getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    spec,
                    new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            cipher.update(buffer, 0, byteReads);
            cipher.doFinal();
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

    /*
    *
    *
    *
    *
    *
    *
    public void importFile(Uri uri) {
        try {
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            if (input != null) {
                boolean isEncrypted = isEncrypted(input);
                if (isEncrypted) {
                    // archivo encriptado
                    String password = promptUserForPassword();
                    desencryptFile(uri, password);
                } else {
                    // no esta encriptado

                }
                input.close();
            }
        } catch (Exception err) {

        }
    }

    private void desencryptFile(Uri uri, String key) {
        try {
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            if (input != null) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                SecretKeySpec keySpec =
                        new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
                cipher.init(
                        Cipher.DECRYPT_MODE,
                        keySpec,
                        new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

                // file
                byte[] encryptedData = input.readAllBytes();
                byte[] descrypted = cipher.doFinal(encryptedData);

                FileOutputStream fileOutput = new FileOutputStream(directory());
                fileOutput.write(descrypted);
                input.close();
                fileOutput.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */

    private View getView() {
        return LayoutInflater.from(mContext).inflate(R.layout.layout_export_alert_dialog, null);
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
