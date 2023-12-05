/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Tienda;

import java.rmi.registry.Registry;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import static org.springframework.security.core.userdetails.User.builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.Tienda.service.UsuarioDetailsService;

/**
 *
 * @author danny
 */
@Configuration
public class ProjectConfig implements WebMvcConfigurer{
    
    
    //estos métodos son para la implementación de la internacionalización
    @Bean
    public LocaleResolver localeResolver(){
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.locale.timezone");
        return slr;
    }
    
    /*localetChangeInterceptor se utiliza para crear un interceptor de cambio de idioma*/
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(localeChangeInterceptor());
    }
    
    /*Bean para poder acceder a los Messages.properties en códigoo Java*/
    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registro/nuevo").setViewName("/registro/nuevo");
    }
    
//    /* El siguiente método se utiliza para completar la clase no es 
//    realmente funcional, la próxima semana se reemplaza con usuarios de BD 
//    
//    SUPER IMPORTANTE PARA EL CASO PROGAMADO*/    
//    @Bean
//    public UserDetailsService users() {
//        //Usuario admin
//        UserDetails admin = User.builder()
//                .username("juan")
//                //NOOP se usa para no encriptar la contraseña
//                .password("{noop}123")
//                .roles("USER", "VENDEDOR", "ADMIN")
//                .build();
//        
//        //Usuario Vendedor
//        UserDetails sales = User.builder()
//                .username("rebeca")
//                .password("{noop}456")
//                .roles("USER", "VENDEDOR")
//                .build();
//        
//        //Usuario User
//        UserDetails user = User.builder()
//                .username("pedro")
//                .password("{noop}789")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user, sales, admin);
//    }
    
    @Autowired
    private UserDetailsService usuarioDetailsService;
    
    @Autowired
    private void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
        builder.userDetailsService(usuarioDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) -> request
                .requestMatchers("/","/index","/errores/**", "/error/","/error/**",
                        "/carrito/**","/pruebas/**","/reportes/**",
                        "/registro/**","/js/**","/webjars/**", "/refrescarBoton")
                        .permitAll()
                .requestMatchers(
                        "/producto/nuevo","/producto/guardar",
                        "/producto/modificar/**","/producto/eliminar/**",
                        "/categoria/nuevo","/categoria/guardar",
                        "/categoria/modificar/**","/categoria/eliminar/**",
                        "/usuario/nuevo","/usuario/guardar",
                        "/usuario/modificar/**","/usuario/eliminar/**",
                        "/reportes/**"
                ).hasRole("ADMIN")
                .requestMatchers(
                        "/producto/listado",
                        "/categoria/listado",
                        "/usuario/listado"
                ).hasAnyRole("VENDEDOR")
                .requestMatchers("/facturar/carrito")
                .hasRole("USER")
                )
                .formLogin((form) -> form
                .loginPage("/login").permitAll())
                .logout((logout) -> logout.permitAll());
        return http.build();
    }


}
