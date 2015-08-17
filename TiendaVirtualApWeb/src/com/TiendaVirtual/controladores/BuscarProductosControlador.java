package com.TiendaVirtual.controladores;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;


import org.zkoss.zul.Window;

import com.TiendaVirtual.entidades.Productos;
import com.TiendaVirtual.modelos.DBProductos;

public class BuscarProductosControlador extends GenericForwardComposer<Component>{
@Wire
Listbox listboxProductos;
Textbox textboxBuscar, buttonBusca;
Button buttonBuscar;

@Override
public void doAfterCompose(Component comp) throws Exception {
	// TODO Auto-generated method stub
	super.doAfterCompose(comp);
	actualizarLista("");
}

public void onSelect$listboxProductos(){
	Productos productoSeleccionado = null;
	if(listboxProductos.getSelectedItem() != null){
		productoSeleccionado =  listboxProductos.getSelectedItem().getValue();
	}else{
		alert ("Por favor seleccione un producto de la lista");
		return;
	}
	Window win= (Window) Executions.createComponents("/Modulo_Control_Productos/VerProducto.zul", null, null);
	win.setTitle("Vista del Producto");
	win.setAttribute("productos", productoSeleccionado);
	win.doModal();	
	actualizarLista("");
}
public void onClick$buttonBuscar(){
		actualizarLista(textboxBuscar.getValue());
}

public void onClick$buttonBusca(){
	actualizarLista("");
	textboxBuscar.setValue("");
}

public void actualizarLista(String criterio){
	DBProductos dbproductos = new DBProductos();
	ArrayList<Productos> lista = dbproductos.buscarproductos(criterio);
	ListModelList<Productos> modeloDeDatos= new ListModelList<Productos>(lista);
	listboxProductos.setModel(modeloDeDatos);
	
	
}



}
