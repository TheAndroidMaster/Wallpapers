package com.james.wallpapers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class Drawer {
    private Activity activity;

    public Drawer(Activity activity) {
        this.activity = activity;
    }

    public void initDrawer(Toolbar toolbar) {
        if (activity == null) return;

        int current = 0;
        if (activity instanceof Flat) current = 1;
        else if (activity instanceof Fav) current = 2;
        else if (activity instanceof SaveOfflineActivity) current = 3;

        new DrawerBuilder().withActivity(activity)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(new AccountHeaderBuilder()
                        .withActivity(activity)
                        .withCompactStyle(false)
                        .withHeaderBackground(R.mipmap.wpicon)
                        .withProfileImagesClickable(false)
                        .withSelectionListEnabledForSingleProfile(false)
                        .addProfiles(
                                new ProfileDrawerItem().withName("Fornax").withEmail("Version " + BuildConfig.VERSION_NAME).withIcon(activity.getResources().getDrawable(R.mipmap.wpicon))
                        )
                        .build())
                .withToolbar(toolbar)
                .withSelectedItem(current)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Home").withIdentifier(1).withIcon(GoogleMaterial.Icon.gmd_home),
                        new SecondaryDrawerItem().withName("Wallpapers").withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_image),
                        new SecondaryDrawerItem().withName("Favorites").withIdentifier(4).withIcon(GoogleMaterial.Icon.gmd_favorite),
                        new SecondaryDrawerItem().withName("Settings").withIdentifier(5).withIcon(GoogleMaterial.Icon.gmd_settings),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("About").withIdentifier(3).withCheckable(false).withIcon(GoogleMaterial.Icon.gmd_info)

                )
                .withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        Intent intent = null;
                        if (drawerItem.getIdentifier() == 1) {
                            intent = new Intent(activity, MainActivity.class);
                        } else if (drawerItem.getIdentifier() == 2) {
                            intent = new Intent(activity, Flat.class);
                        } else if (drawerItem.getIdentifier() == 3) {
                            activity.startActivity(new Intent(activity, About.class));
                        } else if (drawerItem.getIdentifier() == 5) {
                            intent = new Intent(activity, SaveOfflineActivity.class);
                        } else if (drawerItem.getIdentifier() == 4) {
                            intent = new Intent(activity, Fav.class);
                        }

                        if (intent != null) {
                            activity.startActivity(intent);
                            activity.finish();
                        }
                        return false;
                    }
                }).build().getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }
}
