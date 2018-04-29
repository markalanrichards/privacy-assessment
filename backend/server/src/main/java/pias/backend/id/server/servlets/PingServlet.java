package pias.backend.id.server.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PingServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(PingServlet.class);

    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException,
            IOException {
        final String servletPath = request.getServletPath();
        LOGGER.debug("servletPath {}", servletPath);
        response.setStatus(200);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write("pong");
            writer.flush();
        }
    }

}
