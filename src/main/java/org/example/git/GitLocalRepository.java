package org.example.git;

import java.util.Set;

/**
 * Interface for interacting with a local Git repository.
 * Provides methods to retrieve information about commits and file changes in a local repository.
 */
public interface GitLocalRepository {

    /**
     * Retrieves the merge base commit between two branches.
     * The merge base commit is the most recent common ancestor of the two branches.
     *
     * @param branchA The name of the first branch (local and remote as well).
     * @param branchB The name of the second branch (local).
     * @return The commit hash of the merge base commit. Returns null if no merge base is found.
     */
    String getMergeBaseCommit(String branchA, String branchB);

    /**
     * Retrieves the list of files that were added or modified in a branch since the given merge base commit.
     *
     * @param branch          The name of the branch.
     * @param mergeBaseCommit The commit hash to compare the branch against.
     * @return A set of strings representing the paths of files that have been added or modified.
     */
    Set<String> getChangedFilesFromBranch(String branch, String mergeBaseCommit);
}
