package com.example.remindmev2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.remindmev2.databinding.ActivityMainMenuBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Crear canal de notificaciones (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "recordatorios",
                "Recordatorios",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // ✅ Solicitar permiso de notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        // 🔹 Verificar que el usuario esté autenticado
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            irALogin()
            return
        }

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMainMenu.toolbar)

        // Ocultar el FAB por defecto
        binding.appBarMainMenu.fab.hide()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main_menu)

        // Configurar los destinos principales
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_crear_tarea,
                R.id.nav_perfil,
                R.id.nav_amigos,
                R.id.nav_agregar_amigos,
                R.id.nav_compartir_tarea,
                R.id.nav_recordar_tarea
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Manejar clics en el menú de navegación
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_crear_tarea -> {
                    navController.navigate(R.id.nav_crear_tarea)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_perfil -> {
                    navController.navigate(R.id.nav_perfil)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_amigos -> {
                    navController.navigate(R.id.nav_amigos)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_agregar_amigos -> {
                    navController.navigate(R.id.nav_agregar_amigos)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_recordar_tarea -> {
                    navController.navigate(R.id.nav_recordar_tarea)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_acerca_de -> {
                    mostrarAcercaDe()
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_cerrar_sesion -> {
                    confirmarCerrarSesion()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun confirmarCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Cerrar Sesión") { _, _ ->
                cerrarSesion()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        irALogin()
    }

    private fun irALogin() {
        val intent = Intent(this, ActividadLogin::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun mostrarAcercaDe() {
        AlertDialog.Builder(this)
            .setTitle("Acerca de RemindMe")
            .setMessage(
                "RemindMe v2.0\n\n" +
                        "Aplicación de gestión de tareas y recordatorios.\n\n" +
                        "Desarrollado por:\n" +
                        "- Julian Perez\n" +
                        "- Oscar Juagibioy\n\n" +
                        "© 2025 RemindMe"
            )
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
