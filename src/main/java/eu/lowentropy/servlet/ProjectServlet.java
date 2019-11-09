package eu.lowentropy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.bitbucket.project.Project;
import com.atlassian.bitbucket.project.ProjectService;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.google.common.collect.ImmutableMap;

public class ProjectServlet extends HttpServlet {

    private BaseServlet baseServlet;
    private ProjectService projectService;

    public ProjectServlet(SoyTemplateRenderer soyTemplateRenderer, ProjectService projectService) {
        super();
        this.baseServlet = new BaseServlet(soyTemplateRenderer);
        this.projectService = projectService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("toto");
        // Get projectKey from path
        String pathInfo = req.getPathInfo();

        String[] components = pathInfo.split("/");

        if (components.length < 2) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Project project = projectService.getByKey(components[1]);

        if (project == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        baseServlet.render(resp, "plugin.template.pullRequestProject", ImmutableMap.<String, Object>of("project", project));
    }
}
