package com.example.testdrawermenu.ui


import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.testdrawermenu.R
import com.example.testdrawermenu.domain.Repository
import com.example.testdrawermenu.ui.fragments.ImageFragment
import com.example.testdrawermenu.ui.fragments.TextFragment
import com.example.testdrawermenu.ui.fragments.UrlFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.openTool,
            R.string.closeTool
        )
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        getJson(navigationView)


    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getJson(navigationView : NavigationView){
        GlobalScope.launch(Dispatchers.IO){
            val repository = Repository().apply {
                connect(applicationContext)
                val menu = parseJson(applicationContext)
                withContext(Dispatchers.Main){
                    val navMenu = navigationView.menu
                    for (menuItem in menu.menu) {
                        navMenu.add(menuItem.name)
                    }
                    navigationView.setNavigationItemSelectedListener{
                        val id = it.title
                        for (menuItem in menu.menu) {
                            if (id == menuItem.name){
                                var fragment : Fragment? = null
                                when (menuItem.function){
                                    "text" -> fragment = TextFragment.newInstance(menuItem.param)
                                    "image" -> fragment = ImageFragment.newInstance(menuItem.param)
                                    "url" -> fragment = UrlFragment.newInstance(menuItem.param)
                                }
                                val transaction = supportFragmentManager.beginTransaction().apply {
                                    if (fragment != null) replace(R.id.fragment_holder, fragment)
                                    addToBackStack(null)
                                    commit()
                                }
                            }
                        }
                        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }

                }
            }
        }
    }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}