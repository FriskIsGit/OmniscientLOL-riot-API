package lol.ranks;

public class LeagueRank{
    //tier is one of the below
    public static final int
            UNRANKED = 0,
            IRON = 5,
            BRONZE = 9,
            SILVER = 13,
            GOLD = 17,
            PLATINUM = 21,
            DIAMOND = 25,
            MASTER = 26,
            GRANDMASTER = 28,
            CHALLENGER = 30;

    private Queue queue; //queue could be modifiable on the fly because it doesn't need to be validated
    private final int tier;
    private final int division; //must be 1 for tiers which don't have divisions, 1-4 for Iron and 1-5 for the remaining

    public static int divisionScore(int tier, int division){
        switch (tier){
            case IRON:
            case BRONZE:
            case SILVER:
            case GOLD:
            case PLATINUM:
            case DIAMOND:
                return tier - division;
            case MASTER:
                return 26;
            case GRANDMASTER:
                return 28;
            case CHALLENGER:
                return 30;
            case UNRANKED:
            default:
                return 0;
        }
    }
    public int divisionScore(){
        return divisionScore(tier, division);
    }

    public static int division(String rank){
        switch (rank){
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            default:
                throw new IllegalArgumentException("Invalid division: " + rank);
        }
    }

    public static int tier(String tier){
        switch (tier){
            case "UNRANKED":
                return UNRANKED;
            case "IRON":
                return IRON;
            case "BRONZE":
                return BRONZE;
            case "SILVER":
                return SILVER;
            case "GOLD":
                return GOLD;
            case "PLATINUM":
                return PLATINUM;
            case "DIAMOND":
                return DIAMOND;
            case "MASTER":
                return MASTER;
            case "GRANDMASTER":
                return GRANDMASTER;
            case "CHALLENGER":
                return CHALLENGER;
            default:
                throw new IllegalArgumentException("Invalid tier: " + tier);
        }
    }

    //for old ranks
    public static LeagueRank fromDivisionScore(int divisionScore){
        if(divisionScore == 0){
            return new LeagueRank(UNRANKED);
        }
        //divisionScore for IRON4 is 1
        if(1 <= divisionScore && divisionScore <= 4){
            return new LeagueRank(IRON, IRON-divisionScore);
        }
        if(5 <= divisionScore && divisionScore <= 8){
            return new LeagueRank(BRONZE, BRONZE-divisionScore);
        }
        if(9 <= divisionScore && divisionScore <= 12){
            return new LeagueRank(SILVER, SILVER-divisionScore);
        }
        if(13 <= divisionScore && divisionScore <= 16){
            return new LeagueRank(GOLD, GOLD-divisionScore);
        }
        if(17 <= divisionScore && divisionScore <= 20){
            return new LeagueRank(PLATINUM, PLATINUM-divisionScore);
        }
        if(21 <= divisionScore && divisionScore <= 25){
            return new LeagueRank(DIAMOND, Math.max(1, DIAMOND-divisionScore));
        }
        if(MASTER <= divisionScore && divisionScore <= 27){
            return new LeagueRank(MASTER);
        }
        if(GRANDMASTER <= divisionScore && divisionScore <= 29){
            return new LeagueRank(GRANDMASTER);
        }
        if(CHALLENGER <= divisionScore){
            return new LeagueRank(CHALLENGER);
        }
        throw new IllegalStateException("Invalid division score: " + divisionScore);
    }

    public LeagueRank(int tier, int division, Queue queue){
        validate(tier, division);
        this.queue = queue;
        this.tier = tier;
        this.division = division;
    }

    //MASTER, GRANDMASTER, CHALLENGER
    public LeagueRank(int tier, Queue queue){
        validate(tier);
        this.queue = queue;
        this.tier = tier;
        this.division = 1;
    }
    public LeagueRank(int tier){
        validate(tier);
        this.tier = tier;
        this.division = 1;
    }
    public LeagueRank(int tier, int division){
        validate(tier, division);
        this.tier = tier;
        this.division = division;
    }

    private static void validate(int tier){
        switch (tier){
            case UNRANKED:
            case MASTER:
            case GRANDMASTER:
            case CHALLENGER:
                break;
            default:
                throw new IllegalArgumentException("This tier must have a division: " + tier);
        }
    }

    public boolean higherThan(LeagueRank right){
        if(this.tier == right.tier){
            //whoever has lower division
            return this.division < right.division;
        }
        return this.tier > right.tier;
    }

    public boolean equals(LeagueRank rank){
        return this.division == rank.division && this.tier == rank.tier;
    }

    private static void validate(int tier, int division){
        switch (tier){
            case IRON:
                //IRON didn't exist before 2018 so backward compatibility is not a problem
                if(division < 1 || division > 4){
                    throw new IllegalArgumentException("Invalid division for tier: " + tier + ". Must be 1-4");
                }
                break;
            case BRONZE:
            case SILVER:
            case GOLD:
            case PLATINUM:
            case DIAMOND:
                if(division < 1 || division > 5){
                    throw new IllegalArgumentException("Invalid division for tier: " + tier + ". Must be 1-5");
                }
                break;
            default:
                if(division != 1){
                    System.err.println("DIVISION: " + division);
                    throw new IllegalArgumentException("Invalid division for tier: " + tier + ". Must be 1");
                }
        }
    }

    public String toSimpleRank(){
        switch (tier){
            case UNRANKED:
                return "U";
            case MASTER:
                return "M";
            case GRANDMASTER:
                return "GM";
            case CHALLENGER:
                return "CH";
            case DIAMOND:
                return "D" + division;
            case PLATINUM:
                return "P" + division;
            case GOLD:
                return "G" + division;
            case SILVER:
                return "S" + division;
            case BRONZE:
                return "B" + division;
            case IRON:
                return "I" + division;
        }
        throw new IllegalStateException("Unreachable");
    }
    public int getDivision(){
        return division;
    }
    public int getTier(){
        return tier;
    }
    public Queue getLadder(){
        return queue;
    }
    @Override
    public String toString(){
        return toSimpleRank();
    }
}

