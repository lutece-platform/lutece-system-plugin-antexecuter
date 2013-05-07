/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.antexecuter.service;

import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.service.init.StartUpService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import java.io.File;


/**
 * This class dynamically executes an ant buildfile.
 * The ant file is executed only if the database doesn't exist yet.
 * See methods javadoc for more details.
 */
public class AntExecuter implements StartUpService
{
    // CONSTANTS
    private static final String PATH_BUILDFILE = "/WEB-INF/sql/build.xml";
    private static final String ANTFILE_PROPERTY = "ant.file";
    private static final String ANT_TARGET = "all";

    /**
     * Executes the ant buildfile.
     */
    private void executeAntFile(  )
    {
        // Project creation
        Project project = new Project(  );
        project.init(  );

        // Logger to see ant execution and error messages
        DefaultLogger logger = new DefaultLogger(  );
        logger.setMessageOutputLevel( Project.MSG_INFO );
        logger.setErrorPrintStream( System.err );
        logger.setOutputPrintStream( System.out );
        project.addBuildListener( logger );

        // Loading of build file, configuration of the project and execution
        File buildFile = new File( AppPathService.getAbsolutePathFromRelativePath( PATH_BUILDFILE ) );
        ProjectHelper.configureProject( project, buildFile );
        project.setProperty( ANTFILE_PROPERTY, buildFile.getAbsolutePath(  ) );
        project.executeTarget( ANT_TARGET );
    }

    /**
     * This method is specified in the StartUpService interface.
     * It is called when the webapp is deployed.
     * It calls a random SQL query and executes the build file if the query
     * fails.
     */
    public void process(  )
    {
        try
        {
            RightHome.getRightsList(  );
        }
        catch ( Exception e )
        {
            executeAntFile(  );
        }
    }

    /**
     * Returns the name of the process
     * @return The name of the process
     */
    public String getName(  )
    {
        return "Ant Executer";
    }
}
