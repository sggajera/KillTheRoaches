//Sanket Gajera
//L20374308

package com.example.sanket.hm14_gajera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.widget.Toast;


public class MainThread extends Thread {
    private SurfaceHolder holder;
    private Handler handler;		// required for running code in the UI thread
    private boolean isRunning = false;
    Context context;
    Paint paint;
    boolean initialized;
    public boolean bugKilled;
    int touchx, touchy;	// x,y of touch event
    boolean touched;	// true if touch happened
    boolean data_initialized;
    private static final Object lock = new Object();
    public int i;
    public MainThread (SurfaceHolder surfaceHolder, Context context) {
        holder = surfaceHolder;
        this.context = context;
        handler = new Handler();
        data_initialized = false;
        touched = false;
        initialized=false;

    }

    public void setRunning(boolean b) {
        isRunning = b;	// no need to synchronize this since this is the only line of code to writes this variable
    }

    // Set the touch event x,y location and flag indicating a touch has happened
    public void setXY (int x, int y) {
        synchronized (lock) {
            touchx = x;
            touchy = y;
            this.touched = true;
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            // Lock the canvas before drawing
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                // Perform drawing operations on the canvas
                render(canvas);
                // After drawing, unlock the canvas and display it
                holder.unlockCanvasAndPost (canvas);

            }
        }
    }

    // Loads graphics, etc. used in game
    private void loadData (Canvas canvas) {
        Bitmap bmp;
        int newWidth, newHeight;
        float scaleFactor;


        // Create a paint object for drawing vector graphics
        paint = new Paint();


        // Load score bar
        // ADD CODE HERE

        // Load food bar
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.foodbar);

        newHeight = (int) (canvas.getHeight() * 0.1f);
        Assets.foodbar = Bitmap.createScaledBitmap(bmp, canvas.getWidth(), newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        newWidth = (int) (canvas.getWidth() * 0.085f);
        scaleFactor = (float) newWidth / bmp.getWidth();
        newHeight = (int) (bmp.getHeight() * scaleFactor);
        Assets.life = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp = null;


        // Load roach1
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach1);
        newWidth = (int) (canvas.getWidth() * 0.2f);
        scaleFactor = (float) newWidth / bmp.getWidth();
        newHeight = (int) (bmp.getHeight() * scaleFactor);
        Assets.roach1 = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach2);
        Assets.roach2 = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.deadroach);
        Assets.roach3 = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp = null;
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover);
        Assets.gameover = Bitmap.createScaledBitmap(bmp, canvas.getWidth(), canvas.getHeight(), false);
        bmp=null;


        Assets.bug[0] = new Bug();
        Assets.bug[1] = new Bug();
        Assets.bug[2] = new Bug();
        Assets.bug[3] = new Bug();
        Assets.bug[4] = new Bug();

    }

    // Load specific background screen
    private void loadBackground (Canvas canvas, int resId) {
        // Load background
        Bitmap bmp = BitmapFactory.decodeResource (context.getResources(), resId);
        // Scale it to fill entire canvas
        Assets.background = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), canvas.getHeight(), false);
        // Delete the original
        bmp = null;
    }

    private void render (Canvas canvas) {


        int i, x, y;

        if (! data_initialized) {
            loadData(canvas);
            data_initialized = true;
        }

        switch (Assets.state) {
            case GettingReady:
                loadBackground (canvas, R.drawable.wood);

                canvas.drawBitmap (Assets.background, 0, 0, null);
                // Play a sound effect
               Assets.soundPool.play(Assets.sound_getready, 1, 1, 1, 0, 1);
                // Start a timer
                Assets.gameTimer = System.nanoTime() / 1000000000f;
                // Go to next state
                Assets.state = Assets.GameState.Starting;
                break;
            case Starting:
                // Draw the background screen
                canvas.drawBitmap (Assets.background, 0, 0, null);

                // Has 3 seconds elapsed?
                float currentTime = System.nanoTime() / 1000000000f;
                if (currentTime - Assets.gameTimer >= 3)
                    // Goto next state
                    Assets.state = Assets.GameState.Running;
                break;
            case Running:
                // Draw the background screen
                canvas.drawBitmap (Assets.background, 0, 0, null);

                // Draw the score bar at top of screen
                // ADD CODE HERE
                // Draw the foodbar at bottom of screen
                canvas.drawBitmap (Assets.foodbar, 0, canvas.getHeight()-Assets.foodbar.getHeight(), null);
               // Assets.soundPool.play(Assets.mp, 1, 1, 1, 0, 1);
                // Draw one circle for each life at top right corner of screen
                // Let circle radius be 5% of width of screen
                int radius = (int)(canvas.getWidth() * 0.05f);
                int spacing = 5; // spacing in between circles
                x = canvas.getWidth() - radius - spacing;	// coordinates for rightmost circle to draw
                y = spacing;
                canvas.drawText(String.valueOf((Assets.score)),y,10*y,paint);
                paint.setTextSize(60);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                paint.setColor(Color.BLUE);
                paint.setStyle(Paint.Style.FILL);


                for (i=0; i<Assets.livesLeft; i++) {


                    canvas.drawBitmap (Assets.life, x-spacing-30, y, null);
                    canvas.drawBitmap (Assets.life, x-spacing-30, y, null);


                    x -= (radius*2 + spacing);

                }




                // Process a touch
                if (touched) {
                    // Set touch flag to false since we are processing this touch now

                    // See if this touch killed a bug
                    { for(i=0;i<=4;i++){
                        bugKilled = Assets.bug[i].touched(canvas, touchx, touchy);

                        if (bugKilled)
                        { if(i==1)
                            {Assets.soundPool.play(Assets.sound_squish1, 1, 1, 1, 0, 1);
                            Assets.score= Assets.score+1;}
                          else if(i==2){
                            Assets.soundPool.play(Assets.sound_squish2, 1, 1, 1, 0, 1);
                            Assets.score= Assets.score+1;}

                          else {
                            Assets.soundPool.play(Assets.sound_squish, 1, 1, 1, 0, 1);
                            Assets.score= Assets.score+1;}
                        }
                        else
                            Assets.soundPool.play(Assets.sound_thump, 1, 1, 1, 0, 1);}

                        touched = false;

                    }
                }

                for(i=0;i<=4;i++){
                    // Draw dead bugs on screen
                    Assets.bug[i].drawDead(canvas);
                    // Move bugs on screen
                    Assets.bug[i].move(canvas);
                    // Bring a dead bug to life?
                    Assets.bug[i].birth(canvas);}



                // ADD MORE CODE HERE TO PLAY GAME
                if(Assets.toast){

                    Assets.soundPool.play(Assets.sound_eating, 1, 1, 1, 0, 1);
                    Assets.toast=false;
                }


                // Are no lives left?
                if (Assets.livesLeft == 0)
                    // Goto next state

                {Assets.soundPool.play(Assets.sound_gameover, 1, 1, 1, 0, 1);
                    Assets.state = Assets.GameState.GameEnding;}
                break;
            case GameEnding:
                // Show a game over message
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();

                    }
                });
                // Goto next state
                Assets.state = Assets.GameState.GameOver;
                break;
            case GameOver:
                // Fill the entire canvas' bitmap with 'black'
                //canvas.drawColor(Color.BLACK);

                canvas.drawBitmap(Assets.gameover,0,0,null);


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
               // Assets.highscore=score;
                int high_score = preferences.getInt("High_Score",0);

                if (high_score < Assets.score){
                    //Set the new high score
                    editor.putInt("High_Score", Assets.score);
                    editor.commit();
                    Assets.scorenote=true;

                }
                //else keep the previous score

               // score =0;
               // context.startActivity(new Intent(context.getApplicationContext(),TitleActivity.class));
              //  break;
        }
    }
}






