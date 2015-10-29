package com.deveo.plugins.jenkins;

import com.deveo.plugin.jenkins.DeveoRepository;
import com.deveo.plugin.jenkins.DeveoURLException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

public class DeveoRepositoryTest {

    DeveoRepository repository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsDeveoURLExceptionForUnsupportedURL() throws DeveoURLException {
        thrown.expect(DeveoURLException.class);
        thrown.expectMessage("The URL doesn't appear to be a Deveo URL.");

        new DeveoRepository("https://github.com/jenkinsci/deveo-plugin.git");
    }

    @Test
    public void acceptsGitHTTPURL() throws DeveoURLException {
        repository = new DeveoRepository("https://vellu@deveo.com/foo/projects/bar/repositories/git/baz");

        assertEquals("bar", repository.getProjectId());
        assertEquals("baz", repository.getId());
    }

    @Test
    public void acceptsSubversionHTTPURL() throws DeveoURLException {
        repository = new DeveoRepository("https://vellu@deveo.com/foo/projects/bar/repositories/subversion/baz");

        assertEquals("bar", repository.getProjectId());
        assertEquals("baz", repository.getId());
    }

    @Test
    public void acceptsMercurialHTTPURL() throws DeveoURLException {
        repository = new DeveoRepository("https://vellu@deveo.com/foo/projects/bar/repositories/mercurial/baz");

        assertEquals("bar", repository.getProjectId());
        assertEquals("baz", repository.getId());
    }

    @Test
    public void acceptsGitSSHURL() throws DeveoURLException {
        repository = new DeveoRepository("deveo@deveo.com:foo/projects/bar/repositories/git/baz");

        assertEquals("bar", repository.getProjectId());
        assertEquals("baz", repository.getId());
    }

    @Test
    public void acceptsSubversionSSHURL() throws DeveoURLException {
        repository = new DeveoRepository("svn+ssh://deveo@deveo.com/foo/projects/bar/repositories/subversion/baz");

        assertEquals("bar", repository.getProjectId());
        assertEquals("baz", repository.getId());
    }

    @Test
    public void acceptsMercurialSSHURL() throws DeveoURLException {
        repository = new DeveoRepository("ssh://deveo@deveo.com/foo/projects/bar/repositories/mercurial/baz");

        assertEquals("bar", repository.getProjectId());
        assertEquals("baz", repository.getId());
    }
}
