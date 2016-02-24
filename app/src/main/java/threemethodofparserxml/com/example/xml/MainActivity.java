package threemethodofparserxml.com.example.xml;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saxButton;
    private Button pullButton;
    private Button domButton;
    private ArrayList<Person> personList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (personList ==null){
            personList = new ArrayList<Person>();
        }

        saxButton = (Button)findViewById(R.id.sax_parserxml);
        pullButton = (Button)findViewById(R.id.pull_parserxml);
        domButton = (Button)findViewById(R.id.dom_parserxml);

        saxButton.setOnClickListener(this);
        pullButton.setOnClickListener(this);
        domButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sax_parserxml:
                readxmlForSAX();
                break;
            case R.id.pull_parserxml:
                readxmlForPULL();
                break;
            case R.id.dom_parserxml:
                break;
            default:
                break;
        }
    }

    private void readxmlForSAX(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SaxHandler sh = null;
                try {
                    sh = new SaxHandler(personList);
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser parser = factory.newSAXParser();
                    parser.parse(intputStream(), sh);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (intputStream()!=null){
                            intputStream().close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private void readxmlForPULL(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Person person = null;
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(intputStream(), "UTF-8");

                    int eventType = parser.getEventType();
                    while (eventType!=XmlPullParser.END_DOCUMENT){
                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                personList = new ArrayList<Person>();
                                break;
                            case XmlPullParser.START_TAG:
                                if("name".equals(parser.getName())){
                                    person = new Person();
                                    person.setName(parser.nextText());
                                    Log.d("PULL", "正在读取name节点");
                                }else if ("age".equals(parser.getName())){
                                    person.setAge(Integer.parseInt(parser.nextText()));
                                    Log.d("PULL", "正在读取age节点");
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if ("person".equals(parser.getName())){
                                    personList.add(person);
                                    Log.d("PULL", "Person节点读取完成"+person.toString());
                                    person = null;
                                }
                                break;
                        }
                        eventType = parser.next();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(intputStream()!=null){
                        try {
                            intputStream().close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }


            }
        }).start();
    }


    public InputStream intputStream(){
        InputStream is = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://10.0.2.2:8088/data.xml");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            is = connection.getInputStream();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return is;
    }
}
