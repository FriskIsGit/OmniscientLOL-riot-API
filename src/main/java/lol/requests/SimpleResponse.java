package lol.requests;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.io.InputStream;

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
        byte[] buffer = new byte[32768];
        try {
            int writeOffset = 0;
            while (inputStream.available() != 0) {
                int available = inputStream.available();
                if(available + writeOffset < buffer.length){
                    int currentRead = inputStream.read(buffer, writeOffset, available);
                    writeOffset += currentRead;
                }else{
                    byte[] tempBuffer = new byte[buffer.length<<1];
                    System.arraycopy(buffer, 0, tempBuffer, 0, writeOffset);
                    buffer = tempBuffer;
                    tempBuffer = null;
                }
            }
            return new String(buffer, 0, writeOffset);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Stream closed?");
        }
        return null;
    }
}
