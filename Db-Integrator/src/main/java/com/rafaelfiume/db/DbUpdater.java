package com.rafaelfiume.db;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.apache.maven.plugins.annotations.LifecyclePhase.INSTALL;

/**
 * This a very simplistic version of a plugin to update the database in dev, staging and prod environments.
 * It will be improved as we play the stories. \n\n
 * <p/>
 * The goal here is to change the db according with the script it finds in the scripts directory.
 */
@Mojo(name = "updatedb", aggregator = true, defaultPhase = INSTALL)
public class DbUpdater extends AbstractMojo {

    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;

    public void execute() throws MojoExecutionException {
        getLog().info("Updating db now... ");

        File f = outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        File touch = new File(f, "touch.txt");

        try (FileWriter w = new FileWriter(touch)) {
            w.write("touch.txt");

        } catch (IOException e) {
            getLog().error("Error updating db :-( Hopefully this isn't happening in production :P");
            throw new MojoExecutionException("Error creating file " + touch, e);
        }

        getLog().info("Done updating db :-)");
    }
}
