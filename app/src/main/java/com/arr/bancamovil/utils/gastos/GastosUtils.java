package com.arr.bancamovil.utils.gastos;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.arr.bancamovil.adapter.MarketAdapter;
import com.arr.bancamovil.model.Market;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GastosUtils {

    private Context mContext;
    private String mTotal, mGastos, mIngreso, mNewMonto;
    private String mType, mCaregory;

    private List<Market> list = new ArrayList<>();

    public GastosUtils(Context context) {
        this.mContext = context;
    }

    public void saveData(
            String total,
            String gasto,
            String ingreso,
            String newMonto,
            String type,
            String category) {
        this.mTotal = total;
        this.mGastos = gasto;
        this.mIngreso = ingreso;
        this.mNewMonto = newMonto;
        this.mType = type;
        this.mCaregory = category;
        saveDataFileJson();
    }

    // cargar los datos en un List
    public List<Market> getList() {
        list.clear();
        try {
            JSONArray array = jsonArray();
            for (int i = 0; i < array.length(); ++i) {
                String categoria = null;
                String cantidad = null;

                JSONObject data = array.getJSONObject(i);
                if (data.has("id")) {
                    int id = data.getInt("id");
                }
                if (data.has("monto")) {
                    cantidad = data.getString("monto");
                }
                if (data.has("category")) {
                    categoria = data.getString("category");
                }
                list.add(0, new Market(categoria, cantidad + " CUP"));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return list;
    }

    // guardar la informacion en un archivo json
    private void saveDataFileJson() {
        try {
            JSONObject result = new JSONObject();
            JSONObject myGasto = new JSONObject();
            JSONObject data = new JSONObject();
            if (mTotal != null) {
                data.put("total", mTotal);
            }
            if (mGastos != null) {
                data.put("gasto", mGastos);
            }
            if (mIngreso != null) {
                data.put("ingreso", mIngreso);
            }

            JSONArray dataArray = jsonArray();
            JSONObject newData = new JSONObject();
            newData.put("id", dataArray.length());
            if (mNewMonto != null) {
                newData.put("monto", mNewMonto);
            }
            newData.put("type", mType);
            newData.put("category", mCaregory);

            // create array
            dataArray.put(newData);
            data.put("bill", dataArray);
            myGasto.put("data", data);
            result.put("CUP", myGasto);

            // guardar los datos en un archivo json
            saveFile(result);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    // leer el archivo Json
    private JSONArray jsonArray() {
        JSONArray array = new JSONArray();
        try {
            File file = fileDirectory();
            if (file != null && file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                InputStreamReader reader =
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                char[] buffer = new char[4096];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    stringBuilder.append(buffer, 0, bytesRead);
                }
                reader.close();

                // JSON
                JSONObject object = new JSONObject(stringBuilder.toString());
                JSONObject myGasto = object.getJSONObject("CUP");
                JSONObject data = myGasto.getJSONObject("data");
                array = data.getJSONArray("bill");
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return array;
    }

    public String getString(String key) {
        try {
            FileInputStream inputStream = new FileInputStream(fileDirectory());
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject myGasto = jsonObject.getJSONObject("CUP");
            JSONObject data = myGasto.getJSONObject("data");

            if (data.has(key)) {
                return data.getString(key);
            } else {
                return null; // Valor de retorno para indicar que la clave no se encontró
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null; // Valor de retorno por defecto en caso de error
    }

    public String getStringArray(String key, int position) {
        try {
            JSONArray array = jsonArray();
            if (position >= 0 && position < array.length()) {
                JSONObject object = array.getJSONObject(position);
                if (object.has(key)) {
                    return object.getString(key);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    public void delete() {
        File file = fileDirectory();
        if (file != null && file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                showMessage("¡Datos eliminados!");
            } else {
                showMessage("¡Datos no eliminados!");
            }
        }
    }

    // save file to directory
    private void saveFile(JSONObject object) {
        try {
            FileOutputStream output = new FileOutputStream(fileDirectory());
            output.write(object.toString().getBytes(StandardCharsets.UTF_8));
            output.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    // file directory
    private File fileDirectory() {
        File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (file != null) {
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            return new File(file, "bill.json");
        }
        return null;
    }

    // show toast message
    private void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
