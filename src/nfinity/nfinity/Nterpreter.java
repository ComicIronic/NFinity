package nfinity.nfinity;

import nfinity.nfinity.nproject.NProject;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.util.ListUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Comic on 21/05/2016.
 */
public class Nterpreter {

    public static String DEFINE = "#define";

    public static String INCLUDE = "#include";

    public static Pattern definePattern = Pattern.compile("#define[\\s]+([A-Za-z0-9_]+)([\\s]+([A-Za-z0-9_]+))?");

    public static Pattern includePattern = Pattern.compile("#include \"([A-Za-z0-9_\\/\\\\\\.]+)\"");

    public static List<String> Warnings = new ArrayList<String>();

    public static List<String> Errors = new ArrayList<String>();

    /**
     * Creates an NProject from the given DME Path, including creating new copies of all included code files if they don't already exist
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static NProject openProject(Path filePath) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString())));

        NProject project = new NProject();

        try {
            int linecount = 0;

            do {
                String line = reader.readLine();
                linecount++;

                if(line.startsWith("//")) {
                    continue;
                }

                if(addDefine(project, line)) {
                } else if(addInclude(project, line)) {
                } else {
                    warn(filePath, linecount, "This line is not the proper format for a #define or an #include.");
                }
            } while (reader.ready());
        }
        catch (IOException e) {

        }
        
        for(String error : getErrors()) {
        	System.out.println(error);
        }
        
        for(String warning : getWarnings()) {
        	System.out.println(warning);
        }

        return project;
    }

    /**
     * Creates a converted version of a code file in the new format
     * @param filepath
     * @return
     */
    private static Path convertFile(Path filepath) {
        Path parent = filepath.getParent();

        Path convertedPath = Paths.get(parent.toString(), filepath.getFileName().toString().replace(".dm", ".idm"));

        if(!Files.exists(convertedPath)) {
            try {
                Files.copy(filepath, convertedPath);
            } catch (IOException e) {
            }
        }

        return convertedPath;
    }
    
    public static void assemble(NProject project) {
    	project.Assembly.reset();
    	
    	NContext context = project.Assembly.WorldContext;
    	
    	for(Path filePath : project.Files) {
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString())));
    			
    			do {
    				
    			} while(reader.ready());
    			
    		} catch (IOException e) {
    			warn(filePath, 0, "Could not read file.");
    		}
    	}
    }

    public static boolean addDefine(NProject project, String line) {
        if(line.startsWith(DEFINE)) {
            Matcher matcher = definePattern.matcher(line);
            MatchResult result = matcher.toMatchResult();

            if (result.groupCount() == 3) {
                String defineName = result.group(1);
                String defineValue = result.group(3);

                project.Defines.put(defineName, defineValue);

                return true;
            } else if (result.groupCount() == 1) {
                project.Defines.put(result.group(1), "");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean addInclude(NProject project, String line) {
        if(line.startsWith(INCLUDE)) {
            MatchResult pathResult = includePattern.matcher(line).toMatchResult();

            if(pathResult.groupCount() == 1) {
                Path foundPath = Paths.get(project.Defines.get("FILE_DIR"), pathResult.group(1));

                project.Files.add(convertFile(foundPath));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Add warning message to the assembler storage to be printed later - warning messages should not prevent compilation
     * @param filepath
     * @param linecount
     * @param message
     */
    public static void warn(Path filepath, int linecount, String message) {
        Warnings.add(String.format("WARNING: File {0}: Line {1}, {2}", filepath.toString(), linecount, message));
    }

    /**
     * Add error message to the assembler to be printed later
     * @param filepath
     * @param linecount
     * @param message
     */
    public static void error(Path filepath, int linecount, String message) {
        Warnings.add(String.format("ERROR: File {0}: Line {1}, {2}", filepath.toString(), linecount, message));
    }
    
    
    public static List<String> getWarnings() {
    	List<String> warningCopy = ListUtils.copy(Warnings);
    	Warnings.clear();
    	return warningCopy;
    }
    
    public static List<String> getErrors() {
    	List<String> errorCopy = ListUtils.copy(Errors);
    	Errors.clear();
    	return errorCopy;
    }
}
