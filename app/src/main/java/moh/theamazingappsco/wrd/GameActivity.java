package moh.theamazingappsco.wrd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {
    TextView txtTriads, txtGameCode;
    Button btnCopy,btnReady,btnSend;
    EditText etInput;

    String gameCode,iAm;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtTriads = (TextView)findViewById(R.id.txtLetters);
        txtGameCode = (TextView)findViewById(R.id.txtGameCode);
        btnCopy = (Button)findViewById(R.id.btnCopy);
        btnReady = (Button)findViewById(R.id.btnReady);
        btnSend = (Button)findViewById(R.id.btnSend);
        etInput = (EditText)findViewById(R.id.etInput);

        txtTriads.setVisibility(View.INVISIBLE);
        btnSend.setVisibility(View.INVISIBLE);
        etInput.setVisibility(View.INVISIBLE);

        gameCode = (String) getIntent().getSerializableExtra("LobbyCode");
        iAm = (String) getIntent().getSerializableExtra("iam");

        txtGameCode.setText(gameCode);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(gameCode);

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        if((userData.get("OPGame1")!=(null)) && (userData.get("NOTOPGame1")!=(null))){
                            String triad1 = (String) userData.get("Game1");
                            txtTriads.setText(triad1);
                            txtTriads.setVisibility(View.VISIBLE);
                            btnSend.setVisibility(View.VISIBLE);
                            etInput.setVisibility(View.VISIBLE);
                            txtGameCode.setVisibility(View.INVISIBLE);
                            btnCopy.setVisibility(View.INVISIBLE);
                            btnReady.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newPost = mFirebaseDatabase.push();
                if(iAm.equals("OP")){
                    newPost.child("OPGame1").setValue("YES!");
                }else if(iAm.equals("NOTOP")){
                    newPost.child("NOTOPGame1").setValue("YES!");
                }
                btnReady.setEnabled(false);
            }
        });
    }
}