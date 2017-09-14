package br.com.ramonilho.bolados.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.utils.MapUtils
import org.intellij.lang.annotations.Identifier
import android.R.attr.data
import android.app.Activity
import br.com.ramonilho.bolados.fragment.*
import br.com.ramonilho.bolados.model.Store
import com.facebook.login.LoginManager
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceAutocomplete.getStatus
import com.google.android.gms.location.places.Place
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE





class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        StoreCreateFragment.OnCreateStoreListener,
        StoreFragment.ShouldEditListener,
        EditStoreActivity.OnEditedListener {

    val FLAG_MAIN = "MainActivity"

    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ActionBar
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Hamburger menu
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // NavigationView
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        // ActionBar title
        setTitle(R.string.home)

        // Open Home as FirstFragment
        val homeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_main, homeFragment)
        transaction.commit()

    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent br.com.ramonilho.bolados.activity in AndroidManifest.xml.
        val id = item.itemId

//        if (id == R.id.action_settings) {
//            return true
//        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {
            setFragment(R.string.home)

        } else if (id == R.id.nav_profile) {
            setFragment(R.string.profile)

        } else if (id == R.id.nav_store) {
            setFragment(R.string.my_store)

        } else if (id == R.id.nav_settings) {
            LoginManager.getInstance().logOut()
            super.onBackPressed()

        } else if (id == R.id.nav_about) {
            setFragment(R.string.about)
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setFragment(identifier: Int) {

        setTitle(identifier)

        when (identifier) {
            R.string.home -> fragment = HomeFragment()
            R.string.profile -> fragment = ProfileFragment()
            R.string.my_store -> fragment = StoreBaseFragment()
            R.string.settings -> fragment = HomeFragment()
            R.string.about -> fragment = HomeFragment()
            else -> fragment = HomeFragment()
        }

        StoreBaseFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_main, fragment)
        transaction.commit()
    }

    override fun onCreated(store: Store) {
        Log.i(FLAG_MAIN, "onCreated")
        setFragment(R.string.my_store)
    }

    override fun shouldEdit() {
        Log.i(FLAG_MAIN, "shouldEdit")
        val intent = Intent(this, EditStoreActivity::class.java)
        startActivityForResult(intent, 123)
    }

    override fun onEdited(store: Store) {
        Log.i(FLAG_MAIN, "onEdited")
        setFragment(R.string.my_store)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            setFragment(R.string.my_store)
        }
    }
}


