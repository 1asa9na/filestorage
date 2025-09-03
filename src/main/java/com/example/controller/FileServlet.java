package com.example.controller;

import com.example.controller.dto.FileDTO;
import com.example.model.File;
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
 * Servlet for handling file-related requests.
 */

@WebServlet("/files")
public class FileServlet extends HttpServlet {
    private GsonBuilder gsonBuilder;
    private GenericRepository<File, Integer> repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        gsonBuilder = (GsonBuilder) getServletContext().getAttribute("GSON_BLDR");
        RepositoryProvider provider = (RepositoryProvider) getServletContext().getAttribute("REPO_PRVDR");
        repository = provider.getFileRepository();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String param = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            if (param == null) {
                List<FileDTO> files = repository.getAll().stream().map(f -> new FileDTO(f)).toList();
                String json = gsonBuilder.create().toJson(files, List.class);
                System.out.println(json);
                out.write(json);
            } else {
                int id = Integer.parseInt(param);
                FileDTO file = new FileDTO(repository.getById(id));
                String json = gsonBuilder.create().toJson(file, FileDTO.class);
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
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (
                PrintWriter out = response.getWriter();
                BufferedReader reader = request.getReader();) {
            Gson gson = gsonBuilder.create();
            File file = gson.fromJson(reader, File.class);
            String json = gson.toJson(new FileDTO(repository.save(file)));
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
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (
                PrintWriter out = response.getWriter();
                BufferedReader reader = request.getReader();) {
            Gson gson = gsonBuilder.create();
            File file = gson.fromJson(reader, File.class);
            String json = gson.toJson(new FileDTO(repository.update(file)));
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
            HttpServletResponse response) throws ServletException, IOException {
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
