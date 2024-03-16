package com.arr.bancamovil.utils.backups;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.widget.Toast;

import com.arr.bancamovil.R;
import com.arr.bancamovil.utils.dialog.ImportDialog;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileExport {

    private final Context mContext;

    public FileExport(Context context) {
        this.mContext = context;
    }

    /* exportar archivos */
    public void exportFileEncrypt(Uri uri, String password) {
        try {
            // Abrir stream de salida
            OutputStream output = mContext.getContentResolver().openOutputStream(uri);
            byte[] data;
            File file = getDirectory();
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            ByteArrayOutputStream encryptedData = new ByteArrayOutputStream();

            // Generar un IV aleatorio
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);

            // Inicializar el cifrador con el IV
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec =
                    new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            // Escribir el IV en el archivo JSON
            JSONObject object = new JSONObject();
            object.put("encrypt", true);
            object.put("iv", Base64.encodeToString(iv, Base64.DEFAULT));

            // Leer y encriptar los datos del archivo
            while ((bytesRead = input.read(buffer)) != -1) {
                byte[] encryptedChunk = cipher.update(buffer, 0, bytesRead);
                if (encryptedChunk != null) {
                    encryptedData.write(encryptedChunk);
                }
            }
            // Finalizar el proceso de encriptación
            byte[] finalChunk = cipher.doFinal();
            if (finalChunk != null) {
                encryptedData.write(finalChunk);
            }
            data = encryptedData.toByteArray();

            // Agregar los datos encriptados al JSON
            object.put("servicesEncrypted", Base64.encodeToString(data, Base64.DEFAULT));
            String jsonString = object.toString();
            assert output != null;
            output.write(jsonString.getBytes(StandardCharsets.UTF_8));

            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Exportar archivo sin cifrar */
    public void exportFileDescrypt(Uri uri) {
        try {
            OutputStream output = mContext.getContentResolver().openOutputStream(uri);
            if (output != null) {
                File file = getDirectory();
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                ByteArrayOutputStream fileData = new ByteArrayOutputStream();

                while ((bytesRead = input.read(buffer)) != -1) {
                    fileData.write(buffer, 0, bytesRead);
                }

                JSONObject object = new JSONObject();
                object.put("encrypt", false);
                object.put("label", new String(fileData.toByteArray(), StandardCharsets.UTF_8));
                String result = object.toString();

                output.write(result.getBytes(StandardCharsets.UTF_8));
                output.flush();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* importar archivo sin cifrar */
    private void importFileDesencrypt(Uri uri) {
        try {
            // Abrir stream de entrada
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            assert input != null;
            byte[] jsonData = new byte[input.available()];
            input.read(jsonData);
            input.close();

            // Convertir el contenido del archivo en un objeto JSON
            String jsonString = new String(jsonData, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonString);
            // Obtener el valor cifrado de "data" en Base64
            String decryptedData = jsonObject.getString("label");

            File file = getDirectory();
            FileOutputStream output = new FileOutputStream(file);
            output.write(decryptedData.getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();

            showToast("Datos cargados con éxito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importFileDecrypt(Uri uri, String password) {
        try {
            // Abrir stream de entrada
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            byte[] jsonData = new byte[input.available()];
            input.read(jsonData);
            input.close();

            // Convertir el contenido del archivo en un objeto JSON
            String jsonString = new String(jsonData, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonString);

            // Obtener el valor cifrado de "data" en Base64
            String encryptedData = jsonObject.getString("servicesEncrypted");

            // Decodificar el contenido cifrado en Base64
            byte[] decodedData = Base64.decode(encryptedData, Base64.DEFAULT);

            // Obtener el IV del objeto JSON
            byte[] iv = Base64.decode(jsonObject.getString("iv"), Base64.DEFAULT);

            // Inicializar el descifrador con el IV
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec =
                    new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            // Descifrar los datos
            byte[] decryptedData = cipher.doFinal(decodedData);

            // Guardar los datos descifrados en un archivo
            File file = getDirectory();
            FileOutputStream output = new FileOutputStream(file);
            output.write(decryptedData);
            output.flush();
            output.close();

            showToast("Datos cargados con éxito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private void decryptFile(String key, InputStream input) {
        try {
            // Leer el archivo cifrado
            ByteArrayOutputStream encryptedData = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                encryptedData.write(buffer, 0, bytesRead);
            }
            byte[] encryptedBytes = encryptedData.toByteArray();

            // Inicializar el cifrador
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            // Desencriptar los datos
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Escribir los datos desencriptados en el archivo
            FileOutputStream fileOutput = new FileOutputStream(getDirectory());
            fileOutput.write(decryptedBytes);

            input.close();
            fileOutput.close();
            showToast("Datos cargados con éxito");
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

     */

    /* importar copia de seguridad no encriptada */

    /* importar archivo encriptado */
    public void importFile(Uri uri) {
        try {
            InputStream input = mContext.getContentResolver().openInputStream(uri);
            if (input != null) {
                boolean isEmcrypt = isEncrypt(input);
                input.close();
                if (isEmcrypt) {
                    ImportDialog dialog = new ImportDialog(mContext);
                    dialog.setTitle("Importar");
                    dialog.setMessage(mContext.getString(R.string.message_import));
                    dialog.setPositiveButtom(
                            view -> {
                                String token = dialog.getEditTextValue();
                                if (token != null && !token.isEmpty()) {
                                    importFileDecrypt(uri, token);
                                } else {
                                    showToast("No pueden haber campos vacíos");
                                }
                            }).setNegativeButtom(null);
                    dialog.show();
                } else {
                    importFileDesencrypt(uri);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /* desencriptar
    private void desencryptFile(String key, InputStream input) {
        try {
            // file
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    keySpec,
                    new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

            // file
            byte[] encryptedData = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                encryptedData = input.readAllBytes();
            }
            byte[] descrypted = cipher.doFinal(encryptedData);

            FileOutputStream fileOutput = new FileOutputStream(getDirectory());
            fileOutput.write(descrypted);
            input.close();
            fileOutput.close();
            showToast("Datos cargados con éxito");
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void descifrar(InputStream input, String password) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesRead;
            byte[] data = new byte[4096];
            while ((bytesRead = input.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            String jsonString = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                jsonString = buffer.toString(StandardCharsets.UTF_8);
            }
            JSONObject object = new JSONObject(jsonString);
            String encryptedData = object.optString("data");
            byte[] decodedData = Base64.decode(encryptedData, Base64.DEFAULT);

            // Inicializar el descifrador
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec =
                    new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            // Descifrar los datos
            byte[] decryptedData = cipher.doFinal(decodedData);

            String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);
            Log.d("Descifrado", "Datos descifrados: " + decryptedString);

            FileOutputStream fileOutput = new FileOutputStream(getDirectory());
            fileOutput.write(decryptedData);
            input.close();
            fileOutput.close();
            showToast("Guardado");
        } catch (Exception e) {
            e.printStackTrace();
            String error = e.getMessage();
            Log.e("ERROR", "E " + error);
        }
    }
    */


    /* boolean para comprobar si esta encriptado o no el archivo
     * funcional
     */
    private boolean isEncrypt(InputStream input) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();

            JSONObject jsonObject = new JSONObject(outputStream.toString());
            return jsonObject.getBoolean("encrypt");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* metodo de encriptacion
    private Cipher getEncrypt(String password) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec =
                    new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    spec,
                    new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            return cipher;
        } catch (Exception err) {
            return null;
        }
    }
*/
    /* directorio de almacenamiento del archivo */
    private File getDirectory() {
        return new File(mContext.getFilesDir(), "data.json");
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
