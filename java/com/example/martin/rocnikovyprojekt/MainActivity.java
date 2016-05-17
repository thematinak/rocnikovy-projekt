package com.example.martin.rocnikovyprojekt;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.*;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Scanner;

import classes.ptransfer.MatchParcelable;
import classes.zapas.*;

public class MainActivity extends AppCompatActivity {
    public static final int SERVE_P1 = 0;//ActionBarActivity {
    public static final int SERVE_P2 = 1;
    public static final String TYPE_FORHAND = "f";
    public static final String TYPE_BACKHAND = "b";
    public static final String TYPE_FORHAND_VOLLY = "fv";
    public static final String TYPE_BACKHAND_VOLLY = "bv";
    public static final String TYPE_SLIZE = "s";
    public static final String TYPE_SMASH = "sm";
    public static final String TYPE_SERVE = "servis";


    private Vymena vymena;
    private String typeuderu;
    private Match match;
    private boolean playable, winner;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView kurt = (ImageView) findViewById(R.id.kurt);
        playable = winner = false;
        kurt.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        if (playable) {
                                            if (vymena.isEnd()) {
                                                match.add(vymena);
                                                vymena = new Vymena(match.getServe(), match.getInGameScore());
                                                if (match.isEndOfMath()) {
                                                    konieczapasu();
                                                }
                                            }
                                            Paint paint = new Paint();
                                            paint.setColor(Color.BLACK);
                                            Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                                            Canvas cv = new Canvas(bmp);
                                            Hit h = new Hit(event.getX(), event.getY(), paint);

                                            zistiCibolOutaNastavTyp(h, v);
                                            if (h.Out()) {
                                                paint.setColor(Color.RED);
                                            } else {
                                                if (winner) {
                                                    h.setWinner(true);
                                                    paint.setColor(Color.GREEN);
                                                }
                                            }
                                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                                vymena.add(h);
                                                resetWinner();
                                                povolUndo();
                                            } else {
                                                cv.drawCircle(h.getX(), h.getY(), 50, paint);
                                            }
                                            vykreslibodky(cv);
                                            kurt.setImageBitmap(bmp);
                                        }
                                        return true;
                                    }
                                }

        );

    }

    /**
     * otvori dialog po skonceni zapasu
     */
    private void konieczapasu() {
        playable = false;
        final Dialog dialog = new Dialog(MainActivity.this);
        LinearLayout ll = new LinearLayout(MainActivity.this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        dialog.setTitle("End of match!");
        Button bt = new Button(MainActivity.this);
        bt.setText("Save game");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGame();
                dialog.cancel();
            }
        });
        ll.addView(bt);
        bt = new Button(MainActivity.this);
        bt.setText("See statistic");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statistic();
                dialog.cancel();
            }
        });
        ll.addView(bt);
        dialog.setContentView(ll);
        dialog.show();
    }

    /**
     * tu bude dialog na statisky resp dialogy
     */
    private void statistic() {
        Intent intent = new Intent(MainActivity.this, Statistika.class);
        onPause();
        ImageView kurt = (ImageView) findViewById(R.id.kurt);
        intent.putExtra("vyska", (float) kurt.getHeight());
        intent.putExtra("sirka", (float) kurt.getWidth());
        startActivity(intent);
    }

    /**
     * resetuje winner button a aj globalnu premennu winner
     */
    public void resetWinner() {
        Button b = (Button) findViewById(R.id.btwshot);
        b.setText("Winner shot");
        winner = false;
    }

    /**
     * nastavy typ uderu a aj ci bol out
     */
    public void zistiCibolOutaNastavTyp(Hit h, View v) {
        if (vymena.isEmpty() || (vymena.size() == 1 && (vymena.get(0).Out() || vymena.get(0).getType().equals("net"))))
            h.setType(TYPE_SERVE);//"servis"
        else
            h.setType(typeuderu);

        if (h.isOut(v, vymena)) {
            h.setOut();
        }
        nastavTypUderu(findViewById(R.id.btForhand));
    }

    public void vykreslibodky(Canvas cv) {
        for (Hit hit : vymena) {
            cv.drawCircle(hit.getX(), hit.getY(), 10, hit.getPaint());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    /**
     * dialog pre vytvorenie novej hry
     */
    private void newGame() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.newgamedialog);
        dialog.setTitle("New game settings");
        dialog.show();
        Button newgamesub = (Button) dialog.findViewById(R.id.submitNewGameDialog);
        newgamesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et0 = (EditText) dialog.findViewById(R.id.p0name);
                EditText et1 = (EditText) dialog.findViewById(R.id.p1name);
                if (et0.getText().toString().trim().isEmpty() || et1.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "players names are requaret", Toast.LENGTH_LONG).show();
                } else {
                    int servuje = SERVE_P1;
                    RadioButton rb = (RadioButton) dialog.findViewById(R.id.serve1);
                    if (rb.isChecked())
                        servuje = SERVE_P2;

                    String p1, p2;

                    p1 = et0.getText().toString();
                    p2 = et1.getText().toString();

                    int num = 2;
                    rb = (RadioButton) dialog.findViewById(R.id.rbnofs1);
                    if (rb.isChecked())
                        num = 1;
                    rb = (RadioButton) dialog.findViewById(R.id.rbnofs2);
                    if (rb.isChecked())
                        num = 2;
                    rb = (RadioButton) dialog.findViewById(R.id.rbnofs3);
                    if (rb.isChecked())
                        num = 3;

                    playable = true;
                    match = new Match(p1, p2, num, servuje, (TextView) findViewById(R.id.twp1), (TextView) findViewById(R.id.twp2), (TextView) findViewById(R.id.txtskore), (TextView) findViewById(R.id.P1gemscore), (TextView) findViewById(R.id.P2gemscore));
                    vymena = new Vymena(match.getServe(), match.getInGameScore());
                    dialog.cancel();

                    ImageView kurt = (ImageView) findViewById(R.id.kurt);
                    Bitmap bmp = Bitmap.createBitmap(kurt.getWidth(), kurt.getHeight(), Bitmap.Config.ARGB_8888);
                    vykreslibodky(new Canvas(bmp));
                    kurt.setImageBitmap(bmp);
                }

            }

        });


    }

    /**
     * dialog ulozenie hry
     */
    private void saveGame() {
        if (playable) {
            final File parentfile = this.getFilesDir();
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.savegame);
            dialog.setTitle("Save Game!");
            dialog.show();
            Button button = (Button) dialog.findViewById(R.id.saveGamesub);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText fname = (EditText) dialog.findViewById(R.id.etfilename);
                    String sname = fname.getText().toString() + ".txt";
                    File f = new File(parentfile, sname);
                    if (f.exists()) {
                        Toast.makeText(getApplicationContext(), "Name already in use try again with diff name!", Toast.LENGTH_LONG).show();
                    } else {
                        match.saveMath(f, vymena);
                        dialog.cancel();
                    }
                }
            });
        }
    }

    /**
     * dialog pre nacitanie hry
     */
    private void loadGame() {
        final File parentfile = this.getFilesDir();
        final String filenames[] = parentfile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.contains(".txt");
            }
        });
        if (filenames.length > 0) {
            final Dialog dialog = new Dialog(MainActivity.this);
            final RadioGroup radioGrup = new RadioGroup(MainActivity.this);
            radioGrup.setOrientation(RadioGroup.VERTICAL);

            for (int i = 0; i < filenames.length; i++) {
                RadioButton rb = new RadioButton(MainActivity.this);
                rb.setText(filenames[i].replace(".txt", ""));
                rb.setId(i);
                rb.setChecked(true);
                radioGrup.addView(rb);
            }
            Button button = new Button(MainActivity.this);
            button.setText("Load");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File f = new File(parentfile, filenames[radioGrup.getCheckedRadioButtonId()]);
                    loadFromfile(f);
                    ImageView kurt = (ImageView) findViewById(R.id.kurt);
                    Bitmap bmp = Bitmap.createBitmap(kurt.getWidth(), kurt.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas cv = new Canvas(bmp);
                    vykreslibodky(cv);
                    kurt.setImageBitmap(bmp);
                    if (!vymena.isEmpty())
                        povolUndo();
                    dialog.cancel();
                }
            });

            LinearLayout ll = new LinearLayout(MainActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ScrollView scv = new ScrollView(MainActivity.this);
            ll.addView(radioGrup);
            ll.addView(button);
            scv.addView(ll);

            dialog.setContentView(scv);
            dialog.setTitle("Load Game!");
            dialog.show();

        } else {
            Toast.makeText(getApplicationContext(), "I didnt find any saved game!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * nacitanie hry zo suboru f
     */
    private void loadFromfile(File f) {
        Scanner sc = null;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String p1 = sc.next();
        String p2 = sc.next();

        int numberOfsets = sc.nextInt();
        int serve = sc.nextInt();

        match = new Match(p1, p2, numberOfsets, serve, (TextView) findViewById(R.id.twp1), (TextView) findViewById(R.id.twp2), (TextView) findViewById(R.id.txtskore), (TextView) findViewById(R.id.P1gemscore), (TextView) findViewById(R.id.P2gemscore));
        vymena = new Vymena(match.getServe(), match.getInGameScore());
        ImageView kurt = (ImageView) findViewById(R.id.kurt);
        while (sc.hasNext()) {
            float x = sc.nextFloat() / 10000;
            float y = sc.nextFloat() / 10000;
            String type = sc.next();
            boolean winner = sc.nextBoolean();
            Paint p = new Paint();
            Hit hit = new Hit(x, y, p);
            hit.setType(type);
            hit.setWinner(winner);
            if (hit.isOut(kurt, vymena) || type.equals("net")) {
                hit.setOut();
                p.setColor(Color.RED);
            }
            if (winner) {
                p.setColor(Color.GREEN);
            }

            if (vymena.add(hit)) {
                match.add(vymena);
                vymena = new Vymena(match.getServe(), match.getInGameScore());
            }
        }
        sc.close();
        playable = !match.isEndOfMath();
    }

    /**
     * dialog na odstranovanie ulozenych hier
     */
    private void removeMysaves() {
        final File parentfile = this.getFilesDir();
        final String filenames[] = parentfile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.contains(".txt");
            }
        });
        if (filenames.length > 0) {
            LinearLayout ll = new LinearLayout(MainActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            final Dialog dialog = new Dialog(MainActivity.this);
            ScrollView scv = new ScrollView(MainActivity.this);
            dialog.setContentView(scv);
            dialog.setTitle("Delete files!");


            final RadioButton rbs[] = new RadioButton[filenames.length];
            for (int i = 0; i < filenames.length; i++) {
                RadioButton rb = new RadioButton(MainActivity.this);
                rb.setText(filenames[i].replace(".txt", ""));
                rb.setId(i);
                rb.setChecked(false);
                rbs[i] = rb;
                ll.addView(rb);
            }
            Button button = new Button(MainActivity.this);
            button.setText("Remove");
            ll.addView(button);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < filenames.length; i++) {
                        if (rbs[i].isChecked()) {
                            File f = new File(parentfile, filenames[i]);
                            f.delete();
                        }
                    }
                    dialog.cancel();
                }
            });

            scv.addView(ll);
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "You have no saves to delete!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * odstrani posledny uder z vymeny a vykresli kurt
     */
    public void undo() {
        vymena.undo();
        ImageView kurt = (ImageView) findViewById(R.id.kurt);
        Bitmap bmp = Bitmap.createBitmap(kurt.getWidth(), kurt.getHeight(), Bitmap.Config.ARGB_8888);
        vykreslibodky(new Canvas(bmp));
        kurt.setImageBitmap(bmp);
        if (vymena.isEmpty()) {
            zakazUndo();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.new_game:
                newGame();
                break;
            case R.id.menu_load:
                loadGame();
                break;
            case R.id.menu_save:
                saveGame();
                break;
            case R.id.menu_delete:
                removeMysaves();
                break;
            case R.id.menu_undo:
                undo();
                break;
            case R.id.menu_statistics:
                statistic();
                break;
        }

        return true;
    }

    /**
     * nastavy typ uderu do globalnej premennej
     */
    public void nastavTypUderu(View v) {
        if (playable) {
            Button b;
            b = (Button) findViewById(R.id.btForhand);
            b.setEnabled(true);
            b = (Button) findViewById(R.id.btBackhand);
            b.setEnabled(true);
            b = (Button) findViewById(R.id.btFvolly);
            b.setEnabled(true);
            b = (Button) findViewById(R.id.btBvolly);
            b.setEnabled(true);
            b = (Button) findViewById(R.id.btSlize);
            b.setEnabled(true);
            b = (Button) findViewById(R.id.btSmash);
            b.setEnabled(true);
            switch (v.getId()) {
                case R.id.btForhand:
                    typeuderu = TYPE_FORHAND; //"f";
                    b = (Button) findViewById(R.id.btForhand);
                    b.setEnabled(false);
                    break;
                case R.id.btBackhand:
                    typeuderu = TYPE_BACKHAND; //"b";
                    b = (Button) findViewById(R.id.btBackhand);
                    b.setEnabled(false);
                    break;
                case R.id.btFvolly:
                    typeuderu = TYPE_FORHAND_VOLLY; //"fv";
                    b = (Button) findViewById(R.id.btFvolly);
                    b.setEnabled(false);
                    break;
                case R.id.btBvolly:
                    typeuderu = TYPE_BACKHAND_VOLLY; //"bv";
                    b = (Button) findViewById(R.id.btBvolly);
                    b.setEnabled(false);
                    break;
                case R.id.btSlize:
                    typeuderu = TYPE_SLIZE; //"s";
                    b = (Button) findViewById(R.id.btSlize);
                    b.setEnabled(false);
                    break;
                case R.id.btSmash:
                    typeuderu = TYPE_SMASH; //"sm";
                    b = (Button) findViewById(R.id.btSmash);
                    b.setEnabled(false);
                    break;
            }
        }
    }

    /**
     * nastavuje globalnu premennu winner handler pre button winenr
     */
    public void nastavWiner(View v) {
        if (playable) {
            Button b = (Button) findViewById(R.id.btwshot);
            if (winner) {
                b.setText("Winner shot");
                winner = false;
            } else {
                b.setText("Winner shot*");
                winner = true;
            }
        }
    }

    /**
     * handler pre button net a otvara dialog are you sure
     */
    public void nastavNet(View v) {
        if (playable) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.areyousure);
            dialog.setTitle("Are you sure");
            dialog.show();

            Button button = (Button) dialog.findViewById(R.id.btAusureYes);
            button.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    ImageView kurt = (ImageView) findViewById(R.id.kurt);
                    float x = kurt.getMaxWidth() / 382;
                    float y = kurt.getMaxHeight() / 171;
                    Hit hit = new Hit(188 * x, 84 * y, new Paint());
                    hit.setType("net");
                    vymena.add(hit);
                    if (vymena.isEnd()) {
                        match.add(vymena);
                        vymena = new Vymena(match.getServe(), match.getInGameScore());
                        vykresliprazdnykurt();
                    }
                    dialog.cancel();
                }
            });
            button = (Button) dialog.findViewById(R.id.btAysurecancle);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
    }

    public void vykresliprazdnykurt() {
        final ImageView kurt = (ImageView) findViewById(R.id.kurt);
        Bitmap bmp = Bitmap.createBitmap(kurt.getWidth(), kurt.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bmp);
        kurt.setImageBitmap(bmp);
    }

    /**
     * gombik v menu UNDO povoluje ho a zaroven ho ukaze na horej liste
     */
    public void povolUndo() {
        MenuItem item = menu.findItem(R.id.menu_undo);
        item.setEnabled(true);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    /**
     * zakazuje gombik undo a skryje ho z listy
     */
    public void zakazUndo() {
        MenuItem item = menu.findItem(R.id.menu_undo);
        item.setEnabled(false);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        File parentFile = this.getFilesDir();
        File f = new File(parentFile, "TMP.tmp");
        if (f.exists()) {
            loadFromfile(f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        File parentFile = this.getFilesDir();
        File f = new File(parentFile, "TMP.tmp");
        if (match != null)
            match.saveMath(f, vymena);
    }

}

