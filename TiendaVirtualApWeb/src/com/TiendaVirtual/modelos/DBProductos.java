package com.TiendaVirtual.modelos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.zkoss.zul.Image;

import com.TiendaVirtual.entidades.Categorias;
import com.TiendaVirtual.entidades.DetallePedido;
import com.TiendaVirtual.entidades.Productos;

public class DBProductos {

	public int id_ultimoProducto(){			
		int id=0;
		//conectar a la bd
		DBManager dbmanager = new DBManager();
		Connection con = dbmanager.getConection();
		if(con==null){return 0;}
		
		Statement sentencia;
		ResultSet resultados= null;
		
		String query = "SELECT id_productos as id FROM productos as pro order by id desc limit 1";
		System.out.println(query);
		
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
				id=resultados.getInt("id");
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
		
		return id;	
	}
	
		public ArrayList<Productos> buscarproductos(String criterio){			
			ArrayList<Productos> lista= null;
			//conectar a la bd
			DBManager dbmanager = new DBManager();
			Connection con = dbmanager.getConection();
			if(con==null){return lista;}
			
			Statement sentencia;
			ResultSet resultados= null;
			
			String query="";
			if(criterio.equals("") ){
			query = "SELECT * FROM productos as pro, categoria as cat where pro.id_categoria= cat.id_categoria and pro.estado='A'";
				
				
			}
			else{
			query = "SELECT * FROM productos as pro , categoria as cat where pro.id_categoria= cat.id_categoria and pro.estado='A' and (pro.nombre_producto like '%" + criterio + "%' ) order by pro.nombre_producto";
				
			System.out.println(query);
			}
			
			try {
				sentencia= con.createStatement();
				resultados= sentencia.executeQuery(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error en ejecucion de sentencia" + e.getMessage());
			}
			
			Productos productos= null;				
			lista= new ArrayList<Productos>();
			//recorrer los resultados
			try {
				while (resultados.next()){
					productos= new Productos();
					Blob blob=resultados.getBlob("imagen");
					BufferedImage img = null;
					if(blob!=null){
						byte[] data = blob.getBytes(1, (int)blob.length());
						
						try {
							img = ImageIO.read(new ByteArrayInputStream(data));
						} catch (IOException ex) {
							ex.printStackTrace();
							System.out.println("error: "+ex.getMessage());
						}
					}
					
					productos.setId(resultados.getInt("id_productos"));					
					Categorias categorias = new Categorias();
					categorias.setId(resultados.getInt("id_categoria"));
					categorias.setNom_categoria(resultados.getString("nombre_categoria"));
					productos.setCategoria(categorias);					
					productos.setNombre_producto(resultados.getString("nombre_producto"));
					productos.setDescripcion(resultados.getString("descripcion"));
					productos.setStock_minimo(resultados.getInt("stock_minimo"));
					productos.setStock_actual(resultados.getInt("stock_actual"));
					productos.setPrecio(resultados.getDouble("precio"));
					productos.setImagen("lol");
					productos.setImagen2(img);
					productos.setPathservidor(resultados.getString("pathservidor"));
					productos.setPathimagen(resultados.getString("pathimagen"));
					lista.add(productos);
									
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
	
	public boolean crearproductos(Productos productos){
	boolean resultado = false;
	Connection con =null;
	PreparedStatement stmt =null;							
	DBManager dbm = new DBManager(); 
	con = dbm.getConection();
	boolean imgalert=false;
	File file=null;
	FileInputStream fis=null;
	try {
		file = new File(productos.getImagen());
		fis = new FileInputStream(file);
		imgalert=true;
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		imgalert=false;
	}
	
	
	
	if(imgalert){
		String sql ="INSERT INTO productos (id_categoria, nombre_producto,descripcion, stock_minimo, stock_actual,precio, imagen,estado,pathservidor,pathimagen) VALUES (?,?,?,?,?,?,?,?,?,?)";			
		try {
			con.setAutoCommit(false);								
			stmt = con.prepareStatement(sql);
			Categorias categorias = productos.getCategoria();
			stmt.setInt(1, categorias.getId());
			stmt.setString(2, productos.getNombre_producto());
			stmt.setString(3, productos.getDescripcion());
			stmt.setInt(4, productos.getStock_minimo());
			stmt.setInt(5, productos.getStock_actual());
			stmt.setDouble(6, productos.getPrecio());
			stmt.setBinaryStream(7,fis,(int)file.length());
			stmt.setString(8, "A");
			stmt.setString(9, productos.getPathservidor());
			stmt.setString(10,productos.getPathimagen());
			System.out.println(stmt);
			int numerofilas = stmt.executeUpdate();
			if(numerofilas>0){
				con.commit();
				resultado = true;
			}
			else {
	   		    System.out.println("No se puedo crear un nueva categoria");
				con.rollback();
			}
			} catch (SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error al crear una nueva categoria" + e.getMessage());
			}
	}else{
		resultado=false;
		System.out.println("Error al cargar imagen a la Base de datos!");
	}
	
	try {
		stmt.close();
		 fis.close();
		 //file.delete();
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		System.out.println("Lol : "+e.getMessage());
	}
		return resultado;
	}
					
	public boolean editarproductos(Productos productos){
		boolean resultado = false;
		Connection con = null;
		PreparedStatement sentencia=null;
		DBManager dbm = new DBManager();
		con = dbm.getConection();
		boolean imgalert=false;
		File file=null;
		FileInputStream fis=null;
		try {
			file = new File(productos.getImagen());
			fis = new FileInputStream(file);
			imgalert=true;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			imgalert=false;
		}
		
		if(imgalert){
			String sql = "UPDATE productos SET id_categoria=?,nombre_producto=?, descripcion=?, stock_minimo=?, stock_actual=?, precio=?, imagen=?, pathservidor=?, pathimagen=? where id_productos=?";
			try {
				con.setAutoCommit(false);
				sentencia = con.prepareStatement(sql);
				Categorias categorias = productos.getCategoria();
				sentencia.setInt(1, categorias.getId());
				sentencia.setString(2,productos.getNombre_producto());
				sentencia.setString(3,productos.getDescripcion());
				sentencia.setInt(4,productos.getStock_minimo());
				sentencia.setInt(5,productos.getStock_actual());
				sentencia.setDouble(6,productos.getPrecio());
				sentencia.setBinaryStream(7,fis,(int)file.length());
				sentencia.setString(8, productos.getPathservidor());
				sentencia.setString(9, productos.getPathimagen());
				sentencia.setInt(10, productos.getId());
				int numFilasAfectadas = sentencia.executeUpdate();
				if(numFilasAfectadas >0){									
					con.commit();
					resultado =true;
				}else{
					con.rollback();
				}	
			} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		}else{
			resultado=false;
			System.out.println("Error al cargar imagen a la Base de datos!");
		}
		
		try {
			sentencia.close();
			 fis.close();
			 //file.delete();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Lol : "+e.getMessage());
		}
	return resultado;
	}
	

public void editarstockproductos(Integer criterio){			
	DBManager dbmanager = new DBManager();
	Connection con = dbmanager.getConection();
	if(con==null){return;}		
	Statement sentencia;
	ResultSet resultados= null;		
	String query="";		
	query = "SELECT * FROM detallepedido as det , productos as prod where det.estado='A' and det.id_productos=prod.id_productos and (det.id_pedidos like '%" + criterio + "%' ) ";
	System.out.println(query);		
	try {
		sentencia= con.createStatement();
		resultados= sentencia.executeQuery(query);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("Error en ejecucion de sentencia" + e.getMessage());
	}		
	DetallePedido detallepedidos= null;				
	try {
		while (resultados.next()){
			detallepedidos= new DetallePedido();
			Productos productos = new Productos();
			productos.setId(resultados.getInt("id_productos"));
			productos.setStock_actual(resultados.getInt("stock_actual"));
			detallepedidos.setCantidad(resultados.getInt("cantidad"));
			
			
			//
			PreparedStatement sentencia2;			
			String sql = "UPDATE productos SET stock_actual=? where id_productos=? and estado='A'";
			try {
				con.setAutoCommit(false);
				sentencia2 = con.prepareStatement(sql);
				int stock = productos.getStock_actual() - detallepedidos.getCantidad();
				//Messagebox.show(""+stock);
				sentencia2.setInt(1,stock);
				sentencia2.setInt(2, productos.getId());
				int numFilasAfectadas = sentencia2.executeUpdate();
				if(numFilasAfectadas >0){									
					con.commit();
				
				}else{
					
					con.rollback();
				}	
			} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
			
			
			//
							
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
}

public boolean EliminarproductosLogico(Productos productos){
	boolean resultado = false;
	String sql="";
	Connection con = null;							
	PreparedStatement sentencia;
	DBManager dbm = new DBManager();
	con = dbm.getConection();
	sql = "UPDATE productos SET estado=?"
			+ " where id_productos=? ";
	System.out.println(sql);
	try {
		con.setAutoCommit(false);
		sentencia = con.prepareStatement(sql);
		sentencia.setString(1, productos.getEstado());
		sentencia.setInt(2, productos.getId());
		int numFilasAfectadas = sentencia.executeUpdate();
		if(numFilasAfectadas > 0){
			resultado = true;
			con.commit();
		}else{
			con.rollback();
		}	
	} catch (SQLException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
return resultado;
}					
						
}
