public class CountryPlayerCount {
    private String country;
    private int playerCount;
    
    public CountryPlayerCount (String country, int playerCount) {
        this.country = country;
        this.playerCount = playerCount;
    }
    
    public String getCountry () {
        return country;
    }
    
    public int getPlayerCount () {
        return playerCount;
    }
    
    public void setCountry (String country) {
        this.country = country;
    }
    
    public void setPlayerCount (int playerCount) {
        this.playerCount = playerCount;
    }
}
