/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Tienda.service;

import com.Tienda.domain.Categoria;
import java.util.List;

/**
 *
 * @author danny
 */
public interface CategoriaService  {
    //Retorna una lista de categorías, (activas o todas)
    public List<Categoria> getCategorias(boolean activos);
    
    //Retorna una categoria por Id
    public Categoria getCategoria(Categoria categoria);
    
    //Se inserta un nuevo registro si el Id de la categoría está vacío
    //Se actualiza el registro si el Id de la categoría No está vacío
    public void save(Categoria categoria);
    
    //Se elimina el categoria que tiene el id pasado por parámetro
    public void delete(Categoria categoria);
}
