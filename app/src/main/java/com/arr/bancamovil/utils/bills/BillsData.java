package com.arr.bancamovil.utils.bills;

import android.content.Context;
import android.os.Environment;
import com.arr.bancamovil.model.Market;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BillsData {

    private Context mContext;
    private static final String FILE_NAME = "bills.db";
    private String saldo, ingreso, gasto, monto, category, type;

    private List<Market> list = new ArrayList<>();

    public BillsData(Context context) {
        this.mContext = context;
    }

    public void save(
            String saldo,
            String ingreso,
            String gasto,
            String monto,
            String category,
            String type) {
        this.saldo = saldo;
        this.ingreso = ingreso;
        this.gasto = gasto;
        this.monto = monto;
        this.category = category;
        this.type = type;
        saveFileData();
    }

    // Guardar los datos en el archivo
    private void saveFileData() {
        try {
            JSONObject total = new JSONObject();
            JSONObject data = new JSONObject();

            // Comprobar y agregar los campos al objeto "data" si no son null
            if (saldo != null) {
                data.put("saldo", saldo);
            }
            if (ingreso != null) {
                data.put("ingreso", ingreso);
            }
            if (gasto != null) {
                data.put("gasto", gasto);
            }

            // Comprobar si el objeto "data" contiene al menos un campo no null
            if (data.length() > 0) {
                total.put("data", data);
            }

            // Obtener el array "bills"
            JSONArray array = getArrayBills();

            if (array == null) {
                array = new JSONArray();
            }

            JSONObject bill = new JSONObject();

            // Comprobar y agregar los campos al objeto "bill" si no son null
            if (monto != null) {
                bill.put("monto", monto);
            }
            if (category != null) {
                bill.put("category", category);
            }
            if (type != null) {
                bill.put("type", type);
            }

            // Comprobar si el objeto "bill" contiene al menos un campo no null
            if (bill.length() > 0) {
                // Agregar el nuevo elemento al array "bills"
                array.put(bill);
            }

            // Comprobar si se ha agregado algún elemento al array "bills"
            if (array.length() > 0) {
                // Objecto para combinar ambos datos
                JSONObject json = new JSONObject();
                json.put("CUP", total);
                json.put("bills", array);

                // Guardar los datos en el archivo
                saveFileJson(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load list array
    public List<Market> getList() {
        list.clear();
        try {
            JSONArray array = getArrayBills();
            for (int i = 0; i < array.length(); ++i) {
                JSONObject data = array.getJSONObject(i);
                if (data.has("monto") || data.has("category")) {
                    list.add(new Market(data.getString("category"), data.getString("monto")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // eliminar los datos del array
    public void deleteItem(int position) {
        try {
            // Lee el contenido del archivo JSON y lo convierte en un objeto JSONObject
            JSONObject object = new JSONObject(readFileToString());
            JSONArray array = object.getJSONArray("bills");
            if (position >= 0 && position < array.length()) {
                array.remove(position);
            }
            object.put("bills", array);

            if (array.length() == 0 && position == 0) {
                File file = fileDirectory();
                file.delete();
            } else {
                saveFileJson(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // caargar los datos de "data"
    public double getData(String key) {
        try {
            // Obtener el JSON completo del archivo
            JSONObject json = new JSONObject(readFileToString());
            JSONObject data = json.getJSONObject("CUP").getJSONObject("data");
            return Double.parseDouble(data.getString(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getArrayDelete(String key, int position) {
        try {
            JSONArray array = getArrayBills();
            if (position >= 0 && position < array.length()) {
                JSONObject object = array.getJSONObject(position);
                if (object.has(key)) {
                    return object.getString(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // cargar string del array
    public String getStringArray(String key, int position) {
        try {
            JSONArray array = getArrayBills();
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

    // Método para guardar el JSON en el archivo
    private void saveFileJson(JSONObject json) {
        try (FileWriter file = new FileWriter(fileDirectory())) {
            file.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // leer el array "bills" para agregar o eliminar datos segun haga falta.
    private JSONArray getArrayBills() {
        JSONArray array = new JSONArray();
        try {
            FileInputStream inputStream = new FileInputStream(fileDirectory());
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
            // Convertir el contenido del archivo a un JSONObject
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            if (jsonObject.has("bills")) {
                array = jsonObject.getJSONArray("bills");
            } else {
                array = null;
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    /* obtener todo los datos del archivo en forma de String */
    private String readFileToString() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream inputStream = new FileInputStream(fileDirectory());
                InputStreamReader reader =
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    /* ruta del archivo que contiene la imformación de los gastos.
     * @Ruta: storage/emulated/0/Android/data/com.arr.bancamovil/files/data
     */
    private File fileDirectory() {
        File file = mContext.getExternalFilesDir("data");
        if (file != null) {
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            return new File(file, FILE_NAME);
        }
        return null;
    }
}
