package com.ironoid.fdsquiz;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    List<Question> quesList;
    int score=0;
    int qid=0;
    Question currentQ;
    TextView txtQuestion;
    RadioButton rda, rdb, rdc, rdd;
    Button butNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        DbHelper db=new DbHelper(this);
        quesList=db.getAllQuestions();
        currentQ=quesList.get(qid);
        txtQuestion=(TextView)findViewById(R.id.textView1);
        rda=(RadioButton)findViewById(R.id.radio0);
        rdb=(RadioButton)findViewById(R.id.radio1);
        rdc=(RadioButton)findViewById(R.id.radio2);
        rdd=(RadioButton)findViewById(R.id.radio3);
        butNext=(Button)findViewById(R.id.button1);
        Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        setQuestionView();
        butNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioGroup grp=(RadioGroup)findViewById(R.id.radioGroup1);
                RadioButton answer=(RadioButton)findViewById(grp.getCheckedRadioButtonId());


                Log.d("yourans", currentQ.getANSWER() + " " + answer.getText());
                if(currentQ.getANSWER().equals(answer.getText()))
                {
                    Snackbar snackbar = Snackbar
                            .make(v , "Awesome Right Answer", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    score++;
                    Log.d("score", "Your score"+score);
                }
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(v , "Wrong...The Right Answer is " + currentQ.getANSWER(), Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

                if(qid<42){

                    currentQ=quesList.get(qid);
                    setQuestionView();
                }else{
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    Bundle b = new Bundle();
                    Bundle total = new Bundle();
                    b.putInt("score", score); //Your score
                    b.putInt("qid", qid);
                    intent.putExtras(b);
                    intent.putExtras(total);//Put your score to your next Intent
                    startActivity(intent);
                    finish();
                }
            }



        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_quiz, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_settings:

                Intent i = new Intent(QuizActivity.this , SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.exit:
                AlertDialog.Builder close = new AlertDialog.Builder(this);

                close.setTitle("Really Exit ?");
                close.setMessage("Do you really want to exit the app and stop STUDING ??");
                close.setPositiveButton("Yes", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {finish();
                            }
                        });;
                close.setNegativeButton("No",new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });;
                AlertDialog alertDialog = close.create();
                alertDialog.show();

                break;
            case R.id.restart:
                finish();
                Intent restart = new Intent(QuizActivity.this , QuizActivity.class);
                startActivity(restart);
                break;
            case R.id.home:
                finish();
                Intent home = new Intent(QuizActivity.this , MainActivity.class);
                startActivity(home);
                break;

            case R.id.share:

                String shareBody = "Hey , have a look at this wonderfull app F.A.Q.\nYou can Revise Your MCQ's and " +
                        "Frequently Asked Questions asked in Online Exam and Viva Using your Phone.\nJust Download the App From Here www.FAQ.com";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FAQ App:");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                break;
        }
        return true;

    }
    private void setQuestionView()
    {
        txtQuestion.setText(currentQ.getQUESTION());
        rda.setText(currentQ.getOPTA());
        rdb.setText(currentQ.getOPTB());
        rdc.setText(currentQ.getOPTC());
        rdd.setText(currentQ.getOPTD());
        qid++;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder submit =new AlertDialog.Builder(this);
        submit.setTitle("Submit or Exit");
        submit.setMessage("Do Want to Submit Test,Exit Test or Continue");
        submit.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                Bundle b = new Bundle();
                Bundle total = new Bundle();
                b.putInt("score", score); //Your score
                b.putInt("qid", qid);
                intent.putExtras(b);
                intent.putExtras(total);//Put your score to your next Intent
                startActivity(intent);
                finish();

            }
        });
        submit.setNegativeButton("Continue Test", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        submit.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent back = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });
        submit.create();

        submit.show();

    }

}
