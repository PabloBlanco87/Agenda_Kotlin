package com.pablo.contactosapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import android.widget.ListView
import android.widget.Switch
import android.widget.ViewSwitcher
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    var lista: ListView? = null
    var grid: GridView? = null
    var viewSwitcher: ViewSwitcher? = null


    companion object {
        var contactos: ArrayList<Contacto>? = null
        var adaptador: AdaptadorCustom? = null
        var adaptadorGrid: AdaptadorCustomGrid? = null

        fun agregarContacto(contacto: Contacto) {
            adaptador?.addItem(contacto)
        }

        fun obtenerContacto(index: Int): Contacto {
            return adaptador?.getItem(index) as Contacto
        }

        fun eliminarContacto(index: Int) {
            adaptador?.removeItem(index)
        }

        fun actualizarContacto(index: Int, nuevoContacto: Contacto) {
            adaptador?.updateItem(index, nuevoContacto)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Para poder anidar nuestra Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //Esto coloca la Toolbar, pero no los elementos
        setSupportActionBar(toolbar)

        //Creamos el primer objeto Contacto
        contactos = ArrayList()
        contactos?.add(
            Contacto(
                "Pablo",
                "Blanco",
                "Developer",
                32,
                78.0F,
                "Gran Vía, 42",
                "6166210549",
                "pblanc87@gmail.com",
                R.drawable.hombre
            )
        )

        contactos?.add(
            Contacto(
                "Claudia",
                "Moreno",
                "IT Recruiter",
                35,
                52.3F,
                "Jovellanos, 21",
                "616621058",
                "cml@gmail.com",
                R.drawable.foto_04
            )
        )
        contactos?.add(
            Contacto(
                "Andrea",
                "Garcia",
                "Trabajadora Social",
                28,
                49.5F,
                "Castellana, 28",
                "616621054",
                "andygarcia@gmail.com",
                R.drawable.foto_06
            )
        )

        contactos?.add(
            Contacto(
                "María",
                "Hernández",
                "Funcionaria",
                47,
                60.2F,
                "Rivas, 51",
                "652857893",
                "makoque@gmail.com",
                R.drawable.foto_05
            )
        )

        contactos?.add(
            Contacto(
                "Tomás",
                "Mayoral",
                "Enfermero",
                38,
                70.5F,
                "Delicias, 128",
                "616621344",
                "enftom@gmail.com",
                R.drawable.foto_02
            )
        )



        //Creamos la lista y el adaptador
        lista = findViewById<ListView>(R.id.lista)
        grid = findViewById<GridView>(R.id.grid)
        adaptador = AdaptadorCustom(this, contactos!!)
        adaptadorGrid= AdaptadorCustomGrid(this, contactos!!)

        viewSwitcher = findViewById(R.id.viewSwitcher)

        lista?.adapter = adaptador
        grid?.adapter= adaptadorGrid

        lista?.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, Detalle::class.java)
            intent.putExtra("ID", position.toString())
            startActivity(intent)
        }
    }

    //Permite inflar, o adherir los elementos xml a la pantalla
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val itemBusqueda = menu?.findItem(R.id.searchView)
        val searchView = itemBusqueda?.actionView as SearchView

        val itemSwitch = menu.findItem(R.id.switch_view)
        itemSwitch.setActionView(R.layout.switch_item)
        val switchView = itemSwitch.actionView.findViewById<Switch>(R.id.sCambiaVista)

        //Para configurar el servicio de busqueda para nuestro ListView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Buscar contacto..."

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            //Preparar los datos
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                //Filtrar datos
                adaptador?.filtrar(newText!!)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                //Filtrar datos
                return true
            }
        })

        switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            viewSwitcher?.showNext()
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.iNuevo -> {
                val intent = Intent(this, Nuevo::class.java)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        adaptador?.notifyDataSetChanged()
    }
}