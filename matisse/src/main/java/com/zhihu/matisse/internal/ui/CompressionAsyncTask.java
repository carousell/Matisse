package com.zhihu.matisse.internal.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.SelectionSpec;
import com.zhihu.matisse.internal.model.SelectedItemCollection;
import com.zhihu.matisse.internal.utils.CompressionUtils;

import java.io.IOException;

public class CompressionAsyncTask extends AsyncTask<Void, Void, Void> {

    private ProgressDialog progressDialog = null;
    private Context context = null;
    private SelectedItemCollection selectedItemCollection = null;
    private CompressionCallback callback = null;
    private SelectionSpec selectionSpec = null;

    public CompressionAsyncTask(ProgressDialog progressDialog, Context context, SelectedItemCollection selectedItemCollection, CompressionCallback callback, SelectionSpec selectionSpec) {
        this.progressDialog = progressDialog;
        this.context = context;
        this.selectedItemCollection = selectedItemCollection;
        this.callback = callback;
        this.selectionSpec = selectionSpec;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        for (Item item : selectedItemCollection.asList()) {
            try {
                CompressionUtils.compressImage(context, selectionSpec, item);
            } catch (IOException e) {

            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        if (callback!=null) {
            callback.onCompressionDone();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public interface CompressionCallback {
        void onCompressionDone();
    }
};
