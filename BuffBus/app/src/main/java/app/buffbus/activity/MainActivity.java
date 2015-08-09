package app.buffbus.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import app.buffbus.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
        //TODO button things
        Button btn = createRouteButton(R.id.route_template, R.string.route_1, R.id.route_2);
        layout.addView(btn);
        setContentView(layout);
    }

    public Button createRouteButton(int parentElem, int text, int id) {
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.textView_Title);
        relativeParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.textView_Title);
        relativeParams.addRule(RelativeLayout.BELOW, parentElem);
        relativeParams.setMargins(0, 10, 0, 0);

        Button btn = new Button(MainActivity.this, null, R.attr.RouteButtonStyle);

        btn.setText(text);
        btn.setId(id);
        btn.setLayoutParams(relativeParams);

        return btn;
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
