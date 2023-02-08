package com.example.saltena2.ui.dashboard

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.saltena2.SQLite
import com.example.saltena2.databinding.FragmentDashboardBinding
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.time.LocalDateTime

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingresoBtn.setOnClickListener {
            linearLayoutGeneral.isVisible = false
            linearLayoutIngreso.isVisible = true


        }

        ingresoConfirBtn.setOnClickListener {
            linearLayoutGeneral.isVisible = true
            linearLayoutIngreso.isVisible = false
        }

        gastoBtn.setOnClickListener {
            linearLayoutGeneral.isVisible = false
            linearLayoutGasto.isVisible = true
        }

        gastoConfirBtn.setOnClickListener {
            linearLayoutGeneral.isVisible = true
            linearLayoutGasto.isVisible = false
        }

        ventaBtn.setOnClickListener {
            linearLayoutVenta.isVisible = true
            linearLayoutGeneral.isVisible = false
        }

        confirmatVentaBtn.setOnClickListener {
           alertDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun alertDialog() {
        val dialogBuilder = AlertDialog.Builder(context)

        var numSaltena =numSaltenaET.text.toString()
        var total = (numSaltena.toInt() * 8).toString()
        dialogBuilder.setMessage("Estas seguro que quieres vender $numSaltena salteñas por el total" +
                " de Bs. $total?")
            .setCancelable(false)
            .setPositiveButton("Aceptar", DialogInterface.OnClickListener {
                    dialog, id -> anadirVentaDB()
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Confirmar venta")
        alert.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun anadirVentaDB(){
        var con =SQLite(context, "VentaSaltenas", null, 1)
        var db = con.writableDatabase
        var numSaltena =numSaltenaET.text.toString()
        var total = (numSaltena.toInt() * 8).toString()
        var date = LocalDateTime.now().toString()

        if (numSaltena.isEmpty() == false && date.isEmpty() == false && total.isEmpty() == false){
            var registro = ContentValues()
            registro.put("fecha",date)
            registro.put("saltenas", numSaltena)
            registro.put("total", total)

            db.insert("ventas",null, registro)
            Toast.makeText(context, "Se vendieron $numSaltena saltenas dando un total de Bs. $total el dia $date ", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "No se pudo realizar la transaccion", Toast.LENGTH_SHORT).show()
        }
        linearLayoutVenta.isVisible = false
        linearLayoutGeneral.isVisible = true
        numSaltenaET.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}