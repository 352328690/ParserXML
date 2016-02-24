package threemethodofparserxml.com.example.xml;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/2/1.
 */
public class SaxHandler extends DefaultHandler{

    private Person person;
    private ArrayList<Person> mList;
    private String nodeName;
    private StringBuilder name;
    private StringBuilder age;
    private StringBuilder sb;

    public SaxHandler(ArrayList<Person> mList) {
        this.mList = mList;
    }

    public ArrayList<Person> getmList() {
        return mList;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        Log.d("SAX", "读取到文档头，开始SAX解析XML。。。");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        Log.d("SAX", "开始处理"+nodeName+"元素数据");
        person = new Person();
        sb = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        sb.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        mList.add(person);
        String s = sb.toString();
        switch (localName){
            case "name":
                person.setName(s);
                break;
            case "age":
                person.setAge(Integer.parseInt(s));
                break;
            default:
                break;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        Log.d("SAX", "识别到文档尾，xml解析结束。");
    }
}
