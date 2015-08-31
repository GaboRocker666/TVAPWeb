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

public class subPedidosControlador extends GenericForwardComposer<Component>{
//enlace a los componentes de la interfaz
	@Wire
	Button buttonedicionpedidos;
	Window winPedidos;
	Center centro= null;
	Usuario usuario=null;
	DatosUsuario datosusuario=null;
	int roles;

    @Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		 centro = (Center)winPedidos.getAttribute("centro");
		
		
	}

	
   public void onClick$buttonedicionpedidos(){
  	if(centro.getFirstChild()!=null){
  	 centro.removeChild(centro.getFirstChild());
  	 }
   		Window win=(Window) Executions.createComponents("Modulo_Control_Pedido/listar_pedido.zul", centro, null );  		
  		win.setTitle("Listar Pedidos");
  		winPedidos.detach();
   }
}
