package com.TiendaVirtual.controladores.Submenues;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Window;

import com.TiendaVirtual.entidades.DatosUsuario;
import com.TiendaVirtual.entidades.Usuario;

public class subCategoriasControlador extends GenericForwardComposer<Component>{
//enlace a los componentes de la interfaz
	@Wire
	Button buttonnuevocategoria, buttonbusquedacategoria,buttonedicioncategoria;
	Window winCategorias;
	Center centro= null;
	Usuario usuario=null;
	DatosUsuario datosusuario=null;
	int roles;

    @Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		 centro = (Center)winCategorias.getAttribute("centro"); 
		 
		
		
		 Session session = Sessions.getCurrent();
			datosusuario = (DatosUsuario) session.getAttribute("datosusuario");
			roles = datosusuario.getUsuario().getTipousuario().getId();
			crearMenu();
	}

	
    public void crearMenu(){
		if(roles!=1){
			buttonnuevocategoria.setVisible(false);
			buttonedicioncategoria.setVisible(false);
			//winCategorias.removeChild(buttonnuevocategoria);
			 //winCategorias.removeChild(buttonedicioncategoria);
		}
	}
    
	public void onClick$buttonnuevocategoria(){
   	 if(centro.getFirstChild()!=null){
  	 centro.removeChild(centro.getFirstChild());
  	 }
   	   	Window win=(Window) Executions.createComponents("Modulo_Control_Categoria/RegistroCategoria.zul", centro, null );
  		win.setTitle("Nueva Categoria");
  		winCategorias.detach();
   }             
                
   public void onClick$buttonbusquedacategoria(){
  	if(centro.getFirstChild()!=null){
  	 centro.removeChild(centro.getFirstChild());
  	 }
  		Window win=(Window) Executions.createComponents("Modulo_Control_Categoria/BuscarCategoria.zul", centro, null );
  		win.setTitle("Busqueda Categorias"); 
  		winCategorias.detach();
   }  
	
   public void onClick$buttonedicioncategoria(){
  	if(centro.getFirstChild()!=null){
  	 centro.removeChild(centro.getFirstChild());
  	 }
   		Window win=(Window) Executions.createComponents("Modulo_Control_Categoria/ListarCategoria.zul", centro, null );  		
  		win.setTitle("Listar Categorias");
  		winCategorias.detach();
   }
}
