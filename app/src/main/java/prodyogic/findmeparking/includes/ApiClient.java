package prodyogic.findmeparking.includes;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ApiClient {

    public ApiClient(){
    }

    protected abstract void finished(int code, String callResult);

    public void requestGET(String urldir){
        new AsyncTask<String, Void, String>() {
            int code = -1;
            @Override
            protected void onPreExecute() {
                // here is for code you do before the network call. you
                // leave it empty
            }

            @Override
            protected String doInBackground(String... params) {
                String result = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    code = urlConnection.getResponseCode();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result = readStream(in);
                    in.close();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String res) {
                finished(code, res);
            }
        }.execute(urldir);
    }

    public void requestPOST(String urldir,String body) {
    new AsyncTask<String, Void, String>() {
        int code = -1;
        @Override
        protected void onPreExecute() {
            // here is for code you do before the network call. you
            // leave it empty
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try{
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); //output body
            urlConnection.setChunkedStreamingMode(0);//when size is not know
            //setFixedLengthStreamingMode(int); //when size is known

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            OutputStream out =urlConnection.getOutputStream();
            writeStream(params[1], out);
                code = urlConnection.getResponseCode();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result=readStream(in);
            in.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return result;
        }

        @Override
        protected void onPostExecute(String res) {
            finished(code, res);

        }
    }.execute(urldir,body);
}

    private void writeStream(String string,OutputStream out) throws IOException {
        OutputStreamWriter wr= new OutputStreamWriter(out);
        wr.write(string);
        out.write(string.getBytes("UTF-8"));
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
