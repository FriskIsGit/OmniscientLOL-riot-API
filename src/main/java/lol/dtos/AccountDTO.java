package lol.dtos;

import com.alibaba.fastjson.JSONObject;

public class AccountDTO{
    public String puuid;
    public String gameName;
    public String tagLine;

    public static AccountDTO fromJson(String json){
        if(json == null)
            return null;

        AccountDTO account = new AccountDTO();
        JSONObject jsonSummoner = JSONObject.parseObject(json);
        account.tagLine = jsonSummoner.getString("tagLine");
        account.gameName = jsonSummoner.getString("gameName");
        account.puuid = jsonSummoner.getString("puuid");
        return account;
    }

    @Override
    public String toString(){
        return "AccountDTO{" +
                "puuid='" + puuid + '\'' +
                ", gameName='" + gameName + '\'' +
                ", tagLine='" + tagLine + '\'' +
                '}';
    }
}
