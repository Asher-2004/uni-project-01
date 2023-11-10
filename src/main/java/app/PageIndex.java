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
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        // html = html + "<head>" + 
        //        "<title>Homepage</title>";

        html += """
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Home Page</title>
                """;

        // Add some CSS (external file)
        // html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        // html = html + "</head>";
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
        html = html + "<body onload='showImage('New South Wales')'>";

        html = html + """
        <script>
            function showImage(state) {
                const imageMap = {
                  'New South Wales': 'states16/New South Wales.png',
                  'Queensland': 'states16/Queensland.png',
                  'South Australia': 'states16/South Australia.png',
                  'Tasmania': 'states16/Tasmania.png',
                  'Victoria': 'states16/Victoria.png',
                  'Western Australia': 'states16/Western Australia.png',
                  'Northern Territory': 'states16/Northern Territory.png',
                  'Australian Capital Territory': 'states16/Australian Capital Territory.png'
                };
                document.getElementById('stateImage').src = imageMap[state];
                document.getElementById('stateImage').alt = state;
              }
        </script>
        <script type="text/javascript">
      google.charts.load('current', {'packages':['bar']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['State', 'Population-2016', 'Population-2021'],
          ['New South Wales', 6326222, 6350677],
          ['Victoria', 5250090, null],
          ['Queensland', 3926174, 3926174],
          ['South Australia', 1362213, 1362953],
          ['Western Australia', 2086514, 2087137],
          ['Tasmania', 412477, 412477],
          ['Northern Territory', 161536, 161730],
          ['Australian Capital Territory', 425208, 425208],
          ['Other', 2332, 2332]
        ]);

        var options = {
            backgroundColor: 'transparent',
            hAxis: {
              textStyle: {
                color: '#000'
              },
              titleTextStyle: {
                color: '#000'
              }
            },
            vAxis: {
              textStyle: {
                color: '#000'              
            },
              titleTextStyle: {
                color: '#000'             
            }
            }
        };

        var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

        chart.draw(data, google.charts.Bar.convertOptions(options));
      }
    </script>
              
                """;

        html += """
            <header>
            <div class="logo">
                <a href="/"><img src="logo.png" alt=""></a>
            </div>
            <ul id="navbar">
                <li><a href="/"  class="active">Home Page</a></li>
                <li><a href="mission.html">Our Mission</a></li>
                <li><a href="page2A.html">Age & Health</a></li>
                <li><a href="page2B.html">Education</a></li>
                <li><a href="page3A.html">The Gap</a></li>
                <li><a href="page3B.html">LGA Comparison</a></li>
            </ul>
            </header>
                """;

        // Body section
        
        html = html + """
            <main>
            <section id="hero">
                <div class="info-container">
                    <div class="info info1">
                        <h2>Total Number of LGAs</h2>
                        <h2>in 2016</h2>                                
                    """;

        html = html +  "<p>"+ getTotalLGA(2016)+"</p>";
                            
        html = html + """
                    </div>
                    <div class="info info2">
                        <h2>Total Number of LGAs</h2>
                        <h2>in 2021</h2> 
                        """;
        html = html +  "<p>"+ getTotalLGA(2021)+"</p>";
        html = html + """
                    </div>
                </div>
            </section>

            <p class="lol">The indigenous community of Australia have always been at a disadvantage in Australian society, both education and healthcare present a dramatic difference between the Indigenous and non-Indigenous communities. Due to these growing differences, there have been calls for a voice in parliament to speak out for the needs of our indigenous community in the recent 2023 referendum. This website shows the census for both 2016 and 2021 to help Australians make better decisions on the upcoming referendum.</p>
                    
                """;

        html = html + """
            <div class="main-info-container">
            <div class="info">
                <a href="page2A.html"><div class="info-img img1"></div></a>
                <div class="info-para">
                    <a href="page2A.html"><h3>Health & Age Demographic</h3></a>
                    <p>
                        Explore the 2021 census on both the health and age demographic data where you can explore our database to receive your desired dataset. Choose the health database to see how the long-term health condition affects both the indigenous and non-indigenous communities. Choose the Age demographic to see the demographics of both indigenous and non-indigenous communities. Both datasets can be grouped either at an LGA (Local government area) or State Level.                    
                    </p>
                </div>
            </div>
            <div class="info">
                <div class="info-para">
                    <a href="page2B.html"><h3>School & Non-School Education</h3></a>
                    <p>
                        Explore the 2021 census on the school and non-school education completed by both indigenous and non-indigenous communities. Choose the School education database to see the highest school year completion achieved by both indigenous and non-indigenous communities. Choose the Non-School database to see the tertiary education completed by both indigenous and non-indigenous communities. Both datasets can be grouped either at an LGA (Local government area) or State Level.                    
                    </p>
                </div>
                <a href="page2B.html"><div class="info-img img2"></div></a>
            </div>
            <div class="info">
                <a href="page3A.html"><div class="info-img img3"></div></a>
                <div class="info-para">
                    <a href="page3A.html"><h3>The Gap</h3></a>
                    <p>
                        Explore the Gap between the indigenous and non-indigenous communities regarding the differences in age demographic, school education, non-school education, and long-term health. The dataset will compare the 2021 census with the 2016 census, showing the difference in either Raw (numerical) or Proportional (percentage).                    
                    </p>
                </div>
            </div>
            <div class="info">
                <div class="info-para">
                    <a href="page3B.html"><h3>LGA Comparison</h3></a>
                    <p>
                        Explore LGAs (local government areas) that are similar to each other using our app, it enables you to choose an LGA (local government area), the database and its attributes. This allows you to explore LGAs with similar numbers of people and attributes.                    
                    </p>
                </div>
                <a href="page3B.html"><div class="info-img img4"></div></a>
            </div>
        </div>
                """;


        html = html + """
            <div class="surveyed-popu">
            <div class="popu-container">
                <h2>Total Population Surveyed In 2016</h2>        
                """;

        html = html +  "<p>"+ getTotalSurveyedPopulation(2016)+"</p>";
        html = html + """
            </div>
            <div class="line"></div>
            <div class="popu-container">
                <h2>Total Population Surveyed In 2021</h2>
                    """;
        html = html +  "<p>"+ getTotalSurveyedPopulation(2021)+"</p>";        
        html = html + """ 
            </div>
        </div>
                """;

        html = html + """
            <h1 class="img-show">Australian Territories</h1>
            <div class="container">
                <div class="states">
                    <button onclick="showImage('New South Wales')">New South Wales</button>
                    <button onclick="showImage('Queensland')">Queensland</button>
                    <button onclick="showImage('South Australia')">South Australia</button>
                    <button onclick="showImage('Tasmania')">Tasmania</button>
                    <button onclick="showImage('Victoria')">Victoria</button>
                    <button onclick="showImage('Western Australia')">Western Australia</button>
                    <button onclick="showImage('Northern Territory')">Northern Territory</button>
                    <button onclick="showImage('Australian Capital Territory')">Australian Capital Territory</button>
                </div>
                <div class="image-container">
                    <img id="stateImage" src="states16/New South Wales.png" alt="New South Wales">
                </div>
            </div>

            <h1 class="summary-tt">Population of each State in 2016</h1>
            <section id="summary-table">
                <table width="100%" class="table2">
                    <thead>
                        <tr>
                            <th>State Name</th>
                            <th>State Abbrevation</th>
                            <th>Total Population</th>
                        </tr>
                    </thead>
                    <tbody>                                
                    """;

        ArrayList<State> statesData2016 = getStates(2016);

        for (State state : statesData2016) {
            html += "<tr>";
            html += "<td>" + state.name + "</td>";
            html += "<td>" + state.abbreviation + "</td>";
            html += "<td>" + state.population + "</td>";
            html += "</tr>";
        }
                

        html = html + """
                    </tbody>
                </table>
            </section>
        <h1 class="summary-tt">Population of each State in 2021</h1>
            <section id="summary-table">
                <table width="100%" class="table2">
                    <thead>
                        <tr>
                            <th>State Name</th>
                            <th>State Abbrevation</th>
                            <th>Total Population</th>
                        </tr>
                    </thead>
                    <tbody>                                
                    """;

        ArrayList<State> statesData2021 = getStates(2021);

        for (State state : statesData2021) {
            html += "<tr>";
            html += "<td>" + state.name + "</td>";
            html += "<td>" + state.abbreviation + "</td>";
            html += "<td>" + state.population + "</td>";
            html += "</tr>";
        }
                

        html = html + """
                    </tbody>
                </table>
            </section>
            </div>
            <div>
                <h2 class="columnchart_heading">Total population comparission in Australian Stated in 2016 & 2021</h2>

            </div>
            <div id="columnchart_material"></div>
        </main>
                """;

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
                    <h3>
                        About the Website
                    </h3>
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

        // Linking the script file JS
        // html = html + """
        //     <script src="main.js"></script>
        //         """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }




    
    /**
     * Get the names of the LGAs in the database.
     */

    public int getTotalPopulation(int year) {

        Connection connection = null;
        int name = 0;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT sum(count) AS Total FROM Population Where lga_year = " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            name  = results.getInt("Total");

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
        return name;
    }

    public int getTotalLGA(int year) {
        int total = 0;

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "Select count(*) AS Total from LGA where year = " + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            total = results.getInt("Total");

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
        return total;
    }

    public static class State {
        public String name;
        public String abbreviation;
        public int population;
    
        public State(String name, String abbreviation, int population) {
            this.name = name;
            this.abbreviation = abbreviation;
            this.population = population;
        }
    }

    public ArrayList<State> getStates(int year) {
        ArrayList<State> states = new ArrayList<>();

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT state_name, state_abbr, state_pop FROM STATE WHERE year =" + year +";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String stateName = results.getString("state_name");
                String stateAbbr = results.getString("state_abbr");
                int statePop = results.getInt("state_pop");
                states.add(new State(stateName, stateAbbr, statePop));
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
        return states;
        
    }
        public int getTotalSurveyedPopulation(int year) {

        int total_surveyed = 0;

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT SUM(count) AS 'Total Population' FROM Population WHERE lga_year =" + year + ";";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            total_surveyed = results.getInt("Total Population");

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
        return total_surveyed;
    }
}

