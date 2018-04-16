package edu.qc.seclass.grocerylist;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//list of item
public class ItemList extends AppCompatActivity {
    private int position;
    List<Item> itemList;
    ListView listView;
    itemAdapter itemAdapter;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",-1);
        setTitle(MainActivity.itemList.get(position).getListName());
        listView =(ListView) findViewById(R.id.itemList);
        initItems();
        itemAdapter = new itemAdapter(ItemList.this,itemList);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index=i;
                startActivityForResult(new Intent(getApplicationContext(),changeAmount.class),4);
            }
        });
    }

    //button in action menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_list,menu);
        return true;
    }

    //read data
    public void initItems(){
        itemList = new ArrayList<Item>();

        TypedArray arrayText = getResources().obtainTypedArray(R.array.product);

        for(int i=0; i<arrayText.length();i++){
            String s = arrayText.getString(i);
            boolean b= false;
            Item item = new Item(s,b,"food");
            itemList.add(item);
        }

        arrayText.recycle();
    }


    //delete item
    public void detele(){
        Iterator<Item> i = itemList.iterator();
        while (i.hasNext()) {
            Item o = i.next();
            if(o.isSelected())
                i.remove();
        }
        itemAdapter.notifyDataSetChanged();

    }

    //remove all check off
    public void clearCheck(){
        for(Item p:itemList){
            p.isSelected =false;
        }
        itemAdapter.notifyDataSetChanged();
    }

    //action of button in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);//back to preview page
                return true;

            case R.id.action_search:
                startActivity(new Intent(this,Search.class));
                return true;
            case R.id.action_delList:
                startActivityForResult(new Intent(getApplicationContext(),Pop.class),3);// pop up confirm  window

                return true;
            case R.id.action_clearCheck:
                clearCheck();
        }
        return super.onOptionsItemSelected(item);
    }

    // for send back data
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 4 && resultCode == RESULT_OK){
            Item p = itemList.get(index);
            String amount=(String)data.getStringExtra("amount");
            p.setAmount(amount);
            itemAdapter.notifyDataSetChanged();;
        }
        if(requestCode == 3 && resultCode == RESULT_OK){
            detele();
        }
    }
}
