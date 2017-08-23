package br.com.ramonilho.bolados.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.fragment.HomeFragment
import br.com.ramonilho.bolados.fragment.ProfileFragment
import br.com.ramonilho.bolados.fragment.StoreFragment
import org.intellij.lang.annotations.Identifier

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
            setFragment(R.string.settings)

        } else if (id == R.id.nav_about) {
            setFragment(R.string.about)
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setFragment(identifier: Int) {
        val fragment: Fragment

        setTitle(identifier)

        when (identifier) {
            R.string.home -> fragment = HomeFragment()
            R.string.profile -> fragment = ProfileFragment()
            R.string.my_store -> fragment = StoreFragment()
            R.string.settings -> fragment = HomeFragment()
            R.string.about -> fragment = HomeFragment()
            else -> fragment = HomeFragment()
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_main, fragment)
        transaction.commit()
    }
}


