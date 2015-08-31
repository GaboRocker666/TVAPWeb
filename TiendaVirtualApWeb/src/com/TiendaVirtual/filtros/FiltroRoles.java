package com.TiendaVirtual.filtros;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.TiendaVirtual.entidades.DatosUsuario;
/**
 * Servlet Filter implementation class FiltroLogin
 */
@WebFilter("/FiltroLogin")
public class FiltroRoles implements Filter {

    /**
     * Default constructor. 
     */
    public FiltroRoles() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		//VERIFICAR SI USUARIO ESTA AUTENTICADO
		//1. OBTENER LA SESION ACTUAL
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		
		//2. obtener el objeto usuario almacenado en la sesion
		DatosUsuario datosusuario = (DatosUsuario) session.getAttribute("datosusuario");
		
		
		
		int roles = datosusuario.getUsuario().getTipousuario().getId();
		
		// verificar que tipo de usuario inicio sesion
				
		if(roles==1){
			//usuario si tiene permiso
			// pass the request along the filter chain
			chain.doFilter(request, response);
						
		}else{
			//no cuenta con el permiso necesario
			//redirigir a login.zul
			httpRequest.getServletContext().getRequestDispatcher("/login.zul").forward(request,response);
			
		}
		
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
