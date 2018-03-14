package com.example.android.scorekeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String P1, P2;
    public int n;

    class State {
        int P1_points, P1_set1_games, P1_set2_games, P1_set3_games, P1_wonSets;
        int P2_points, P2_set1_games, P2_set2_games, P2_set3_games, P2_wonSets;
        int currentSet, gameOver;
    }

    //I'll be storing current scores in a State class so I can easily undo mistake
    State[] Gamestate;
    ImageView P1_name_1, P1_name_2, P1_name_3, P1_name_4, P1_name_5, P1_name_6, P1_name_7, P1_set1, P1_set2, P1_set3, P1_advantage, P1_points_1, P1_points_10;
    ImageView P2_name_1, P2_name_2, P2_name_3, P2_name_4, P2_name_5, P2_name_6, P2_name_7, P2_set1, P2_set2, P2_set3, P2_advantage, P2_points_1, P2_points_10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        P1 = "P1";
        P2 = "P2";
        initialize();

    }

    void initialize() {
        //find views
        P1_name_1 = findViewById(R.id.P1_name_1);
        P1_name_2 = findViewById(R.id.P1_name_2);
        P1_name_3 = findViewById(R.id.P1_name_3);
        P1_name_4 = findViewById(R.id.P1_name_4);
        P1_name_5 = findViewById(R.id.P1_name_5);
        P1_name_6 = findViewById(R.id.P1_name_6);
        P1_name_7 = findViewById(R.id.P1_name_7);
        P1_set1 = findViewById(R.id.P1_set1);
        P1_set2 = findViewById(R.id.P1_set2);
        P1_set3 = findViewById(R.id.P1_set3);
        P1_points_1 = findViewById(R.id.P1_points_1);
        P1_points_10 = findViewById(R.id.P1_points_10);
        P1_advantage = findViewById(R.id.P1_advantage);

        P2_name_1 = findViewById(R.id.P2_name_1);
        P2_name_2 = findViewById(R.id.P2_name_2);
        P2_name_3 = findViewById(R.id.P2_name_3);
        P2_name_4 = findViewById(R.id.P2_name_4);
        P2_name_5 = findViewById(R.id.P2_name_5);
        P2_name_6 = findViewById(R.id.P2_name_6);
        P2_name_7 = findViewById(R.id.P2_name_7);
        P2_set1 = findViewById(R.id.P2_set1);
        P2_set2 = findViewById(R.id.P2_set2);
        P2_set3 = findViewById(R.id.P2_set3);
        P2_points_1 = findViewById(R.id.P2_points_1);
        P2_points_10 = findViewById(R.id.P2_points_10);
        P2_advantage = findViewById(R.id.P2_advantage);

        //initialize gamestates
        n = 3; //here I define how many game states program will remember i.e. how far back I can undo changes
        Gamestate = new State[n + 1];

        for (int i = 0; i < Gamestate.length; i++) {
            Gamestate[i] = new State();   //without this line we would have nothing inside Gamestate[i] so app would crash trying to access its variables
            if (Gamestate[i] == null) System.out.println("something wrong with class");

            Gamestate[i].P1_points = 0;
            Gamestate[i].P2_points = 0;
            Gamestate[i].P1_set1_games = 0;
            Gamestate[i].P1_set2_games = 0;
            Gamestate[i].P1_set3_games = 0;
            Gamestate[i].P2_set1_games = 0;
            Gamestate[i].P2_set2_games = 0;
            Gamestate[i].P2_set3_games = 0;
            Gamestate[i].P1_wonSets = 0;
            Gamestate[i].P2_wonSets = 0;
            Gamestate[i].currentSet = 1;
            Gamestate[i].gameOver = 0;
        }
        displayNames(P1, P2);
        displayState();
    }

    void undo(View view) {
        for (int i = 0; i < Gamestate.length - 1; i++) {
            Gamestate[i].P1_points = Gamestate[i + 1].P1_points;
            Gamestate[i].P2_points = Gamestate[i + 1].P2_points;
            Gamestate[i].P1_set1_games = Gamestate[i + 1].P1_set1_games;
            Gamestate[i].P1_set2_games = Gamestate[i + 1].P1_set2_games;
            Gamestate[i].P1_set3_games = Gamestate[i + 1].P1_set3_games;
            Gamestate[i].P2_set1_games = Gamestate[i + 1].P2_set1_games;
            Gamestate[i].P2_set2_games = Gamestate[i + 1].P2_set2_games;
            Gamestate[i].P2_set3_games = Gamestate[i + 1].P2_set3_games;
            Gamestate[i].P1_wonSets = Gamestate[i + 1].P1_wonSets;
            Gamestate[i].P2_wonSets = Gamestate[i + 1].P2_wonSets;
            Gamestate[i].currentSet = Gamestate[i + 1].currentSet;
            Gamestate[i].gameOver = Gamestate[i + 1].gameOver;
        }
        displayState();
    }

    void P1Scored(View view) {
        if (Gamestate[0].gameOver == 0) {
            backupScore();
            Gamestate[0].P1_points++;
            // check if the game is won
            if (Gamestate[0].P1_points >= 4 && Gamestate[0].P1_points > Gamestate[0].P2_points + 1) {
                Gamestate[0].P1_points = 0;
                Gamestate[0].P2_points = 0;
                if (Gamestate[0].currentSet == 1) {
                    Gamestate[0].P1_set1_games++;
                    //check if the set is won
                    if (Gamestate[0].P1_set1_games == 7 || (Gamestate[0].P1_set1_games == 6 && Gamestate[0].P2_set1_games < 5)) {
                        Gamestate[0].P1_wonSets++;
                        Gamestate[0].currentSet++;
                        showToast(P1 + " " + getString(R.string.set));
                    }
                } else if (Gamestate[0].currentSet == 2) {
                    Gamestate[0].P1_set2_games++;
                    //check if the set is won
                    if (Gamestate[0].P1_set2_games == 7 || (Gamestate[0].P1_set2_games == 6 && Gamestate[0].P2_set2_games < 5)) {
                        Gamestate[0].P1_wonSets++;
                        Gamestate[0].currentSet++;
                        showToast(P1 + " " + getString(R.string.set));
                    }
                } else {
                    Gamestate[0].P1_set3_games++;
                    //check if the set is won
                    if (Gamestate[0].P1_set3_games == 7 || (Gamestate[0].P1_set3_games == 6 && Gamestate[0].P2_set3_games < 5)) {
                        Gamestate[0].P1_wonSets++;
                        Gamestate[0].currentSet++;
                    }
                }
                //check if the match is won
                if (Gamestate[0].P1_wonSets >= 2) {
                    Gamestate[0].gameOver=1;
                    showToast(P1 + " " + getString(R.string.match));
                }
            }
            displayState();
        } else showToast(getString(R.string.gameOver));
    }

    void P2Scored(View view) {
        if (Gamestate[0].gameOver == 0) {
            backupScore();

            Gamestate[0].P2_points++;
            // check if the game is won
            if (Gamestate[0].P2_points >= 4 && Gamestate[0].P2_points > Gamestate[0].P1_points + 1) {
                Gamestate[0].P1_points = 0;
                Gamestate[0].P2_points = 0;
                if (Gamestate[0].currentSet == 1) {
                    Gamestate[0].P2_set1_games++;
                    //check if the set 1 is won
                    if (Gamestate[0].P2_set1_games == 7 || (Gamestate[0].P2_set1_games == 6 && Gamestate[0].P1_set1_games < 5)) {
                        Gamestate[0].P2_wonSets++;
                        Gamestate[0].currentSet++;
                        showToast(P2 + " " + getString(R.string.set));
                    }
                } else if (Gamestate[0].currentSet == 2) {
                    Gamestate[0].P2_set2_games++;
                    //check if the set 2 is won
                    if (Gamestate[0].P2_set2_games == 7 || (Gamestate[0].P2_set2_games == 6 && Gamestate[0].P1_set2_games < 5)) {
                        Gamestate[0].P2_wonSets++;
                        Gamestate[0].currentSet++;
                        showToast(P2 + " " + getString(R.string.set));
                    }
                } else {
                    Gamestate[0].P2_set3_games++;
                    //check if the set is won
                    if (Gamestate[0].P2_set3_games == 7 || (Gamestate[0].P2_set3_games == 6 && Gamestate[0].P1_set3_games < 5)) {
                        Gamestate[0].P2_wonSets++;
                        Gamestate[0].currentSet++;
                    }
                }
                //check if the match is won
                if (Gamestate[0].P2_wonSets >= 2) {
                    Gamestate[0].gameOver=1;
                    showToast(P2 + " " + getString(R.string.match));
                }
            }

            displayState();
        } else showToast(getString(R.string.gameOver));
    }

    void backupScore() {
        for (int i = Gamestate.length - 1; i > 0; i--) {
            Gamestate[i].P1_points = Gamestate[i - 1].P1_points;
            Gamestate[i].P2_points = Gamestate[i - 1].P2_points;
            Gamestate[i].P1_set1_games = Gamestate[i - 1].P1_set1_games;
            Gamestate[i].P1_set2_games = Gamestate[i - 1].P1_set2_games;
            Gamestate[i].P1_set3_games = Gamestate[i - 1].P1_set3_games;
            Gamestate[i].P2_set1_games = Gamestate[i - 1].P2_set1_games;
            Gamestate[i].P2_set2_games = Gamestate[i - 1].P2_set2_games;
            Gamestate[i].P2_set3_games = Gamestate[i - 1].P2_set3_games;
            Gamestate[i].P1_wonSets = Gamestate[i - 1].P1_wonSets;
            Gamestate[i].P2_wonSets = Gamestate[i - 1].P2_wonSets;
            Gamestate[i].currentSet = Gamestate[i - 1].currentSet;
        }

    }

    void reset(View view) {
        initialize();
    }

    void displayState() {
        //display sets
        char temp;
        temp = Character.forDigit(Gamestate[0].P1_set1_games, 10);
        displayCharacter(P1_set1, temp);

        temp = Character.forDigit(Gamestate[0].P1_set2_games, 10);
        displayCharacter(P1_set2, temp);

        temp = Character.forDigit(Gamestate[0].P1_set3_games, 10);
        displayCharacter(P1_set3, temp);

        temp = Character.forDigit(Gamestate[0].P2_set1_games, 10);
        displayCharacter(P2_set1, temp);

        temp = Character.forDigit(Gamestate[0].P2_set2_games, 10);
        displayCharacter(P2_set2, temp);

        temp = Character.forDigit(Gamestate[0].P2_set3_games, 10);
        displayCharacter(P2_set3, temp);

        //display advantage
        if (Gamestate[0].P1_points >= 3 && Gamestate[0].P2_points >= 3 && Gamestate[0].P1_points > Gamestate[0].P2_points) {
            P1_advantage.setImageResource(R.drawable.advantage);
        } else if (Gamestate[0].P1_points >= 3 && Gamestate[0].P2_points >= 3 && Gamestate[0].P2_points > Gamestate[0].P1_points) {
            P2_advantage.setImageResource(R.drawable.advantage);
        } else {
            P2_advantage.setImageResource(R.drawable.blank);
            P1_advantage.setImageResource(R.drawable.blank);
        }

        //display points
        if (Gamestate[0].P1_points == 0) {
            displayCharacter(P1_points_10, '0');
            displayCharacter(P1_points_1, '0');
        } else if (Gamestate[0].P1_points == 1) {
            displayCharacter(P1_points_10, '1');
            displayCharacter(P1_points_1, '5');
        } else if (Gamestate[0].P1_points == 2) {
            displayCharacter(P1_points_10, '3');
            displayCharacter(P1_points_1, '0');
        } else if (Gamestate[0].P1_points >= 3) {
            displayCharacter(P1_points_10, '4');
            displayCharacter(P1_points_1, '0');
        }

        if (Gamestate[0].P2_points == 0) {
            displayCharacter(P2_points_10, '0');
            displayCharacter(P2_points_1, '0');
        } else if (Gamestate[0].P2_points == 1) {
            displayCharacter(P2_points_10, '1');
            displayCharacter(P2_points_1, '5');
        } else if (Gamestate[0].P2_points == 2) {
            displayCharacter(P2_points_10, '3');
            displayCharacter(P2_points_1, '0');
        } else if (Gamestate[0].P2_points >= 3) {
            displayCharacter(P2_points_10, '4');
            displayCharacter(P2_points_1, '0');
        }
    }

    void displayCharacter(ImageView view, char sign) {
        if (sign == 'a' || sign == 'A')
            view.setImageResource(R.drawable.l_a);
        else if (sign == 'b' || sign == 'B')
            view.setImageResource(R.drawable.l_b);
        else if (sign == 'c' || sign == 'C')
            view.setImageResource(R.drawable.l_c);
        else if (sign == 'd' || sign == 'D')
            view.setImageResource(R.drawable.l_d);
        else if (sign == 'e' || sign == 'E')
            view.setImageResource(R.drawable.l_e);
        else if (sign == 'f' || sign == 'F')
            view.setImageResource(R.drawable.l_f);
        else if (sign == 'g' || sign == 'G')
            view.setImageResource(R.drawable.l_g);
        else if (sign == 'h' || sign == 'H')
            view.setImageResource(R.drawable.l_h);
        else if (sign == 'i' || sign == 'I')
            view.setImageResource(R.drawable.l_i);
        else if (sign == 'j' || sign == 'J')
            view.setImageResource(R.drawable.l_j);
        else if (sign == 'k' || sign == 'K')
            view.setImageResource(R.drawable.l_k);
        else if (sign == 'l' || sign == 'L')
            view.setImageResource(R.drawable.l_l);
        else if (sign == 'm' || sign == 'M')
            view.setImageResource(R.drawable.l_m);
        else if (sign == 'n' || sign == 'N')
            view.setImageResource(R.drawable.l_n);
        else if (sign == 'o' || sign == 'O')
            view.setImageResource(R.drawable.l_o);
        else if (sign == 'p' || sign == 'P')
            view.setImageResource(R.drawable.l_p);
        else if (sign == 'r' || sign == 'R')
            view.setImageResource(R.drawable.l_r);
        else if (sign == 's' || sign == 'S')
            view.setImageResource(R.drawable.l_s);
        else if (sign == 't' || sign == 'T')
            view.setImageResource(R.drawable.l_t);
        else if (sign == 'u' || sign == 'U')
            view.setImageResource(R.drawable.l_u);
        else if (sign == 'w' || sign == 'W')
            view.setImageResource(R.drawable.l_w);
        else if (sign == 'x' || sign == 'X')
            view.setImageResource(R.drawable.l_x);
        else if (sign == 'y' || sign == 'Y')
            view.setImageResource(R.drawable.l_y);
        else if (sign == 'z' || sign == 'Z')
            view.setImageResource(R.drawable.l_z);
        else if (sign == '0')
            view.setImageResource(R.drawable.d_0);
        else if (sign == '1')
            view.setImageResource(R.drawable.d_1);
        else if (sign == '2')
            view.setImageResource(R.drawable.d_2);
        else if (sign == '3')
            view.setImageResource(R.drawable.d_3);
        else if (sign == '4')
            view.setImageResource(R.drawable.d_4);
        else if (sign == '5')
            view.setImageResource(R.drawable.d_5);
        else if (sign == '6')
            view.setImageResource(R.drawable.d_6);
        else if (sign == '7')
            view.setImageResource(R.drawable.d_7);
        else if (sign == '8')
            view.setImageResource(R.drawable.d_8);
        else if (sign == '9')
            view.setImageResource(R.drawable.d_9);
        else if (sign == '.')
            view.setImageResource(R.drawable.dot);
        else
            view.setImageResource(R.drawable.blank);
    }

    void displayNames(String name1, String name2) {
        int length;
        String string;
        Context context = this;

        //currently the interface design allows us to display 7 signs for name
        if (name1.length() <= 7) {
            length = name1.length();
        } else {
            length = 7;
            name1 = name1.substring(0, 6) + '.';
        }

        for (int i = 0; i < length; i++) {
            string = "P1_name_" + (i + 1);
            int id = getResources().getIdentifier(string, "id", context.getPackageName());
            ImageView view = findViewById(id);
            displayCharacter(view, name1.charAt(i));
        }
        for (int i = length; i < 7; i++) {
            string = "P1_name_" + (i + 1);
            int id = getResources().getIdentifier(string, "id", context.getPackageName());
            ImageView view = findViewById(id);
            view.setImageResource(R.drawable.blank);
        }

        //the same but for player 2
        if (name2.length() <= 7) {
            length = name2.length();
        } else {
            length = 7;
            name2 = name2.substring(0, 6) + '.';
        }
        for (int i = 0; i < length; i++) {
            string = "P2_name_" + (i + 1);
            int id = getResources().getIdentifier(string, "id", context.getPackageName());
            ImageView view = findViewById(id);
            displayCharacter(view, name2.charAt(i));
        }
        for (int i = length; i < 7; i++) {
            string = "P2_name_" + (i + 1);
            int id = getResources().getIdentifier(string, "id", context.getPackageName());
            ImageView view = findViewById(id);
            view.setImageResource(R.drawable.blank);
        }
    }

    void changeName1(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.playerName));

// Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                P1 = input.getText().toString();
                displayNames(P1, P2);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    void changeName2(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.playerName));

// Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                P2 = input.getText().toString();
                displayNames(P1, P2);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    void showToast(String content) {
        Context context = this;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, content, duration);
        toast.show();
    }
}
