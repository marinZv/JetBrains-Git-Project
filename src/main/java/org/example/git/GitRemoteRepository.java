package org.example.git;

import java.util.Set;

/**
 * Interface for interacting with a remote Git repository.
 * Provides a method to retrieve information about changed files from a remote repository.
 */
public interface GitRemoteRepository {

    /**
     * Retrieves the list of files that were added or modified in a remote branch since the given merge base commit.
     *
     * @param branch          The name of the branch in the remote repository.
     * @param mergeBaseCommit The commit hash to compare the branch against.
     * @return A set of strings representing the paths of files that have been added or modified in the remote branch.
     */
    Set<String> getChangedFiles(String branch, String mergeBaseCommit);
}
