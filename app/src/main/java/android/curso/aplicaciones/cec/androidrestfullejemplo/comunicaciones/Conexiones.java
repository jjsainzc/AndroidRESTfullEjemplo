package android.curso.aplicaciones.cec.androidrestfullejemplo.comunicaciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by CEC on 30/07/2016.
 */
public class Conexiones {


    private static String getStringFromInputStream(InputStream is) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new IOException(e);
                }
            }
        }
        return sb.toString();
    }

    public static synchronized Map<String, String> connectREST(String urlStr,
                                                               String script,
                                                               Map<String, String> param,
                                                               String contentType,
                                                               String data,
                                                               String method) throws IOException {

        Map<String, String> res = new HashMap();
        StringBuilder sb = new StringBuilder();
        String paramStr = "";

        if ((param != null) && !param.isEmpty()) {
            String llave;
            sb.append("?");
            for (Iterator it = param.keySet().iterator(); it.hasNext();) {
                llave = (String) it.next();
                sb.append(llave).append("=");
                sb.append(param.get(llave).toString()).append("&");
            }
            paramStr = sb.toString().substring(0, sb.toString().length() - 1);
        }

        try {
            URL url = new URL(urlStr + script + paramStr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            //connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept", contentType);

            if ((data != null) && (!data.isEmpty())) {
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(data);
                osw.flush();
                osw.close();
            }

            InputStream response = connection.getInputStream();
            String encoding = connection.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;

            //String body = IOUtils.toString(response, encoding);
            String body = getStringFromInputStream(response);

            res.put("body", body);
            res.put("message", connection.getResponseMessage());
            res.put("code", String.valueOf(connection.getResponseCode()));
        } catch (IOException e) {
            throw new IOException(e);
        }
        return res;
    }
}
