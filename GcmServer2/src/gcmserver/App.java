package gcmserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import database.UserDao;
import gcmserver.Content;

public class App 
{
    public static void main( String[] args)
    {
    	//static voor testing
    	String username = "isaac";
    	UserDao userDao = new UserDao();
    	userDao.createUuid(username);
    	//einde
    	
        System.out.println( "Sending POST to GCM" );
        String apiKey = "AIzaSyC3clKuTxILxby8euNiyO9dqTJy2wqCWcg";
        Content content = createContent(username);

        PostGcm.post(apiKey, content);
    }

    public static Content createContent(String username){
    	
    	HashMap<String, String> map = new HashMap<String, String>();
        Content c = new Content();
        UserDao userDao = new UserDao();
        
        map = userDao.getUserInfo(username);
        String uuid = map.get("uuid");
        String regId = map.get("regid");
        
        c.addRegId(regId);
        //c.addRegId("APA91bEXu_2F1q0tBggVJwehyvdamnHO7yqzHV85JZ_jtOYGPqCGg2WLmy0bk6SJJBweyU1pIwHcgiDsOD6tDnUMxcSbsCuX8T_hjaOLUz15Q8yH1W94OcKaNmCzOzSpR3aAfyrxHsgcGJl7VWqWhA3oBFPabyBvphIvrsadh9LIzXsye9fuJts");
        c.createData(uuid, username);

        return c;
    }
}
