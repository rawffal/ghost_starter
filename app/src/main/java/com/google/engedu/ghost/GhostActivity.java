package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private Button challenge;
    private Button restart;
    private String s;
    private boolean userTurn;
    private Random random = new Random();

    private TextView wordFragment;

    private String text;


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char character = (char) event.getUnicodeChar();
        int ascii = (int) character;
        if((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123)){
            s = wordFragment.getText().toString();
            s += character;
            wordFragment.setText(s);
            computerTurn();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        wordFragment = (TextView)findViewById(R.id.ghostText);

        try {
            InputStream is = getAssets().open("words.txt");
            /*dictionary = new SimpleDictionary(is);*/
            dictionary = new FastDictionary(is);
            Log.d("isWord: ", "" + dictionary.isWord("quotes"));
            Log.d("isWord: ", "" + dictionary.isWord("dog"));
            Log.d("isWord: ", "" + dictionary.isWord("buddy"));
            Log.d("isWord: ", "" + dictionary.isWord("quoting"));
            Log.d("isWord: ", "" + dictionary.isWord("nicetrywingwhy"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        challenge = (Button)findViewById(R.id.challengeButton);
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challenge();
            }
        });

        restart = (Button)findViewById(R.id.restartButton);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(null);
            }
        });
        onStart(null);

        if (savedInstanceState != null) {
            wordFragment.setText(savedInstanceState.getString("Ghost Text"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        label.setText(COMPUTER_TURN);
        userTurn = false;
        if(wordFragment.getText().toString().length() >= 4
                && dictionary.isWord(wordFragment.getText().toString())){
            Toast.makeText(this,"Computer wins!", Toast.LENGTH_SHORT).show();
        }
        else{
            String newWord = dictionary.getAnyWordStartingWith(wordFragment.getText().toString());
            if(newWord != null){
                s = wordFragment.getText().toString();
                if(newWord.length() > s.length()){
                    s += newWord.substring(s.length(), s.length() + 1);
                }
                wordFragment.setText(s);
            }
            else challenge();
        }

        userTurn = true;

        label.setText(USER_TURN);
    }

    private void challenge() {
        if(!userTurn){
            String newWord = dictionary.getAnyWordStartingWith(wordFragment.getText().toString());
            if(newWord != null){ // word can be formed from fragment
                Toast.makeText(this,"Player wins!", Toast.LENGTH_SHORT).show();
            }
            else{// no word can be formed from fragment
                Toast.makeText(this,"Computer wins!", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            if(wordFragment.getText().toString().length() >= 4
                    && dictionary.isWord(wordFragment.getText().toString())){
                Toast.makeText(this,"Player wins!", Toast.LENGTH_SHORT).show();
            }
            else {
                String newWord = dictionary.getAnyWordStartingWith(wordFragment.getText().toString());
                if(newWord != null){ // word can be formed from fragment
                    Toast.makeText(this,"Computer wins!", Toast.LENGTH_SHORT).show();
                    wordFragment.setText(newWord);
                }
                else{ // no word can be formed from fragment
                    Toast.makeText(this,"Player wins!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wordFragment = (TextView) findViewById(R.id.ghostText);
        outState.putString("Ghost Text", wordFragment.getText().toString());
    }
}