package org.example.git.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.example.git.GitRemoteRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitRemoteRepositoryImpl implements GitRemoteRepository {

    private static final Logger LOGGER = Logger.getLogger(GitRemoteRepositoryImpl.class.getName());
    private static final String GITHUB_API_URL_TEMPLATE = "https://api.github.com/repos/%s/%s/compare/%s...%s";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final String owner;
    private final String repo;
    private final String accessToken;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public GitRemoteRepositoryImpl(String owner, String repo, String accessToken) {
        this.owner = owner;
        this.repo = repo;
        this.accessToken = accessToken;
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Set<String> getChangedFiles(String branch, String mergeBaseCommit) {
        Set<String> changedFiles = new HashSet<>();

        String url = String.format(GITHUB_API_URL_TEMPLATE, owner, repo, mergeBaseCommit, branch);
        Request request = buildRequest(url);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.log(Level.WARNING, "GitHub API call failed with status: {0} - {1}",
                        new Object[]{response.code(), response.message()});
                return changedFiles;
            }

            Optional.ofNullable(response.body())
                    .ifPresent(body -> parseChangedFiles(body, changedFiles));

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while getting remote changed files: " + e.getMessage(), e);
        }

        return changedFiles;
    }

    private Request buildRequest(String url) {
        return new Request.Builder()
                .url(url)
                .header(AUTHORIZATION_HEADER, "token " + accessToken)
                .build();
    }

    private void parseChangedFiles(ResponseBody body, Set<String> changedFiles) {
        try {
            JsonNode jsonNode = objectMapper.readTree(body.string());
            JsonNode filesNode = jsonNode.path("files");

            if (filesNode.isArray()) {
                filesNode.forEach(fileNode ->
                        changedFiles.add(fileNode.path("filename").asText())
                );
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error parsing JSON response: " + e.getMessage(), e);
        }
    }

}
