package org.example.api;

import java.util.Set;


/**
 * Interface for analyzing changes between two Git branches.
 * Provides functionality to retrieve the common changed files between two branches.
 */
public interface GitAnalyzer {


    /**
     * Retrieves the common changed files between two branches by comparing them from the merge base commit.
     *
     * @param branchA The name of the first branch (local and remote as well).
     * @param branchB The name of the second branch (local).
     * @return A set of strings representing the paths of changed files common to both branches.
     * An empty set is returned if no common changes are found.
     */
    Set<String> getChangedFiles(String branchA, String branchB);
}
