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
                Toast.makeText(MainActivity.this, "Clicked Luni", Toast.LENGTH_SHORT).show();
                startLuni(v);
            }
        });
        luni.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "LongClicked Luni", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        TextView marti = (TextView) findViewById(R.id.marti);
        marti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Marti", Toast.LENGTH_SHORT).show();
            }
        });

        TextView miercuri = (TextView) findViewById(R.id.miercuri);
        miercuri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Miercuri", Toast.LENGTH_SHORT).show();
            }
        });

        TextView joi = (TextView) findViewById(R.id.joi);
        joi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Joi", Toast.LENGTH_SHORT).show();
            }
        });

        TextView vineri = (TextView) findViewById(R.id.vineri);
        vineri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Vineri", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startLuni(View v) {
        Intent intent = new Intent(this, LuniActivity.class);
        startActivity(intent);
    }
}
