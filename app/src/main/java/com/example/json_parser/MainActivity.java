package com.example.json_parser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url = "http://192.168.1.36:45460/api/Etudiants";
    private ListView lv;
    ArrayList<Etudiant> mesEtu =new ArrayList<>();
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=findViewById(R.id.lst);
        new MySiteAPI().execute(url);

    }
    public class MySiteAPI extends AsyncTask<String, Void, List<Etudiant>>
    {
        public String ConvertStream2String (InputStream is) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = null;
            String str;

            br = new BufferedReader (new InputStreamReader(is));

            while ((str = br.readLine()) != null)
                sb.append(str);

            return sb.toString();
        }

        @Override
        protected List<Etudiant> doInBackground(String... strings)
        {
            String urlStr = strings[0];
            String rssStr;

            try {
                URL url = new URL(urlStr);
                HttpURLConnection cnx = (HttpURLConnection) url.openConnection();

                rssStr = ConvertStream2String(cnx.getInputStream());
                //parsing

                JSONArray LivreLArr = new JSONArray(rssStr);

                for(int index = 0; index < LivreLArr.length(); index++)
                {
                    JSONObject LivreObj = LivreLArr.getJSONObject(index);
                    Etudiant livre = new Etudiant(0,
                            LivreObj.getString("prenom"),
                            LivreObj.getString("nom"),
                            LivreObj.getString("image")
                    );
                    mesEtu.add(livre);
                }
                adapter= new MyAdapter(getApplicationContext(),mesEtu);
                lv.setAdapter(adapter);

            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return mesEtu;
        }

        protected void onPostExecute(List<Etudiant> result)
        {
            lv = (ListView) findViewById(R.id.lst);
            List<String> txt  = new ArrayList<String>();
            for(Etudiant l: result) {
                txt.add((String) l.print());
                Log.i("tag1",l.toString());
            }
            adapter= new MyAdapter(getApplicationContext(),mesEtu);
            lv.setAdapter(adapter);
        }
    }

}
    /*public static List<Etudiant> getelem(Context context, URL url) throws IOException, JSONException {

        JSONObject obj = new JSONObject(Readbodyhttp_Req(context,url));
        JSONArray m_jArry = obj.getJSONArray("Etudiant");

        for (int i=0; i<m_jArry.length(); i++) {
            lst.add(
                    new Etudiant(
                            m_jArry.getJSONObject(i).getString("nom"),
                            //m_jArry.getJSONObject(i).getJSONObject("prenom").getString("m")
                            m_jArry.getJSONObject(i).getString("prenom")
                    )
            );

        }
        return  lst;
    }


    public static String Readbodyhttp_Req(Context constext, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String body = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            body = sb.toString();

            Log.d("HTTP-GET", body);



        } catch (Exception e){
            Log.i("Exception", String.valueOf(e));
        }
        return  body;
    }

    public class JSON extends AsyncTask<String, Void, List<Etudiant>> {

        @Override
        protected List<Etudiant> doInBackground(String... strings) {
            List<Etudiant> lst = new ArrayList<Etudiant>();
            try {
                lst = getelem(getApplicationContext(), new URL(strings[0]));

            } catch (IOException e) {
                System.out.println("==IO" + e);
            } catch (JSONException e) {
                System.out.println("==JSON" + e);
            }

            return lst;
        }

        @Override
        protected void onPostExecute(List<Etudiant> flickItems) {
            super.onPostExecute(lst);
            final RecyclerView view = (RecyclerView) findViewById(R.id.rv);
            view.setAdapter(new MyAdapter(getApplicationContext(), lst));

        }

    }
    }
}*/