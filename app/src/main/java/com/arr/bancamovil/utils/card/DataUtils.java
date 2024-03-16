package com.arr.bancamovil.utils.card;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.arr.bancamovil.model.Data;
import com.arr.bancamovil.model.DataCards;
import com.arr.bancamovil.model.Items;
import com.arr.bancamovil.model.TicketModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataUtils {

    private Context mContext;
    private List<DataCards> mList = new ArrayList<>();

    public DataUtils(Context context) {
        this.mContext = context;
    }

    /* guardar información de las tarjetas en un archivo json */
    public void saveData(String user, String tarjeta, String moneda, String vence, String phone) {
        JSONArray mainArray = loadJSONArray();
        JSONObject data = new JSONObject();
        JSONObject result = new JSONObject();
        try {
            int id = mainArray.length();
            data.put("id", id);
            data.put("user", user);
            data.put("card", tarjeta);
            data.put("currency", moneda);
            data.put("expire", vence);
            data.put("phone", phone);

            mainArray.put(data);
            result.put("data", mainArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // save file
        saveDataFile(result.toString());
    }

    /* guardar los datos en el archivo json */
    private void saveDataFile(String jsonString) {
        try {
            FileOutputStream output = new FileOutputStream(directory());
            output.write(jsonString.toString().getBytes(StandardCharsets.UTF_8));
            output.close();

            showToast("Datos guardados");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray loadJSONArray() {
        JSONArray array = new JSONArray();
        try {
            FileInputStream inputStream = new FileInputStream(directory());
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            StringBuilder strungBuilder = new StringBuilder();
            char[] buffer = new char[4096];
            int byteRead;
            while ((byteRead = reader.read(buffer)) != -1) {
                strungBuilder.append(buffer, 0, byteRead);
            }
            reader.close();
            array = new JSONObject(strungBuilder.toString()).getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public List<DataCards> loadData() {
        mList.clear();
        try {
            JSONArray array = loadJSONArray();
            for (int i = 0; i < array.length(); ++i) {
                String user = "";
                String tarjeta = "";
                String moneda = "";

                JSONObject object = array.getJSONObject(i);
                if (object.has("id")) {
                    int id = object.getInt("id");
                }
                if (object.has("user")) {
                    user = object.getString("user");
                }
                if (object.has("card")) {
                    tarjeta = object.getString("card");
                }
                if (object.has("currency")) {
                    moneda = object.getString("currency");
                }

                String card = "**** **** **** " + tarjeta.substring(tarjeta.length() - 4);
                mList.add(new DataCards(card, moneda, user));

                System.out.println(tarjeta + " + " + moneda + " + " + user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    // Método para cargar información específica del JSON según la posición y el tipo de información
    public String loadInfo(int position, String info) {
        try {
            JSONArray array = loadJSONArray();
            if (position >= 0 && position < array.length()) {
                JSONObject object = array.getJSONObject(position);
                if (object.has(info)) {
                    return object.getString(info).toString();
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    // eliminar tarjeta de crédito
    public void deleteCard(int idToDelete) {
        try {
            JSONArray array = loadJSONArray();
            JSONArray newArray = new JSONArray();
            for (int i = 0; i < array.length(); ++i) {
                JSONObject jObject = array.getJSONObject(i);
                int id = jObject.getInt("id");

                if (id == idToDelete) {
                    idToDelete = i;
                } else {
                    if (id > idToDelete) {
                        jObject.put("id", id - 1);
                    }
                    newArray.put(jObject); // Agrega el objeto al nuevo JSONArray
                }
            }

            array.remove(idToDelete); // Elimina el objeto del JSONArray original

            FileOutputStream fileOutput = new FileOutputStream(directory());
            JSONObject newObject = new JSONObject();
            newObject.put("data", newArray);
            fileOutput.write(newObject.toString().getBytes());
            fileOutput.close();

            // si el ultimo dato esta en la posición 0 se elimina el archivo
            if (newArray.length() == 0 && idToDelete == 0) {
                File file = directory();
                file.delete();
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /*
    public void deleteCard(int idToDelete) {
        try {
            JSONArray array = loadJSONArray();
            JSONArray newArray = new JSONArray();
            JSONObject obj = array.getJSONObject(idToDelete);

            int pos = obj.getInt("id");
            array.remove(pos);

            for (int i = 0; i < array.length(); ++i) {
                JSONObject jObject = array.getJSONObject(i);
                int id = jObject.getInt("id");
                if (id > pos) {
                    jObject.put("id", id - 1);
                    if (id != idToDelete) {
                        newArray.put(obj);
                    }
                }
            }

            FileOutputStream fileOutput = new FileOutputStream(directory());
            JSONObject newObject = new JSONObject();
            newObject.put("data", newArray);
            fileOutput.write(newObject.toString().getBytes());
            fileOutput.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    */

    public void editData(
            int id, String user, String tarjeta, String moneda, String vence, String phone) {
        JSONArray array = loadJSONArray();
        try {
            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);
                if (object.getInt("id") == id) {
                    object.put("user", user);
                    object.put("card", tarjeta);
                    object.put("currency", moneda);
                    object.put("expire", vence);
                    object.put("phone", phone);
                    break;
                }
            }

            JSONObject result = new JSONObject();
            result.put("data", array);

            saveDataFile(result.toString());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /* directorio */
    public File directory() {
        /*   File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        */
        File file = new File(mContext.getFilesDir(), "data.json");
        return file;
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
