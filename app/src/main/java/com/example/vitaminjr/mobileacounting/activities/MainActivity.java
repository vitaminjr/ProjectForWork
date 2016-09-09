package com.example.vitaminjr.mobileacounting.activities;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.vitaminjr.mobileacounting.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class MainActivity extends AppCompatActivity {

    private final int GAIN= 1;
    private final int EXPENSE= 2;
    private final int CHECKPRICES = 3;
    private final int INVERTORY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGui();
    }

    public void initGui(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
    }

    public void initDrawer(Toolbar toolbar){
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                //.withHeader(R.drawable.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.gain).withIdentifier(GAIN),
                        new PrimaryDrawerItem().withName(R.string.expense).withIdentifier(EXPENSE),
                        new PrimaryDrawerItem().withName(R.string.check_prices).withIdentifier(CHECKPRICES),
                        new PrimaryDrawerItem().withName(R.string.inventory).withIdentifier(INVERTORY)
                )

                .withOnDrawerItemClickListener( new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }

                        if(drawerItem != null) {

                            switch (drawerItem.getIdentifier()) {

                                case GAIN:
                                    Intent gainIntent = new Intent(getApplicationContext(), GainInvoiceActivity.class);
                                    startActivity(gainIntent);
                                    break;
                                case EXPENSE:
                                    Intent expenseIntent = new Intent(getApplicationContext(), ExpenseInvoiceActivity.class);
                                    startActivity(expenseIntent);
                                    break;
                                case CHECKPRICES:
                                    Intent checkPricesIntent = new Intent(getApplicationContext(), CheckPricesActivity.class);
                                    startActivity(checkPricesIntent);
                                    break;
                                case INVERTORY:
                                    Intent inventoryIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                                    startActivity(inventoryIntent);
                                    break;
                                default:
                            }
                        }
                    }
                }).build();
    }
}
