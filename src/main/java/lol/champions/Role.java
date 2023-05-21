package lol.champions;

public enum Role{
    TOP,
    JUNGLE,
    MID,
    ADC,
    SUPPORT;

    public static Role from(char identifier){
        switch (identifier){
            case 'T':
                return TOP;
            case 'J':
                return JUNGLE;
            case 'M':
                return MID;
            case 'B':
            case 'A':
                return ADC;
            case 'S':
                return SUPPORT;
            default:
                throw new IllegalArgumentException("Illegal identifier: " + identifier);
        }
    }
}
