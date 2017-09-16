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
import br.com.ramonilho.bolados.fragment.*
import br.com.ramonilho.bolados.model.Store
import com.facebook.login.LoginManager
import android.widget.TextView
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.User
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        StoreCreateFragment.OnCreateStoreListener,
        StoreFragment.ShouldEditListener,
        EditStoreActivity.OnEditedListener{

    val FLAG_MAIN = "MainActivity"

    lateinit var fragment: Fragment
    lateinit var navigationView: NavigationView

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

        val storeAPI = APIUtils.storeAPIVersion

        if (User.shared.storeId != 0) {
            storeAPI!!.store(User.shared.storeId).enqueue(object : retrofit2.Callback<Store> {
                override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                    if (response!!.isSuccessful) {
                        Store.fromUser = response.body()
                        updateHeader()
                    } else {
                        Log.e("MainActivity", "Info response failed.")
                    }
                }

                override fun onFailure(call: Call<Store>?, t: Throwable?) {
                    Log.e("MainActivity", "Edit info failed.")
                }

            })
        }

        // NavigationView
        updateHeader()

        // ActionBar title
        setTitle(R.string.home)

        // Open Home as First Fragment
        setFragment(R.string.home)

    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            // Send app to background instead of going back to login
            super.moveTaskToBack(true)
//            super.onBackPressed()
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

        } else if (id == R.id.nav_signout) {
            LoginManager.getInstance().logOut()
            super.onBackPressed()
        } else if (id == R.id.nav_about) {
            setFragment(R.string.about)
        }
        else if (id == R.id.nav_favs) {
            setFragment(R.string.favorites)
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
            R.string.favorites -> fragment = FavoritesFragment()
            R.string.my_store -> fragment = StoreBaseFragment()
            R.string.about -> fragment = AboutFragment()
            else -> fragment = HomeFragment()
        }

        updateHeader()

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
        } else if(requestCode == 321) {
            setFragment(R.string.favorites)
        }
    }

    fun updateHeader() {
        navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val headerLayout = navigationView.getHeaderView(0)
        val headerNameView = headerLayout.findViewById(R.id.navUserName) as TextView
        headerNameView.text = User.shared.name
        val hHasStore = headerLayout.findViewById(R.id.navStoreName) as TextView
        if (Store.fromUser.id == 0) {
            hHasStore.text = getString(R.string.doesnt_have_store)
        } else {
            hHasStore.text = Store.fromUser.name
        }
    }
}


