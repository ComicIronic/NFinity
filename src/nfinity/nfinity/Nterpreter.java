package nfinity.nfinity;

import nfinity.nfinity.exceptions.NTypeCannotExtendException;
import nfinity.nfinity.exceptions.NTypeNotFoundException;
import nfinity.nfinity.grammar.NFinityLexer;
import nfinity.nfinity.grammar.NFinityParse;
import nfinity.nfinity.nassembly.NAssembly;
import nfinity.nfinity.ncontext.NAccess;
import nfinity.nfinity.nmember.NField;
import nfinity.nfinity.nmember.NMethod;
import nfinity.nfinity.nproject.NProject;
import nfinity.nfinity.ncontext.NContext;
import nfinity.nfinity.nsignature.NArg;
import nfinity.nfinity.nsignature.NSignature;
import nfinity.nfinity.ntype.NType;
import nfinity.nfinity.util.ListUtils;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Comic on 21/05/2016.
 */
public class Nterpreter {
    public static List<String> Warnings = new ArrayList<String>();

    public static List<String> Errors = new ArrayList<String>();

    public static NProject CurrentProject = null;

    public static Path FilePath;
    public static int LineNumber;

    /**
     * Creates an NProject from the given DME Path, including creating new copies of all included code files if they don't already exist
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static NProject openProject(Path filePath) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString())));

        CurrentProject = new NProject();

        processFile(filePath);

        for(String error : getErrors()) {
        	System.out.println(error);
        }
        
        for(String warning : getWarnings()) {
        	System.out.println(warning);
        }

        return CurrentProject;
    }

    /**
     * Creates a converted version of a code file in the new format
     * @param filepath
     * @return
     */
    public static Path convertFile(Path filepath) {
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
    
    public static void assemble(NProject CurrentProject) {
    	CurrentProject.Assembly.reset();
    	
    	for(Path filePath : CurrentProject.Files) {
            processFile(filePath);
    	}
    }

    private static void processFile(Path filePath) {
        try {
            ANTLRInputStream fileStream = new ANTLRFileStream(filePath.toString());

            NFinityLexer lexer = new NFinityLexer(fileStream);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            NFinityParse parser = new NFinityParse(tokens);

            while(!parser.isMatchedEOF()) {
                NFinityParse.LineContext line = parser.line();

                if(line != null) {
                    processLine(line);
                }
            }

        } catch (IOException e) {
            error(filePath, 0, "Could not read file.");
        }
    }

    private static void processLine(NFinityParse.LineContext context) {
        NFinityParse.PreprocessContext preprocessor = context.preprocess();

        if(preprocessor != null) {
            if(preprocessor.DEFINE() != null) {
                CurrentProject.Defines.put(preprocessor.member_name().getText(), preprocessor.statement() != null? preprocessor.statement().getText() : "");
                return;
            }

            if(preprocessor.UNDEF() != null) {
                CurrentProject.Defines.remove(preprocessor.member_name().getText());
                return;
            }

            if(preprocessor.INCLUDE() != null) {
                CurrentProject.addInclude(preprocessor.bare_value().getText());
                return;
            }

            return;
        }

        NFinityParse.Type_blockContext typeContext = context.type_block();

        if(typeContext != null) {
            processType(typeContext);
        }

        return;
    }

    private static void processType(NFinityParse.Type_blockContext typeContext) {
        String typepath = "";

        if(typeContext.type_declare() != null) {
          for(NFinityParse.TypepathContext context : typeContext.type_declare().typepath()) {
              typepath += (typepath.length() > 0 ? "/" : "") + context.getText();
          }
        }

        NType currentType = CurrentProject.CurrentContext.getType();

        if(typepath != "") {
            NType pathedType = CurrentProject.Assembly.World;
            try {
                if (CurrentProject.CurrentContext == CurrentProject.Assembly.WorldContext) {
                    pathedType = CurrentProject.Assembly.getTypeOrCreateInPath(typepath);
                } else {
                    pathedType = CurrentProject.Assembly.getChildTypeOrCreateInPath(CurrentProject.CurrentContext.getType(), typepath);
                }
            } catch (NTypeCannotExtendException e) {
                error(Nterpreter.FilePath, Nterpreter.LineNumber, "Attempted to extend an inextendable type: " + e.FinalType);
                return;
            }

            if(typeContext.type_declare().ABSTRACT() != null) {
                pathedType.Abstract = true;
            }

            CurrentProject.CurrentContext = pathedType.TypeContext;
        }

        NFinityParse.Method_declareContext methodDeclareContext = typeContext.method_declare();

        if(methodDeclareContext != null) {
            NAccess access = NAccess.Default;
            if(methodDeclareContext.access_modifier() != null) {
                access = NAccess.valueOf(methodDeclareContext.access_modifier().getText());
            }

            try {
                NSignature signature = new NSignature(
                        methodDeclareContext.member_name().getText(),
                        CurrentProject.Assembly.getTypeInPath(methodDeclareContext.typepath().getText()),
                        access,
                        processArgs(methodDeclareContext.argument_declares()));

                NMethod method = new NMethod(CurrentProject.CurrentContext, signature);
            } catch (NTypeNotFoundException e) {
                error(Nterpreter.FilePath, Nterpreter.LineNumber, "Could not find the type " + e.FailedType);
                return;
            }
        }
    }

    public static NArg[] processArgs(NFinityParse.Argument_declaresContext arguments) throws NArgDeclareException {
        List<NArg> args = new ArrayList<NArg>();

        boolean failure = false;
        
        for(NFinityParse.Argument_declareContext argument : arguments.argument_declare()) {

        	NField produced = null;
        	
            try {
                produced = processVar(argument.argument_var_declare().optional_var_declare());
            } catch (NTypeNotFoundException e) {
                error(Nterpreter.FilePath, Nterpreter.LineNumber, "Could not find type " + e.FailedType);
                failure = true;
            } catch (NMemberDeclareException e) {
            	error(Nterpreter.FilePath, Nterpreter.LineNumber, "Could not create variable " + e.MemberName + ": " + e.Message);
            	failure = true;
            }
        }
        
        if(failure) {
        	throw new NArgDeclareException("Arguments were not defined properly");
        }

        return args.toArray(new NArg[args.size()]);
    }

    public static NField processVar(NFinityParse.Optional_var_declareContext context) throws NTypeNotFoundException, NMemberDeclareException {
        NAccess access = NAccess.Default;
        if(context.access_modifier() != null) {
            access = NAccess.valueOf(context.access_modifier().getText());
        }

        String fieldName = context.member_name().getText();
        NType fieldType = CurrentProject.Assembly.Any;

        if(context.typepath() != null) {
            fieldType = CurrentProject.Assembly.getTypeInPath(context.typepath().getText());
        }
        
        if(CurrentProject.Preferences.DisableAny && fieldType == CurrentProject.Assembly.Any) {
        	throw new NMemberDeclareException(fieldName, "Untyped variables are forbidden by preferences")
        }

        NSignature signature = new NSignature(fieldName, fieldType, access);

        return new NField(CurrentProject.CurrentContext, signature);
    }
    
    public static NType getReturnType(StatementContext statement) {
    	statement
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
