package app;

import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import javax.print.attribute.standard.MediaSize.NA;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class PageMission implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        // html = html + "<head>" + 
        //        "<title>Our Mission</title>";
        html += """
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Our Mission</title>
                """;

        // Add some CSS (external file)
        // html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html += """
            <link rel='stylesheet' type='text/css' href='common.css' />
            <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css">
            <link rel="shortcut icon" href="/draft.ico" type="image/x-icon">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" />
            <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
                """;
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        // html = html + """
        //     <div class='topnav'>
        //         <a href='/'>Homepage</a>
        //         <a href='mission.html'>Our Mission</a>
        //         <a href='page2A.html'>Sub Task 2.A</a>
        //         <a href='page2B.html'>Sub Task 2.B</a>
        //         <a href='page3A.html'>Sub Task 3.A</a>
        //         <a href='page3B.html'>Sub Task 3.B</a>
        //     </div>
        // """;
        html += """
            <header>
            <div class="logo">
                <a href="/"><img src="logo.png" alt=""></a>
            </div>
            <ul id="navbar">
                <li><a href="/">Home Page</a></li>
                <li><a href="mission.html" class="active">Our Mission</a></li>
                <li><a href="page2A.html">Age & Health</a></li>
                <li><a href="page2B.html">Education</a></li>
                <li><a href="page3A.html">The Gap</a></li>
                <li><a href="page3B.html">LGA Comparison</a></li>
            </ul>
            </header>
                """;

        // // Add header content block
        // html = html + """
        //     <div class='header'>
        //         <h1>Our Mission</h1>
        //     </div>
        // """;

        // // Add Div for page Content
        // html = html + "<div class='content'>";

        // // Add HTML for the page content
        // html = html + """
        //     <p>Mission page content</p>
        //     """;

        // // This example uses JDBC to lookup the LGAs
        // JDBCConnection jdbc = new JDBCConnection();

        // // Next we will ask this *class* for the LGAs
        // ArrayList<LGA> lgas = jdbc.getLGAs2016();

        // // Add HTML for the LGA list
        // html = html + "<h1>All 2016 LGAs in the Voice to Parliament database (using JDBC Connection)</h1>" + "<ul>";

        // // Finally we can print out all of the LGAs
        // for (LGA lga : lgas) {
        //     html = html + "<li>" + lga.getCode()
        //                 + " - " + lga.getName() + "</li>";
        // }

        // // Finish the List HTML
        // html = html + "</ul>";


        // // Close Content div
        // html = html + "</div>";

        html += """
            <main>
            <section id="mission-header">
                        <div class="overlay"></div>
                        <div class="overlay-text">
                            <h2>Our Mission</h2>
                            <p>Our mission is to present unbiased data for those individuals who wish to be educated on the current affairs regarding the differences between the indigenous and non-indigenous communities.</p>
                        </div>
                    </section>
            <div class="hero">
                <div class="perspective">
                <h3>Perspective</h3>
                    <p>
                        This data would help the user decide to vote on the upcoming referendum, helping them make better and more rational decisions in their choices. The data would not only paint a better understanding of the indigenous community's struggles, but also show how information regarding health, education and age demographics could inform about the needs of the indigenous community.
                    </p>
                </div>
            </div>                    
                """;


            html = html + """
                    
            <div class="persona-container">
                <div class="persona">
                    
                """;

            ArrayList<personaImage> personaImageInfo = getPersona();
            if (!personaImageInfo.isEmpty()) {
                personaImage persona = personaImageInfo.get(0);
                String imagePath = persona.getFilePath();
                String personaName = persona.getName();
                html += "<h2>" + personaName + "</h2>";
                html += "<img src='" + imagePath + "' alt='persona 01'>";
            }

            ArrayList<Persona> personaAttributes1 = getPersonas("James");
            if (!personaAttributes1.isEmpty()) {
                html += "<ul>";
                for (Persona attribute : personaAttributes1) {
                    html += "<li><strong>" + attribute.getAttributeType() + ":</strong> " + attribute.getDescription() + "</li>";
                }
                html += "</ul>";
            } else {
                html += "<p>No attributes found for this persona.</p>";
            }


            html = html + """
                </div>

                <div class="persona">

                """;

            if (!personaImageInfo.isEmpty()) {
                personaImage persona = personaImageInfo.get(1);
                String imagePath = persona.getFilePath();
                String personaName = persona.getName();
                html += "<h2>" + personaName + "</h2>";
                html += "<img src='" + imagePath + "' alt='persona 02'>";
            }

            ArrayList<Persona> personaAttributes2 = getPersonas("Dr.Amelia");
            if (!personaAttributes2.isEmpty()) {
                html += "<ul>";
                for (Persona attribute : personaAttributes2) {
                    html += "<li><strong>" + attribute.getAttributeType() + ":</strong> " + attribute.getDescription() + "</li>";
                }
                html += "</ul>";
            } else {
                html += "<p>No attributes found for this persona.</p>";
            }

            html = html + """
                </div>

                <div class="persona">

                """;
            
            if (!personaImageInfo.isEmpty()) {
                personaImage persona = personaImageInfo.get(2);
                String imagePath = persona.getFilePath();
                String personaName = persona.getName();
                html += "<h2>" + personaName + "</h2>";
                html += "<img src='" + imagePath + "' alt='persona 03'>";
            }

            ArrayList<Persona> personaAttributes3 = getPersonas("Layla Benne");
            if (!personaAttributes3.isEmpty()) {
                html += "<ul>";
                for (Persona attribute : personaAttributes3) {
                    html += "<li><strong>" + attribute.getAttributeType() + ":</strong> " + attribute.getDescription() + "</li>";
                }
                html += "</ul>";
            } else {
                html += "<p>No attributes found for this persona.</p>";
            }

            html = html + """
                </div>

            </div>

                """;

        //Starting JDBC Connection
        ArrayList<teamMember> teamInfo = getTeamMembers();

        html = html + """
            <section class="team-section">
            <div class="team-member">
                <h3>Minguan</h3>
                """;

        if (!teamInfo.isEmpty()) {
            teamMember member1 = teamInfo.get(0);
            html = html + "<p><span>SID: </span>" + member1.getSID() + "</p>";
            html = html + "<p><span>Name: </span>" + member1.getName() + "</p>";
            html = html + "<p><span>Email: </span>" + member1.getEmail() + "</p>";
        }

        html = html + """
                </div>
                """;

        html = html + """
            <div class="team-member">
                <h3>Sakitha</h3>
                """;

        if (teamInfo.size() > 1) {
            teamMember member2 = teamInfo.get(1);
            html = html + "<p><span>SID: </span>" + member2.getSID() + "</p>";
            html = html + "<p><span>Name: </span>" + member2.getName() + "</p>";
            html = html + "<p><span>Email: </span>" + member2.getEmail() + "</p>";
        }

        html = html + """
                </div>
                """;


            // <div class="team-member">
            //     <h3>Member 2</h3>
            //     <p>Details about member 2</p>
            // </div>

        html = html + """
        </section>   
    
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

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public class personaImage {
        private String name;
        private String filePath;

        public personaImage(String name, String filePath) {
            this.name=name;
            this.filePath=filePath;
        }

        public String getName() {
            return name;
        }
        public String getFilePath() {
            return filePath;
        }

        public void setName(String name) {
            this.name=name;
        }

        public void setFilePath(String filePath) {
            this.filePath=filePath;
        }
    }

    // Getting the Persona 01 from database
        public ArrayList<personaImage> getPersona() {
        // Create the ArrayList of LGA objects to return
        ArrayList<personaImage> personas = new ArrayList<personaImage>();


        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT Name, ImageFilePath FROM Persona";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String persona  = results.getString("Name");
                String image  = results.getString("ImageFilePath");

                // Add the lga object to the array
                personaImage personass = new personaImage(persona, image);
                personas.add(personass);
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
        return personas;
    }

    public class teamMember {
        private String SID;
        private String name;
        private String email;

        public teamMember(String SID, String name, String email) {
            this.SID = SID;
            this.name = name;
            this.email = email;
        }

        public String getSID() {
            return SID;
        }
        public String getName() {
            return name;
        }
        public String getEmail() {
            return email;
        }

        public void setSID(String SID) {
            this.SID = SID;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    public ArrayList<teamMember> getTeamMembers() {
        // Create the ArrayList of LGA objects to return
        ArrayList<teamMember> member = new ArrayList<teamMember>();


        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT SID, Name, Email FROM Student";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                String sid  = results.getString("SID");
                String Name  = results.getString("Name");
                String Email  = results.getString("Email");

                // Add the lga object to the array
                teamMember members = new teamMember(sid, Name, Email);
                member.add(members);
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
        return member;
    }

    public class Persona {
        private String AttributeType;
        private String Description;

        public Persona(String AttributeType, String Description) {
            this.AttributeType = AttributeType;
            this.Description = Description;
        }

        public String getAttributeType() {
            return AttributeType;
        }
        public String getDescription() {
            return Description;
        }

        public void setAttributeType(String AttributeType) {
            this.AttributeType = AttributeType;
        }

        public void setDescription(String Description) {
            this.Description =  Description;
        }

    }

    // public ArrayList<Persona> getPersonas(String name) {
    //     // Create the ArrayList of LGA objects to return
    //     ArrayList<Persona> persona = new ArrayList<Persona>();


    //     // Setup the variable for the JDBC connection
    //     Connection connection = null;

    //     try {
    //         // Connect to JDBC data base
    //         connection = DriverManager.getConnection(JDBCConnection.DATABASE);

    //         // Prepare a new SQL Query & Set a timeout
    //         Statement statement = connection.createStatement();
    //         statement.setQueryTimeout(30);

    //         // The Query
    //         String query = "SELECT AttributeType, Description FROM PersonaAttribute WHERE personaname = " + name + ";";
            
    //         // Get Result
    //         ResultSet results = statement.executeQuery(query);

    //         // Process all of the results
    //         while (results.next()) {
    //             String attributeType = results.getString("AttributeType");
    //             String description = results.getString("Description");

    //             // Add the lga object to the array
    //             Persona data = new Persona(attributeType, description);
    //             persona.add(data);
    //         }

    //         // Close the statement because we are done with it
    //         statement.close();
    //     } catch (SQLException e) {
    //         // If there is an error, lets just pring the error
    //         System.err.println(e.getMessage());
    //     } finally {
    //         // Safety code to cleanup
    //         try {
    //             if (connection != null) {
    //                 connection.close();
    //             }
    //         } catch (SQLException e) {
    //             // connection close failed.
    //             System.err.println(e.getMessage());
    //         }
    //     }

    //     // Finally we return all of the lga
    //     return persona;
    // }

    public ArrayList<Persona> getPersonas(String name) {
    ArrayList<Persona> persona = new ArrayList<>();

    Connection connection = null;

    try {
        connection = DriverManager.getConnection(JDBCConnection.DATABASE);
        String query = "SELECT AttributeType, Description FROM PersonaAttribute WHERE personaname = ?;";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);

            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                String attributeType = results.getString("AttributeType");
                String description = results.getString("Description");
                Persona data = new Persona(attributeType, description);
                persona.add(data);
            }
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    return persona;
}


}
