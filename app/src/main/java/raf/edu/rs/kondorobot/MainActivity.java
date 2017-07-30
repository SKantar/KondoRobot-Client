package raf.edu.rs.kondorobot;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.os.Message;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView mainImage;
    private MessageHandler messageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 0);

        mainImage = (ImageView) findViewById(R.id.main_image);

    }

    public void createHandler(String host, int port) throws Exception{
        HandlerThread thread = new HandlerThread("StartService");
        thread.start();

        Looper looper = thread.getLooper();
        this.messageHandler = new MessageHandler(looper, host, port);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int port = data.getIntExtra("port", 0);
        String host = data.getStringExtra("host");

        Log.d("RESULT", host);
        Log.d("RESULT", "" + port);

        try{
            createHandler(host, port);
        }catch (Exception e){
            e.printStackTrace();
        }

        mainImage.setOnTouchListener(new SwipeListener(MainActivity.this, this.messageHandler));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
