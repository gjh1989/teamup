package mpt.is416.com.teamup;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class GridViewActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    public ImageItem item;
    public ImageItem finalSelected;
    AddNewGroupActivity addNewGroupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);
        addNewGroupActivity = new AddNewGroupActivity();

        /*AdapterView.OnItemClickListener MyOnItemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                item = (ImageItem) parent.getItemAtPosition(position);
                //itemName = item.getTitle();

                //addNewGroupActivity.setImage(item);

                Intent returnIntent = new Intent(GridViewActivity.this, AddNewGroupActivity.class);
                returnIntent.putExtra("result",item.getImage());
                returnIntent.putExtra("imageTitle",item.getTitle());
                setResult(Activity.RESULT_OK, returnIntent);

                //Toast.makeText(GridViewActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                //finish();
                finish();


            }
        };*/


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                item = (ImageItem) parent.getItemAtPosition(position);
                //itemName = item.getTitle();

                //addNewGroupActivity.setImage(item);

                Intent returnIntent = new Intent(GridViewActivity.this, AddNewGroupActivity.class);
                returnIntent.putExtra("result",item.getImage());
                returnIntent.putExtra("imageTitle",item.getTitle());
                setResult(Activity.RESULT_OK, returnIntent);

                //Toast.makeText(GridViewActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                //finish();
                finish();


            }
        });

    }
    public ArrayList<ImageItem> getData(){
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.schoolsPic);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, 1));
            imageItems.add(new ImageItem(bitmap,imgs.getString(i)));
        }
        return imageItems;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid_view, menu);
        return true;
    }

    public String getName(){
        return item.getTitle();

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
