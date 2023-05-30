package com.androrubin.genesis

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androrubin.genesis.databinding.ActivityMainBinding
import com.androrubin.genesis.login_and_splash.LoginActivity
import com.androrubin.genesis.side_nav_frags.DiaryFragment
import com.androrubin.genesis.side_nav_frags.InfoFragment
import com.androrubin.genesis.side_nav_frags.ProfileFragment2
import com.androrubin.genesis.ui.home.HomeFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val intent=getIntent()
        val week_c= intent?.getStringExtra("week_count")

//        val fragment = HomeFragment()
//         val bundle=Bundle()
//        bundle.putString("weekCount",week_c)
//        fragment.arguments= bundle

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_community, R.id.navigation_notifications,R.id.navigation_profile

            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        drawerLayout  =findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()


        val navViews : NavigationView =findViewById(R.id.nav_drawer)
        navViews.setNavigationItemSelectedListener {

            it.isChecked = true
            when(it.itemId){

                R.id.nav_profile -> {startActivity(Intent(this,ProfileFragment2::class.java))}
                R.id.nav_journel-> startActivity(Intent(this, DiaryFragment::class.java))
                R.id.nav_does-> startActivity(Intent(this,InfoFragment::class.java))



            }
            true
        }


    }



    private  fun replaceFragment(fragment: Fragment, title :String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction= fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}