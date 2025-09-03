package com.example.controller;

import com.example.controller.dto.EventDTO;
import com.example.model.Event;
import com.example.repository.EventRepository;
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
    private EventRepository eventRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        gsonBuilder = (GsonBuilder) getServletContext().getAttribute("GSON_BLDR");
        RepositoryProvider provider = (RepositoryProvider) getServletContext().getAttribute("REPO_PRVDR");
        eventRepository = provider.getEventRepository();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String userId = request.getParameter("user_id");
        String eventId = request.getParameter("event_id");
        PrintWriter out = response.getWriter();
        try {
            if (userId == null && eventId == null) {
                List<EventDTO> events = eventRepository.getAll().stream().map(e -> new EventDTO(e)).toList();
                String json = gsonBuilder.create().toJson(events, List.class);
                System.out.println(json);
                out.write(json);
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (userId != null && eventId == null) {
                int id = Integer.parseInt(userId);
                List<EventDTO> events = eventRepository
                    .findByUserId(id)
                    .stream()
                    .map(e -> new EventDTO(e))
                    .toList();
                String json = gsonBuilder.create().toJson(events, List.class);
                System.out.println(json);
                out.write(json);
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (userId == null && eventId != null) {
                int id = Integer.parseInt(eventId);
                EventDTO event = new EventDTO(eventRepository.getById(id));
                String json = gsonBuilder.create().toJson(event, EventDTO.class);
                System.out.println(json);
                out.write(json);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
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
        PrintWriter out = response.getWriter();
        try (BufferedReader reader = request.getReader()) {
            Gson gson = gsonBuilder.create();
            EventDTO event = gson.fromJson(reader, EventDTO.class);
            Event e = eventRepository.save(event.getUserId(), event.getFileId());
            String json = gson.toJson(new EventDTO(e));
            out.write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (RepositoryException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IllegalStateException | IllegalArgumentException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try (BufferedReader reader = request.getReader()) {
            Gson gson = gsonBuilder.create();
            Event event = gson.fromJson(reader, Event.class);
            String json = gson.toJson(new EventDTO(eventRepository.update(event)));
            out.write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RepositoryException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String param = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            int id = Integer.parseInt(param);
            eventRepository.deleteById(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RepositoryException e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.close();
        }
    }
}
