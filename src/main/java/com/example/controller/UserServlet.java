package com.example.controller;

import com.example.controller.dto.UserDTO;
import com.example.model.User;
import com.example.repository.GenericRepository;
import com.example.repository.RepositoryException;
import com.example.util.RepositoryProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet for handling user-related requests.
 */

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private GsonBuilder gsonBuilder;
    private GenericRepository<User, Integer> repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        gsonBuilder = (GsonBuilder) getServletContext().getAttribute("GSON_BLDR");
        RepositoryProvider provider = (RepositoryProvider) getServletContext().getAttribute("REPO_PRVDR");
        repository = provider.getUserRepository();
    }

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/json");
        String param = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            if (param == null) {
                List<UserDTO> users = repository.getAll().stream().map(u -> new UserDTO(u)).toList();
                String json = gsonBuilder.create().toJson(users, List.class);
                System.out.println(json);
                out.write(json);
            } else {
                int id = Integer.parseInt(param);
                UserDTO user = new UserDTO(repository.getById(id));
                String json = gsonBuilder.create().toJson(user, UserDTO.class);
                System.out.println(json);
                out.write(json);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RepositoryException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/json");
        try (
            PrintWriter out = response.getWriter();
            BufferedReader reader = request.getReader();
        ) {
            Gson gson = gsonBuilder.create();
            User user = gson.fromJson(reader, User.class);
            String json = gson.toJson(new UserDTO(repository.save(user)));
            out.write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RepositoryException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/json");
        try (
            PrintWriter out = response.getWriter();
            BufferedReader reader = request.getReader();
        ) {
            Gson gson = gsonBuilder.create();
            User user = gson.fromJson(reader, User.class);
            String json = gson.toJson(new UserDTO(repository.update(user)));
            out.write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RepositoryException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/json");
        String param = request.getParameter("id");
        try (PrintWriter out = response.getWriter()) {
            if (param == null) {
                throw new IllegalArgumentException("Invalid parameter");
            }
            int id = Integer.parseInt(param);
            repository.deleteById(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RepositoryException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
