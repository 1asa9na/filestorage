package com.example.util;

import com.example.repository.EventRepository;
import com.example.repository.FileRepository;
import com.example.repository.UserRepository;
import com.example.repository.hibernate.HibernateEventRepositoryImpl;
import com.example.repository.hibernate.HibernateFileRepositoryImpl;
import com.example.repository.hibernate.HibernateUserRepositoryImpl;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * ServletContextListener implementation for initializing the File Storage application.
 */

@WebListener
public class FileStorageServletContextListener implements ServletContextListener {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("filestorage");
    /**
     * Context initialization for the File Storage application.
     */

    public void contextInitialized(ServletContextEvent sce) {
        UserRepository ur
            = new HibernateUserRepositoryImpl(emf);
        EventRepository er
            = new HibernateEventRepositoryImpl(emf);
        FileRepository fr
            = new HibernateFileRepositoryImpl(emf);
        RepositoryProvider provider = new RepositoryProviderImpl(sce.getServletContext());
        provider.setUserRepository(ur);
        provider.setEventRepository(er);
        provider.setFileRepository(fr);
        GsonBuilder gsonBuilder = new GsonBuilder();
        sce.getServletContext().setAttribute("GSON_BLDR", gsonBuilder);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
