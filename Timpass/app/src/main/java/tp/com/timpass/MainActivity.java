package tp.com.timpass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG=MainActivity.class.getSimpleName();

    List<String> l=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        RecyclerView res=(RecyclerView)findViewById(R.id.rec);

        res.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i <10 ; i++) {
            l.add(i+"");
        }
        res.setAdapter(new Adp());

    }
    private class Adp extends RecyclerView.Adapter<Adp.MyVh>{

        @Override
        public MyVh onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyVh(LayoutInflater.from(MainActivity.this).inflate(R.layout.arra, parent, false));
        }

        @Override
        public void onBindViewHolder(MyVh holder, int position) {
            holder.th.setText(l.get(position));
        }

        @Override
        public int getItemCount() {
            Log.d(TAG,l.size()+"asdf");
            return l.size();
        }

        class MyVh extends RecyclerView.ViewHolder{
            TextView th;

            public MyVh(View itemView) {
                super(itemView);
                th=(TextView)itemView.findViewById(R.id.txt);
            }
        }
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
