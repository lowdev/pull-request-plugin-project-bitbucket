package eu.lowentropy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestSearchRequest;
import com.atlassian.bitbucket.pull.PullRequestService;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageRequest;
import com.atlassian.bitbucket.util.PageRequestImpl;

public class MyPullRequestService {

    private RepositoryService repositoryService;
    private PullRequestService pullRequestService;

    public MyPullRequestService(RepositoryService repositoryService, PullRequestService pullRequestService) {
        this.pullRequestService = pullRequestService;
        this.repositoryService = repositoryService;
    }

    public List<PullRequest> findByProject(String projectId) {
        PageRequest pageRequest = new PageRequestImpl(0, 30);
        Page<Repository> repositories = repositoryService.findByProjectKey(projectId, pageRequest);

        return repositories.stream()
            .map(repository -> findByRepository(repository.getId()))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    private List<PullRequest> findByRepository(int repositoryId) {
        PullRequestSearchRequest search =
                new PullRequestSearchRequest.Builder()
                .fromRepositoryId(repositoryId)
                .build();

        List<PullRequest> pullRequests = new ArrayList<>();

        PageRequest pageRequest = new PageRequestImpl(0, 30);
        Page<PullRequest> pullRequestPage = pullRequestService.search(search, pageRequest);
        while (!pullRequestPage.getIsLastPage()) {
            pullRequestPage.getValues().forEach(pullRequests::add);
            pageRequest = pullRequestPage.getNextPageRequest();
            pullRequestPage = pullRequestService.search(search, pageRequest);
        }
        pullRequestPage.getValues().forEach(pullRequests::add);

        return pullRequests;
    }
}
