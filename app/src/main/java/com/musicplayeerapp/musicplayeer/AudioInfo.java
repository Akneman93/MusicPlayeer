package com.musicplayeerapp.musicplayeer;



public class AudioInfo {
    private String Album = "unknown";
    private String Artist = "unknown";
    private String Compostion = "unknown";
    private String Genre = "unknown";
    private String Data = null;

    public AudioInfo(){};
    public AudioInfo(AudioInfo audioinfo)
    {
        Album = audioinfo.Album;
        Artist = audioinfo.Artist;
        Compostion = audioinfo.Compostion;
        Genre = audioinfo.Genre;
        Data = audioinfo.Data;
    }

    public AudioInfo setAlbum(String album) {Album = album; return this;}
    public AudioInfo setArtist(String artist) {Artist = artist; return this;}
    public AudioInfo setComposition(String compostion) {Compostion = compostion; return this;}
    public AudioInfo setGenre(String genre) {Genre = genre; return this;}
    public AudioInfo setData(String data) {Data = data; return this;}


    public String getAlbum() {return Album;}
    public String getArtist() {return Artist;}
    public String getComposition() {return Compostion;}
    public String getGenre() {return Genre;}
    public String getData() {return Data;}

    static AudioInfo generateDummy(int i)
    {
        return new AudioInfo().setAlbum("Dummy_Album_" + i)
                              .setArtist("Dummy_Artist_" + i)
                              .setComposition("Dummy_Composition_" + i)
                              .setGenre("Dummy_Genre_" + i)
                              .setData(null);

    }




}
