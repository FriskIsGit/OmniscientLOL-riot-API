package lol.ranks;

public enum Queue{
    RANKED_SOLO_5x5, RANKED_FLEX_SR, RANKED_TFT_DOUBLE_UP, TFT, ARENA, UNKNOWN;
    public static Queue fromString(String queue){
        switch (queue){
            case "RANKED_FLEX_SR":
            case "FLEX":
                return RANKED_FLEX_SR;
            case "RANKED_SOLO_5x5":
            case "SOLO_DUO":
                return RANKED_SOLO_5x5;
            case "RANKED_TFT_DOUBLE_UP":
            case "TFT_DOUBLE_UP":
                return RANKED_TFT_DOUBLE_UP;
            case "RANKED_TFT":
            case "TFT":
                return TFT;
            case "CHERRY":
                return ARENA;
            default:
                System.err.println("Unknown queue type: " + queue);
                return UNKNOWN;
        }
    }
}
