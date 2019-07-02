package com.example.pokemontcg;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {
    TextView textView, notification, round, restart;
    ImageView imageView1, imageView2, ball1, ball2, ball3, ball4, ball5, winner;
    ImageButton reset;
    ProgressBar progressBar;
    String currentCard1, currentCard2;
    int score = 0;
    boolean isClicked = false;
    boolean ableToClick = false;
    boolean isClickedReset = true;
    int roundNum = 1;
    ArrayList<Card> downloadedCards = new ArrayList<Card>();
    String[] rarities = {"Common", "Uncommon", "Rare", "Rare Holo", "Rare Ultra", "Rare Secret"};
    static final String BASE_API = "https://api.pokemontcg.io/v1/cards";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textView = findViewById(R.id.info);
        restart = findViewById(R.id.restart);
        round = findViewById(R.id.round);
        notification = findViewById(R.id.notification);
        progressBar = findViewById(R.id.progressBar2);
        imageView1 = findViewById(R.id.card1);
        imageView2 = findViewById(R.id.card2);
        reset = findViewById(R.id.imageButton);
        ball1 = findViewById(R.id.ball1);
        ball2 = findViewById(R.id.ball2);
        ball3 = findViewById(R.id.ball3);
        ball4 = findViewById(R.id.ball4);
        ball5 = findViewById(R.id.ball5);
        winner = findViewById(R.id.winner);
        notification.setVisibility(View.VISIBLE);
        notification.setText("After fetching data, there will be two pictures on the screen. To gain score, you need to pick the rarer picture. " +
                "After you get 5 scores, you win the game.\n\n                   'Touch me to start'");
    }

    protected void retrieveCard(){
        new RetrieveCards().execute();
    }

    protected void showPic(String[] pics){
        new DownloadImageFromInternet().execute(pics[0], pics[1]);
    }

    public void hide(View view){
        if(!isClicked){
            notification.setVisibility(View.GONE);
            isClicked = true;
            retrieveCard();
        }
    }

    public void reset(View view){
        if(!isClickedReset){
            isClickedReset = true;
            round.setText("Round: 1");
            roundNum = 1;
            score = 0;
            ball1.setVisibility(View.GONE);
            ball2.setVisibility(View.GONE);
            ball3.setVisibility(View.GONE);
            ball4.setVisibility(View.GONE);
            ball5.setVisibility(View.GONE);
            winner.setVisibility(View.GONE);
            restart.setVisibility(View.GONE);
            reset.setVisibility(View.GONE);
            showPic(pickPic());
        }
    }

    protected String[] pickPic(){
        Random r = new Random();
        String rarity1="";
        String rarity2="";
        int card1 = 0;
        int card2 = 0;

        while(true){
            card1 = r.nextInt(downloadedCards.size());
            card2 = r.nextInt(downloadedCards.size());
            rarity1 = downloadedCards.get(card1).getRarity();
            rarity2 = downloadedCards.get(card2).getRarity();



            if(!rarity1.equals(rarity2)){
                if(rarity1!=rarity2){
                    break;
                }
            }
        }
        String[] pics = new String[2];
        currentCard1 = rarity1;
        currentCard2 = rarity2;
        pics[0] = downloadedCards.get(card1).getImage();
        pics[1] = downloadedCards.get(card2).getImage();
        return pics;
    }

    public void determineRarity(View view){
        if(ableToClick){
            ableToClick = false;
            int rarityLevel1 = -1;
            int rarityLevel2 = -1;

            for(int i=0;i<rarities.length;i++){
                if(currentCard1.equals(rarities[i])){
                    rarityLevel1 = i;
                }
                if(currentCard2.equals(rarities[i])){
                    rarityLevel2 = i;
                }
            }
            if(rarityLevel1 == -1){
                rarityLevel1 = 0;
            }
            if(rarityLevel2 == -1){
                rarityLevel2 = 0;
            }

            if(view.getId()==R.id.card1){
                if(rarityLevel1>rarityLevel2){
                    Toast.makeText(Game.this, "You Win! Get 1 score!", Toast.LENGTH_LONG).show();
                    score++;
                    if(score==1){
                        ball1.setVisibility(View.VISIBLE);
                    }
                    if(score==2){
                        ball2.setVisibility(View.VISIBLE);
                    }
                    if(score==3){
                        ball3.setVisibility(View.VISIBLE);
                    }
                    if(score==4){
                        ball4.setVisibility(View.VISIBLE);
                    }
                    if(score==5){
                        ball5.setVisibility(View.VISIBLE);
                    }
                }else if (rarityLevel1<rarityLevel2){
                    Toast.makeText(Game.this, "You have lost!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Game.this, rarityLevel1+"--"+rarityLevel2, Toast.LENGTH_LONG).show();
                }
            }else if(view.getId()==R.id.card2){
                if(rarityLevel2>rarityLevel1){
                    Toast.makeText(Game.this, "You Win! Get 1 score!", Toast.LENGTH_LONG).show();
                    score++;
                    if(score==1){
                        ball1.setVisibility(View.VISIBLE);
                    }
                    if(score==2){
                        ball2.setVisibility(View.VISIBLE);
                    }
                    if(score==3){
                        ball3.setVisibility(View.VISIBLE);
                    }
                    if(score==4){
                        ball4.setVisibility(View.VISIBLE);
                    }
                    if(score==5){
                        ball5.setVisibility(View.VISIBLE);
                    }
                }else if(rarityLevel2<rarityLevel1){
                    Toast.makeText(Game.this, "You have lost!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Game.this, rarityLevel1+"--"+rarityLevel2, Toast.LENGTH_LONG).show();
                }
            }

            if(score>=5){
                isClickedReset = false;
                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                winner.setVisibility(View.VISIBLE);
                restart.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
            }else{
                //Toast.makeText(Game.this, "Next Round!", Toast.LENGTH_LONG).show();
                roundNum++;
                round.setText("Round: "+roundNum);
                showPic(pickPic());
            }
        }
    }

    class RetrieveCards extends AsyncTask<Void, Integer, ArrayList<String>> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            textView.setText("Fetching Cards...\n");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> jsons = new ArrayList<String>();
            try {
                for(int i=1;i<101;i++){
                    String API_Url = BASE_API+"?page="+i+"&rarity=Rare|Rare Holo|Uncommon|Common|Rare Ultra|Rare Secret";
                    URL url = new URL(API_Url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        jsons.add(stringBuilder.toString());
                        publishProgress(i);
                    }
                    finally{
                        urlConnection.disconnect();
                    }
                }
                return jsons;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            textView.setText("Fetching("+values[0]+"/100)\n");
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            if(s == null) {
                textView.setText("THERE WAS AN ERROR\n");
            }else{
                textView.setText("Cards Fetched!\n");
            }

            try {
                for(int i=0;i<s.size();i++){
                    JSONObject object = (JSONObject) new JSONObject(s.get(i));
                    JSONArray cards = object.getJSONArray("cards");

                    for(int j=0;j<cards.length();j++){
                        if(cards.getJSONObject(j).has("rarity")&&
                                cards.getJSONObject(j).has("imageUrlHiRes")){
                            String id = cards.getJSONObject(j).getString("id");
                            String rarity = cards.getJSONObject(j).getString("rarity");
                            String image = cards.getJSONObject(j).getString("imageUrlHiRes");
                            Card temp = new Card(id,rarity,image);
                            downloadedCards.add(temp);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textView.setText("Cards Loaded!\n");
            textView.setText("Total: "+String.valueOf(downloadedCards.size()));
            //Toast.makeText(Game.this, String.valueOf(downloadedCards.size()), Toast.LENGTH_LONG).show();
            showPic(pickPic());
        }
    }

    class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            textView.setText("Loading Images...\n");
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap[] doInBackground(String... urls) {
            String url = urls[0];
            String url2 = urls[1];
            Bitmap[] bitmap = new Bitmap[2];
            try {
                InputStream in = new java.net.URL(url).openStream();
                InputStream in2 = new java.net.URL(url2).openStream();
                bitmap[0] = BitmapFactory.decodeStream(in);
                bitmap[1] = BitmapFactory.decodeStream(in2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmap) {
            super.onPostExecute(bitmap);
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(Game.this, "Done!", Toast.LENGTH_SHORT).show();
            imageView1.setImageBitmap(bitmap[0]);
            imageView2.setImageBitmap(bitmap[1]);
            ableToClick = true;
        }
    }
}
