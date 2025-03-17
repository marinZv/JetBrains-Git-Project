package org.example.api.impl;

import org.example.git.GitLocalRepository;
import org.example.git.GitRemoteRepository;
import org.example.git.impl.GitLocalRepositoryImpl;
import org.example.git.impl.GitRemoteRepositoryImpl;
import org.example.api.GitAnalyzer;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitAnalyzerImpl implements GitAnalyzer {

    private static final Logger LOGGER = Logger.getLogger(GitAnalyzerImpl.class.getName());
    private final GitLocalRepository localRepo;
    private final GitRemoteRepository remoteRepo;

    public GitAnalyzerImpl(String localRepoPath, String owner, String repo, String accessToken) {
        this.localRepo = new GitLocalRepositoryImpl(localRepoPath);
        this.remoteRepo = new GitRemoteRepositoryImpl(owner, repo, accessToken);
    }

    public GitAnalyzerImpl(GitLocalRepositoryImpl localRepo, GitRemoteRepositoryImpl remoteRepo){
        this.localRepo = localRepo;
        this.remoteRepo = remoteRepo;
    }

    public Set<String> getChangedFiles(String branchA, String branchB) {

        String mergeBaseCommit = localRepo.getMergeBaseCommit(branchA, branchB);

        if (mergeBaseCommit == null) {
            LOGGER.log(Level.WARNING, "Merge base commit could not be found.");
            return new HashSet<>();
        }

        Set<String> localChanges = localRepo.getChangedFilesFromBranch(branchB, mergeBaseCommit);
        Set<String> remoteChanges = remoteRepo.getChangedFiles(branchA, mergeBaseCommit);

        Set<String> commonChangedFiles = new HashSet<>(localChanges);
        commonChangedFiles.retainAll(remoteChanges);

        return commonChangedFiles;
    }

}
