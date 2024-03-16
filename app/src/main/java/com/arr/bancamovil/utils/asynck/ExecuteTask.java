package com.arr.bancamovil.utils.asynck;
import android.app.Activity;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ExecuteTask<Params, Progress, Result> {

    private final Executor executor;
    private final Activity activity;

    public ExecuteTask(Activity activity) {
        this.activity = activity;
        executor = Executors.newSingleThreadExecutor();
    }

    /*
     *
     */
    public void execute(final Params... params) {
        executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Result result = doInBackground(params);
                        activity.runOnUiThread(
                                () -> {
                                    onPostExecute(result);
                                });
                    }
                });
    }

    protected abstract Result doInBackground(Params... params);

    protected void onPostExecute(Result result) {}
}
