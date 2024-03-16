package com.arr.bancamovil.utils.tasas;

import android.content.Context;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class TasasUtils {

    private Context mContext;
    private String url_inform = "https://exchange-rate.decubba.com/api/v2/informal/target/cup.json";
    private String url_formal = "https://exchange-rate.decubba.com/api/v2/formal/target/cup.json";

    public TasasUtils(Context context) {
        this.mContext = context;
    }

    /* obtener tasas de cambio */
    public JSONObject obtainsTasas() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url_inform).build();
            Response response = client.newCall(request).execute();

            // resultado
            String jsonString = response.body().string();

            // parsear el Json
            JSONObject json = new JSONObject(jsonString);
            JSONObject rates = json.getJSONObject("rates");
            String[] currencies = {"USD", "EUR", "MLC", "CUP"};
            JSONObject informal = new JSONObject();
            for (String currency : currencies) {
                JSONObject object = rates.getJSONObject(currency);
                    double buy = object.isNull("buy") ? -1 : object.getDouble("buy");
                    double sell = object.isNull("sell") ? -1 : object.getDouble("sell");

                    JSONObject currencyData = new JSONObject();
                    currencyData.put("buy", buy == -1 ? 0 : (int) buy);
                    currencyData.put("sell", sell == -1 ? 0 : (int) sell);
                    informal.put(currency, currencyData);
            }

            //  tasas de cambio formal
            Request requestFormal = new Request.Builder().url(url_formal).build();
            Response responseFormal = client.newCall(requestFormal).execute();
            String stringFormal = responseFormal.body().string();
            JSONObject jsonFormal = new JSONObject(stringFormal);
            JSONObject ratesFormal = jsonFormal.getJSONObject("rates");
            String[] currenciesFormal = {"CAD", "CHF", "EUR", "GBP", "JPY", "MXN", "USD"};
            JSONObject formal = new JSONObject();

            for (String currency : currenciesFormal) {
                JSONObject object = ratesFormal.getJSONObject(currency);
                if (!object.isNull("sell") && !object.isNull("buy")) {
                    double buy = object.getDouble("buy");
                    double sell = object.getDouble("sell");
                    JSONObject currencyData = new JSONObject();
                    currencyData.put("buy", (int) buy);
                    currencyData.put("sell", (int) sell);
                    formal.put(currency, currencyData);
                }
            }

            JSONObject result = new JSONObject();
            result.put("informal", informal);
            result.put("formal", formal);

            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveTasas(JSONObject result) {
        try {
            String string = result.toString();
            FileOutputStream outputStream = new FileOutputStream(getDirectory());
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /* directorio donde se guarda la información */
    private File getDirectory() {
        File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return new File(file, "exchange.json");
    }

    public JSONObject getFileJson() {
        try {
            FileInputStream fis = new FileInputStream(getDirectory());
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            inputStreamReader.close();
            fis.close();
            String json = stringBuilder.toString();
            return new JSONObject(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
