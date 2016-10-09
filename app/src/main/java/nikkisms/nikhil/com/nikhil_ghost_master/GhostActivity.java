package nikkisms.nikhil.com.nikhil_ghost_master;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";

    TextView tvGhostText, tvGameStatus;
    Button btnChallenge, btnRestart;

    private GhostDictionary dictionary;
    private boolean userTurn = false;

    private Random random = new Random();
    private String stringFragment = "";
    private String challengeString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        tvGhostText = (TextView) findViewById(R.id.tv_ghost_text);
        tvGameStatus = (TextView) findViewById(R.id.tv_game_status);

        btnChallenge = (Button) findViewById(R.id.btn_challenge);
        btnRestart = (Button) findViewById(R.id.btn_restart);

        btnChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengeComputer();
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(view);
            }
        });

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            // FastDictionary uses Tries while
            // SimpleDictionary uses Binary Search
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        onStart(null);
    }

    private void challengeComputer() {

        challengeString = dictionary.getAnyWordStartingWith(stringFragment);


        if (stringFragment.length() >= 4 && dictionary.isWord(stringFragment)) {
            challengeString = stringFragment + " is a valid word!";
            tvGhostText.setText(challengeString);
            tvGameStatus.setText("Player wins!");
        } else {
            tvGameStatus.setText("Computer wins!" + "\n" + "The suitable word can be: " + challengeString);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char userInput = (char) event.getUnicodeChar();
        userInput = Character.toLowerCase(userInput);

        if (userInput < 'a' || userInput > 'z') {
            tvGameStatus.setText("Invalid key.");
            return super.onKeyUp(keyCode, event);
        } else {
            tvGameStatus.setText("Valid key.");
            stringFragment = (stringFragment + event.getDisplayLabel()).toLowerCase();
            tvGhostText.setText(stringFragment);

            userTurn = false;
            tvGameStatus.setText(COMPUTER_TURN);
            computerTurn();
            return true;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("stringFragment", stringFragment);
        outState.putBoolean("userTurn", userTurn);
        outState.putString("challengeString",challengeString);
        outState.putString("tvGhostText", tvGhostText.getText().toString());
        outState.putString("tvGameStatus", tvGameStatus.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        stringFragment = savedInstanceState.getString("stringFragment");
        userTurn = savedInstanceState.getBoolean("userTurn");
        tvGhostText.setText(savedInstanceState.getString("tvGhostText"));
        tvGameStatus.setText(savedInstanceState.getString("tvGameStatus"));
        challengeString = savedInstanceState.getString("challengeString");
    }

    /**
     * Handler for the "Restart" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();

        stringFragment = "";
        tvGhostText.setText("");

        if (userTurn) {
            tvGameStatus.setText(USER_TURN);
        } else {
            tvGameStatus.setText(COMPUTER_TURN);
            computerTurn();
        }

        return true;
    }

    private void computerTurn() {
        String computerString;

        if (stringFragment.length() == 0) {
            char randChar = (char) (random.nextInt(26) + 97);
            stringFragment = "";
            stringFragment += randChar;
            tvGhostText.setText(stringFragment);

            userTurn = true;
            tvGameStatus.setText(USER_TURN);
            return;
        }

        if (stringFragment != null && stringFragment.length() >= 4) {
            if (dictionary.isWord(stringFragment)) {
                computerString = stringFragment + " is a word!";
                tvGhostText.setText(computerString);
                tvGameStatus.setText("Computer Wins!");
                return;
            }
        }

        computerString = dictionary.getAnyWordStartingWith(stringFragment);

        if (computerString == null) {
            computerString = "A valid word cannot be formed with " + stringFragment;
            tvGhostText.setText(computerString);
            tvGameStatus.setText("Computer Wins!");
        } else {
            char nextChar = computerString.charAt(stringFragment.length());
            stringFragment += nextChar;
            tvGhostText.setText(stringFragment);

            userTurn = true;
            tvGameStatus.setText(USER_TURN);
        }
    }
}

