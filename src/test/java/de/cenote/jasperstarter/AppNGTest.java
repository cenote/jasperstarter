/*
 * Copyright 2013 Cenote GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cenote.jasperstarter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 */
public class AppNGTest {

    public AppNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of private createArgumentParser method, of class App
     */
    @Test
    public void testCreateArgumentParser() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException {
        System.out.println("createArgumentParser");
        App app = new App();
        Config config = null;
        try {
            config = new Config();
        } catch (IOException ex) {
            fail(ex.getMessage(), ex);
        }
        Method method = app.getClass().getDeclaredMethod(
                "createArgumentParser", Config.class);
        method.setAccessible(true);
        ArgumentParser parser = null;
        try {
            parser = (ArgumentParser) method.invoke(app, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        assertNotNull(parser);
    }

    /**
     * Test of private parseArgumentParser method, of class App
     */
    @Test(dependsOnMethods = {"testCreateArgumentParser"})
    public void testParseArgumentParser() throws NoSuchMethodException, IllegalAccessException {
        System.out.println("parseArgumentParser");
        App app = new App();
        String[] args = {};
        Config config = null;
        try {
            config = new Config();
        } catch (IOException ex) {
            fail(ex.getMessage(), ex);
        }
        Method methodCreateArgumentParser = app.getClass().getDeclaredMethod(
                "createArgumentParser", Config.class);
        methodCreateArgumentParser.setAccessible(true);
        ArgumentParser parser = null;
        try {
            parser = (ArgumentParser) methodCreateArgumentParser.invoke(app, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        Method method = app.getClass().getDeclaredMethod(
                "parseArgumentParser", String[].class, ArgumentParser.class,
                Config.class);
        method.setAccessible(true);
        // empty args
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("too few arguments", ex.getCause().getMessage());
        }
        // one wrong arg (space)
        args = "".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            // new error output in argparse4j-0.4.0:
            assertEquals("invalid choice: '' (choose from 'compile', 'cp',"
                    + " 'process', 'pr', 'list_printers', 'printers', 'lpr',"
                    + " 'list_parameters', 'params', 'lpa')",
                    ex.getCause().getMessage());
        }
    }

    /**
     * Test of private parseArgumentParser method, of class App
     *
     * detailed tests for command process
     */
    @Test(dependsOnMethods = {"testCreateArgumentParser"})
    public void testParseArgumentParserCommandProcess() throws NoSuchMethodException, IllegalAccessException {
        System.out.println("parseArgumentParserCommandProcess");
        App app = new App();
        String[] args = {};
        Config config = null;
        try {
            config = new Config();
        } catch (IOException ex) {
            fail(ex.getMessage(), ex);
        }
        Method methodCreateArgumentParser = app.getClass().getDeclaredMethod(
                "createArgumentParser", Config.class);
        methodCreateArgumentParser.setAccessible(true);
        ArgumentParser parser = null;
        try {
            parser = (ArgumentParser) methodCreateArgumentParser.invoke(app, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        Method method = app.getClass().getDeclaredMethod(
                "parseArgumentParser", String[].class, ArgumentParser.class,
                Config.class);
        method.setAccessible(true);
        // just the process command
        args = "process".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("too few arguments", ex.getCause().getMessage());
        }
        // now the alias pr
        args = "pr".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("too few arguments", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr -f".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -f: expected 1 argument(s)", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr -f ''".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("argument -f: could not convert '''' (choose from ");
            sb.append("{view,print,jrprint,pdf,rtf,docx,odt,html,xml,xls,");
            sb.append("xlsx,csv,ods,pptx,xhtml})");
            assertEquals(sb.toString(), ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr -f pdf".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("too few arguments", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        // this one shoud be complete (no db report):
        args = "pr fakefile -f pdf ".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // starting with db reports
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -t: expected one argument", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t ''".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("argument -t: could not convert '''' (choose from ");
            sb.append("{none,csv,xml,mysql,postgres,oracle,generic})");
            assertEquals(sb.toString(), ex.getCause().getMessage());
        }
        // this one shoud be complete (no db report):
        args = "pr fakefile -f pdf -t none".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // starting with db mysql ( first modification of parser)
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t mysql".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -H is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t mysql -H".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -H: expected one argument", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t mysql -H myhost".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -u is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t mysql -H myhost -u".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -u: expected one argument", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t mysql -H myhost -u myuser".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -n is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t mysql -H myhost -u myuser -n".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -n: expected one argument", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t mysql -H myhost -u myuser -n dbname".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            // this is the minimal mysql argument set
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // checking the mysql default port
        assertEquals(config.getDbPort(), new Integer("3306"));

        // starting with db postgres (next modification of parser)
        // create a fresh parser
        try {
            parser = (ArgumentParser) methodCreateArgumentParser.invoke(app, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t postgres".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -H is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        // argument -H is tested before, so fulfill immediate with argument
        args = "pr fakefile -f pdf -t postgres -H myhost".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -u is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        // argument -u is tested before, so fulfill immediate with argument
        args = "pr fakefile -f pdf -t postgres -H myhost -u myuser".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -n is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        // argument -u is tested before, so fulfill immediate with argument
        args = "pr fakefile -f pdf -t postgres -H myhost -u myuser -n dbname".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            // this is the minimal postgres argument set
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // checking the postgres default port
        assertEquals(config.getDbPort(), new Integer("5432"));

        // starting with db oracle (next modification of parser)
        // create a fresh parser
        try {
            parser = (ArgumentParser) methodCreateArgumentParser.invoke(app, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t oracle".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -H is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        // argument -H is tested before, so fulfill immediate with argument
        args = "pr fakefile -f pdf -t oracle -H myhost".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -u is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        // argument -u is tested before, so fulfill immediate with argument
        args = "pr fakefile -f pdf -t oracle -H myhost -u myuser".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -p is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t oracle -H myhost -u myuser -p".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument -p: expected one argument", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t oracle -H myhost -u myuser -p passwd".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument --db-sid is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t oracle -H myhost -u myuser -p passwd --db-sid".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument --db-sid: expected one argument", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t oracle -H myhost -u myuser -p passwd --db-sid orcl".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            // this is the minimal oracle argument set
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // checking the oracle default port
        assertEquals(config.getDbPort(), new Integer("1521"));

        // starting with csv
        // create a fresh parser
        try {
            parser = (ArgumentParser) methodCreateArgumentParser.invoke(app, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t csv".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument --data-file is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t csv --data-file fakedatafile".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            assertEquals("argument --csv-columns is required", ex.getCause().getMessage());
        }
        // try and error - follow the help message for next input
        args = "pr fakefile -f pdf -t csv --data-file fakedatafile --csv-columns a,b,c,d".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            // this is one minimal csv argument set
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // create a fresh parser
        try {
            parser = (ArgumentParser) methodCreateArgumentParser.invoke(app, config);
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
        }
        // --csv-columns only required unless --csv-use-1row is given
        args = "pr fakefile -f pdf -t csv --data-file fakedatafile --csv-first-row".split(" ");
        try {
            method.invoke(app, args, parser, config);
        } catch (InvocationTargetException ex) {
            // this is another minimal csv argument set
            fail(ex.getCause().getMessage(), ex.getCause());
        }


        // create a fresh parser
//        try {
//            parser = (ArgumentParser) methodCreateArgumentParser.invoke(app, config);
//        } catch (InvocationTargetException ex) {
//            fail(ex.getCause().getMessage(), ex.getCause());
//        }

    }

    /**
     * Test of private parseArgumentParser method, of class App
     *
     * detailed tests for command process
     */
    @Test
    public void testLocateInputFile() throws NoSuchMethodException,
            IllegalAccessException, IOException {
        System.out.println("locateInputFile");
        App app = new App();
        Method method = app.getClass().getDeclaredMethod(
                "locateInputFile", File.class);
        method.setAccessible(true);
        // try a nonexistent file
        try {
            method.invoke(app, new File("nonexistent file"));
        } catch (InvocationTargetException ex) {
            assertTrue(ex.getCause().getMessage().startsWith("Error: file not found:"));
        }
        // try a directory
        try {
            method.invoke(app, new File("src"));
        } catch (InvocationTargetException ex) {
            assertTrue(ex.getCause().getMessage().endsWith("is a directory, file needed"));
        }
        // try basename of an existing file (must be created first)
        File testfile1 = new File("target/test-classes/testfile.jrxml");
        testfile1.createNewFile();

        Object result = null;
        try {
            result = method.invoke(app, new File("target/test-classes/testfile"));
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
            //assertEquals("argument -f is required", ex.getCause().getMessage());
            System.out.println(ex.getCause().getMessage());
            assertTrue(ex.getCause().getMessage().endsWith("is a directory, file needed"));
        }
        assertEquals(((File) result).getName(), "testfile.jrxml");

        // now create the next filetype of same basename and test again
        File testfile2 = new File("target/test-classes/testfile.jasper");
        testfile2.createNewFile();
        try {
            result = method.invoke(app, new File("target/test-classes/testfile"));
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
            //assertEquals("argument -f is required", ex.getCause().getMessage());
            System.out.println(ex.getCause().getMessage());
            assertTrue(ex.getCause().getMessage().endsWith("is a directory, file needed"));
        }
        assertEquals(((File) result).getName(), "testfile.jasper");

        // now give the full filename, the same must be returned
        try {
            result = method.invoke(app, new File("target/test-classes/testfile.jrxml"));
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
            //assertEquals("argument -f is required", ex.getCause().getMessage());
            System.out.println(ex.getCause().getMessage());
            assertTrue(ex.getCause().getMessage().endsWith("is a directory, file needed"));
        }
        assertEquals(((File) result).getName(), "testfile.jrxml");

        try {
            result = method.invoke(app, new File("target/test-classes/testfile.jasper"));
        } catch (InvocationTargetException ex) {
            fail(ex.getCause().getMessage(), ex.getCause());
            //assertEquals("argument -f is required", ex.getCause().getMessage());
            System.out.println(ex.getCause().getMessage());
            assertTrue(ex.getCause().getMessage().endsWith("is a directory, file needed"));
        }
        assertEquals(((File) result).getName(), "testfile.jasper");

        // delete the testfiles
        testfile1.delete();
        testfile2.delete();
    }
}
