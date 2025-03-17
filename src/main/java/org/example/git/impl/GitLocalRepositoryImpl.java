package org.example.git.impl;

import org.example.git.GitLocalRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitLocalRepositoryImpl implements GitLocalRepository {

    private static final Logger LOGGER = Logger.getLogger(GitLocalRepositoryImpl.class.getName());
    private final String localRepoPath;

    public GitLocalRepositoryImpl(String localRepoPath) {
        this.localRepoPath = localRepoPath;
    }

    public String getMergeBaseCommit(String branchA, String branchB) {
        return executeGitCommand("git", "merge-base", branchA, branchB);
    }

    public Set<String> getChangedFilesFromBranch(String branch, String mergeBaseCommit) {
        Set<String> changedFiles = new HashSet<>();
        String output = executeGitCommand("git", "diff", "--name-only", "--diff-filter=AM", mergeBaseCommit + ".." + branch);
        if (output != null) {
            for (String line : output.split("\n")) {
                changedFiles.add(line);
            }
        }
        return changedFiles;
    }

    private String executeGitCommand(String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new java.io.File(localRepoPath));
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String output = reader.readLine();
                process.waitFor();
                return output;
            } catch (IOException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error executing git command: " + e.getMessage(), e);
                return null;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing git command process: " + e.getMessage(), e);
            return null;
        }
    }
}
