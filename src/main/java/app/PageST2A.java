package app;

import java.util.ArrayList;

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
public class PageST2A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2A.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html += """
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Subtask 2.1</title>
                """;

        // Add some CSS (external file)
        html += """
            <link rel='stylesheet' type='text/css' href='common.css' />
            <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css">
            <link rel="shortcut icon" href="/draft.ico" type="image/x-icon">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" />
            <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
                """;
        html = html + "</head>";

        // Add the body
        html = html + "<body class='task2a-body'>";

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
                <li><a href="page2A.html" class="active">Age & Health</a></li>
                <li><a href="page2B.html">Education</a></li>
                <li><a href="page3A.html">The Gap</a></li>
                <li><a href="page3B.html">LGA Comparison</a></li>
            </ul>
            </header>
                """;

        html += """
            <section id="scl-nonscl-header1">
                        <div class="overlay"></div>
                        <div class="overlay-text">
                            <h2>Age Demographics & Health Conditions</h2>
                            <p>Find unbiased information about age demographics and health conditions</p>
                        </div>
                    </section>
                        """;
        // Form

        html = html + """
        <form action="/page2A.html" method="post">
            <h2>Select either Long Term Health or Age Category to view</h2>
            <div>
            <input type="radio" id="rad" name="DataBase" value="LTHC">
            <label for="DataBase">Long Term Health</label>
            </div>
            <div>
            <input type="radio" id="rad" name="DataBase" value="Age_Cat">
            <label for="Database">Age Category</label>
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
        } else if (DataBase1.equals("LTHC")) {
            html = html + outputDataBase1(DataBase1);
        } else if (DataBase1.equals("Age_Cat")) {
            html = html + outputDataBase2(DataBase1);
        } else {
            html = html + "";
        }
        String DataBase2 = context.formParam("LGA_STATE");
        String DataBase3 = context.formParam("dataType");
        String DataBase4 = context.formParam("indige1");
        String DataBase5 = context.formParam("sort");
        String DataBase6 = context.formParam("LTHC");
        String DataBase7 = context.formParam("AgeCat");
       
        if (DataBase6 == null) {
            html = html + "";
        } else if (DataBase2.equals("lga") && DataBase3.equals("Raw")){
            html += "<h3>Database for LGA Raw data " + "For " + getIndig(DataBase4) + " where Long Term Health condition is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> LTHC_lga_code1 = new ArrayList<String>();
            ArrayList<Integer> LTHC_lga_code2 = new ArrayList<Integer>();
            ArrayList<Integer> LTHC_lga_value = new ArrayList<Integer>();
            String query = "Select lga.name as name, lga_code, count from LTHC join LGA on LTHC.lga_code = LGA.code where lga_year = 2021 " + " and indigenous_status = " + "'" + DataBase4 + "'" + " and Long_Term = " + "'" + DataBase6 + "'" + " Group by lga_code Order by count " + DataBase5 + ";";
            LTHC_lga_code1 = getTableS(query, "name");
            LTHC_lga_code2 = getTable(query, "lga_code");
            LTHC_lga_value = getTable(query, "count");
            html = html + CreateTableState(LTHC_lga_code1, LTHC_lga_code2, LTHC_lga_value);
        } else if (DataBase2.equals("state") && DataBase3.equals("Raw")){
            html += "<h3>Database for State Raw data " + "For " + getIndig(DataBase4) + " where Long Term Health condition is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> LTHC_State_code = new ArrayList<String>();
            ArrayList<Integer> LTHC_State_value = new ArrayList<Integer>();
            String query1 = "select state_abbr, sum(count) as count from lga join LTHC on lga.code = LTHC.lga_code where LTHC.indigenous_status = " + "'" + DataBase4 + "'" + " and Long_Term = " + "'" + DataBase6 + "'" + " Group by state_abbr Order by count " + DataBase5 + ";";
            LTHC_State_code = getTableS(query1, "state_abbr");
            LTHC_State_value = getTable(query1, "count");
            html = html + CreateTableState1(LTHC_State_code, LTHC_State_value);
        } else if (DataBase2.equals("lga") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for LGA Proportional data " + "For " + getIndig(DataBase4) + " where Long Term Health condition is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> LTHC_lga_code_pro1 = new ArrayList<String>();
            ArrayList<Integer> LTHC_lga_code_pro2 = new ArrayList<Integer>();
            ArrayList<Integer> LTHC_lga_value_pro = new ArrayList<Integer>();
            String query3 = "With population1 as"
                    + "(Select lga.name as code, lga.code as code1, sum(count) as count1 from lga join LTHC "
                    + "on lga.code = LTHC.lga_code "
                    + "where LTHC.indigenous_status = " + "'" + DataBase4 + "'" + " and year = 2021 "
                    + "group by LTHC.lga_code), "
                    + "population2 as "
                    + "(select lga.code as code2, sum(count) as count2 from lga join LTHC "
                    + "on lga.code = LTHC.lga_code "
                    + "where LTHC.indigenous_status = " + "'" + DataBase4 + "'" + " and Long_Term = " + "'" + DataBase6 + "'" + " and year = 2021 "
                    + "group by LTHC.lga_code) "
                    + "Select population1.code as code_lga, population1.code1 as lga_code , ((population2.count2 * 100)/(population1.count1)) as count3 "
                    + "from population1 join population2 on population1.code1 = population2.code2 order by count3 " + DataBase5 + ";";
            LTHC_lga_code_pro1 = getTableS(query3, "code_lga");
            LTHC_lga_code_pro2 = getTable(query3, "lga_code");
            LTHC_lga_value_pro = getTable(query3, "count3");
            LTHC_lga_value_pro = removeNull(LTHC_lga_value_pro);
            html = html + CreateTableState2(LTHC_lga_code_pro1, LTHC_lga_code_pro2, LTHC_lga_value_pro);
        } else if (DataBase2.equals("state") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for LGA Raw data " + "For " + getIndig(DataBase4) + " where Long Term Health condition is " + DataBase6 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> LTHC_lga_code_pro3 = new ArrayList<String>();
            ArrayList<Integer> LTHC_lga_value_pro1 = new ArrayList<Integer>();
            String query3 = "With population1 as"
                    + "(Select lga.state_abbr as code1, sum(count) as count1 from lga join LTHC "
                    + "on lga.code = LTHC.lga_code "
                    + "where LTHC.indigenous_status = " + "'" + DataBase4 + "'" + " and year = 2021 "
                    + "group by lga.state_abbr), "
                    + "population2 as "
                    + "(select lga.state_abbr as code2, sum(count) as count2 from lga join LTHC "
                    + "on lga.code = LTHC.lga_code "
                    + "where LTHC.indigenous_status = " + "'" + DataBase4 + "'" + " and Long_Term = " + "'" + DataBase6 + "'" + " and year = 2021 "
                    + "group by lga.state_abbr) "
                    + "Select population1.code1 as code_lga, ((population2.count2 * 100)/(population1.count1)) as count3 "
                    + "from population1 join population2 on population1.code1 = population2.code2 order by count3 " + DataBase5 + ";";
            LTHC_lga_code_pro3 = getTableS(query3, "code_lga");
            LTHC_lga_value_pro1 = getTable(query3, "count3");
            html = html + CreateTableState3(LTHC_lga_code_pro3, LTHC_lga_value_pro1);
        }

        if (DataBase7 == null) {
            html = html + "";
        } else if (DataBase2.equals("lga") && DataBase3.equals("Raw")) {
            html += "<h3>Database for LGA Raw data " + "For " + getIndig(DataBase4) + " where Age range is " + AgeConvert1(DataBase7) + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> Age_lga_code1 = new ArrayList<String>();
            ArrayList<Integer> Age_lga_code2 = new ArrayList<Integer>();
            ArrayList<Integer> Age_lga_value = new ArrayList<Integer>();
            String query3 = "Select LGA.name as code, lga_code, count from population join LGA on population.lga_code = LGA.code where lga_year = 2021 " + " and indigenous_status = " + "'" + DataBase4 + "'" + " and age_category = " + "'" + DataBase7 + "'" + " Group by lga_code Order by count " + DataBase5 + ";";
            Age_lga_code1 = getTableS(query3, "code");
            Age_lga_code2 = getTable(query3, "lga_code");
            Age_lga_value = getTable(query3, "count");
            html = html + CreateTableState(Age_lga_code1, Age_lga_code2, Age_lga_value);
        } else if (DataBase2.equals("state") && DataBase3.equals("Raw")){
            html += "<h3>Database for State Raw data " + "For " + getIndig(DataBase4) + " where Age range is " + AgeConvert1(DataBase7) + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> Age_State_code = new ArrayList<String>();
            ArrayList<Integer> Age_State_value = new ArrayList<Integer>();
            String query4 = "select state_abbr, sum(count) as count from lga join population on lga.code = population.lga_code where population.indigenous_status = " + "'" + DataBase4 + "'" + " and age_category = " + "'" + DataBase7 + "'" + " Group by state_abbr Order by count " + DataBase5 + ";";
            Age_State_code = getTableS(query4, "state_abbr");
            Age_State_value = getTable(query4, "count");
            Age_State_value = removeNull(Age_State_value);
            html = html + CreateTableState1(Age_State_code, Age_State_value);
        } else if (DataBase2.equals("lga") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for LGA Proportional data " + "For " + getIndig(DataBase4) + " where Age range is " + AgeConvert1(DataBase7) + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> Population_lga_code_pro1 = new ArrayList<String>();
            ArrayList<Integer> Population_lga_code_pro2 = new ArrayList<Integer>();
            ArrayList<Integer> Population_lga_value_pro = new ArrayList<Integer>();
            String query5 = "With population1 as"
                    + "(Select lga.name as code, lga.code as code1, sum(count) as count1 from lga join population "
                    + "on lga.code = population.lga_code "
                    + "where population.indigenous_status = " + "'" + DataBase4 + "'" + " and year = 2021 "
                    + "group by population.lga_code), "
                    + "population2 as "
                    + "(select lga.code as code2, sum(count) as count2 from lga join population "
                    + "on lga.code = population.lga_code "
                    + "where population.indigenous_status = " + "'" + DataBase4 + "'" + " and age_category = " + "'" + DataBase7 + "'" + " and year = 2021 "
                    + "group by population.lga_code) "
                    + "Select population1.code as code_lga, population1.code1 as lga_code, ((population2.count2 * 100)/(population1.count1)) as count3 "
                    + "from population1 join population2 on population1.code1 = population2.code2 order by count3 " + DataBase5 + ";";
            Population_lga_code_pro1 = getTableS(query5, "code_lga");
            Population_lga_code_pro2 = getTable(query5, "lga_code");
            Population_lga_value_pro = getTable(query5, "count3");
            Population_lga_value_pro = removeNull(Population_lga_value_pro);
            html = html + CreateTableState2(Population_lga_code_pro1, Population_lga_code_pro2, Population_lga_value_pro);
        }  else if (DataBase2.equals("state") && DataBase3.equals("Proportional")) {
            html += "<h3>Database for State Proportional data " + "For " + getIndig(DataBase4) + " where Age range is " + DataBase7 + " Sorted by " + getSort(DataBase5) + "</h3>";
            ArrayList<String> Population_lga_code_pro3 = new ArrayList<String>();
            ArrayList<Integer> Population_lga_value_pro1 = new ArrayList<Integer>();
            String query5 = "With population1 as"
                    + "(Select lga.state_abbr as code1, sum(count) as count1 from lga join population "
                    + "on lga.code = population.lga_code "
                    + "where population.indigenous_status = " + "'" + DataBase4 + "'" + " and year = 2021 "
                    + "group by lga.state_abbr), "
                    + "population2 as "
                    + "(select lga.state_abbr as code2, sum(count) as count2 from lga join population "
                    + "on lga.code = population.lga_code "
                    + "where population.indigenous_status = " + "'" + DataBase4 + "'" + " and age_category = " + "'" + DataBase7 + "'" + " and year = 2021 "
                    + "group by lga.state_abbr) "
                    + "Select population1.code1 as code_lga, ((population2.count2 * 100)/(population1.count1)) as count3 "
                    + "from population1 join population2 on population1.code1 = population2.code2 order by count3 " + DataBase5 + ";";
            Population_lga_code_pro3 = getTableS(query5, "code_lga");
            Population_lga_value_pro1 = getTable(query5, "count3");
            html = html + CreateTableState3(Population_lga_code_pro3, Population_lga_value_pro1);
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
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public ArrayList<String> getState(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> lgas = new ArrayList<String>();

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
                String name16  = results.getString("state_name");

                // Add the lga object to the array
                lgas.add(name16);
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
        return lgas;
    }
    public ArrayList<String> getLTHC(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> LTHC = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct Long_Term FROM LTHC WHERE lga_year = " + year + " order by Long_Term;";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String name16  = results.getString("Long_Term");

                // Add the lga object to the array
                LTHC.add(name16);
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
        return LTHC;
    }
    public ArrayList<String> getAgeCat(int year) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> lgas = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Distinct age_category FROM population WHERE lga_year = " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String name16  = results.getString("age_category");

                // Add the lga object to the array
                lgas.add(name16);
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
        return lgas;
    }
    public ArrayList<String> AgeConvert(ArrayList<String> age1){
        ArrayList<String> age2 = new ArrayList<String>();
        String word;
        for (int i = 0; i < age1.size(); i++) {
            if (age1.get(i).equals("_0_4")) {
                word = "0 to 4 Years old";
            } else if (age1.get(i).equals("_5_9")) {
                word = "5 to 9 Years old";
            } else if (age1.get(i).equals("_10_14")) {
                word = "10 to 14 Years old";
            } else if (age1.get(i).equals("_15_19")) {
                word = "15 to 19 Years old";
            } else if (age1.get(i).equals("_20_24")) {
                word = "20 to 24 Years old";
            } else if (age1.get(i).equals("_25_29")) {
                word = "25 to 29 Years old";
            } else if (age1.get(i).equals("_30_34")) {
                word = "30 to 34 Years old";
            } else if (age1.get(i).equals("_35_39")) {
                word = "35 to 39 Years old";
            } else if (age1.get(i).equals("_40_44")) {
                word = "40 to 44 Years old";
            } else if (age1.get(i).equals("_45_49")) {
                word = "45 to 49 Years old";
            } else if (age1.get(i).equals("_50_54")) {
                word = "50 to 54 Years old";
            } else if (age1.get(i).equals("_54_59")) {
                word = "55 to 59 Years old";
            } else if (age1.get(i).equals("_60_64")) {
                word = "60 to 64 Years old";
            } else {
                word = "65 Years old and above";
            }
            age2.add(word);
        }
        return age2;
    }
    public String outputDataBase1(String type) {
        String html = "";
        // Select Disease
         html = html + """
            <form action="/page2A.html" method="post">
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
                <label for="LTHC">Choose a Long Term Health Condition:</label>
            <select id="drop_down" name="LTHC">
            """;
        ArrayList<String> LTHC = new ArrayList<String>();
        LTHC = getLTHC(2021);
        String LTHCname;
        
        for (int i = 0; i < LTHC.size(); i ++) {
            LTHCname = LTHC.get(i);
            html = html + "<option value= " + LTHCname + " > " + LTHCname + "</option>";
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
        // Select Age Category
        html = html + """
            <form action="/page2A.html" method="post">
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
            <label for="AgeCat">Choose a Age Category:</label>
            <select id="drop_down" name="AgeCat">
            """;
        ArrayList<String> AgeCat = new ArrayList<String>();
        AgeCat = getAgeCat(2021);
        ArrayList<String> AgeCat1 = new ArrayList<String>();
        AgeCat1 = AgeConvert(AgeCat);
        String name1;
        
        for (int i = 0; i < AgeCat.size(); i ++) {
            name1 =  AgeCat1.get(i);
            html = html + "<option value= " + AgeCat.get(i) + " > " + name1 + "</option>";
        }
                
        html = html + """
            </select>
            <input type="submit">
            </form>
            """;
        return html;
        
    }
    
    public String CreateTableState(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<Integer> LTHC3) {
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
        int count = 1;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + count + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td> " + LTHC3.get(i) + "</td>";
            html = html + "</tr>";
            count++;
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableState1(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2) {
        String html = "";
        html = html + """
            <table>
            <tr>
              <th>Ranking</th>
              <th>State name</th>
              <th>Count</th>
            </tr>
            """;
        int count = 1;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + count + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "</tr>";
            count++;
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableState2(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2, ArrayList<Integer> LTHC3) {
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
            int count = 1;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + count + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "<td> " + LTHC3.get(i) + "%</td>";
            html = html + "</tr>";
            count++;
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public String CreateTableState3(ArrayList<String> LTHC1, ArrayList<Integer> LTHC2) {
        String html = "";
        html = html + """
            <table>
            <tr>
            <th>Ranking</th>
              <th>State name</th>
              <th>Percentage</th>
            </tr>
            """;
            int count = 1;
        for (int i = 0; i < LTHC1.size(); i++) {
            html = html + "<tr>";
            html = html + "<td> " + count + "</td>";
            html = html + "<td> " + LTHC1.get(i) + "</td>";
            html = html + "<td>" + LTHC2.get(i) + "</td> ";
            html = html + "</tr>";
            count++;
        }  
        html = html + """
          </table>
                """;
        return html;
    }
    public ArrayList<Integer> getTable(String query1, String column) {
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
    public ArrayList<String> getTableS(String query1, String column) {
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
    public String getIndig(String indig){
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
    public String AgeConvert1(String age1){
        String word;
            if (age1.equals("_0_4")) {
                word = "0 to 4 Years old";
            } else if (age1.equals("_5_9")) {
                word = "5 to 9 Years old";
            } else if (age1.equals("_10_14")) {
                word = "10 to 14 Years old";
            } else if (age1.equals("_15_19")) {
                word = "15 to 19 Years old";
            } else if (age1.equals("_20_24")) {
                word = "20 to 24 Years old";
            } else if (age1.equals("_25_29")) {
                word = "25 to 29 Years old";
            } else if (age1.equals("_30_34")) {
                word = "30 to 34 Years old";
            } else if (age1.equals("_35_39")) {
                word = "35 to 39 Years old";
            } else if (age1.equals("_40_44")) {
                word = "40 to 44 Years old";
            } else if (age1.equals("_45_49")) {
                word = "45 to 49 Years old";
            } else if (age1.equals("_50_54")) {
                word = "50 to 54 Years old";
            } else if (age1.equals("_54_59")) {
                word = "55 to 59 Years old";
            } else if (age1.equals("_60_64")) {
                word = "60 to 64 Years old";
            } else {
                word = "65 Years old and above";
            }
        return word;
    }
}