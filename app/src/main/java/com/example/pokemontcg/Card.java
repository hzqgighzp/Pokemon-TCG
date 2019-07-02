package com.example.pokemontcg;

public class Card {
    private String id;
    private String rarity;
    private String image;

    public Card(String id, String rarity, String image){
        this.id = id;
        this.rarity = rarity;
        this.image = image;
    }

    public String getId(){
        return this.id;
    }

    public String getRarity() {
        return this.rarity;
    }

    public String getImage() {
        return this.image;
    }
}
