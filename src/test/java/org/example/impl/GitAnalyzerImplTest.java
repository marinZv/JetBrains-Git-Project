package org.example.impl;

import org.example.api.impl.GitAnalyzerImpl;
import org.example.git.impl.GitLocalRepositoryImpl;
import org.example.git.impl.GitRemoteRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GitAnalyzerImplTest {

    private GitLocalRepositoryImpl localRepo;
    private GitRemoteRepositoryImpl remoteRepo;

    private GitAnalyzerImpl gitAnalyzer;

    @BeforeEach
    public void setUp() {
        localRepo = Mockito.mock(GitLocalRepositoryImpl.class);
        remoteRepo = Mockito.mock(GitRemoteRepositoryImpl.class);

        gitAnalyzer = new GitAnalyzerImpl(localRepo, remoteRepo);
    }


    @Test
    public void testGetChangedFilesCombined() {

        Set<String> localChangedFiles = new HashSet<>();
        localChangedFiles.add("fileA.txt");
        localChangedFiles.add("fileB.txt");

        Mockito.when(localRepo.getChangedFilesFromBranch("branchB", "mergeBaseCommit"))
                .thenReturn(localChangedFiles);

        Mockito.when(localRepo.getMergeBaseCommit("branchA", "branchB"))
                .thenReturn("mergeBaseCommit");

        Set<String> remoteChangedFiles = new HashSet<>();
        remoteChangedFiles.add("fileB.txt");

        Mockito.when(remoteRepo.getChangedFiles("branchA", "mergeBaseCommit"))
                .thenReturn(remoteChangedFiles);

        Set<String> allChangedFiles = gitAnalyzer.getChangedFiles("branchA", "branchB");

        assertNotNull(allChangedFiles);
        assertEquals(1, allChangedFiles.size());
        assertTrue(allChangedFiles.contains("fileB.txt"));
    }

    @Test
    public void testNoCommonChangedFiles() {
        Set<String> localChangedFiles = new HashSet<>();
        localChangedFiles.add("fileA.txt");

        Mockito.when(localRepo.getChangedFilesFromBranch("branchB", "mergeBaseCommit"))
                .thenReturn(localChangedFiles);

        Mockito.when(localRepo.getMergeBaseCommit("branchA", "branchB"))
                .thenReturn("mergeBaseCommit");

        Set<String> remoteChangedFiles = new HashSet<>();
        remoteChangedFiles.add("fileB.txt");

        Mockito.when(remoteRepo.getChangedFiles("branchA", "mergeBaseCommit"))
                .thenReturn(remoteChangedFiles);

        Set<String> allChangedFiles = gitAnalyzer.getChangedFiles("branchA", "branchB");

        assertNotNull(allChangedFiles);
        assertTrue(allChangedFiles.isEmpty());
    }

    @Test
    public void testOnlyLocalChanges() {
        Set<String> localChangedFiles = new HashSet<>();
        localChangedFiles.add("fileA.txt");

        Mockito.when(localRepo.getChangedFilesFromBranch("branchB", "mergeBaseCommit"))
                .thenReturn(localChangedFiles);

        Mockito.when(localRepo.getMergeBaseCommit("branchA", "branchB"))
                .thenReturn("mergeBaseCommit");

        Set<String> remoteChangedFiles = new HashSet<>();

        Mockito.when(remoteRepo.getChangedFiles("branchA", "mergeBaseCommit"))
                .thenReturn(remoteChangedFiles);

        Set<String> allChangedFiles = gitAnalyzer.getChangedFiles("branchA", "branchB");

        assertNotNull(allChangedFiles);
        assertTrue(allChangedFiles.isEmpty());
    }

    @Test
    public void testNoChangesInBothBranches() {
        Set<String> localChangedFiles = new HashSet<>();
        Set<String> remoteChangedFiles = new HashSet<>();

        Mockito.when(localRepo.getChangedFilesFromBranch("branchB", "mergeBaseCommit"))
                .thenReturn(localChangedFiles);

        Mockito.when(localRepo.getMergeBaseCommit("branchA", "branchB"))
                .thenReturn("mergeBaseCommit");

        Mockito.when(remoteRepo.getChangedFiles("branchA", "mergeBaseCommit"))
                .thenReturn(remoteChangedFiles);

        Set<String> allChangedFiles = gitAnalyzer.getChangedFiles("branchA", "branchB");

        assertNotNull(allChangedFiles);
        assertTrue(allChangedFiles.isEmpty());
    }

    @Test
    public void testMergeBaseCommitIsNull() {
        Mockito.when(localRepo.getMergeBaseCommit("branchA", "branchB"))
                .thenReturn(null);

        Set<String> allChangedFiles = gitAnalyzer.getChangedFiles("branchA", "branchB");

        assertNotNull(allChangedFiles);
        assertTrue(allChangedFiles.isEmpty());
    }

}