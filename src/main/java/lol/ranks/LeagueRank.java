package lol.ranks;

// https://images.contentstack.io/v3/assets/blt731acb42bb3d1659/blt2a8d492cdc35e583/647e7feeeb65a1ce636e1098/Live_end_of_Season_CHART_03.jpg
public class LeagueRank{
    // tier always represents 1st division
    public static final int
            UNRANKED = 0,
            IRON = 4,     //+4
            BRONZE = 12,
            SILVER = 17,  //+1
            GOLD = 21,
            PLATINUM = 25,
            EMERALD = 30, //+1
            DIAMOND = 39, //+5
            MASTER = 42,
            GRANDMASTER = 45,
            CHALLENGER = 49;

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
            case EMERALD:
            case DIAMOND:
                return tier - division + 1;
            case MASTER:
                return MASTER;
            case GRANDMASTER:
                return GRANDMASTER;
            case CHALLENGER:
                return CHALLENGER;
            case UNRANKED:
            default:
                return UNRANKED;
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
            case "EMERALD":
                return EMERALD;
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
        if(divisionScore <= IRON-4){
            return new LeagueRank(UNRANKED);
        }
        if(divisionScore <= IRON){
            return new LeagueRank(IRON, IRON-divisionScore + 1);
        }
        if(divisionScore <= BRONZE-4){ // exactly between IRON & BRONZE
            boolean scalesUp = scalesUp(IRON+1, BRONZE-4, divisionScore);
            return scalesUp ? new LeagueRank(BRONZE, 4) : new LeagueRank(IRON, 1);
        }
        if(divisionScore <= BRONZE){
            return new LeagueRank(BRONZE, BRONZE-divisionScore + 1);
        }
        if(divisionScore <= SILVER-4){ // exactly between BRONZE & SILVER
            boolean scalesUp = scalesUp(BRONZE+1, SILVER-4, divisionScore);
            return scalesUp ? new LeagueRank(SILVER, 4) : new LeagueRank(BRONZE, 1);
        }
        if(divisionScore <= SILVER){
            return new LeagueRank(SILVER, SILVER-divisionScore + 1);
        }
        if(divisionScore <= GOLD){
            return new LeagueRank(GOLD, GOLD-divisionScore + 1);
        }
        if(divisionScore <= PLATINUM){
            return new LeagueRank(PLATINUM, PLATINUM-divisionScore + 1);
        }
        if(divisionScore <= EMERALD-4){ // exactly between PLATINUM & EMERALD
            boolean scalesUp = scalesUp(PLATINUM+1, EMERALD-4, divisionScore);
            return scalesUp ? new LeagueRank(EMERALD, 4) : new LeagueRank(PLATINUM, 1);
        }
        if(divisionScore <= EMERALD){
            return new LeagueRank(EMERALD, EMERALD-divisionScore + 1);
        }
        if(divisionScore <= DIAMOND-4){ // exactly between EMERALD & DIAMOND
            boolean scalesUp = scalesUp(EMERALD+1, DIAMOND-4, divisionScore);
            return scalesUp ? new LeagueRank(DIAMOND, 4) : new LeagueRank(EMERALD, 1);
        }
        if(divisionScore <= DIAMOND){
            return new LeagueRank(DIAMOND, DIAMOND-divisionScore + 1);
        }
        if(divisionScore < MASTER){ //exactly between DIAMOND & MASTER
            boolean scalesUp = scalesUp(DIAMOND+1, MASTER, divisionScore);
            return scalesUp ? new LeagueRank(MASTER) : new LeagueRank(DIAMOND, 1);
        }
        if(divisionScore < GRANDMASTER){
            return new LeagueRank(MASTER);
        }
        if(divisionScore < CHALLENGER){
            return new LeagueRank(GRANDMASTER);
        }
        return new LeagueRank(CHALLENGER);
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

    //inclusive range
    private static boolean scalesUp(int lower, int higher, int val){
        double mid = (lower + higher) / 2D;
        return mid <= val;
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
            case EMERALD:
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
            case EMERALD:
                return "E" + division;
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
        throw new IllegalStateException("Tier " + tier + " couldn't be resolved to a rank");
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

