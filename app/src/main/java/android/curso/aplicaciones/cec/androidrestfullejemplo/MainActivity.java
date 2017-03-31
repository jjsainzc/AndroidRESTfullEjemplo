package android.curso.aplicaciones.cec.androidrestfullejemplo;

import android.app.ProgressDialog;
import android.curso.aplicaciones.cec.androidrestfullejemplo.comunicaciones.Conexiones;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button calcular;
    private TextView resultado;
    private EditText valor;
    private String resultadoRest;
    private Hilo hilo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcular = (Button) findViewById(R.id.calcular);
        resultado = (TextView) findViewById(R.id.resultado);
        valor = (EditText) findViewById(R.id.valor);


        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hilo = new Hilo();
                hilo.execute();
            }
        });

    }

    private synchronized Map<String, String> llamadaRES() throws IOException {
        Map<String, String> param = new HashMap<>();
        param.put("base", valor.getText().toString());


        return  Conexiones.connectREST(
                "http://192.168.1.10:8080/SimpleRESTweb/resources/",
                "factorial",
                param,
                "application/x-www-form-urlencoded",
                "",
                "GET");
    }


    class Hilo extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                resultadoRest = llamadaRES().get("body");
            } catch (IOException e) {
               resultadoRest = e.toString();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Procesando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            resultado.setText(resultadoRest);
            pDialog.dismiss();

        }
    }




}
