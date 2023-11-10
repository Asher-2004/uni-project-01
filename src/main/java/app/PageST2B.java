package app;

import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Map;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST2B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2B.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html += """
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Subtask 2.2</title>
                """;

        // Add some CSS (external file)
        html += """
            <link rel='stylesheet' type='text/css' href='common.css' />
            <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css">
            <link rel="shortcut icon" href="/draft.ico" type="image/x-icon">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" />
            <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                """;
        html = html + "</head>";

        // Add the body
        html = html + "<body class='task2b-body'>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html += """
            <header>
            <div class="logo">
                <a href="/"><img src="logo.png" alt=""></a>
            </div>
            <ul id="navbar">
                <li><a href="/">Home Page</a></li>
                <li><a href="mission.html">Our Mission</a></li>
                <li><a href="page2A.html">Age & Health</a></li>
                <li><a href="page2B.html" class="active">Education</a></li>
                <li><a href="page3A.html">The Gap</a></li>
                <li><a href="page3B.html">LGA Comparison</a></li>
            </ul>
            </header>
                """;

        // Add header content block


        // hero image
        html += """
            <section id="scl-nonscl-header">
                <div class="overlay"></div>
                <div class="overlay-text">
                    <h2>School & Non School Education</h2>
                    <p>Find unbiased information about school and non school completion</p>
                </div>
            </section>
                """;
        // Form

        html = html + """
            <form action="/page2B.html" method="post">
                <h2>Select either School or Non School to view</h2>
                <div>
                    <input type="radio" id="rad" name="DataBase" value="SCL">
                    <label for="DataBase">School</label>
                </div>
                <div>
                    <input type="radio" id="rad" name="DataBase" value="NonSCL">
                    <label for="Database">Non School</label>
                </div>
                <input type="submit" value="Submit">
            </form>
                """;
    
        //Code for Selector

        String DataBase1 = context.formParam("DataBase");
        // String movietype_drop = context.queryParam("DataBase");
        if (DataBase1 == null) {
            // If NULL, nothing to show, therefore we make some "no results" HTML
            html = html + "";
        } else if (DataBase1.equals("SCL")) {
            html = html + outputDataBase1(DataBase1);
        } else if (DataBase1.equals("NonSCL")) {
            html = html + outputDataBase2(DataBase1);
        } else {
            html = html + "";
        }
        String DataBase2 = context.formParam("LGA_STATE");
        String DataBase3 = context.formParam("dataType");
        String DataBase4 = context.formParam("indige1");
        String DataBase5 = context.formParam("sort");
        String DataBase6 = context.formParam("SCL");
        String DataBase7 = context.formParam("NonSCL");
       
        if (DataBase6 == null) {
            html = html + "";
        } else if (DataBase2.equals("lga") && DataBase3.equals("Raw")){
            html += "<h3>Database for LGA Raw data " + "For " + getIndigStatus(DataBase4) + " where School Year is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> SCL_lga_ranking = new ArrayList<Integer>();
            ArrayList<String> SCL_lga_name = new ArrayList<String>();
            ArrayList<Integer> SCL_lga_code = new ArrayList<Integer>();
            ArrayList<Integer> SCL_lga_count = new ArrayList<Integer>();
            String query = "SELECT ROW_NUMBER() OVER (ORDER BY sc.count " + DataBase5 + ") AS Ranking, l.name AS name, sc.lga_code, sc.count FROM SchoolYear sc JOIN LGA l on sc.lga_code = l.code WHERE sc.lga_year = 2021 " + " AND sc.indigenous_status = " + "'" + DataBase4 + "'" + " AND sc.School_Year = " + "'" + DataBase6 + "'" + " GROUP BY sc.lga_code ORDER BY sc.count " + DataBase5 + ";";
            SCL_lga_ranking = getTable1(query, "Ranking");
            SCL_lga_name = getTable2(query, "name");
            SCL_lga_code = getTable1(query, "lga_code");
            SCL_lga_count = getTable1(query, "count");
            html = html + CreateTableLGA(SCL_lga_ranking, SCL_lga_name, SCL_lga_code, SCL_lga_count);
        } else if (DataBase2.equals("state") && DataBase3.equals("Raw")){
            html += "<h3>Database for State Raw data " + "For " + getIndigStatus(DataBase4) + " where School Year is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> SCL_State_ranking = new ArrayList<Integer>();
            ArrayList<String> SCL_State_name = new ArrayList<String>();
            ArrayList<String> SCL_State_abbr = new ArrayList<String>();
            ArrayList<Integer> SCL_State_count = new ArrayList<Integer>();
            String query1 = "SELECT ROW_NUMBER() OVER (ORDER BY sum(sc.count) " + DataBase5 + ") AS Ranking, s.state_name, l.state_abbr, sum(sc.count) AS count FROM LGA l JOIN SchoolYear sc ON l.code = sc.lga_code JOIN STATE s ON l.state_abbr = s.state_abbr WHERE sc.indigenous_status = " + "'" + DataBase4 + "'" + " AND sc.School_Year = " + "'" + DataBase6 + "'" + " GROUP BY l.state_abbr ORDER BY sum(sc.count) " + DataBase5 + ";";
            SCL_State_ranking = getTable1(query1, "Ranking");
            SCL_State_name = getTable2(query1, "state_name");
            SCL_State_abbr = getTable2(query1, "state_abbr");
            SCL_State_count = getTable1(query1, "count");
            html = html + CreateTableState(SCL_State_ranking, SCL_State_name, SCL_State_abbr, SCL_State_count);
        } else if (DataBase2.equals("lga") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for LGA Proportional data " + "For " + getIndigStatus(DataBase4) + " where School Year is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> SCL_lga_ranking_pro = new ArrayList<Integer>();
            ArrayList<String> SCL_lga_name_pro = new ArrayList<String>();
            ArrayList<Integer> SCL_lga_code_pro = new ArrayList<Integer>();
            ArrayList<Integer> SCL_lga_count_pro = new ArrayList<Integer>();
            String query2 = "WITH population1 AS "
                    + "(SELECT l.name AS name, l.code AS code, sum(sc.count) AS count FROM LGA l JOIN SchoolYear sc "
                    + "ON l.code = sc.lga_code "
                    + "WHERE sc.indigenous_status = " + "'" + DataBase4 + "'" + " AND sc.lga_year = 2021 "
                    + "GROUP BY sc.lga_code), "
                    + "population2 AS "
                    + "(SELECT l.name AS name2, l.code AS code2, sum(sc.count) AS count2 FROM lga l JOIN SchoolYear sc "
                    + "ON l.code = sc.lga_code "
                    + "WHERE sc.indigenous_status = " + "'" + DataBase4 + "'" + " AND sc.School_Year = " + "'" + DataBase6 + "'" + " AND year = 2021 "
                    + "GROUP BY sc.lga_code) "
                    + "SELECT ROW_NUMBER() OVER (ORDER BY ( (population2.count2 * 100) / (population1.count) ) " + DataBase5 + ") AS Ranking, population1.name AS name, population1.code AS lga_code, ( (population2.count2 * 100) / (population1.count) ) AS count3 FROM population1 JOIN population2 ON population1.code = population2.code2 ORDER BY count3 " + DataBase5 + ";";
            SCL_lga_ranking_pro = getTable1(query2, "Ranking");
            SCL_lga_name_pro = getTable2(query2, "name");
            SCL_lga_code_pro = getTable1(query2, "lga_code");
            SCL_lga_count_pro = getTable1(query2, "count3");
            SCL_lga_count_pro = removeNull(SCL_lga_count_pro);
            html = html + CreateTableLGAPropotion(SCL_lga_ranking_pro, SCL_lga_name_pro, SCL_lga_code_pro, SCL_lga_count_pro);
        } else if (DataBase2.equals("state") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for LGA Raw data " + "For " + getIndigStatus(DataBase4) + " where School Year is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> SCL_state_ranking_pro = new ArrayList<Integer>();
            ArrayList<String> SCL_state_name_pro = new ArrayList<String>();
            ArrayList<String> SCL_state_abbr_pro = new ArrayList<String>();
            ArrayList<Integer> SCL_state_count_pro = new ArrayList<Integer>();
            String query3 = "WITH population1 AS "
                    + "(SELECT s.state_name AS state_name, l.state_abbr AS state_abbr, sum(sc.count) AS count FROM LGA l JOIN SchoolYear sc "
                    + "ON l.code = sc.lga_code JOIN STATE s ON l.state_abbr = s.state_abbr "
                    + "WHERE sc.indigenous_status = " + "'" + DataBase4 + "'" + " AND l.year = 2021 "
                    + "GROUP BY l.state_abbr), "
                    + "population2 AS "
                    + "(SELECT s.state_name AS state_name2, l.state_abbr AS state_abbr2, sum(sc.count) AS count2 FROM LGA l JOIN SchoolYear sc "
                    + "ON l.code = sc.lga_code JOIN STATE s ON l.state_abbr = s.state_abbr "
                    + "WHERE sc.indigenous_status = " + "'" + DataBase4 + "'" + " AND sc.School_Year = " + "'" + DataBase6 + "'" + " AND l.year = 2021 "
                    + "GROUP BY l.state_abbr) "
                    + "SELECT ROW_NUMBER() OVER (ORDER BY ( (population2.count2 * 100) / (population1.count) ) " + DataBase5 + ") AS Ranking, population1.state_name AS Name,\n" + //
                            "       population1.state_abbr,\n" + //
                            "       ( (population2.count2 * 100) / (population1.count) ) AS count3\n" + //
                            "  FROM population1\n" + //
                            "       JOIN\n" + //
                            "       population2 ON population1.state_abbr = population2.state_abbr2\n" + //
                            " ORDER BY count3 " + DataBase5 + ";";
            SCL_state_ranking_pro = getTable1(query3, "Ranking");
            SCL_state_name_pro = getTable2(query3, "Name");
            SCL_state_abbr_pro = getTable2(query3, "state_abbr");
            SCL_state_count_pro = getTable1(query3, "count3");
            SCL_state_count_pro = removeNull(SCL_state_count_pro);
            html = html + CreateTableStatePropotion(SCL_state_ranking_pro, SCL_state_name_pro, SCL_state_abbr_pro, SCL_state_count_pro);
        }

        if (DataBase7 == null) {
            html = html + "";
        } else if (DataBase2.equals("lga") && DataBase3.equals("Raw")) {
            html += "<h3>Database for LGA Raw data " + "For " + getIndigStatus(DataBase4) + " where Non School Education is " + DataBase7 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> NonSCL_lga_ranking = new ArrayList<Integer>();
            ArrayList<String> NonSCL_lga_name = new ArrayList<String>();
            ArrayList<Integer> NonSCL_lga_code = new ArrayList<Integer>();
            ArrayList<Integer> NonSCL_lga_count = new ArrayList<Integer>();
            String query = "SELECT ROW_NUMBER() OVER (ORDER BY nsc.count " + DataBase5 + ") AS Ranking, l.name AS name, nsc.lga_code, nsc.count FROM Non_School_ED nsc JOIN LGA l on nsc.lga_code = l.code WHERE nsc.lga_year = 2021 " + " AND nsc.indigenous_status = " + "'" + DataBase4 + "'" + " AND nsc.Education = " + "'" + DataBase7 + "'" + " GROUP BY nsc.lga_code ORDER BY nsc.count " + DataBase5 + ";";
            NonSCL_lga_ranking = getTable1(query, "Ranking");
            NonSCL_lga_name = getTable2(query, "name");
            NonSCL_lga_code = getTable1(query, "lga_code");
            NonSCL_lga_count = getTable1(query, "count");
            html = html + CreateTableLGA(NonSCL_lga_ranking, NonSCL_lga_name, NonSCL_lga_code, NonSCL_lga_count);
        } else if (DataBase2.equals("state") && DataBase3.equals("Raw")){
            html += "<h3>Database for State Raw data " + "For " + getIndigStatus(DataBase4) + " where Non School Education is is " + DataBase7 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> NonSCL_State_ranking = new ArrayList<Integer>();
            ArrayList<String> NonSCL_State_name = new ArrayList<String>();
            ArrayList<String> NonSCL_State_abbr = new ArrayList<String>();
            ArrayList<Integer> NonSCL_State_count = new ArrayList<Integer>();
            String query2 = "SELECT ROW_NUMBER() OVER (ORDER BY sum(nsc.count) " + DataBase5 + ") AS Ranking, s.state_name, l.state_abbr, sum(nsc.count) AS count FROM LGA l JOIN Non_School_ED nsc ON l.code = nsc.lga_code JOIN STATE s ON l.state_abbr = s.state_abbr WHERE nsc.indigenous_status = " + "'" + DataBase4 + "'" + " AND nsc.Education = " + "'" + DataBase7 + "'" + " GROUP BY l.state_abbr ORDER BY sum(nsc.count) " + DataBase5 + ";";
            NonSCL_State_ranking = getTable1(query2, "Ranking");
            NonSCL_State_name = getTable2(query2, "state_name");
            NonSCL_State_abbr = getTable2(query2, "state_abbr");
            NonSCL_State_count = getTable1(query2, "count");
            html = html + CreateTableState(NonSCL_State_ranking, NonSCL_State_name, NonSCL_State_abbr, NonSCL_State_count);
        }  else if (DataBase2.equals("lga") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for LGA Proportional data " + "For " + getIndigStatus(DataBase4) + " where Non School Education is " + DataBase7 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> NonSCL_lga_ranking_pro = new ArrayList<Integer>();
            ArrayList<String> NonSCL_lga_name_pro = new ArrayList<String>();
            ArrayList<Integer> NonSCL_lga_code_pro = new ArrayList<Integer>();
            ArrayList<Integer> NonSCL_lga_count_pro = new ArrayList<Integer>();
            String query3 = "WITH population1 AS "
                    + "(SELECT l.name AS name, l.code AS code, sum(nsc.count) AS count FROM LGA l JOIN Non_School_ED nsc "
                    + "ON l.code = nsc.lga_code "
                    + "WHERE nsc.indigenous_status = " + "'" + DataBase4 + "'" + " AND nsc.lga_year = 2021 "
                    + "GROUP BY nsc.lga_code), "
                    + "population2 AS "
                    + "(SELECT l.name AS name2, l.code AS code2, sum(nsc.count) AS count2 FROM lga l JOIN Non_School_ED nsc "
                    + "ON l.code = nsc.lga_code "
                    + "WHERE nsc.indigenous_status = " + "'" + DataBase4 + "'" + " AND nsc.Education = " + "'" + DataBase7 + "'" + " AND year = 2021 "
                    + "GROUP BY nsc.lga_code) "
                    + "SELECT ROW_NUMBER() OVER (ORDER BY ( (population2.count2 * 100) / (population1.count) ) " + DataBase5 + ") AS Ranking, population1.name AS name, population1.code AS lga_code, ( (population2.count2 * 100) / (population1.count) ) AS count3 FROM population1 JOIN population2 ON population1.code = population2.code2 ORDER BY count3 " + DataBase5 + ";";
            NonSCL_lga_ranking_pro = getTable1(query3, "Ranking");
            NonSCL_lga_name_pro = getTable2(query3, "name");
            NonSCL_lga_code_pro = getTable1(query3, "lga_code");
            NonSCL_lga_count_pro = getTable1(query3, "count3");
            NonSCL_lga_count_pro = removeNull(NonSCL_lga_count_pro);
            html = html + CreateTableLGAPropotion(NonSCL_lga_ranking_pro, NonSCL_lga_name_pro, NonSCL_lga_code_pro, NonSCL_lga_count_pro);
        }  else if (DataBase2.equals("state") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for State Proportional data " + "For " + getIndigStatus(DataBase4) + " where Non School Education is " + DataBase7 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<Integer> NonSCL_state_ranking_pro = new ArrayList<Integer>();
            ArrayList<String> NonSCL_state_name_pro = new ArrayList<String>();
            ArrayList<String> NonSCL_state_abbr_pro = new ArrayList<String>();
            ArrayList<Integer> NonSCL_state_count_pro = new ArrayList<Integer>();
            String query4 = "WITH population1 AS "
                    + "(SELECT s.state_name AS state_name, l.state_abbr AS state_abbr, sum(nsc.count) AS count FROM LGA l JOIN Non_School_ED nsc "
                    + "ON l.code = nsc.lga_code JOIN STATE s ON l.state_abbr = s.state_abbr "
                    + "WHERE nsc.indigenous_status = " + "'" + DataBase4 + "'" + " AND l.year = 2021 "
                    + "GROUP BY l.state_abbr), "
                    + "population2 AS "
                    + "(SELECT s.state_name AS state_name2, l.state_abbr AS state_abbr2, sum(nsc.count) AS count2 FROM LGA l JOIN Non_School_ED nsc "
                    + "ON l.code = nsc.lga_code JOIN STATE s ON l.state_abbr = s.state_abbr "
                    + "WHERE nsc.indigenous_status = " + "'" + DataBase4 + "'" + " AND nsc.Education = " + "'" + DataBase7 + "'" + " AND l.year = 2021 "
                    + "GROUP BY l.state_abbr) "
                    + "SELECT ROW_NUMBER() OVER (ORDER BY ( (population2.count2 * 100) / (population1.count) ) " + DataBase5 + ") AS Ranking, population1.state_name AS Name,\n" + //
                            "       population1.state_abbr,\n" + //
                            "       ( (population2.count2 * 100) / (population1.count) ) AS count3\n" + //
                            "  FROM population1\n" + //
                            "       JOIN\n" + //
                            "       population2 ON population1.state_abbr = population2.state_abbr2\n" + //
                            " ORDER BY count3 " + DataBase5 + ";";
            NonSCL_state_ranking_pro = getTable1(query4, "Ranking");
            NonSCL_state_name_pro = getTable2(query4, "Name");
            NonSCL_state_abbr_pro = getTable2(query4, "state_abbr");
            NonSCL_state_count_pro = getTable1(query4, "count3");
            NonSCL_state_count_pro = removeNull(NonSCL_state_count_pro);
            html = html + CreateTableStatePropotion(NonSCL_state_ranking_pro, NonSCL_state_name_pro, NonSCL_state_abbr_pro, NonSCL_state_count_pro);
        }




        // Footer
        html = html + """
            <footer>
            <div class="footer-container">
            <div class="footer footer-left">
            <a href="/"><img src="/logo.png" alt="logo"></a>
        </div>
    
                <div class="footer footer-left-02">
                    <ul>
                        <li><a href="/">Home Page</a></li>
                        <li><a href="mission.html">Our Mission</a></li>
                        <li><a href="page2A.html">Age & Health</a></li>
                        <li><a href="page2B.html">Education</a></li>
                        <li><a href="page3A.html">The Gap</a></li>
                        <li><a href="page3B.html">LGA Comparison</a></li>
                    </ul>
                </div>
    
                <div class="footer footer-center">
                    <div>
                        <i class="fa fa-map-marker"></i>
                        <p>Melbourne, Victoria</p>
                    </div>
    
                    <div>
                        <i class="fa fa-phone"></i>
                        <p>+61449990065</p>
                    </div>
                    <div>
                        <i class="fa fa-envelope"></i>
                        <p><a href="mailto:mail@gmail.com">mail@gmail.com</a></p>
                    </div>
                </div>
    
                <div class="footer footer-right">
                    <h4>
                        About the Website
                    </h4>
                    <p>
                        <strong>Voice To Parliament Data Hub</strong> Our website simply give you an idea of the cencus data in 2016 & 2021 related to voice to parliament referendum in a unbiased way. 
                    </p>
                    <div class="footer-icons">
                        <a href="https://www.facebook.com" target="_blank"><img src="facebook.svg"></a>
                        <a href="https://www.instagram.com" target="_blank"><img src="instagram.svg"></a>
                        <a href="https://www.linkedin.com" target="_blank"><img src="linkedin-in.svg"></a>
                        <a href="https://www.twitter.com" target="_blank"><img src="twitter.svg"></a>
                        <a href="https://www.youtube.com" target="_blank"><img src="youtube.svg"></a>
                    </div>
                </div>
            </div>
    
            <p class="footer-copyright">
                Copyright © 2023 | All Rights Reserved <strong> | RMIT</strong> 
            </p>
    
        </footer>
        <script src="script.js"></script>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public ArrayList<String> getState(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> state = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct state_name FROM STATE WHERE year= " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String states  = results.getString("state_name");

                // Add the lga object to the array
                state.add(states);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return state;
    }

    public ArrayList<String> getSCL(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> SCL = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct School_Year FROM SchoolYear WHERE lga_year = " + year + " and school_year = 'did_not_go_to_school' or school_year = 'y8_below' or school_year = 'y9_equivalent' or school_year = 'y10_equivalent' or school_year = 'y11_equivalent' or school_year = 'y12_equivalent' order by School_Year;";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String scl_year  = results.getString("School_Year");

                // Add the lga object to the array
                SCL.add(scl_year);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return SCL;
    }
    public ArrayList<String> getNonSCL(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> NonSCL = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct Education FROM Non_School_ED WHERE lga_year = " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String non_scl_education  = results.getString("Education");
                NonSCL.add(non_scl_education);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return NonSCL;
    }

    public ArrayList<String> NonSclConvert(ArrayList<String> nscYear1){
        ArrayList<String> nscYear2 = new ArrayList<String>();
        String word;
        for (int i = 0; i < nscYear1.size(); i++) {
            if (nscYear1.get(i).equals("bd")) {
                word = "Bachelor Degree Level";
            } else if (nscYear1.get(i).equals("pd_gd_gc")) {
                word = "Postgraduate Degree Level, Graduate Diploma, Graduate Certificate Level";
            } else if (nscYear1.get(i).equals("adip")) {
                word = "Advanced Diploma & Diploma Level";
            } else if (nscYear1.get(i).equals("ct_iii_iv")) {
                word = "Certificate III & IV Level";
            } else {
                word = "Certificate I & II Level";
            }
            nscYear2.add(word);
        }
        return nscYear2;
    }

    public String outputDataBase1(String type) {
        String html = "";
        // Select School Year
         html = html + """
            <form action="/page2B.html" method="post">
                <label for="lga_state">Choose LGA or STATE:</label>
                <select id="drop_down" name="LGA_STATE">
                    <option value="lga">LGA</option>
                    <option value="state">State</option>
                </select>

                <label for="Data_Type">Choose Raw or Proportional Data:</label>
                <select id="drop_down" name="dataType">
                    <option value="Raw">Raw total values</option>
                    <option value="Proportional">Proportional values</option>
                </select>
            
                <label for="indige1">Indigenous Status:</label>
                <select id="drop_down" name="indige1">
                    <option value="indig">Indigenous</option>
                    <option value="non_indig">Non Indigenous</option>
                    <option value="indig_ns">Indigenous Not Stated</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>
                <label for="SCL">Choose a School Year:</label>
            <select id="drop_down" name="SCL">
            """;

        ArrayList<String> SCL = new ArrayList<String>();
        SCL = getSCL(2021);
        String SclYear;
        
        for (int i = 0; i < SCL.size(); i ++) {
            SclYear = SCL.get(i);
            html = html + "<option value= " + SclYear + " > " + getSchoolYear(SclYear) + "</option>";
        }
        html = html + """
            </select>
            <input type="submit">
            </form>
            """;
        return html;
        
    }

    public String outputDataBase2(String type) {
        String html = "";
        // Select Non School
        html = html + """
            <form action="/page2B.html" method="post">
                <label for="lga_state">Choose LGA or STATE:</label>
                <select id="drop_down" name="LGA_STATE">
                    <option value="lga">LGA</option>
                    <option value="state">State</option>
                </select>

                <label for="Data_Type">Choose Raw or Proportional Data:</label>
                <select id="drop_down" name="dataType">
                    <option value="Raw">Raw total values</option>
                    <option value="Proportional">Proportional values</option>
                </select>
            
                <label for="indige1">Indigenous Status:</label>
                <select id="drop_down" name="indige1">
                    <option value="indig">Indigenous</option>
                    <option value="non_indig">Non Indigenous</option>
                    <option value="indig_ns">Indigenous Not Stated</option>
                </select>

                <label for="sort">Sorted by:</label>
                <select id="drop_down" name="sort">
                    <option value="DESC">Descending</option>
                    <option value="ASC">Ascending</option>
                </select>
            <label for="NonSCL">Choose a Non School Education:</label>
            <select id="drop_down" name="NonSCL">
            """;
        ArrayList<String> NonSCL = new ArrayList<String>();
        NonSCL = getNonSCL(2021);
        ArrayList<String> NonSclConvert1 = new ArrayList<String>();
        NonSclConvert1 = NonSclConvert(NonSCL);
        String name1;
        
        for (int i = 0; i < NonSCL.size(); i ++) {
            name1 =  NonSclConvert1.get(i);
            html = html + "<option value= " + NonSCL.get(i) + " > " + name1 + "</option>";
        }
                
        html = html + """
            </select>
            <input type="submit">
            </form>
            """;
        return html;
        
    }

    public String CreateTableLGA(ArrayList<Integer> SCLR, ArrayList<String> SCL1, ArrayList<Integer> SCL2, ArrayList<Integer> SCL3) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>Count</th>
            </tr>
            """;
        for (int i = 0; i < SCL1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td>" + SCLR.get(i) + "</td>";
            html = html + "<td> " + SCL1.get(i) + "</td>";
            html = html + "<td>" + SCL2.get(i) + "</td> ";
            html = html + "<td> " + SCL3.get(i) + "</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }

    public String CreateTableState(ArrayList<Integer> SCLR, ArrayList<String> SCL1, ArrayList<String> SCL2,  ArrayList<Integer> SCL3) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>State Name</th>
              <th>State Abbrevation</th>
              <th>Count</th>
            </tr>
            """;
        for (int i = 0; i < SCL1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td>" + SCLR.get(i) + "</td>";
            html = html + "<td> " + SCL1.get(i) + "</td>";
            html = html + "<td> " + SCL2.get(i) + "</td>";
            html = html + "<td>" + SCL3.get(i) + "</td> ";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }

    public String CreateTableLGAPropotion(ArrayList<Integer> SCLR, ArrayList<String> SCL1, ArrayList<Integer> SCL2, ArrayList<Integer> SCL3) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>LGA Name</th>
              <th>LGA Code</th>
              <th>Percentage</th>
            </tr>
            """;
        for (int i = 0; i < SCL1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + SCLR.get(i) + "</td>";
            html = html + "<td> " + SCL1.get(i) + "</td>";
            html = html + "<td>" + SCL2.get(i) + "</td> ";
            html = html + "<td> " + SCL3.get(i) + "%</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }

    public String CreateTableStatePropotion(ArrayList<Integer> SCLR, ArrayList<String> SCL1, ArrayList<String> SCL2, ArrayList<Integer> SCL3) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>State Name</th>
              <th>State Abbrevation</th>
              <th>Percentage</th>
            </tr>
            """;
        for (int i = 0; i < SCL1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + SCLR.get(i) + "</td>";
            html = html + "<td> " + SCL1.get(i) + "</td>";
            html = html + "<td>" + SCL2.get(i) + "</td> ";
            html = html + "<td> " + SCL3.get(i) + "%</td>";
            html = html + "</tr>";
        }  
        html = html + """
          </table>
                """;
        return html;
    }

    public ArrayList<Integer> getTable1(String query1, String column) {
        // Create the ArrayList of LGA objects to return
        ArrayList<Integer> lga_code = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = query1;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                int count  = results.getInt(column);

                // Add the lga object to the array
                lga_code.add(count);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lga_code;
    }

    public ArrayList<String> getTable2(String query1, String column) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> lga_code = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = query1;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String count  = results.getString(column);

                // Add the lga object to the array
                lga_code.add(count);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lga_code;
    }

    public String getIndigStatus(String indig){
        if (indig.equals("indig")){
            indig = "Indigenous";
        } else if (indig.equals("non_indig")){
            indig = "Non Indigenous";
        } else if (indig.equals("indig_ns")){
            indig = "Indigenous not Stated";
        } else {
            return indig;
        }
        return indig;
    }

    public String getSort(String sort){
        String lol;
        if (sort.equals("ASC")) {
            lol = "Ascending";
        } else {
            lol = "Decending";
        }
        return lol;
    }

    public String getPercentage(int divide1, int divide2){
        return String.format("%.2f",divide1/divide2);
    }

    public ArrayList<Integer> removeNull(ArrayList<Integer> lol) {
        for (int i = 0; i < lol.size(); i++){
            if (lol.get(i) == null){
                lol.set(i, 0);
            }
        }
        return lol;
    }
    public String getSchoolYear(String school) {
        String word = "";
            if (school.equals("did_not_go_to_school")){
                word = "Did not go to school";
            } else if (school.equals("y8_below")){
                word = "Year 8 and below";
            } else if (school.equals("y9_equivalent")){
                word = "Year 9 or equivalent";
            } else if (school.equals("y10_equivalent")){
                word = "Year 10 or equivalent";
            } else if (school.equals("y11_equivalent")){
                word = "Year 11 or equivalent";
            } else if (school.equals("y12_equivalent")){
                word = "Year 12 or equivalent";
            }
        return word;
    }
}
