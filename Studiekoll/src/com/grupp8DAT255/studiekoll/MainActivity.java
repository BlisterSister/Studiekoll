package com.grupp8DAT255.studiekoll;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.content.Intent;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createHelpActivity(View view){
    	Intent helpIntent = new Intent(this, HelpActivity.class);
    	startActivity(helpIntent);
    }
    
    /**
     * Is called when graphbutton is clicked
     */
    public void createGraphActivity(View view){
    	Intent graphIntent = new Intent(this, GraphActivity.class);
    	startActivity(graphIntent);
    }
    
    /**
     * Called when the user clicks on the input_button
     * @param view
     */
    public void createInputActivity(View view){
    	Intent inputIntent = new Intent(this, InputActivity.class);
    	startActivity(inputIntent);
    }
    
    /**
     * Called when the user clicks the category_button
     * @param view
     */
    public void createCategoryActivity(View view){
    	Intent categoryIntent = new Intent(this, CategoryActivity.class);
    	startActivity(categoryIntent);
    }
    
    /**
     * Called when the user clicks the category_button
     * @param view
     */
    public void createDataManagementActivity(View view){
    	Intent categoryIntent = new Intent(this, DataManagementActivity.class);
    	startActivity(categoryIntent);
    }
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}