package site.shawnxxy.eventreporter.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import site.shawnxxy.eventreporter.R;
import site.shawnxxy.eventreporter.fragments.EventsFragment;
import site.shawnxxy.eventreporter.utils.SessionManager;

public class EventActivity extends AppCompatActivity {

    // Session Manager Class
    SessionManager session;

    private Fragment mEventsFragment;
//    EventsFragment eventsFragment;
    String username;
//    TextView usernameTextView;

    @BindView(R.id.relativelayout_event)
    CoordinatorLayout clContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        /**
         *  Check session
         */
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        /**
         *  Add the fragment to event activity dynamically
         */
        // get username from MainActivity
//        Intent intent = getIntent();
//        username = intent.getStringExtra("Username");
//        usernameTextView = (TextView) findViewById(R.id.text_user);
//        usernameTextView.setText("Welcome, " + username);
        // Create ReportEventFragment
        if (mEventsFragment == null) {
            mEventsFragment = new EventsFragment();
        }
        // Add Fragment to the fragment
        getSupportFragmentManager().beginTransaction().add(R.id.relativelayout_event, mEventsFragment).commit();

        /**
         *  Drawer nav menu
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
	    final DrawerLayout drawerLayout = findViewById(R.id.root);

	    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
	    drawerLayout.setDrawerListener(actionBarDrawerToggle);
	    actionBarDrawerToggle.syncState();

	    NavigationView navigationView = findViewById(R.id.nav_view);
	    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
		    @Override
		    public boolean onNavigationItemSelected(MenuItem item) {
			    drawerLayout.closeDrawers();
			    int id = item.getItemId();

			    if (id == R.id.menu_feed) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_photo_you_liked) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_news) {
			    	// Do something
			    } else if (id == R.id.menu_popular) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_photos_nearby) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_rests_nearby) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_events_nearby) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_settings) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_logout) {
			    	// Do something
				    return true;
			    } else if (id == R.id.menu_about) {
			    	// Do something
					return true;
			    }
			    return false;
		    }
	    });

//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
//        // Set item click listener to the menu items
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_profile:
//                                getSupportFragmentManager().beginTransaction()
//                                        .replace(R.id.fragment_container, new EventsFragment()).commit();
//                                break;
//                            case R.id.action_events:
//                        }
//                        return false;
//                    }
//                }
//        );
    } // End of onCreate()

    public void showLikedSnackbar() {
        Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_simple_nav_drawer_and_view, menu);
//		return true;
//	}

    // Username used by fragment
//    public String getUsername() {
//        return username;
//    }
}
