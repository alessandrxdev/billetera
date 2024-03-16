package com.arr.bancamovil.utils.gastos;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.arr.bancamovil.model.Market;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GastosBd {

    private Context mContext;
    private int mTotal, mGastos, newGasto, mIngreso, newIngreso;
    private String catIngreso, catGasto, date, hora, type;
    private JSONArray arrayData, arrayNewData;

    private List<Market> list = new ArrayList<>();

    public GastosBd(Context context) {
        this.mContext = context;
    }

    // guardar gastos
    public void setGasto(
            int total,
            int gasto,
            int newGasto,
            String category,
            String type,
            String hora,
            String fecha) {
        this.mTotal = total;
        this.mGastos = gasto;
        this.newGasto = newGasto;
        this.catGasto = category;
        this.type = type;
        this.hora = hora;
        this.date = fecha;
        saveJson();
    }

    public void setIngreso(
            int total,
            int ingreso,
            int newIngreso,
            String cat,
            String type,
            String hora,
            String fecha) {
        this.mTotal = total;
        this.mIngreso = ingreso;
        this.newIngreso = newIngreso;
        this.catIngreso = cat;
        this.type = type;
        this.hora = hora;
        this.date = fecha;

        saveJson();
    }

    // cargar los datos en un List
    public List<Market> getList() {
        list.clear();
        try {
            JSONArray array = loadJSONArray();
            for (int i = 0; i < array.length(); ++i) {
                String categoria = "";
                int cantidad = 0;
                JSONObject data = array.getJSONObject(i);
                if (data.has("id")) {
                    int id = data.getInt("id");
                }
                if (data.has("newGasto")) {
                    cantidad = data.getInt("newGasto");
                }
                if (data.has("catGa")) {
                    categoria = data.getString("catGa");
                }
                list.add(new Market(categoria, String.valueOf(cantidad) + " CUP"));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return list;
    }

    public int getInfoBd(String key) {
        try {
            FileInputStream inputStream = new FileInputStream(directory());
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject myGasto = jsonObject.getJSONObject("myGasto");
            JSONObject data = myGasto.getJSONObject("data");

            if (data.has(key)) {
                return data.getInt(key);
            } else {
                return -1; // Valor de retorno para indicar que la clave no se encontró
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return -1; // Valor de retorno por defecto en caso de error
    }

    // logica para crear el Json con gastos
    private void saveJson() {
        try {
            JSONObject result = new JSONObject();
            JSONObject myGasto = new JSONObject();
            JSONObject data = new JSONObject();
            if (mTotal != 0) {
                data.put("total", mTotal);
            }
            if (mGastos != 0) {
                data.put("gasto", mGastos);
            }
            if (mIngreso != 0) {
                data.put("ingreso", mIngreso);
            }

            JSONArray newDataArray = loadJSONArray();
            JSONObject newData = new JSONObject();
            newData.put("id", newDataArray.length());
            if (newGasto != 0) {
                newData.put("newGasto", newGasto);
            }
            newData.put("newIngreso", newIngreso);
            newData.put("catIn", catIngreso);
            newData.put("catGa", catGasto);
            newData.put("date", date);
            newData.put("hore", hora);
            newData.put("type", type);
            newDataArray.put(newData);

            data.put("newData", newDataArray);
            myGasto.put("data", data);
            result.put("myGasto", myGasto);

            saveDataFile(result);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private JSONArray loadJSONArray() {
        JSONArray array = new JSONArray();
        try {
            FileInputStream inputStream = new FileInputStream(directory());
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[4096];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject myGasto = jsonObject.getJSONObject("myGasto");
            JSONObject data = myGasto.getJSONObject("data");
            array = data.getJSONArray("newData");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    /*
        private void saveGastos() {
            this.arrayData = loadArray();
            this.arrayNewData = loadJSONArray();
            try {
                // logica para guardar el total de todo
                JSONObject data = new JSONObject();
                int id = arrayData.length(); // obtiene la longitud del array
                data.put("id", id);

                if (mTotal == 0) {
                    data.put("total", mTotal);
                }
                if (mGastos == 0) {
                    data.put("gastos", mGastos);
                }

                JSONObject newData = new JSONObject();
                if (newGasto == 0) {
                    newData.put("newGasto", newGasto);
                }
                arrayData.put(data);
                arrayNewData.put(newData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private JSONObject saveJson(JSONArray array, JSONArray insert) {
            try {
                JSONObject result = new JSONObject();
                JSONObject data = new JSONObject();
                JSONObject newData = new JSONObject();
                data.put("data", array);
                newData.put("newData", insert);

                return result;
            } catch (Exception err) {
                return null;
            }
        }
    */
    private JSONObject saveJsonO(JSONArray array, JSONArray insert) {
        try {
            JSONObject result = new JSONObject();
            JSONObject data = new JSONObject();
            JSONObject newData = new JSONObject();

            // Crear el objeto "data" con los valores de "total", "gasto" e "ingreso"
            JSONObject innerData = new JSONObject();
            innerData.put("total", mTotal);
            innerData.put("gasto", mGastos);
            innerData.put("ingreso", mIngreso);
            data.put("data", innerData);

            // Crear el arreglo "newData" con los objetos JSON correspondientes
            JSONArray innerNewData = new JSONArray();
            for (int i = 0; i < insert.length(); i++) {
                JSONObject object = insert.getJSONObject(i);
                JSONObject newObject = new JSONObject();
                newObject.put("id", object.getInt("id"));
                newObject.put("newGasto", object.getInt("newGasto"));
                newObject.put("newIngreso", object.getInt("newIngreso"));
                newObject.put("catIn", object.getString("catIn"));
                newObject.put("catGa", object.getString("catGa"));
                innerNewData.put(newObject);
            }

            newData.put("newData", innerNewData);

            // Agregar "data" y "newData" al objeto "myGasto"
            JSONObject myGasto = new JSONObject();
            myGasto.put("data", data);
            myGasto.put("newData", newData);

            // Agregar "myGasto" al objeto "result"
            result.put("myGasto", myGasto);

            return result;
        } catch (Exception err) {
            return null;
        }
    }

    /*
    public void setGastos(
            int total,
            int gastos,
            int ingreso,
            int newGasto,
            int newIngreso,
            String catIngreso,
            String catGastos) {

        JSONArray mainArray = loadJSONArray();

        JSONObject result = new JSONObject();

        try {
            JSONObject data = new JSONObject();
            JSONObject newGastoData = new JSONObject();
            if (total != 0) {
                data.put("total", total);
            }
            if (gastos != 0) {
                data.put("gasto", gastos);
            }
            if (ingreso != 0) {
                data.put("ingreso", ingreso);
            }

            // Si al menos uno de los campos no es cero, se añade a "gastos"
            if (total != 0 && gastos != 0 && ingreso != 0) {
                mainArray.getJSONObject(0).getJSONArray("gastos").put(newGastoData);
            }

            // Crear el objeto para los nuevos datos y agregarlos a "newData" si cumplen las
            // condiciones
            if (newGasto != 0 && newIngreso != 0 && !categoria.isEmpty()) {
                JSONObject newGastoDataNew = new JSONObject();
                newGastoDataNew.put("newGasto", newGasto);
                newGastoDataNew.put("newIngreso", newIngreso);
                newGastoDataNew.put("category", categoria);

                mainArray.getJSONObject(0).getJSONArray("newData").put(newGastoDataNew);
            }

            data.put("data", mainArray);
            result.put("data", data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        saveDataFile(result);
    }
    */

    private void saveDataFile(JSONObject jsonObject) {
        try {
            FileOutputStream output = new FileOutputStream(directory());
            output.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            output.close();

            showToast("Datos guardados");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray loadArray() {
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

    // directory
    private File directory() {
        File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return new File(file, "bills.json");
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
