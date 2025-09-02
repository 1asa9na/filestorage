package com.example.controller;

import com.example.controller.dto.EventDTO;
import com.example.model.Event;
import com.example.model.File;
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
 * Servlet for handling event-related requests.
 */

@WebServlet("/events")
public class EventServlet extends HttpServlet {
    private GsonBuilder gsonBuilder;
    private GenericRepository<Event, Integer> eventRepository;
    private GenericRepository<User, Integer> userRepository;
    private GenericRepository<File, Integer> fileRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        gsonBuilder = (GsonBuilder) getServletContext().getAttribute("GSON_BLDR");
        RepositoryProvider provider = (RepositoryProvider) getServletContext().getAttribute("REPO_PRVDR");
        eventRepository = provider.getEventRepository();
        userRepository = provider.getUserRepository();
        fileRepository = provider.getFileRepository();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String userId = request.getParameter("user_id");
        String eventId = request.getParameter("event_id");
        try (PrintWriter out = response.getWriter()) {
            if (userId == null && eventId == null) {
                List<EventDTO> events = eventRepository.getAll().stream().map(e -> new EventDTO(e)).toList();
                String json = gsonBuilder.create().toJson(events, List.class);
                System.out.println(json);
                out.write(json);
            } else if (userId == null ^ eventId == null) {
                if (userId != null) {
                    int id = Integer.parseInt(userId);
                    List<EventDTO> events = userRepository
                        .getById(id)
                        .getEvents()
                        .stream()
                        .map(e -> new EventDTO(e))
                        .toList();
                    String json = gsonBuilder.create().toJson(events, List.class);
                    System.out.println(json);
                    out.write(json);
                } else {
                    int id = Integer.parseInt(eventId);
                    EventDTO event = new EventDTO(eventRepository.getById(id));
                    String json = gsonBuilder.create().toJson(event, List.class);
                    System.out.println(json);
                    out.write(json);
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String path = request.getPathInfo();
        try (
                PrintWriter out = response.getWriter();
                BufferedReader reader = request.getReader();) {
            if (path != null) {
                throw new IllegalArgumentException("Invalid path");
            }
            Gson gson = gsonBuilder.create();
            EventDTO eventdto = gson.fromJson(reader, EventDTO.class);
            File file = fileRepository.getById(eventdto.getFileId());
            User user = userRepository.getById(eventdto.getUserId());
            if (file == null || user == null) {
                throw new IllegalArgumentException("Invalid arguments");
            }
            Event event = new Event();
            event.setFile(file);
            event.setUser(user);
            String json = gson.toJson(new EventDTO(eventRepository.save(event)));
            out.write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String path = request.getPathInfo();
        try (
                PrintWriter out = response.getWriter();
                BufferedReader reader = request.getReader();) {
            if (path != null) {
                throw new IllegalArgumentException("Invalid path");
            }
            Gson gson = gsonBuilder.create();
            Event event = gson.fromJson(reader, Event.class);
            String json = gson.toJson(new EventDTO(eventRepository.update(event)));
            out.write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
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
                throw new IllegalArgumentException("Invalid path");
            }
            int id = Integer.parseInt(param);
            eventRepository.deleteById(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (RepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
