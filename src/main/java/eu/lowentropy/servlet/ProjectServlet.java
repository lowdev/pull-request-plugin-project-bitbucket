package eu.lowentropy.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.bitbucket.project.Project;
import com.atlassian.bitbucket.project.ProjectService;
import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestService;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.google.common.collect.ImmutableMap;

import eu.lowentropy.service.MyPullRequestService;

public class ProjectServlet extends HttpServlet {

    private static final long serialVersionUID = 590756777144017815L;

    private BaseServlet baseServlet;
    private ProjectService projectService;
    private MyPullRequestService myPullRequestService;

    public ProjectServlet(SoyTemplateRenderer soyTemplateRenderer, ProjectService projectService,
            PullRequestService pullRequestService, RepositoryService repositoryService) {
        super();
        this.baseServlet = new BaseServlet(soyTemplateRenderer);
        this.projectService = projectService;
        this.myPullRequestService = new MyPullRequestService(repositoryService, pullRequestService);
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

        List<PullRequest> pullRequests = myPullRequestService.findByProject(project.getKey());

        baseServlet.render(resp, "plugin.template.pullRequestProject", ImmutableMap.<String, Object>of("project", project));
    }
}
