package ro.stery.orar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView luni = (TextView) findViewById(R.id.luni);
        luni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLuni(v);
            }
        });

        TextView marti = (TextView) findViewById(R.id.marti);
        marti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMarti(v);
            }
        });
    }

    public void startLuni(View v) {
        Intent intent = new Intent(this, LuniActivity.class);
        startActivity(intent);
    }

    public void startMarti(View v) {
        Intent intent = new Intent(this, MartiActivity.class);
        startActivity(intent);
    }
}
