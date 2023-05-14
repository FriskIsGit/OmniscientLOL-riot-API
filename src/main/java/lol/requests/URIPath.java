package lol.requests;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// Removes brackets and inserts arguments into the path
// api/v1/{arg1}/friends/{arg2}
public class URIPath{
    public String path;
    private URIPath(String path){
        this.path = path;
    }

    public String args(String ...args){
        return setArgs(this.path, args);
    }

    public static URIPath of(String path){
        return new URIPath(path);
    }

    public static String setArgs(String path, String ...args){
        StringBuilder str = new StringBuilder();
        int a = 0;
        int start = 0;
        for (int i = 0; i < path.length(); i++){
            char chr = path.charAt(i);
            if(chr == '{'){
                str.append(path, start, i);
            }
            else if(chr == '}'){
                start = i+1;
                if(a >= args.length){
                    return str.toString();
                }
                String encoded = encode(args[a++]);
                str.append(encoded);
            }
        }
        str.append(path, start, path.length());
        return str.toString();
    }

    private static String encode(String toEncode){
        try{
            return URLEncoder.encode(toEncode, "utf-8");
        }catch (UnsupportedEncodingException ignored){
        }
        throw new IllegalStateException("Unreachable code");
    }

    @Override
    public String toString(){
        return "URIPath{" +
                "path='" + path + '\'' +
                '}';
    }
}
