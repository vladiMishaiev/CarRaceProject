package vladi_yaki_project.raceMVC;

public class Song {
	private String name;
	private long lenth;
	private String url;
	
	public Song(Song song){
		this.name=song.getName();
		this.lenth=song.getLenth();
		this.url=song.getUrl();
	}
	public Song(String name, long lenth, String url) {
		this.name = name;
		this.lenth = lenth;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLenth() {
		return lenth;
	}
	public void setLenth(long lenth) {
		this.lenth = lenth;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "Song [name=" + name + ", lenth=" + lenth + ", url=" + url + "]";
	}
	
}
