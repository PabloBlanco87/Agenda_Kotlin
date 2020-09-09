package com.pablo.contactosapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar

class Nuevo : AppCompatActivity() {

    var fotoIndex: Int = 0
    var foto: ImageView? = null
    val fotos = arrayOf(
        R.drawable.foto_01,
        R.drawable.foto_02,
        R.drawable.foto_03,
        R.drawable.foto_04,
        R.drawable.foto_05,
        R.drawable.foto_06
    )

    var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Esto lo hacemos para poder volver a atrás
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        foto = findViewById<ImageView>(R.id.ivFotoDetalle)
        foto?.setOnClickListener {
            seleccionarFoto()
        }

        //Reconocer acción de nuevo VS editar
        if (intent.hasExtra("ID")) {
            index = intent.getStringExtra("ID").toInt()
            rellenarDatos(index)
        }

    }

    //Permite inflar, o adherir los elementos xml a la pantalla
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //Esta es la flecha de hacia atrás
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.iCrearNuevo -> {
                //Crear un nuevo elemento de tipo Contacto
                val nombre = findViewById<EditText>(R.id.etNombre)
                val apellidos = findViewById<EditText>(R.id.etApellidos)
                val empresa = findViewById<EditText>(R.id.etEmpresa)
                val edad = findViewById<EditText>(R.id.tvEdad)
                val peso = findViewById<EditText>(R.id.tvPeso)
                val telefono = findViewById<EditText>(R.id.tvTelefono)
                val email = findViewById<EditText>(R.id.tvEmail)
                val direccion = findViewById<EditText>(R.id.tvDireccion)

                //Validar campos
                var campos = ArrayList<String>()
                campos.add(nombre.text.toString())
                campos.add(apellidos.text.toString())
                campos.add(empresa.text.toString())
                campos.add(edad.text.toString())
                campos.add(peso.text.toString())
                campos.add(telefono.text.toString())
                campos.add(email.text.toString())
                campos.add(direccion.text.toString())

                var flag = 0
                for (campo in campos) {
                    if (campo.isNullOrEmpty())
                        flag++
                }

                if (flag > 0) {
                    Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_LONG).show()
                } else {
                    if (index > -1) {
                        MainActivity.actualizarContacto(
                            index, Contacto(
                                campos.get(0),
                                campos.get(1),
                                campos.get(2),
                                campos.get(3).toInt(),
                                campos.get(4).toFloat(),
                                campos.get(5),
                                campos.get(6),
                                campos.get(7),
                                obtenerFoto(fotoIndex)
                            )
                        )
                    } else {
                        MainActivity.agregarContacto(
                            Contacto(
                                campos.get(0),
                                campos.get(1),
                                campos.get(2),
                                campos.get(3).toInt(),
                                campos.get(4).toFloat(),
                                campos.get(5),
                                campos.get(6),
                                campos.get(7),
                                obtenerFoto(fotoIndex)
                            )
                        )
                    }
                    finish()
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    //Funcion para que el usuario selecciona una foto de una lista, la que más le convenga
    fun seleccionarFoto() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione imagen de perfil")

        val adaptadorDialogo =
            ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item)
        adaptadorDialogo.add("Imagen 01")
        adaptadorDialogo.add("Imagen 02")
        adaptadorDialogo.add("Imagen 03")
        adaptadorDialogo.add("Imagen 04")
        adaptadorDialogo.add("Imagen 05")
        adaptadorDialogo.add("Imagen 06")

        builder.setAdapter(adaptadorDialogo) { dialog, which ->
            fotoIndex = which
            foto?.setImageResource((obtenerFoto(fotoIndex)))
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun obtenerFoto(index: Int): Int {
        return fotos.get(index)
    }

    fun rellenarDatos(index: Int) {
        val contacto = MainActivity.obtenerContacto(index)

        val tvNombre = findViewById<EditText>(R.id.etNombre)
        val tvApellidos = findViewById<EditText>(R.id.etApellidos)
        val tvEmpresa = findViewById<EditText>(R.id.etEmpresa)
        val tvEdad = findViewById<EditText>(R.id.tvEdad)
        val tvPeso = findViewById<EditText>(R.id.tvPeso)
        val tvTelefono = findViewById<EditText>(R.id.tvTelefono)
        val tvEmail = findViewById<EditText>(R.id.tvEmail)
        val tvDireccion = findViewById<EditText>(R.id.tvDireccion)
        val ivFoto = findViewById<ImageView>(R.id.ivFotoDetalle)

        tvNombre.setText(contacto.nombre, TextView.BufferType.EDITABLE)
        tvApellidos.setText(contacto.apellidos, TextView.BufferType.EDITABLE)
        tvEmpresa.setText(contacto.empresa, TextView.BufferType.EDITABLE)
        tvEdad.setText(contacto.edad.toString(), TextView.BufferType.EDITABLE)
        tvPeso.setText(contacto.peso.toString(), TextView.BufferType.EDITABLE)
        tvTelefono.setText(contacto.telefono, TextView.BufferType.EDITABLE)
        tvEmail.setText(contacto.email, TextView.BufferType.EDITABLE)
        tvDireccion.setText(contacto.direccion, TextView.BufferType.EDITABLE)
        ivFoto.setImageResource(contacto.foto)

        //Necesitamos mapear fotoIndex
        var posicion = 0
        for (foto in fotos) {
            if (contacto.foto == foto) {
                fotoIndex = posicion
            }
            posicion++
        }
    }
}