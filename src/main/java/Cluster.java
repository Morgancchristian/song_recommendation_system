import java.util.ArrayList;

public class Cluster {
    private ArrayList<Double> centroid;
    private ArrayList<String> songs;
    
    public Cluster() {
        this.centroid = new ArrayList<>();
        this.songs = new ArrayList<>();
    }
    
    public void setCentroid(ArrayList<Double> centroid) {
        this.centroid = new ArrayList<>(centroid);
    }
    
    public ArrayList<Double> getCentroid() {
        return this.centroid;
    }
    
    public void addSong(String song) {
        this.songs.add(song);
    }
    
    public void clearSongs() {
        this.songs.clear();
    }
    
    public ArrayList<String> getSongs() {
        return this.songs;
    }
}
