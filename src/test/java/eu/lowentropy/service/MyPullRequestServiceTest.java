package eu.lowentropy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestSearchRequest;
import com.atlassian.bitbucket.pull.PullRequestService;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageImpl;
import com.atlassian.bitbucket.util.PageRequest;
import com.atlassian.bitbucket.util.PageRequestImpl;

public class MyPullRequestServiceTest {

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private PullRequestService pullRequestService;

    private MyPullRequestService myPullRequestService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        myPullRequestService = new MyPullRequestService(repositoryService, pullRequestService);
    }

    @Test
    public void testFindByProject() {
        // Given
        String projectId = "1";

        List<Repository> repositories = Arrays.asList(mock(Repository.class));
        Page<Repository> repositoryPage = new PageImpl<>(new PageRequestImpl(0, 30), repositories, true);
        given(repositoryService.findByProjectKey(anyString(), any(PageRequest.class)))
            .willReturn(repositoryPage);

        List<PullRequest> pullRequests1 = Arrays.asList(mock(PullRequest.class));
        Page<PullRequest> pullRequestPage1 = new PageImpl<>(new PageRequestImpl(0, 30), pullRequests1, false);
        Page<PullRequest> pullRequestPage2 = new PageImpl<>(new PageRequestImpl(30, 30), pullRequests1, true);
        given(pullRequestService.search(any(PullRequestSearchRequest.class), any(PageRequest.class)))
            .willReturn(pullRequestPage1, pullRequestPage2);

        // When
        List<PullRequest> pullRequestsResult = myPullRequestService.findByProject(projectId);

        // Then
         assertThat(pullRequestsResult).hasSize(2);
         assertThat(pullRequestsResult.get(0)).isEqualTo(pullRequests1.get(0));
         assertThat(pullRequestsResult.get(1)).isEqualTo(pullRequests1.get(0));
    }
}
