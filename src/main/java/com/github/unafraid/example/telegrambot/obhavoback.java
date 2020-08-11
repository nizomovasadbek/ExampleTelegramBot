import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class obhavoback {
    private String html = "";
    private String sana = "";
    private String harorat = "";
    private String shahar = "";
    private HashMap<String, String> map = new HashMap<String, String>();
    
    public void init(){
        map.put("Jizzax", "jizzakh");
        map.put("Toshkent", "tashkent");
        map.put("Andijon", "andijan");
        map.put("Buxoro", "bukhara");
        map.put("Guliston", "gulistan");
        map.put("Zarafshon", "zarafshan");
        map.put("Qarshi", "karshi");
        map.put("Navoiy", "navoi");
        map.put("Namangan", "namangan");
        map.put("Nukus", "nukus");
        map.put("Samarqand", "samarkand");
        map.put("Urganch", "urgench");
        map.put("Farg'ona", "ferghana");
        map.put("Xiva", "khiva");
    }
    
    public String getShahar(){
        return shahar;
    }
    
    public obhavoback(String city){
        shahar = city;
        init();
        try{
            URL url = new URL("https://obhavo.uz/"+map.get(city));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader buffer = 
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            while((line = buffer.readLine()) != null){
                html += line + "\n";
            }
            String current_day_class = "\"current-day\"";
            //Bugungi kun klasi
            Pattern pat = Pattern.compile("class=" + current_day_class);
            Matcher most = pat.matcher(html);
            
            while(most.find()){
                String xml = html.substring(most.start()-10, most.end()+30).trim();
                xml = xml.substring(xml.indexOf(">")+1, xml.indexOf("</"));
                sana = xml;
            }
            
            String current_forecast = "\"current-forecast\"";
            
            Pattern ptr = Pattern.compile("class="+current_forecast);
            Matcher most1 = ptr.matcher(html);
            
            while(most1.find()){
                String xml = html.substring(most1.start()-10, most1.end()+225).trim();
                xml = xml.substring(xml.indexOf("<strong>")+8, xml.indexOf("</strong>"));
                harorat = xml;
            }
            
            buffer.close();
            con.disconnect();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getSana(){
        return sana;
    }
    
    public String getHarorat(){
        return harorat;
    }
}
