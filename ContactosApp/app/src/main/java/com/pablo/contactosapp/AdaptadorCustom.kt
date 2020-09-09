package com.pablo.contactosapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

//Aquí definimos el adaptador para que tome como referencia el archivo de template.xml
class AdaptadorCustom(var contexto: Context, items: ArrayList<Contacto>) : BaseAdapter() {

    /*Antes de configurar los métodos necesitamos crear una variable de tipo ArrayList que es donde
    se van a almacenar los elementos que se van a mostrar en el ListView */
    var items: ArrayList<Contacto>? = null

    var copiaItems: ArrayList<Contacto>? = null

    //Inicializamos nuestra variable
    init {
        this.items = ArrayList(items)
        this.copiaItems = items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /*Asociar el renderizado de los elementos con los objetos que yo tengo en mi lista, pero
        para eso necesito implementar una clase que me permita definir los elementos gráficos
        para hacer más eficiente la memoria, para ello definimos un ViewHolder */

        /*Configuramos tanto el ViewHolder como los valores que le voy a asignar de acuerdo a los
        elementos de mi lista
         */
        var viewHolder: ViewHolder? = null
        var vista: View? = convertView

        //Tenemos que validar que esta vista esté vacía o ya tenga algo (si se está reciclando)
        if (vista == null) {
            //Si la vista está vacía le creamos una nueva referencia
            vista = LayoutInflater.from(contexto).inflate(R.layout.template_contacto, null)
            viewHolder = ViewHolder(vista)
            vista.tag = viewHolder
        } else {
            viewHolder = vista.tag as? ViewHolder
        }

        //Hace falta definir los valores de cada uno de los campos
        val item = getItem(position) as Contacto

        //Asignación de valores a elementos gráficos
        viewHolder?.nombre?.text = "${item.nombre} ${item.apellidos}"
        viewHolder?.empresa?.text = item.empresa
        viewHolder?.foto?.setImageResource(item.foto)

        return vista!!

        //Por último hay que implementarlo en nuestro MainActivity
    }

    override fun getItem(position: Int): Any {
        //Devolver el objeto entero
        return this.items?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        //Nos devuelve el id del elemento que nosotros seleccionemos
        return position.toLong()
    }

    override fun getCount(): Int {
        //Devolver el número de elementos de mi lista
        return this.items?.count()!!    //Doble signo de exclamación para obtener el valor
    }

    fun addItem(item: Contacto) {
        copiaItems?.add(item)
        items = ArrayList(copiaItems!!)
        notifyDataSetChanged()
    }

    fun removeItem(index: Int) {
        copiaItems?.removeAt(index)
        items = ArrayList(copiaItems!!)
        notifyDataSetChanged()
    }

    fun updateItem(index: Int, newItem: Contacto) {
        copiaItems?.set(index, newItem)
        items = ArrayList(copiaItems!!)
        notifyDataSetChanged()
    }

    fun filtrar(str: String) {
        items?.clear()
        if (str.isEmpty()) {
            items = ArrayList(copiaItems!!)
            notifyDataSetChanged()
            return
        }
        var busqueda = str
        busqueda = busqueda.toLowerCase()

        for (item in copiaItems!!) {
            val nombre = item.nombre.toLowerCase()

            if (nombre.contains(busqueda)) {
                items?.add(item)
            }
        }
        notifyDataSetChanged()
    }

    private class ViewHolder(vista: View) {
        var nombre: TextView? = null
        var foto: ImageView? = null
        var empresa: TextView? = null

        init {
            nombre = vista.findViewById(R.id.tvNombre)
            empresa = vista.findViewById(R.id.tvEmpresa)
            foto = vista.findViewById(R.id.tvNombreGrid)
        }
    }

}