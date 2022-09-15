package moh.theamazingappsco.wrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText etGameCode;
    Button btnPlay,btnNew;

    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etGameCode = (EditText) findViewById(R.id.etGameCode);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnNew = (Button) findViewById(R.id.btnNew);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = getRandomCode();
                Log.d("MOHYEA","Code : "+code);
                mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(code);
                DatabaseReference newPost = mFirebaseDatabase.push();

                ArrayList<String> triads = get5Triads();
                newPost.child("Game1").setValue(triads.get(0));
                newPost.child("Game2").setValue(triads.get(1));
                newPost.child("Game3").setValue(triads.get(2));
                newPost.child("Game4").setValue(triads.get(3));
                newPost.child("Game5").setValue(triads.get(4));
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                i.putExtra("LobbyCode",code);
                i.putExtra("iam","OP");
                startActivity(i);
                finish();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String roomCode = etGameCode.getText().toString();
                mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(roomCode);
                if(mFirebaseDatabase != null){
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    i.putExtra("LobbyCode",roomCode);
                    i.putExtra("iam","NOTOP");
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    private ArrayList<String> get5Triads() {
        List<String> list = new ArrayList<String>(Arrays.asList("e","t","a","o","i","n","s","h","r","d","l","c","u","m"));
        int numberOfElements = 3;
        ArrayList<String> my5Triads = new ArrayList<String>();
        for(int i=0;i<5;i++) {
            my5Triads.add(createString(getTriad(list, numberOfElements)));
        }
        return my5Triads;
    }

    private String createString(List<String> triad) {
        return(triad.get(0)+""+triad.get(1)+""+triad.get(2));
    }

    private List<String>getTriad(List<String> list, int totalItems){
        Random rand = new Random();
        List<String> templist = new ArrayList<String>(list);
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = rand.nextInt(templist.size());
            newList.add(templist.get(randomIndex));
            templist.remove(randomIndex);
        }
        return newList;
    }

    private String getRandomCode() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}