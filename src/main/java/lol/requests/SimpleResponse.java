package lol.requests;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SimpleResponse{
    public int code;
    public String body;
    public long timeTakenMs;

    public SimpleResponse(int code, String body){
        this.code = code;
        this.body = body;
    }

    public static Option<SimpleResponse> performRequest(Request request) {
        InputStream responseAsStream = null;
        try{
            long start = System.currentTimeMillis();
            Response response = request.execute();
            HttpResponse consumedResponse = response.returnResponse();
            long end = System.currentTimeMillis();
            int code = consumedResponse.getStatusLine().getStatusCode();
            responseAsStream = consumedResponse.getEntity().getContent();
            SimpleResponse simpleResponse = new SimpleResponse(code, streamToString(responseAsStream));
            simpleResponse.timeTakenMs = end-start;
            return Option.of(simpleResponse);
        }catch (IOException e){
            e.printStackTrace();
            return Option.none();
        }finally{
            if(responseAsStream != null){
                try{
                    responseAsStream.close();
                }catch (IOException ignored){
                }
            }
        }
    }

    @Override
    public String toString(){
        return "[" + code + "]\n" + body + "\nTime taken ms: " + timeTakenMs;
    }

    public static String streamToString(InputStream inputStream){
        if(inputStream == null){
            return "";
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder content = new StringBuilder();
        boolean oneError = false;
        while (true) {
            try{
                if ((line = br.readLine()) == null){
                    break;
                }
                content.append(line).append('\n');
            }catch (IOException e){
                if(oneError){
                    break;
                }
                oneError = true;
                System.out.println(e.getMessage());
            }
        }
        return content.toString();
    }
}
