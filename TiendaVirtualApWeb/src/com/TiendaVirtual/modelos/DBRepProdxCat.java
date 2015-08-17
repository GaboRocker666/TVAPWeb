package com.TiendaVirtual.modelos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.TiendaVirtual.entidades.RepProdxCatEntidad;

public class DBRepProdxCat {
	public ArrayList<RepProdxCatEntidad>ReportePorAño(String criterio3, String criterio4){			
		ArrayList<RepProdxCatEntidad> lista= null;
		//conectar a la bd
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		if(con==null){return lista;}
		
		Statement sentencia;
		ResultSet resultados= null;
		String query="";
		query="select year(ped.fecha) as anio,count(ped.id_pedidos) as ct,prod.nombre_producto as pro,cat.nombre_categoria as cat from pedidos as ped,detallepedido as detp,productos as prod,categoria as cat where ped.id_pedidos=detp.id_pedidos and detp.id_productos=prod.id_productos and prod.id_categoria=cat.id_categoria and year(ped.fecha)="+criterio3+" and ped.estado='A' and detp.estado='A' and prod.estado='A' and cat.estado='A' and ped.estadoPedido='Realizado' and cat.nombre_categoria='"+criterio4+"' group by anio,prod.id_productos,cat order by cat,ct desc";
			
			try {
				sentencia= con.createStatement();
				resultados= sentencia.executeQuery(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error en ejecucion de sentencia" + e.getMessage());
			}
			
		
			RepProdxCatEntidad ped= null;				
		lista= new ArrayList<RepProdxCatEntidad>();
		try {
				while (resultados.next()){
					ped= new RepProdxCatEntidad();
					ped.setFinicial(Integer.toString(resultados.getInt("anio")));
					ped.setCtpedidos(resultados.getInt("ct"));
					ped.setProductos(resultados.getString("pro"));
					ped.setCategoria(resultados.getString("cat"));
					lista.add(ped);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error en recorrer los resultados");
			}
				//recorrer los resultados
			
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al cerrar la conexion");
		}
		
		return lista;	
	}
	
	public ArrayList<RepProdxCatEntidad> ReportePorMes(int criterio3,String criterio4, String criterio5){
		int anio=Integer.parseInt(criterio4);
		ArrayList<RepProdxCatEntidad> lista= null;
		//conectar a la bd
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		if(con==null){return lista;}
		
		Statement sentencia;
		ResultSet resultados= null; 
		String query=""; 
		RepProdxCatEntidad ped= null;				
		lista= new ArrayList<RepProdxCatEntidad>();
		query="select year(ped.fecha) as anio,month(ped.fecha) as fecha2,count(ped.id_pedidos) as ct,prod.nombre_producto as pro,cat.nombre_categoria as cat from pedidos as ped,detallepedido as detp,productos as prod,categoria as cat where ped.id_pedidos=detp.id_pedidos and detp.id_productos=prod.id_productos and prod.id_categoria=cat.id_categoria and year(ped.fecha)="+criterio4+" and month(ped.fecha)="+criterio3+" and cat.nombre_categoria='"+criterio5+"' and ped.estado='A' and detp.estado='A' and prod.estado='A' and cat.estado='A' and ped.estadoPedido='Realizado' group by anio,fecha2,prod.id_productos,cat order by cat,ct desc";
			try {
				sentencia= con.createStatement();
				resultados= sentencia.executeQuery(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error en ejecucion de sentencia" + e.getMessage());
			}
			
			//recorrer los resultados
			try {
				while (resultados.next()){
					ped= new RepProdxCatEntidad();
					ped.setFinicial(Integer.toString(resultados.getInt("anio")));
					System.out.println("lol: "+ped.getFinicial());
					ped.setFfinal(ObtenerMes(resultados.getInt("fecha2")));
					ped.setCtpedidos(resultados.getInt("ct"));
					ped.setProductos(resultados.getString("pro"));
					ped.setCategoria(resultados.getString("cat"));
					lista.add(ped);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error en recorrer los resultados");
			}
			
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al cerrar la conexion");
		}
		
		return lista;	
	}
	
public ArrayList<RepProdxCatEntidad> ReportePorFecha( String criterio3,String criterio4, String criterio5){
		
		ArrayList<RepProdxCatEntidad> lista= null;
		//conectar a la bd
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		if(con==null){return lista;}
		
		Statement sentencia;
		ResultSet resultados= null; 
		String query="";
		RepProdxCatEntidad ped= null;				
		
		lista= new ArrayList<RepProdxCatEntidad>();
				query="select year(ped.fecha) as anio,month(ped.fecha) as fecha2,count(ped.id_pedidos) as ct,prod.nombre_producto as pro,cat.nombre_categoria as cat from pedidos as ped,detallepedido as detp,productos as prod,categoria as cat where ped.id_pedidos=detp.id_pedidos and detp.id_productos=prod.id_productos and prod.id_categoria=cat.id_categoria and ((ped.fecha>='"+criterio3+"') and (ped.fecha<='"+criterio4+"')) and cat.nombre_categoria='"+criterio5+"' and ped.estado='A' and detp.estado='A' and prod.estado='A' and cat.estado='A' and ped.estadoPedido='Realizado' group by prod.id_productos,cat order by cat,ct desc";
			
			try {
				sentencia= con.createStatement();
				resultados= sentencia.executeQuery(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error en ejecucion de sentencia" + e.getMessage());
			}
			
			try {
					while (resultados.next()){
						ped= new RepProdxCatEntidad();
						ped.setFinicial(criterio3);
						ped.setFfinal(criterio4);
						ped.setProductos(resultados.getString("pro"));
						ped.setCtpedidos(resultados.getInt("ct"));
						ped.setCategoria(resultados.getString("cat"));
						lista.add(ped);
										
					}
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error en recorrer los resultados");
			}
		
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al cerrar la conexion");
		}
		
		return lista;	
	}
	
	public String ObtenerMes(int mes){
		String month2="";
		switch(mes){
			case 1:
				month2="Enero";
				break;
			case 2: 
				month2="Febrero";
				break;
			case 3: 
				month2="Marzo";
				break;
			case 4: 
				month2="Abril";
				break;
			case 5: 
				month2="Mayo";
				break;
			case 6: 
				month2="Junio";
				break;
			case 7: 
				month2="Julio";
				break;
			case 8: 
				month2="Agosto";
				break;
			case 9: 
				month2="Septiembre";
				break;
			case 10: 
				month2="Octubre";
				break;
			case 11: 
				month2="Noviembre";
				break;
			case 12: 
				month2="Diciembre";
				break;
		}
		return month2;
	}

}
